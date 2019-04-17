package Controller;
import Model.Agency;
import Model.Sort;
import Model.SortCriteria;
import com.google.gson.*;

import java.util.*;

import spark.Request;
import spark.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class AgencyController {


    public Collection<Agency> agency(Request req, Response res) throws ExceptionAgency {

        if (req.queryParams("sites") == null || req.queryParams("payment_methods") == null || req.queryParams("near_to") == null) {
            res.status(400);
            throw new ExceptionAgency("los paramentros sites, payment_methods, near_to no pueden ser nulos");
        }
        String url = "https://api.mercadolibre.com/sites/" + req.queryParams("sites") + "/payment_methods/" + req.queryParams("payment_methods") + "/agencies?near_to=" + req.queryParams("near_to");
        if (req.queryParams("limit")!=null){
            url.concat("&limit=" + (req.queryParams("limit")));
        }
        if (req.queryParams("offset")!=null){
            url.concat("&offset=" + (req.queryParams("offset")));
        }
        try{
            String data = readUrl(url);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(data).getAsJsonObject();
            Agency[] agencies = new Gson().fromJson(jsonObject.get("results"), Agency[].class);
            if (req.queryParams("orderBy")!= null){
                switch(req.queryParams("orderBy")) {
                    case "address_line": {
                        Agency.setSortCriteria(SortCriteria.ADDRESS_LINE);
                        break;
                    }
                    case "distance":
                        Agency.setSortCriteria(SortCriteria.DISTANCE);
                        break;
                    case "agency_code":
                        Agency.setSortCriteria(SortCriteria.AGENCY_CODE);
                        break;
                    default:
                        throw new ExceptionAgency("Metodo de ordenamiento no admitido");
                }
                Sort.sortByCriteria(agencies);
            }
            res.status(200);
            return Arrays.asList(agencies);

        }catch (IOException E){
            res.status(500);
            throw new ExceptionAgency("Error en el servidor");
        }
    }


    private static String readUrl(String urlStr) throws IOException {

        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        System.out.flush();
        int read = 0;
        char[] chars = new char[1024];
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}