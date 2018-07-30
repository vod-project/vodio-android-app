package app.vodio.com.vodio.fragments;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.activities.LoginActivity;
import app.vodio.com.vodio.services.LoginService;
import app.vodio.com.vodio.utils.AuthentificationChecker;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class LoginFragment extends AbstractFragment{
    //clickable
    private Button signInButton;
    private Button signUpButton;
    private TextView forgotPasswordView;

    //fields
    private TextInputLayout tInputLayoutLogin;
    private EditText loginField;
    private TextInputLayout tInputLayoutPassword;
    private EditText passwordField;

    //
    private ProgressBar progressBar;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        //clickable
        signInButton = rootView.findViewById(R.id.signInPerformButton);
        signUpButton = rootView.findViewById(R.id.signUpButton);
        forgotPasswordView = rootView.findViewById(R.id.passwordForgotLogin);

        // fields
        tInputLayoutLogin = rootView.findViewById(R.id.loginEditTextLogin);
        loginField = rootView.findViewById(R.id.loginFieldLogin);
        tInputLayoutPassword = rootView.findViewById(R.id.passwordEditTextLogin);
        passwordField = rootView.findViewById(R.id.passwordFieldLogin);

        // progressbar
        progressBar = rootView.findViewById(R.id.loginProgressBar);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        // setup on click listeners
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        forgotPasswordView.setOnClickListener(this);
        // setup on text change listener
        loginField.addTextChangedListener(new LoginPasswordTextWatcher());
        passwordField.addTextChangedListener(new LoginPasswordTextWatcher());
        // setup error enabled for edit text
        tInputLayoutLogin.setErrorEnabled(true);
        tInputLayoutPassword.setErrorEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if(v == signInButton){
            performSignIn();
        }
        if(v == forgotPasswordView){
            Toast.makeText(getContext(),"forgot", Toast.LENGTH_SHORT).show();
        }
        onItemSelected(v.getId());
    }

    public void onItemSelected(int itemId) {
        if(parent != null) {
            parent.onItemSelected(itemId);
        }
    }

    public boolean checkLoginAndUpdateView(boolean setErrorMessage){
        String login = loginField.getText().toString();
        if(AuthentificationChecker.checkLogin(login).isEmpty()){
            tInputLayoutLogin.setError("");
            return true;
        }else{
            if(setErrorMessage){
                List<AuthentificationChecker.AuthCheckResult> list = AuthentificationChecker.checkLogin(login);
                tInputLayoutLogin.setError(list.toString());
            }
            return false;
        }
    }

    public boolean checkPasswordAndUpdateView(boolean setErrorMessage){
        String password = passwordField.getText().toString();
        if(AuthentificationChecker.checkPasswordBoolean(password)){
            tInputLayoutPassword.setError("");
            return true;
        }else{
            if(setErrorMessage){
                List<AuthentificationChecker.AuthCheckResult> list = AuthentificationChecker.checkPassword(password);
                tInputLayoutPassword.setError(list.toString());
            }
            return false;
        }
    }

    public boolean checkFieldsAndUpdateView(boolean setErrorMessage){
        return checkLoginAndUpdateView(setErrorMessage) && checkPasswordAndUpdateView(setErrorMessage);
    }

    private void performSignIn(){
        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if(checkFieldsAndUpdateView(true)){
            LoginService.signIn(login,password,new OnCompleteLogin());
        }else{
            authenticationFail("wrong fields");
        }
    }

    private void authenticationSuccess(){
        LoginActivity parentAct = (LoginActivity)LoginFragment.this.parent;
        parentAct.authenticated();
    }

    private void authenticationFail(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        passwordField.setText("");
    }

    class LoginPasswordTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            checkFieldsAndUpdateView(false);
        }
    }

    class OnCompleteLogin implements OnCompleteAsyncTask{
        @Override
        public void onSuccess() {
            authenticationSuccess();
        }

        @Override
        public void onFail() {
            authenticationFail("sign in failed");
        }
    }
}
