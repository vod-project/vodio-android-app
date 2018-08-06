package app.vodio.com.vodio.utils;

import android.os.AsyncTask;

public abstract class MyAsyncTask  extends AsyncTask{
    private boolean isSuccess = false;
    private OnCompleteAsyncTask onComplete;
    public MyAsyncTask(OnCompleteAsyncTask onComplete){
        this.onComplete = onComplete;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    protected void setSuccess(boolean s){
        isSuccess = s;
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(onComplete != null) {
            if (isSuccess) {
                onComplete.onSuccess(getObject());
            } else {
                onComplete.onFail();
            }
        }
    }

    public abstract Object getObject();
}
