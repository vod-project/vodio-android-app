package app.vodio.com.vodio.database;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.beans.Vod;
import app.vodio.com.vodio.utils.MyAsyncTask;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;

public class VodMapper {


    public static class GetListVodTask extends MyAsyncTask<List<Vod>> {
        List<Vod> object ;
        private String host;
        private String path = "/vod/get.php";
        public GetListVodTask(OnCompleteAsyncTask onComplete, Context c) {
            super(onComplete);
            host = c.getString(R.string.host_database);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            object = new ArrayList<>();
            JSONArray array = ServerMapper.getArray(host,path,new HashMap<String,String>());
            for(int i = 0 ; i < array.length() ; i++){
                try {
                    object.add(new Vod(array.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            setSuccess(object != null);
            return null;
        }

        @Override
        public List<Vod> getObject() {
            return object;
        }
    }

    public class CreateVodTask extends MyAsyncTask<DatabaseResponse>{
        private Vod vod;
        private String path = "/vod/get.php";
        private String host ;
        private DatabaseResponse response;
        public CreateVodTask(OnCompleteAsyncTask onComplete, Context c , Vod vod) {
            super(onComplete);
            this.vod = vod;
            host = c.getString(R.string.host_database);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Map<String, String> map = new HashMap<>();
            map.put("timeInSecond",vod.getTimeInSecond()+"");
            map.put("authorLogin",vod.getAuthorLogin());
            map.put("title",vod.getTitle());
            JSONObject obj = ServerMapper.getObject(host,path,map);
            DatabaseResponse response = new DatabaseResponse(obj);
            return null;
        }

        @Override
        public DatabaseResponse getObject() {
            return response;
        }
    }
}
