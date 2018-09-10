package app.vodio.com.vodio.models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.vodio.com.vodio.database.retrofit.utils.RetrofitInstance
import retrofit2.Retrofit

class VodsPresenterFactory(private val context: Context)  {

    fun  create() : VodsPresenter {
        return VodsPresenter(RetrofitInstance.getRetrofitInstance(context))
    }
}