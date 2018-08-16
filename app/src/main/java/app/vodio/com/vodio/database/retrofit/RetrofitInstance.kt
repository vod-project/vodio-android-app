package app.vodio.com.vodio.database.retrofit;

import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory;
class RetrofitInstance{
    companion object {
        private var retrofit : Retrofit? = null
        private val BASE_URL : String = "http://www.assimsen.fr/vodio/rest/"
        fun getRetrofitInstance() : Retrofit?{
            if(retrofit == null){
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
            }
            return retrofit
        }
    }
}