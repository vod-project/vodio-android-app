package app.vodio.com.vodio.beans

import android.util.JsonReader;
import org.json.JSONObject;

data class User(var name : String?, var surname : String?, var login : String?, var email : String?, var password : String){

    constructor(reader: JsonReader): this("","","","",""){
        provideFromJsonReader(reader)
    }
    constructor(jsonObject: JSONObject): this("","","","",""){
        provideFromJsonObject(jsonObject)
    }

    fun provideFromJsonReader(reader: JsonReader){

    }

    fun isProvided() : Boolean {
        var isProvided = false;
        if(! (name == null && surname == null && login == null && email == null)){
            isProvided = true;
        }
        return isProvided;
    }
    fun provideFromJsonObject(obj: JSONObject){
        name = checkStringJsonData(obj.get("name").toString());
        surname = checkStringJsonData(obj.get("surname").toString());
        login = checkStringJsonData(obj.get("login").toString());
        email = checkStringJsonData(obj.get("email").toString());
    }
    fun checkStringJsonData(str : String) : String?{
        if(str.equals("null")){
            return null
        }
        return str;
    }
}
/*
public class User {
    String name;
    String surname;
    String login;
    String email;
    String password;
    boolean isProvided;
    public User(JsonReader reader){
        try {
            provideFromJsonReader(reader);
        } catch (IOException e) {
            isProvided = false;
        }catch (IllegalStateException ee){
            isProvided = false;
        }
    }
    public User(JSONObject jsonObject){
        try {
            provideFromJsonObject(jsonObject);
        } catch (JSONException e) {
            isProvided = false;
        }
    }
    private void provideFromJsonObject(JSONObject obj) throws JSONException {

        name = checkStringJsonData(obj.get("name").toString());
        surname = checkStringJsonData(obj.get("surname").toString());
        login = checkStringJsonData(obj.get("login").toString());
        email = checkStringJsonData(obj.get("email").toString());
        if(name == null && surname == null && login == null && email == null){
            isProvided = false;
        }else {
            isProvided = true;
        }
    }
    private String checkStringJsonData(String s){
        if(s.equals("null")){
            s = null;
        }
        return s;
    }
    private void provideFromJsonReader(JsonReader reader) throws IOException, IllegalStateException {
        if(reader.hasNext()) {
            reader.beginObject();
            reader.nextName();
            login = reader.nextString();
            reader.nextName();
            password = reader.nextString();
            reader.nextName();
            name = reader.nextString();
            reader.nextName();
            surname = reader.nextString();
            reader.nextName();
            email = reader.nextString();
            isProvided = true;
        }
    }
    public boolean isProvided(){
        if(name == null && surname == null && login == null && email == null){
            isProvided = false;
        }else {
            isProvided = true;
        }
        return isProvided;
    }
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
*/