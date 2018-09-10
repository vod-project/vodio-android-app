package app.vodio.com.vodio.fragments


import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.LoginActivity
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.utils.AuthentificationChecker
import app.vodio.com.vodio.utils.tasking.OnCompleteAsyncTask
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
        return inflater.inflate(R.layout.fragment_register, container, false)
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
        return  checkNameAndUpdateView(setErrorMessage) && checkSurnameAndUpdateView(setErrorMessage) &&
                checkEmailAndUpdateView(setErrorMessage) && checkLoginAndUpdateView(setErrorMessage) &&
                checkPasswordAndUpdateView(setErrorMessage)
    }

    private fun checkLoginAndUpdateView(setErrorMessage: Boolean): Boolean {
        val login = loginFieldRegister.text.toString()
        var res = false
        val list = AuthentificationChecker.checkLogin(login)
        if (list.isEmpty()) {
            nameEditLayout!!.error = ""
            res = true
        } else {
            if (setErrorMessage) {
                nameEditLayout!!.error = AuthentificationChecker.checkLogin(login).toString()
            }
            res = false
        }
        return res
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

    private fun checkSurnameAndUpdateView(setErrorMessage: Boolean): Boolean {
        val name = surnameFieldRegister.text.toString()
        var res = false
        val list = AuthentificationChecker.checkSurname(name)
        if (list.isEmpty()) {
            surnameEditLayout!!.error = ""
            res = true
        } else {
            if (setErrorMessage) {
                surnameEditLayout!!.error = AuthentificationChecker.checkSurname(name).toString()
            }
            res = false
        }
        return res
    }

    private fun checkEmailAndUpdateView(setErrorMessage: Boolean): Boolean {
        val login = emailFieldRegister.text.toString()
        var res = false
        val list = AuthentificationChecker.checkEmail(login)
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
        val surname = surnameFieldRegister.text.toString()
        val email = emailFieldRegister.text.toString()
        val login = loginFieldRegister.text.toString()
        val password = passwordFieldRegister.text.toString()
        if (checkFieldsAndUpdateView(true)) {
            LoginRepository.getInstance(context!!).signUp(login, password, name, surname, email, OnCompleteRegister())
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

    override fun getIconId(): Int? {
        return null
    }
}
