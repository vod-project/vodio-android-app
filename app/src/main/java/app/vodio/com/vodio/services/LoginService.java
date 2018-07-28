package app.vodio.com.vodio.services;

import android.os.AsyncTask;

import java.util.List;

import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.database.UserMapper;
import app.vodio.com.vodio.utils.AuthentificationChecker;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;


public class LoginService {
    private static User loggedIn;
    public static void setLoggedIn(User usr){loggedIn = usr;}
    public static User getLoggedIn(){return loggedIn;}

    public static List<AuthentificationChecker.AuthCheckResult> signIn(String login, String password, OnCompleteAsyncTask onComplete){
        List<AuthentificationChecker.AuthCheckResult> checkResult = AuthentificationChecker.check(login, password);
        if(checkResult.size() == 0) {
            AsyncTask getTask = new UserMapper.GetUserTask(login,password, onComplete);
            getTask.execute();
        }
        return checkResult;
    }

    public static List<AuthentificationChecker.AuthCheckResult> signUp(String login, String password,String name, OnCompleteAsyncTask onComplete) {
        List<AuthentificationChecker.AuthCheckResult> checkResult = AuthentificationChecker.check(login, password,name);
        if(checkResult.size() == 0) {
            final User usr = new User();
            usr.setLogin(login);usr.setPassword(password);usr.setName(name);
            AsyncTask createTask = new UserMapper.CreateUserTask(usr, onComplete);
            createTask.execute();
        }
        return checkResult;
    }
}
