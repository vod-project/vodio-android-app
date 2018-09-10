package app.vodio.com.vodio.database.retrofit.utils

import android.content.Context
import android.util.Log
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.net.NetworkInfo
import app.vodio.com.vodio.R
import okhttp3.*


class RetrofitInstance{

    companion object {
        private var mCache : Cache? = null
        private var retrofit : Retrofit? = null
        private var BASE_URL : String? = null
        fun getRetrofitInstance(context : Context) : Retrofit{
            if(retrofit == null){
                BASE_URL = context.resources.getString(R.string.database_url)
                // create http client without cache to force retrofit to retrieve data from http first
                var httpClient = provideHttpClient(context)

                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                        .client(httpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
            }
            return retrofit!!
        }

        private fun provideCache(context: Context): Cache {
            if (mCache == null) {
                try {
                    mCache = Cache(File(context.cacheDir, "http-cache"),
                            10 * 1024 * 1024) // 10 MB
                } catch (e: Exception) {
                    Log.e(this.javaClass.simpleName, "Could not create Cache!")
                }

            }

            return mCache!!
        }

        private fun provideHttpClient(context: Context) : OkHttpClient{
            return OkHttpClient.Builder()
                    .cache(provideCache(context))
                    .addInterceptor { chain: Interceptor.Chain? ->
                        var request = chain!!.request()
                        val networkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
                        if(networkInfo == null && request.cacheControl() != CacheControl.FORCE_NETWORK) {
                            request = chain.request().newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build()
                        }else {
                            request = chain.request().newBuilder()
                                    .cacheControl(CacheControl.FORCE_NETWORK)
                                    .build()
                        }
                        chain.proceed(request)
                    }
                    .build()
        }
    }





}