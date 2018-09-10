package app.vodio.com.vodio.models

import android.content.Context
import app.vodio.com.vodio.database.retrofit.utils.RetrofitInstance

class ProfilePresenterFactory(val context: Context){

    fun create() : ProfilePresenter{
        return ProfilePresenter(RetrofitInstance.getRetrofitInstance(context))
    }
}