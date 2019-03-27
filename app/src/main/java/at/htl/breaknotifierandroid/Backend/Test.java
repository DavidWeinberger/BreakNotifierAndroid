package at.htl.breaknotifierandroid.Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Test {
    private String idSearch = "wu_schulsuche-1542658388792";
    private Client client = ClientBuilder.newClient();
    private WebTarget target;

    public JSONObject testing(){
        this.target = this.client.target("https://mobile.webuntis.com/ms/schoolquery2");
        JSONObject values = new JSONObject();
        JSONObject object = new JSONObject();
        JSONArray temp = new JSONArray();
        try {

            values.put("search", "Htbla Leonding");
            temp.put(0, values);
            object.put("id", idSearch);
            object.put("jsonrpc","2.0");
            object.put("method","searchSchool");
            object.put("params", temp);
            System.out.println(object);

        } catch (JSONException e) {
            System.err.println("JSON-Crash");
        }

        //JsonArray values = javax.json.Json.createArrayBuilder().add(javax.json.Json.createObjectBuilder().add("search", "Htbla Leon")).build();

        //JsonObject obj = javax.json.Json.createObjectBuilder().add("id", "wu_schulsuche-1542658388792")
        // .add("jsonrpc","2.0").add("method","searchSchool").add("params", values).build();

        Response response = this.target.request(MediaType.APPLICATION_JSON).post(Entity.json(object));
        JSONObject resultList = response.readEntity(JSONObject.class);
        return resultList;
    }
}
