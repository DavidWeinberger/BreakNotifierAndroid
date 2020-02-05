package at.htl.breaknotifierlit.data;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import at.htl.breaknotifierlit.MainActivity;
import io.vertx.core.json.JsonObject;

public class CheckIfUserAlreadyLoggedIn implements Runnable{


    public CheckIfUserAlreadyLoggedIn(String id) {
        this.id = id;
    }

    private String id;

    public Boolean getRes() {
        return res;
    }

    private Boolean res;

    @Override
    public void run() {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target;
            String serverUrl = MainActivity.IP + "register/" + id;
            target = client.target(serverUrl);

            Response response = target.request(MediaType.TEXT_PLAIN).get();
            if(response.getStatus() == 200){
                res = true;
            } else {
                res = false;
            }
        } finally {
            client.close();
        }
    }
}
