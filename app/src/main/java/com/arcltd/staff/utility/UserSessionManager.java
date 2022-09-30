package com.arcltd.staff.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.arcltd.staff.authentication.LoginActivity;

import java.util.HashMap;
import java.util.Set;

public class UserSessionManager {
    public final String PREF_NAME = "Session_Prefrence";
    public static Context baseCtx;
    public static final String USER_NAME = "username", TERMINAL_ID = "terminalId", PASSWORD = "password",URL_PART="urlPart";
    //    public static final String USER_NAME_NL = "usernamenl", TERMINAL_ID_NL = "terminalIdnl";
    public static final String URL_PART_KEY = "urlpartkey", REQ_PARAM_KEY = "req_param_key",JOB_APPLIED_STATUS="jobappliedstatus",JOB_SAVED_STATUS="jobsavedstatus",FB_LOGIN_STATUS = "fbloginStatus",
            HASHMAP_LOGIN="hashmapstring",LOGIN_STATUS="login_status",USER_ID="User_Id",USER_TYPE="Type",STUDENT_ID="student_id",FNAME="fname",LNAME="lname",DOB="dob",GENDER="gender",
            ADDRESS="address";
    SharedPreferences sharedpreferences;

    SharedPreferences.Editor editor;

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";


    public UserSessionManager(Context ctx) {
        this.baseCtx = ctx;
    }


    /**
     * @param user_name  is demand for  user name of the person  whoc is going to login.
     * @param terminalId is demand for user code which is given by service provider.
     * @param password   is demand for password for which  is given by service provider.
     */

    public void setLogin(String user_name, String terminalId, String password) {
        InitSessionBase();
        editor.putString(USER_NAME, user_name);
        editor.putString(TERMINAL_ID, terminalId);
        editor.putString(PASSWORD, password);

        editor.commit();
    }
    public void createUserLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, sharedpreferences.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, sharedpreferences.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    public void logoutUser()
    {

        // Clearing all user data from Shared Preferences
       editor.clear();
       editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(baseCtx, LoginActivity.class);


        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);



        // Staring Login Activity
        baseCtx.startActivity(i);

    }
    public boolean checkLogin()
    {
        InitSessionBase();
        return sharedpreferences.contains(TERMINAL_ID);

    }
    public void setLoginDeatil(String user_name, String terminalId, String password) {
        InitSessionBase();
        editor.putString(USER_NAME, user_name);
        editor.putString(TERMINAL_ID, terminalId);
        editor.putString(PASSWORD,password);
        editor.commit();
    }
    public String getValueOnkeyFromSession(String keyfromSession) {
        String outPut;
        InitSessionBase();
        if (sharedpreferences.contains(keyfromSession)) {
            outPut = sharedpreferences.getString(keyfromSession, "NONE");
        } else {
            outPut = "";
        }
        return outPut;
    }
    public void onSuccessPutParams(String UrlsPart, String requestParams) {
        InitSessionBase();
        editor.putString(URL_PART_KEY, UrlsPart);
        editor.putString(REQ_PARAM_KEY, requestParams);
        editor.commit();
    }
    public void onLoginSuccess(String responce)
    {
        InitSessionBase();
        editor.putString("SESSION_RESPONCE", responce);
        editor.commit();
    }
    public void keyValueInSession(String key, String vlaue)
    {
        InitSessionBase();
        editor.putString(key, vlaue);
        editor.commit();
    }
    public String getSessionData()
    {
        InitSessionBase();
        return sharedpreferences.getString("SESSION_RESPONCE","NONE");
    }
    public void clearDataInSession() {
        InitSessionBase();
        // editor.clear();
        editor.clear();
        // editor.remove(PASSWORD);


        editor.commit();


    }


    private void InitSessionBase()
    {
        sharedpreferences = baseCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public void setUrlData(Context context, String value, String pref)
    {
//        InitSessionBase();
        sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(pref,value);
        editor.commit();

    }
    public String getUrlData(Context context, String key)
    {
//        InitSessionBase();
        String text;
        sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        text=sharedpreferences.getString(key,URL_PART);
        return text;

    }

    public void setCropWork(boolean misCropWork) {
        editor = sharedpreferences.edit();
        editor.putBoolean("misCropWork", misCropWork);
        editor.commit();
    }

    public boolean isCropWork() {
        boolean misCropWork = sharedpreferences.getBoolean("misCropWork", true);
        return misCropWork;
    }



    public void setHashmapData(Context context, HashMap<String, String> value, String pref)
    {
//        InitSessionBase();
        sharedpreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
//        editor.putString(pref,value);
        editor.putStringSet(pref, (Set<String>) value);
        editor.commit();

    }

}
