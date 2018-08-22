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
import android.widget.Toast

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.LoginActivity
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.AuthentificationChecker
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
// Using R.layout.activity_main from the main source set
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class RegisterFragment : AbstractFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_register, container, false)
        return v
    }

    override fun onResume() {
        super.onResume()
        registerPerformButton.setOnClickListener(this)
        nameEditLayout!!.isErrorEnabled = true
        loginEditLayout!!.isErrorEnabled = true
        passwordEditLayout!!.isErrorEnabled = true
    }

    override fun onClick(v: View) {
        if (v === registerPerformButton) {
            performSignUp()
        }
        onItemSelected(v.id)
    }

    private fun checkFieldsAndUpdateView(setErrorMessage: Boolean): Boolean {
        return checkNameAndUpdateView(setErrorMessage) &&
                checkLoginAndUpdateView(setErrorMessage) &&
                checkPasswordAndUpdateView(setErrorMessage)
    }

    private fun checkNameAndUpdateView(setErrorMessage: Boolean): Boolean {
        val name = nameFieldRegister.text.toString()
        var res = false
        val list = AuthentificationChecker.checkName(name)
        if (list.isEmpty()) {
            nameEditLayout!!.error = ""
            res = true
        } else {
            if (setErrorMessage) {
                nameEditLayout!!.error = AuthentificationChecker.checkName(name).toString()
            }
            res = false
        }
        return res
    }

    private fun checkLoginAndUpdateView(setErrorMessage: Boolean): Boolean {
        val login = loginFieldRegister.text.toString()
        var res = false
        val list = AuthentificationChecker.checkLogin(login)
        if (list.isEmpty()) {
            loginEditLayout!!.error = ""
            res = true
        } else {
            if (setErrorMessage) {
                loginEditLayout!!.error = list.toString()
            }
            res = false
        }
        return res
    }

    private fun checkPasswordAndUpdateView(setErrorMessage: Boolean): Boolean {
        val password = passwordFieldRegister.text.toString()
        var res = false
        val list = AuthentificationChecker.checkPassword(password)
        if (list.isEmpty()) {
            passwordEditLayout!!.error = ""
            res = true
        } else {
            if (setErrorMessage) {
                passwordEditLayout!!.error = list.toString()
            }
            res = false
        }
        return res
    }

    private fun performSignUp() {
        val name = nameFieldRegister.text.toString()
        val login = loginFieldRegister.text.toString()
        val password = passwordFieldRegister.text.toString()
        if (checkFieldsAndUpdateView(true)) {
            LoginService.getInstance()?.signUp(login, password, name, OnCompleteRegister())
        } else {
            registrationFail("wrong fields")
        }
    }

    private fun registrationSuccess() {
        val parentAct = this.parent as LoginActivity?
        parentAct!!.authenticated()
    }

    private fun registrationFail(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        checkFieldsAndUpdateView(true)
    }

    internal inner class OnCompleteRegister : OnCompleteAsyncTask {
        override fun onSuccess(obj: Any) {
            registrationSuccess()
        }

        override fun onFail(t : Throwable) {
            registrationFail("registration failed")
        }
    }

    internal inner class FieldsTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            checkFieldsAndUpdateView(false)
        }
    }
}// Required empty public constructor
