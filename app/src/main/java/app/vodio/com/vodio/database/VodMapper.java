package app.vodio.com.vodio.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import app.vodio.com.vodio.utils.MyAsyncTask;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;

public class VodMapper {
    private static String host = "http://www.assimsen.fr/vodio/rest";
    private static String path = "/vod/get.php";

    public static class GetVodTask extends MyAsyncTask {
        Object object ;
        public GetVodTask(OnCompleteAsyncTask onComplete) {
            super(onComplete);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            object = HTTPUtils.getArray(host,path,new HashMap<String,String>());
            if(object != null)
                setSuccess(true);
            else{
                setSuccess(false);
            }
            return super.doInBackground(objects);
        }

        @Override
        public Object getObject() {
            return object;
        }
    }
}
