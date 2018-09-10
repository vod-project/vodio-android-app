package app.vodio.com.vodio.views

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.ExternalProfileActivity
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.ProfileRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import app.vodio.com.vodio.fragments.AbstractFragment
import app.vodio.com.vodio.models.UserModel
import app.vodio.com.vodio.models.VodModel
import app.vodio.com.vodio.utils.MediaPlayerService
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.HashSet

class VodAdapter : RecyclerView.Adapter<VodAdapter.Holder> {
    private var list: ArrayList<Vod>
    private var c: Context
    private var fragment : Fragment
    private var usrModel : UserModel? = null
    private var vodModel : VodModel? = null
    private var internalProfile = true

    private var configuredHolder = HashSet<Holder>()

    private val executor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    constructor(list: ArrayList<Vod>,c: Context, fragment : AbstractFragment, internalProfile : Boolean = true) {
        this.list = list
        this.c = c
        this.fragment = fragment
        this.internalProfile = internalProfile
        usrModel = ViewModelProviders.of(fragment.requireActivity()).get(UserModel::class.java)
        usrModel!!.initRepositories(
                LoginRepository.getInstance(c),
                ProfileRepository.getInstance(c),
                VodRepository.getInstance(c),
                fragment
        )
        vodModel = ViewModelProviders.of(fragment.requireActivity()).get(VodModel::class.java)
        vodModel!!.initRepositories(VodRepository.getInstance(c), fragment)
    }

    override fun getItemCount(): Int { return list.size }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // media prepare

        val obj = list.get(position)
        val curUsr = LoginRepository.getInstance(c).getLoggedUser().value!!

        if(obj.isLikedByFollower()){
            holder.isVodLiked.visibility = View.VISIBLE
        }

        // datation vods
        val time = Calendar.getInstance().timeInMillis / 1000
        val timeVod = obj.timestamp
        val timeElapsed = (time - timeVod).toInt()
        holder.timeVodText.text = secondToTimeStr(timeElapsed)


