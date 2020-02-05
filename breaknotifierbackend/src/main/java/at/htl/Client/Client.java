package at.htl.Client;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.enterprise.inject.New;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;


public class Client {
    private String id;
    private NewCookie cookie;
    private Thread thread;

    public String getId() {
        return id;
    }

    public NewCookie getCookie() {
        return cookie;
    }

    public Thread getThread() {
        return thread;
    }

    public Client(String _id, NewCookie _cookie, Thread _thread){
        id=_id;
        cookie=_cookie;
        thread=_thread;
    }
}
