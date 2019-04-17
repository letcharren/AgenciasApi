package Controller;

import Utils.StandardResponse;
import Utils.StatusResponse;
import com.google.gson.Gson;
import Model.Agency;

import java.util.Collection;

import static Utils.JsonUtils.json;
import static spark.Spark.*;
/*
Implementacion de una API que consume de AGENCIES API de Mercado Libre
El puerto por defecto es 4567
 */
public class AgenciasAPI {

    public static void main(String[] args) {
¡        AgencyController agencyController = new AgencyController();
        /**
         * Devuelve un arreglo de agencias para un sitio y un método de pago en particular,
         * según las coordenadas de GeoLocation y el radio de búsqueda (en metros).
         * El response es enviado en formato json
         */
        get("/agencias" ,(req,res)-> {
            StandardResponse body= new StandardResponse(StatusResponse.SUCCESS);
            try{
                Collection<Agency> agencies = agencyController.agency(req,res);
                res.type("application/json");
                body.setStatus(StatusResponse.SUCCESS);
                body.setData(new Gson().toJsonTree(agencies));
            }catch (ExceptionAgency e){
                body.setStatus(StatusResponse.ERROR);
                body.setMessage(e.getMessage());
            }
            return body;
        },json());
    }
}
