package at.htl;

import at.htl.Client.Client;
import at.htl.Client.ClientRepository;
import at.htl.Client.WebUntisConnection;
import at.htl.GarbageCollector.Cleaner;
import io.vertx.core.json.JsonObject;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/register")
public class Register {

    @PostConstruct
    private void init(){
        Thread cleanerThread = new Thread(new Cleaner());
        cleanerThread.start();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isRegisterd(@PathParam("id") String id) {
        final Response[] response = new Response[1];
        ClientRepository.clients.forEach(x -> {
            if(x.getId().equals(id)){
                response[0] = Response.ok().build();
            }
        });
        if(response[0] == null){
            response[0] = Response.serverError().build();
        }
        return response[0];
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(String data){
        JsonObject jsonObject = new JsonObject(data);
        System.out.println(jsonObject);
        NewCookie cookie = new NewCookie("JSESSIONID",jsonObject.getValue("cookie").toString());
        String id = jsonObject.getValue("id").toString();
        WebUntisConnection webUntisConnection = new WebUntisConnection(id,cookie);
        Thread currentThread = new Thread(webUntisConnection);
        currentThread.start();
        Client newClient = new Client(id, cookie, currentThread);
        ClientRepository.clients.add(newClient);
        //System.out.println(ClientRepository.clients.size());
        //System.out.println(currentThread.isAlive());
        //System.out.println(data.cookie);
        return  Response.ok().build();
    }
}
