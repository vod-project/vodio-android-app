package app.vodio.com.vodio.activities

import android.content.Intent
import android.os.Bundle
import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.fragments.LoginFragment
import app.vodio.com.vodio.fragments.RegisterFragment
import app.vodio.com.vodio.services.LoginService

class LoginActivity : AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var usr: User? = LoginService.getInstance()?.loggedIn
        if (usr != null) {
            val intent: Intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showFragment(LoginFragment(), getMainLayout())
        }
    }

    fun authenticated() {
        var intent: Intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    // ABstract activity
    override fun getMainLayout(): Int {return R.id.mainLoginLayout}
    override fun getBackgroundId(): Int {return R.drawable.background_login_light}
    override fun getLayoutContentView(): Int {return R.layout.activity_main}
    override fun onItemSelected(itemId: Int) {
        var intent: Intent? = null
        when (itemId) {
            R.id.signUpButton -> showFragment(RegisterFragment(), getMainLayout())
            R.id.signInPerformButton -> {
            }
            R.id.registerPerformButton -> {
            }
        }
        if (intent != null) {
            startActivity(intent)
        }
    }

}