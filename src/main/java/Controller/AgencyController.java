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

/**
 * Clase que maneja los request del model Agencia
 */
public class AgencyController {

    /**
     *
     * @param req Request HTTP
     *            parametros en req.queryParams():
     *            * sites (obligatorio): es el código del sitio ("MLA", "MLM", "MLV", etc.)
     *            * payment_methods (obligatorio): es el nombre del método de pago ("rapipago", "pagofacil", "serfin", "banamex", etc.)
     *            * near_to (mandatory): is the latitude and longitude coordinates and the search radius specified in meters.
     *            * limits (opcional): es el número máximo de elementos que se recuperarán. Valor por defecto: 50, mín. valor: 1, max. valor: 100.
     *            * offset (opcional): es el número del primer elemento que se recupera (útil para la paginación).
     *            Valor por defecto: 0, mín. valor: 0, max. valor: -
     *            * orderBy (opcional): Es por el valor por el cual puede ser ordenado. Los valores admitidos son address_line,distance,agency_code
     * @param res Respose HTTP. Se setea el status de la respuesta
     * @return Devuelve un arreglo de agencias para un sitio y un método de pago en particular,
     * según las coordenadas de GeoLocation y el radio de búsqueda (en metros).
     * El arreglo de resultados está ordenada en orden ascendente, considerando la distancia entre cada agencia
     * y las coordenadas de GeoLocation.
     * @throws ExceptionAgency cuando Ocurre una excepcion. Si esto ocurre, se setea el correspondiente codigo de respuesta
     */
    public Collection<Agency> agency(Request req, Response res) throws ExceptionAgency {

        if (req.queryParams("sites") == null || req.queryParams("payment_methods") == null || req.queryParams("near_to") == null) {
            res.status(400);
            throw new ExceptionAgency("los paramentros sites, payment_methods, near_to no pueden ser nulos");
        }
        if (req.queryParams("sites").isEmpty() || req.queryParams("payment_methods").isEmpty() || req.queryParams("near_to").isEmpty()) {
            res.status(400);
            throw new ExceptionAgency("los paramentros sites, payment_methods, near_to no pueden ser vacios");
        }
        String url = "https://api.mercadolibre.com/sites/" + req.queryParams("sites") + "/payment_methods/" + req.queryParams("payment_methods") + "/agencies?near_to=" + req.queryParams("near_to");
        if (req.queryParams("limit")!=null){
            if(req.queryParams("limit").isEmpty()){
                throw new ExceptionAgency("los paramentros sites, payment_methods, near_to no pueden ser vacios");
            }
            url.concat("&limit=" + (req.queryParams("limit")));
        }
        if (req.queryParams("offset")!=null){
            if(req.queryParams("offset").isEmpty()){
                throw new ExceptionAgency("Parametro Limit vacio");
            }
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
        }catch (IOException e){
            int ResponseCodeInit = e.getMessage().lastIndexOf("HTTP");
            int ResponseCodeLast = e.getMessage().lastIndexOf("for");
            String messagge;
            res.status(500);
            if (ResponseCodeInit<0 || ResponseCodeLast<0) {
                messagge = "Error con la conexion a la API Mercado Libre";
            }else{
                String respuesta = e.getMessage().substring(ResponseCodeInit, ResponseCodeLast).replaceFirst("response code", "codigo respuesta");
                if (respuesta.contains("400")){
                    res.status(400);
                    messagge = ("Error con el formato de parametros mandados");
                }else{
                    messagge= "Error Procesando request por parte de API ML";
                }
            }
            throw new ExceptionAgency(messagge);
        }
    }

    /**
     *
     * @param urlStr String de la Url de la cual se quiere hace la solicitud
     * @return Retorna un String con con la respuesta de la URL
     * @throws IOException si hubo un error cuando se solicita el recurso a la URL
     */
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