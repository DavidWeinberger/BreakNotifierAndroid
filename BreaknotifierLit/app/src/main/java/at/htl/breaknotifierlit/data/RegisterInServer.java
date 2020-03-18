package at.htl.breaknotifierlit.data;

import android.util.Log;

import at.htl.breaknotifierlit.MainActivity;
import io.vertx.core.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RegisterInServer {

    public static boolean register(String uname, String pw, String id) {
        Log.i("RegisterInServer", "Entered register");
        Client client = ClientBuilder.newClient();
        Log.i("RegisterInServer", "Created Client");
        try {
            Log.i("RegisterInServer", "Entered try");
            WebTarget target;
            String serverUrl = MainActivity.URL + "register";
            target = client.target(serverUrl);
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id",id);
            jsonObject.put("username",PasswordEncrypt.encrypt(uname, id));
            jsonObject.put("password",PasswordEncrypt.encrypt(pw, id));
            Log.i("RegisterInServer", "Sending Request");
            Response response = target.request().post(Entity.json(jsonObject.toString()));
            if(response.getStatus() == 200){
                return true;
            }
        } catch (Exception e) {
            Log.i("RegisterInServer", "Some Error Occured");
            e.printStackTrace();
        } finally {
            client.close();
        }

        return false;
    }
}
