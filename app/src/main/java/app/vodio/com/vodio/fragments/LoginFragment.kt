package app.vodio.com.vodio.fragments

import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.LoginActivity
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.AuthentificationChecker
import app.vodio.com.vodio.utils.OnCompleteAsyncTask


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
class LoginFragment : AbstractFragment() {
    //clickable
    private var signInButton: Button? = null
    private var signUpButton: Button? = null
    private var forgotPasswordView: TextView? = null

    //fields
    private var tInputLayoutLogin: TextInputLayout? = null
    private var loginField: EditText? = null
    private var tInputLayoutPassword: TextInputLayout? = null
    private var passwordField: EditText? = null

    //
    private var progressBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        //clickable
        signInButton = rootView.findViewById(R.id.signInPerformButton)
        signUpButton = rootView.findViewById(R.id.signUpButton)
        forgotPasswordView = rootView.findViewById(R.id.passwordForgotLogin)

        // fields
        tInputLayoutLogin = rootView.findViewById(R.id.loginEditTextLogin)
        loginField = rootView.findViewById(R.id.loginFieldLogin)
        tInputLayoutPassword = rootView.findViewById(R.id.passwordEditTextLogin)
        passwordField = rootView.findViewById(R.id.passwordFieldLogin)

        // progressbar
        progressBar = rootView.findViewById(R.id.loginProgressBar)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        // setup on click listeners
        signInButton!!.setOnClickListener(this)
        signUpButton!!.setOnClickListener(this)
        forgotPasswordView!!.setOnClickListener(this)
        // setup on text change listener
        loginField!!.addTextChangedListener(LoginPasswordTextWatcher())
        passwordField!!.addTextChangedListener(LoginPasswordTextWatcher())
        // setup error enabled for edit text
        tInputLayoutLogin!!.isErrorEnabled = true
        tInputLayoutPassword!!.isErrorEnabled = true
    }

    override fun onClick(v: View) {
        if (v === signInButton) {
            performSignIn()
        }
        if (v === forgotPasswordView) {
            Toast.makeText(context, "forgot", Toast.LENGTH_SHORT).show()
        }
        onItemSelected(v.id)
    }

    fun checkLoginAndUpdateView(setErrorMessage: Boolean): Boolean {
        val login = loginField!!.text.toString()
        if (AuthentificationChecker.checkLogin(login).isEmpty()) {
            tInputLayoutLogin!!.error = ""
            return true
        } else {
            if (setErrorMessage) {
                val list = AuthentificationChecker.checkLogin(login)
                tInputLayoutLogin!!.error = list.toString()
            }
            return false
        }
    }

    fun checkPasswordAndUpdateView(setErrorMessage: Boolean): Boolean {
        val password = passwordField!!.text.toString()
        if (AuthentificationChecker.checkPasswordBoolean(password)) {
            tInputLayoutPassword!!.error = ""
            return true
        } else {
            if (setErrorMessage) {
                val list = AuthentificationChecker.checkPassword(password)
                tInputLayoutPassword!!.error = list.toString()
            }
            return false
        }
    }

    fun checkFieldsAndUpdateView(setErrorMessage: Boolean): Boolean {
        return checkLoginAndUpdateView(setErrorMessage) && checkPasswordAndUpdateView(setErrorMessage)
    }

    private fun performSignIn() {
        val login = loginField!!.text.toString()
        val password = passwordField!!.text.toString()
        progressBar!!.visibility = View.VISIBLE
        if (checkFieldsAndUpdateView(true)) {
            LoginService.getInstance().signIn(login, password, OnCompleteLogin(), context)
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
        progressBar!!.visibility = View.GONE
        passwordField!!.setText("")
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

        override fun onFail() {
            authenticationFail("sign in failed")
        }
    }
}// Required empty public constructor
