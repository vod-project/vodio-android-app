package app.vodio.com.vodio.models

import app.vodio.com.vodio.database.retrofit.services.UserDatabaseInterface
import app.vodio.com.vodio.fragments.utils.ProfileView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class ProfilePresenter(private val retrofit: Retrofit) : BasePresenter<ProfileView>(){

    fun loadProfileByLogin(login : String){
        retrofit.create(UserDatabaseInterface::class.java)
                .getProfile(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view()?.showProfileLoading()
                }.subscribe({
                    if(it == null){
                        view()?.showNoProfileData()
                    }else {
                        view()?.showProfile(it!!)
                    }
                }, {
                    view()?.showProfileError(it.message!!)
                })
    }
}