package at.htl.Client;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.enterprise.inject.New;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;


public class Client {
    private String id;
    private Thread thread;

    public String getId() {
        return id;
    }


    public Thread getThread() {
        return thread;
    }

    public Client(String _id, Thread _thread){
        id=_id;
        thread=_thread;
    }
}
