package app.vodio.com.vodio.services

import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.RetrofitInstance
import app.vodio.com.vodio.database.retrofit.services.UserService
import app.vodio.com.vodio.database.retrofit.services.VodService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VodService{
    fun getVods(onComplete : OnCompleteAsyncTask){
        val service : VodService? = RetrofitInstance.getRetrofitInstance()?.create(VodService::class.java)

        val call : Call<Array<Vod>>? = service?.getVods()

        call?.enqueue(OnVodsProvided(onComplete));
    }
    class OnVodsProvided (private var onComplete: OnCompleteAsyncTask) : Callback<Array<Vod>>{
        override fun onResponse(call: Call<Array<Vod>>?, response: Response<Array<Vod>>?) {
            if(response?.body() != null) {
                onComplete.onSuccess(response?.body() as Array<Vod>)
            }else{
                onComplete.onFail()
            }
        }

        override fun onFailure(call: Call<Array<Vod>>?, t: Throwable?) {
            onComplete.onFail()
        }

    }
}