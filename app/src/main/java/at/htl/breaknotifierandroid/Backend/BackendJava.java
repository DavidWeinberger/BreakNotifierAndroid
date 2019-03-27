package at.htl.breaknotifierandroid.Backend;

import android.os.AsyncTask;
import android.os.Looper;
import android.os.StrictMode;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackendJava {
    private String idSearch = "wu_schulsuche-1542658388792";
    //private Client client = ClientBuilder.newClient();
    //private WebTarget target;

    public BackendJava(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    public JSONObject getSchools(String input) throws JSONException {
        /*if (Looper.myLooper() == null){
            Looper.prepare();
        }
        Looper.loop();
*/
        HttpClient client = new DefaultHttpClient();
               // = HttpClientBuilder.create().build();

        HttpResponse response;
        //this.target = this.client.target("https://mobile.webuntis.com/ms/schoolquery2");
        JSONObject values = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray temp = new JSONArray();

        values.put("search", input);
        temp.add(0,values);
        object.put("id", idSearch);
        object.put("jsonrpc","2.0");
        object.put("method","searchSchool");
        object.put("params", temp);
        System.out.println(object);

        JSONObject jsonArray = null;
        try{
            HttpPost post = new HttpPost("https://mobile.webuntis.com/ms/schoolquery2");


            StringEntity se = new StringEntity(object.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);

            response = client.execute(post);

            if (response != null) {

                InputStream in = response.getEntity().getContent();
                JSONParser jsonParser = new JSONParser();
                jsonArray = (JSONObject) jsonParser.parse(
                        new InputStreamReader(in, "UTF-8"));
            }
        }catch (Exception e){
            System.err.println(e);
        }

        //JsonArray values = javax.json.Json.createArrayBuilder().add(javax.json.Json.createObjectBuilder().add("search", "Htbla Leon")).build();

        //JsonObject obj = javax.json.Json.createObjectBuilder().add("id", "wu_schulsuche-1542658388792")
        // .add("jsonrpc","2.0").add("method","searchSchool").add("params", values).build();

        //Response response = this.target.request(MediaType.APPLICATION_JSON).post(Entity.json(object));


        //JSONObject resultList = response.readEntity(JSONObject.class);
        //return resultList;

        return jsonArray;
    }

    public List<String> schools(JSONObject jsonObject){
        List<String> names = new ArrayList<>();
        JSONObject object1 = (JSONObject) jsonObject.get("result");
        JSONArray object2 = (JSONArray) object1.get("schools");

        for (int i = 0; i < object2.size(); i++){
            JSONObject obj = (JSONObject) object2.get(i);
            String name = obj.get("displayName").toString();
            names.add(name);

        }

        return names;
    }

    public NewCookie login(String serverUrl, String loginName, String username, String password){
        //HttpClient client = new DefaultHttpClient();
        // = HttpClientBuilder.create().build();

        //HttpResponse response;


        Client client = ClientBuilder.newClient();
        WebTarget target;

        try{
            //HttpPost post = new HttpPost("https://"+serverUrl+"/WebUntis/j_spring_security_check");
            target = client.target("https://"+serverUrl+"/WebUntis/j_spring_security_check");

            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("school", loginName);
            formData.add("j_username", username);
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

            /*StringEntity se = new StringEntity(formData.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);

            //response = client.execute(post);

            if (response != null) {
                InputStream in = response.getEntity().getContent();
                int i = in.read();
                String theString = IOUtils.toString(in, "UTF-8");
                response.getHeaders("Cookies");
            }*/
        }catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
}

