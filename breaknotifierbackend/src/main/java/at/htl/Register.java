package at.htl;

import at.htl.Client.Client;
import at.htl.Client.ClientRepository;
import at.htl.Client.WebUntisConnection;
import at.htl.Data.PasswordEncrypt;
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
        String id = jsonObject.getValue("id").toString();
        String uname = jsonObject.getValue("username").toString();
        String pw = jsonObject.getValue("password").toString();
        WebUntisConnection webUntisConnection = new WebUntisConnection(id,uname,pw);
        NewCookie cookie = webUntisConnection.login();
        if(cookie != null){
            Thread currentThread = new Thread(webUntisConnection);
            currentThread.start();
            Client newClient = new Client(id, currentThread);
            ClientRepository.clients.add(newClient);
            return  Response.ok().build();
        }
        else{
            return Response.serverError().build();
        }
    }
}
