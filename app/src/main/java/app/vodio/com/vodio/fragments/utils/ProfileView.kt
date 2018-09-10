package app.vodio.com.vodio.fragments.utils

import app.vodio.com.vodio.beans.Profile

interface ProfileView{
    fun showProfile(profile : Profile)
    fun showNoProfileData()
    fun showProfileLoading()
    fun showProfileError(msg : String)
}