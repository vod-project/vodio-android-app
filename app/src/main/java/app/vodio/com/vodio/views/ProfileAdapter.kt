package app.vodio.com.vodio.views

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.ExternalProfileActivity
import app.vodio.com.vodio.beans.Profile
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.ProfileRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import app.vodio.com.vodio.fragments.AbstractFragment
import app.vodio.com.vodio.models.UserModel


class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.Holder>{
    private val list : ArrayList<Profile>
    private val fragment : Fragment
    private var usrModel : UserModel? = null

    constructor(list: ArrayList<Profile>, c: Context, fragment : AbstractFragment) : super() {
        this.list = list
        this.fragment = fragment
        usrModel = ViewModelProviders.of(fragment.parent as FragmentActivity).get(UserModel::class.java)
        usrModel!!.initRepositories(LoginRepository.getInstance(c), ProfileRepository.getInstance(c), VodRepository.getInstance(c), fragment)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setIsRecyclable(false)
        val p = list.get(position)

        val loginProfile = p.login!!
        val curUsrLogin = usrModel!!.getLoggedUser().value!!.login

        holder.loginBtn.text = p.login
        if (!loginProfile.equals(curUsrLogin)) {

            usrModel!!.isFollowing(curUsrLogin!!, loginProfile).observe(fragment, Observer {
                if (it!!) {
                    holder.followBtn.text = fragment.getString(R.string.unfollow)
                    holder.followBtn.setOnClickListener {
                        usrModel!!.unfollow(loginProfile)
                        notifyDataSetChanged()
                    }
                } else {
                    holder.followBtn.text = fragment.getString(R.string.follow)
                    holder.followBtn.setOnClickListener {
                        usrModel!!.follow(loginProfile)
                        notifyDataSetChanged()
                    }
                }

            })

            usrModel!!.isFollowing(loginProfile, curUsrLogin).observe(fragment, Observer {
                if(it){
                    holder.followingYou.visibility = View.VISIBLE
                }else{
                    holder.followingYou.visibility = View.GONE
                }
            })
        } else {
            holder.followBtn.visibility = View.INVISIBLE
        }

        if(!curUsrLogin.equals(p.login)) {
            holder.loginBtn.setOnClickListener{
                val intent = Intent(fragment.context!!, ExternalProfileActivity::class.java)
                intent.putExtra(ExternalProfileActivity.ParameterName.USER_KEY, p.login)
                fragment.context!!.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.Holder{
        // create a new view
        val viewGroup = LayoutInflater.from(parent.context)
                .inflate(R.layout.profile_item, parent, false) as ViewGroup
        // set the view's size, margins, paddings and layout parameters
        return Holder(viewGroup)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder : RecyclerView.ViewHolder{
        val loginBtn : Button
        val followBtn : Button
        val followingYou : TextView
        constructor(v : ViewGroup) : super(v){
            loginBtn  = v.findViewById(R.id.loginBtn)
            followBtn = v.findViewById(R.id.followBtn)
            followingYou = v.findViewById(R.id.followingYouTv)
        }
    }
}