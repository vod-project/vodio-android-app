package app.vodio.com.vodio.beans;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Vod {
    float timeInSecond;
    String authorLogin;
    String title;
    List<String> tags;
    String audioFilePath;

    public Vod(JSONObject obj){
        try {
            timeInSecond = Float.parseFloat(obj.get("timeInSecond").toString());
            authorLogin = obj.get("authorLogin").toString();
            title = obj.get("title").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public float getTimeInSecond() {
        return timeInSecond;
    }

    public void setTimeInSecond(float timeInSecond) {
        this.timeInSecond = timeInSecond;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }
}
