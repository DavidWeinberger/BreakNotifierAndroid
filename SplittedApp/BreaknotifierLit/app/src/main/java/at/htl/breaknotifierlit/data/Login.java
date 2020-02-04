package at.htl.breaknotifierlit.data;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


public class Login {

    public static NewCookie LoginToWebUntis(String user, String password){
        Client client = ClientBuilder.newClient();
        WebTarget target;
        String serverUrl = "https://mese.webuntis.com/WebUntis/j_spring_security_check";
        try{
            //HttpPost post = new HttpPost("https://"+serverUrl+"/WebUntis/j_spring_security_check");
            target = client.target(serverUrl);
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("school", "htbla linz leonding");
            formData.add("j_username", user);
            formData.add("j_password", password);
            formData.add("token", "");


            try {
                Response response = target.request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(formData));
                System.out.print("");
                String responseString = response.readEntity(String.class);


                if (response == null || response.getStatus() == 302){
                    System.out.println("Failed");
                    //loggedIn = false;
                    //loginCookie = null;

                }
                else if (responseString.contains("Your Browser is not supported. Please use IE10 or later!")){
                    System.out.println("Failed");
                    //loggedIn = false;
                    //loginCookie = null;

                }
                else if (responseString.contains("NO_MANDANT")){
                    System.out.println("Failed");
                }
                else if (response.getStatus() == 200 && responseString.contains("SUCCES")) {
                    Map<String, NewCookie> map = response.getCookies();
                    System.out.println("Erfolgreich");
                    return map.get("JSESSIONID"); //Speichert den Cookie vom Erfolgreichen Login
                    //loggedIn = true;

                }
            }catch (Exception e){
                System.out.println("Falsches Passwort");
            }

        }catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
}
