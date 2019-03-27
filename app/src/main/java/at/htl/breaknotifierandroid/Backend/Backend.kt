package at.htl.breaknotifierandroid.Backend

//import khttp.get as httpGet
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

object Backend {

    val schoolSearchUri = "https://mobile.webuntis.com/ms/schoolquery2"
    val schoolSearchId = "id" to "wu_schulsuche-1542658388792"
    val schoolSearchRpcVersion = "jsonrpc" to "2.0"
    val schoolSearchMethod = "method" to "searchSchool"

    var selectedSchool: School? = null

    fun getListOfSchools(filter: String): List<School>{

        val result = mutableListOf<School>()
        /*val response = httpGet(url = this.schoolSearchUri, json = mapOf(this.schoolSearchId, this.schoolSearchRpcVersion, this.schoolSearchMethod, "params" to JSONArray(filter).toString()))
        val jsonResponse = response.jsonObject
        if(jsonResponse.getJSONObject("error").getString("message") == "too many results") throw Exception("Too many results")
        else{
            val schools = jsonResponse.getJSONObject("result").getJSONArray("schools")
            for(i in 0..schools.length()) result.add(School(displayName = schools.getJSONObject(i).getString("displayName"), server = schools.getJSONObject(i).getString("server")))
        }*/
        return result

    }

    fun login(username: String, password: String){

    }
}