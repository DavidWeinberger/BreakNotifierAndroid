package at.htl.breaknotifierlit.data;

import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONException;

import at.htl.breaknotifierlit.MainActivity;
import io.vertx.core.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class RegisterInServer {

    public static boolean register(String uname, String pw, String id) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target;
            String serverUrl = MainActivity.IP + "register";
            target = client.target(serverUrl);
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id",id);
            jsonObject.put("username",PasswordEncrypt.encrypt(uname, id));
            jsonObject.put("password",PasswordEncrypt.encrypt(pw, id));
            Response response = target.request().post(Entity.json(jsonObject.toString()));
            if(response.getStatus() == 200){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        return false;
    }
}
