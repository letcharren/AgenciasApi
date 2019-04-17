package Controller;

/**
 * Excepcion que maneja las excepciones de AgencyController para poder setear un mensaje mas amigable
 */
public class ExceptionAgency extends Exception{

    public ExceptionAgency(){
        super();
    }

    public ExceptionAgency(String message){
        super(message);
    }
}
