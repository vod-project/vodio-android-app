package app.vodio.com.vodio.activities

import android.os.Bundle
import app.vodio.com.vodio.R
import app.vodio.com.vodio.fragments.ProfileFragment

class ExternalProfileActivity : AbstractActivity() {
    object ParameterName{
        const val USER_KEY = "USER"
    }
    override fun getMainLayout(): Int {return R.id.profile_fragment}
    override fun getBackgroundId(): Int {return R.drawable.background_login_light}
    override fun getLayoutContentView(): Int {return R.layout.activity_external_profile}
    override fun onItemSelected(itemId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.extras.containsKey(ParameterName.USER_KEY)){
            val frag = ProfileFragment()
            val bundle = Bundle()
            bundle.putString(ParameterName.USER_KEY, intent.extras.getString(ParameterName.USER_KEY))
            frag.arguments = bundle
            showFragment(frag, R.id.profile_fragment)
        }
    }
}