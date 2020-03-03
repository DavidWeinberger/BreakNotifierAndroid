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
        //System.out.println(data);
        JsonObject jsonObject = new JsonObject(data);
        //NewCookie cookie = new NewCookie("JSESSIONID",jsonObject.getValue("cookie").toString());
        //System.out.println("Before id");
        String id = jsonObject.getValue("id").toString();
        //System.out.println("Start decrypt");
        String uname = jsonObject.getValue("username").toString();
        String pw = jsonObject.getValue("password").toString();
        //System.out.println("Finishing decrypt");
        WebUntisConnection webUntisConnection = new WebUntisConnection(id,uname,pw);
        //System.out.println("Create Conn");
        Thread currentThread = new Thread(webUntisConnection);
        currentThread.start();
        //System.out.println("Create Thread");
        Client newClient = new Client(id, currentThread);
        ClientRepository.clients.add(newClient);

        return  Response.ok().build();
    }
}
