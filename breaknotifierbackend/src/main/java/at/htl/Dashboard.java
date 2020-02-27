package at.htl;

import at.htl.Client.ClientRepository;
import at.htl.Client.SendPushNotification;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/dashboard")
public class Dashboard {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response currentConnected() {
        return Response.ok(ClientRepository.clients.size()).build();
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
