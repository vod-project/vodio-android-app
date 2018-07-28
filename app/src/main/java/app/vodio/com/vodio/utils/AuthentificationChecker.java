package app.vodio.com.vodio.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AuthentificationChecker {
    private static final int MIN_PASSWORD_LENGTH = 8;

    public static List<AuthCheckResult> check(String login, String password){
        List<AuthCheckResult> resCheck = new ArrayList<>();
        resCheck.addAll(checkLogin(login));
        resCheck.addAll(checkPassword(password));
        return resCheck;
    }

    public static List<AuthCheckResult> checkName(String name){
        List<AuthCheckResult> list = new ArrayList<>();
        if(name.isEmpty())
            list.add(AuthCheckResult.NAME_EMPTY);
        return list;
    }

    public static List<AuthCheckResult> checkLogin(String login){
        List<AuthCheckResult> list = new ArrayList<>();
        if(login.isEmpty()){
            list.add(AuthCheckResult.LOGIN_EMPTY);
        }else{
            if(!validateEmail(login)) {
                list.add(AuthCheckResult.EMAIL_NOT_WELL_FORMED);
            }
        }
        return list;
    }
    public static List<AuthCheckResult> checkPassword(String password){
        List<AuthCheckResult> list = new ArrayList<>();
        if(password.isEmpty()){
            list.add(AuthCheckResult.PASSWORD_EMPTY);
        }else if(password.length() < MIN_PASSWORD_LENGTH){
            list.add(AuthCheckResult.PASSWORD_TOO_SHORT);
        }
        return list;
    }
    public static boolean checkPasswordBoolean(String password){
        boolean isEmpty = password.isEmpty(), isBadLength = password.length() < MIN_PASSWORD_LENGTH;
        if(isEmpty || isBadLength){
            return false;
        }else{
            return true;
        }
    }

    public static List<AuthCheckResult> check(String login, String password, String name){
        List<AuthCheckResult> c = check(login, password);
        c.addAll(checkName(name));
        return c;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public enum AuthCheckResult{
        LOGIN_EMPTY,PASSWORD_EMPTY,OK, DATABASE_ERROR,NAME_EMPTY, EMAIL_NOT_WELL_FORMED,PASSWORD_TOO_SHORT,ALREADY_USED_EMAIL,AUTHENTIFICATION_FAILED;

    }
}
