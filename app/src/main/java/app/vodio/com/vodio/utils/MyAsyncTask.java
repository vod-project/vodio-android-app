package app.vodio.com.vodio.utils;

import android.os.AsyncTask;

public abstract class MyAsyncTask<T>  extends AsyncTask{
    private boolean isSuccess = false;
    private OnCompleteAsyncTask onComplete;
    public MyAsyncTask(OnCompleteAsyncTask onComplete){
        this.onComplete = onComplete;
    }
    @Override
    protected abstract Object doInBackground(Object[] objects);

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

    public abstract T getObject();
}