        if(!configuredHolder.contains(holder)) {
            configuredHolder.add(holder)

            executor.execute { MediaPlayerService.getInstance(c)
                    .start(list.get(position), holder.playPauseBtn, holder.seekbar, holder.progressTimeTv) }

            holder.authorLoginBtn.text = obj.authorLogin
            holder.titleTv.text = obj.title
            holder.timeTv.setTime(obj.timeInSecond!!)

            vodModel!!.isLiking(obj.idVod).observe(fragment, androidx.lifecycle.Observer {
                if (it) {
                    holder.likeVodBtn.setImageResource(android.R.drawable.btn_star_big_on)
                    holder.likeLayout.setOnClickListener {
                        vodModel!!.unlike(obj.idVod)
                        notifyDataSetChanged()
                    }
                } else {
                    holder.likeVodBtn.setImageResource(android.R.drawable.btn_star_big_off)
                    holder.likeLayout.setOnClickListener {
                        vodModel!!.like(obj.idVod)
                        notifyDataSetChanged()
                    }
                }
            })

            holder.numberOfLikeTv.text = "-"
            vodModel!!.numberOfLike(obj.idVod).observe(fragment, androidx.lifecycle.Observer {
                if(!(it == null || it == -1))
                    holder.numberOfLikeTv.text = "${it}"
            })

            if (internalProfile) {
                holder.authorBtn.setOnClickListener {
                    if (!holder.authorBtn.text.equals(usrModel!!.getLoggedUser().value!!.login)) {
                        val intent = Intent(c, ExternalProfileActivity::class.java)
                        intent.putExtra(ExternalProfileActivity.ParameterName.USER_KEY, holder.authorBtn.text)
                        c.startActivity(intent)
                    }
                }
                holder.authorBtn.setOnLongClickListener {
                    if (!curUsr.login.equals(obj.authorLogin)) {
                        val author = holder.authorBtn.text.toString()
                        val curUser = usrModel!!.getLoggedUser().value!!
                        val isFollowingAuthor = usrModel!!.isFollowing(curUser.login!!, author).value!!
                        if (isFollowingAuthor) {
                            usrModel!!.unfollow(author)
                        } else {
                            usrModel!!.follow(author)
                        }
                    }
                    true
                }
            }
            holder.optionMenuVod.setOnClickListener {
                val popupMenu = PopupMenu(c, holder.optionMenuVod)
                popupMenu.inflate(R.menu.menu_more_vod)

                popupMenu.setOnMenuItemClickListener {
                    val id = it.itemId
                    if (id == R.id.shareVod) {
                        Toast.makeText(c, "not yet implemented!", Toast.LENGTH_LONG).show()
                    }
                    if (id == R.id.signalVod) {
                        Toast.makeText(c, "not yet implemented", Toast.LENGTH_LONG).show()
                    }
                    if (id == R.id.deleteVod) {
                        if (curUsr.login.equals(obj.authorLogin)) {
                            Toast.makeText(c, "deleting...", Toast.LENGTH_SHORT).show()
                            vodModel!!.deleteVod(obj.idVod).observe(fragment, androidx.lifecycle.Observer {
                                if (it) {
                                    Toast.makeText(c, "deleted (${obj.idVod})", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(c, "delete failed", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            Toast.makeText(c, "not authorized to delete this vod", Toast.LENGTH_SHORT).show()
                        }

                    }
                    true
                }
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): VodAdapter.Holder {
        // create a new view
        val viewGroup = LayoutInflater.from(parent.context)
                .inflate(R.layout.vod_item, parent, false) as ViewGroup
        // set the view's size, margins, paddings and layout parameters
        val holder = Holder(viewGroup)
        holder.setIsRecyclable(false)
        return holder
    }

    inner class Holder : RecyclerView.ViewHolder {
        var authorLoginBtn : TextView
        var titleTv : TextView
        var timeTv : TimeTextView
        var progressTimeTv : TimeTextView
        var timeVodText : TextView
        var playPauseBtn : PlayPauseButton
        var seekbar : SeekBar
        var authorBtn : Button

        var likeVodBtn : ImageView
        var likeLayout : ViewGroup
        var numberOfLikeTv : TextView
        var optionMenuVod : ImageButton
        var  isVodLiked : TextView

        constructor(v : ViewGroup) : super(v){
            authorLoginBtn = v.findViewById(R.id.authorBtn)
            titleTv = v.findViewById(R.id.titleTv)
            timeTv = v.findViewById(R.id.timeTv)
            timeVodText = v.findViewById(R.id.timeVodText)
            progressTimeTv = v.findViewById(R.id.progressTimeTv)
            playPauseBtn = v.findViewById(R.id.playButton)
            seekbar = v.findViewById(R.id.mediaSeekbar)

            authorBtn = v.findViewById(R.id.authorBtn)
            likeVodBtn = v.findViewById(R.id.btnLikeVod)
            likeLayout = v.findViewById(R.id.likeLayout)
            numberOfLikeTv = v.findViewById(R.id.numberOfLikeTv)
            optionMenuVod = v.findViewById(R.id.optionMenuVod)
            isVodLiked = v.findViewById(R.id.isVodLiked)
        }
    }

    private fun secondToTimeStr(timeElapsed: Int) : String{
        val secondes : Int
        val minutes : Int
        val heures : Int
        val jours : Int
        val semaine : Int
        val mois : Int
        val annees : Int

        val resource = c.resources

        val str : String

        secondes = timeElapsed
        heures = timeElapsed / 3600
        jours = heures / 24
        minutes = secondes / 60
        semaine = jours / 7
        mois = jours / 30
        annees = mois / 12
        if(secondes < 60){ // en secondes
            val numberStr = resource.getQuantityString(R.plurals.number_of_seconds, secondes)
            str = resource.getString(R.string.there_is_time,secondes,numberStr)
        }else if(minutes < 60){ // en minutes
            val numberStr = resource.getQuantityString(R.plurals.number_of_minutes, minutes)
            str = resource.getString(R.string.there_is_time,minutes,numberStr)
        }else if(heures < 24){ // en heures
            val numberStr = resource.getQuantityString(R.plurals.number_of_hours, heures)
            str = resource.getString(R.string.there_is_time,heures,numberStr)
        }else if(jours < 7){ // en jours
            val numberStr = resource.getQuantityString(R.plurals.number_of_days, jours)
            str = resource.getString(R.string.there_is_time,jours,numberStr)
        }else if(jours < 30){ // en semaine
            val numberStr = resource.getQuantityString(R.plurals.number_of_weeks, semaine)
            str = resource.getString(R.string.there_is_time,semaine,numberStr)
        }else if(mois < 12){// en mois
            val numberStr = resource.getQuantityString(R.plurals.number_of_months, mois)
            str = resource.getString(R.string.there_is_time,mois,numberStr)
        }else{// en annee
            val numberStr = resource.getQuantityString(R.plurals.number_of_years, annees)
            str = resource.getString(R.string.there_is_time,annees,numberStr)
        }
        return str

    }
}