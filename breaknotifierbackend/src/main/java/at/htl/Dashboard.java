package at.htl;

import at.htl.Client.ClientRepository;
import at.htl.Client.SendPushNotification;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/dashboard")
public class Dashboard {
    public static List<Exception> exceptions = new LinkedList<>();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response currentConnected() {
        return Response.ok(ClientRepository.clients.size()).build();
    }

    @GET
    @Path("/errors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response errorMessaging(){
        return Response.ok(exceptions).build();
    }

    @GET
    @Path("/test")
    public Response testMessaging(){
        ClientRepository.clients.forEach(client -> {
            try {
                SendPushNotification.pushFCMNotification(client.getId(),"Testing", "Testing Messaging Service");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return Response.ok().build();
    }
}
