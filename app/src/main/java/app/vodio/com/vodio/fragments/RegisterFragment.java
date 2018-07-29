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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.activities.LoginActivity;
import app.vodio.com.vodio.services.LoginService;
import app.vodio.com.vodio.utils.AuthentificationChecker;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RegisterFragment extends AbstractFragment{

    // clickable
    private Button registerButton;

    //fields
    private TextInputLayout nameEditLayout;
    private EditText nameField;
    private TextInputLayout loginEditLayout;
    private EditText loginField;
    private TextInputLayout passwordEditLayout;
    private EditText passwordField;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // clickable
        registerButton = v.findViewById(R.id.registerPerformButton);

        //fields
        nameEditLayout = v.findViewById(R.id.nameEditLayout);
        nameField = v.findViewById(R.id.nameFieldRegister);
        loginEditLayout = v.findViewById(R.id.loginEditLayout);
        loginField = v.findViewById(R.id.loginFieldRegister);
        passwordEditLayout = v.findViewById(R.id.passwordEditLayout);
        passwordField = v.findViewById(R.id.passwordFieldRegister);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerButton.setOnClickListener(this);
        nameEditLayout.setErrorEnabled(true);
        loginEditLayout.setErrorEnabled(true);
        passwordEditLayout.setErrorEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if(v == registerButton) {
            performSignUp();
        }
        onItemSelected(v.getId());
    }

    public void onItemSelected(int itemId){
        parent.onItemSelected(itemId);
    }

    private void checkFieldsAndUpdateView(boolean setErrorMessage){
        checkNameAndUpdateView(setErrorMessage);
        checkLoginAndUpdateView(setErrorMessage);
        checkPasswordAndUpdateView(setErrorMessage);
    }
    private void checkNameAndUpdateView(boolean setErrorMessage){
        String name = nameField.getText().toString();
        List<AuthentificationChecker.AuthCheckResult> list = AuthentificationChecker.checkName(name);
        if(list.isEmpty()){
            nameEditLayout.setError("");
        }else{
            if(setErrorMessage){
                nameEditLayout.setError(AuthentificationChecker.checkName(name).toString());
            }
        }
    }
    private void checkLoginAndUpdateView(boolean setErrorMessage){
        String login = loginField.getText().toString();
        List<AuthentificationChecker.AuthCheckResult> list = AuthentificationChecker.checkLogin(login);
        if(list.isEmpty()){
            loginEditLayout.setError("");
        }else{
            if(setErrorMessage){
                loginEditLayout.setError(list.toString());
            }
        }
    }
    private void checkPasswordAndUpdateView(boolean setErrorMessage){
        String password = passwordField.getText().toString();
        List<AuthentificationChecker.AuthCheckResult> list = AuthentificationChecker.checkPassword(password);
        if(list.isEmpty()){
            passwordEditLayout.setError("");
        }else{
            if(setErrorMessage){
                passwordEditLayout.setError(list.toString());
            }
        }
    }

    private void performSignUp(){
        String name = nameField.getText().toString();
        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();
        List<AuthentificationChecker.AuthCheckResult> check = LoginService.signUp(login,password,name, new OnCompleteRegister());
        if(check.size() != 0){
            registrationFail();
        }
    }

    private void registrationSuccess(){
        LoginActivity parentAct = (LoginActivity)this.parent;
        parentAct.authenticated();
    }

    private void registrationFail(){
        Toast.makeText(getContext(),"failed",Toast.LENGTH_SHORT).show();
        checkFieldsAndUpdateView(true);
    }

    class OnCompleteRegister implements OnCompleteAsyncTask{

        @Override
        public void onSuccess() {
            registrationSuccess();
        }

        @Override
        public void onFail() {
            registrationFail();
        }
    }

    class FieldsTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            checkFieldsAndUpdateView(false);
        }
    }
}
