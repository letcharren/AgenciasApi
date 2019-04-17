package Controller;

import Utils.StandardResponse;
import Utils.StatusResponse;
import com.google.gson.Gson;
import Model.Agency;

import java.util.Collection;

import static Utils.JsonUtils.json;
import static spark.Spark.*;

public class AgenciasAPI {

    public static void main(String[] args) {
        //port (8000);
        AgencyController agencyController = new AgencyController();

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
