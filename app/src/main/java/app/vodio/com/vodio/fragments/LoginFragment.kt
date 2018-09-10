package app.vodio.com.vodio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.LoginActivity
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.utils.AuthentificationChecker
import app.vodio.com.vodio.utils.tasking.OnCompleteAsyncTask
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
class LoginFragment : AbstractFragment() {
    override fun getIconId(): Int? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onResume() {
        super.onResume()
        // setup on click listeners
        signInPerformButton.setOnClickListener(this)
        signUpButton!!.setOnClickListener(this)
        passwordForgotLogin.setOnClickListener(this)
        // setup on text change listener
        loginFieldLogin.addTextChangedListener(LoginPasswordTextWatcher())
        passwordFieldLogin.addTextChangedListener(LoginPasswordTextWatcher())
        // setup error enabled for edit text
        loginEditTextLogin.isErrorEnabled = true
        passwordEditTextLogin.isErrorEnabled = true
    }

    override fun onClick(v: View) {
        if (v === signInPerformButton) {
            performSignIn()
        }
        if (v === passwordForgotLogin) {
            Toast.makeText(context, "forgot", Toast.LENGTH_SHORT).show()
        }
        onItemSelected(v.id)
    }

    fun checkLoginAndUpdateView(setErrorMessage: Boolean): Boolean {
        val login = loginFieldLogin.text.toString()
        if (AuthentificationChecker.checkLogin(login).isEmpty()) {
            loginEditTextLogin.error = ""
            return true
        } else {
            if (setErrorMessage) {
                val list = AuthentificationChecker.checkLogin(login)
                loginEditTextLogin.error = list.toString()
            }
            return false
        }
    }

    fun checkPasswordAndUpdateView(setErrorMessage: Boolean): Boolean {
        val password = passwordFieldLogin!!.text.toString()
        if (AuthentificationChecker.checkPasswordBoolean(password)) {
            passwordEditTextLogin.error = ""
            return true
        } else {
            if (setErrorMessage) {
                val list = AuthentificationChecker.checkPassword(password)
                passwordEditTextLogin.error = list.toString()
            }
            return false
        }
    }

    fun checkFieldsAndUpdateView(setErrorMessage: Boolean): Boolean {
        return checkLoginAndUpdateView(setErrorMessage) && checkPasswordAndUpdateView(setErrorMessage)
    }

    private fun performSignIn() {
        val login = loginFieldLogin.text.toString()
        val password = passwordFieldLogin.text.toString()
        loginProgressBar.visibility = View.VISIBLE
        if (checkFieldsAndUpdateView(true)) {
            LoginRepository.getInstance(context!!).signIn(login, password, OnCompleteLogin())
        } else {
            authenticationFail("wrong fields")
        }
    }

    private fun authenticationSuccess() {
        val parentAct = this@LoginFragment.parent as LoginActivity?
        parentAct!!.authenticated()
    }

    private fun authenticationFail(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        loginProgressBar.visibility = View.GONE
        passwordFieldLogin.setText("")
    }

    internal inner class LoginPasswordTextWatcher : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            checkFieldsAndUpdateView(false)
        }
    }

    internal inner class OnCompleteLogin : OnCompleteAsyncTask {
        override fun onSuccess(obj: Any) {
            authenticationSuccess()
        }

        override fun onFail(t : Throwable) {
            authenticationFail("sign in failed")
        }
    }
}
