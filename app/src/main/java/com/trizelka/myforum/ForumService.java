package com.trizelka.myforum;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.trizelka.myforum.utils.SettingsHelper;
import com.trizelka.myforum.utils.UserData;

import com.trizelka.myforum.database.DataProvider;
import com.trizelka.myforum.ui.SplashActivity;
import com.trizelka.myforum.utils.JSONSharedPreferences;
import com.trizelka.myforum.controller.VolleyController;
import com.trizelka.myforum.ui.MainActivity;

/**
 * Created by NB01 on 10/5/2015.
 */
public class ForumService extends Service {
    private static final String TAG = "ForumService";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Binder binder = new ForumServiceBinder();
    public final Handler mHandler = new Handler();
    VolleyController volleyController = new VolleyController();
    String token, tokenSignUp;
    ProgressDialog progress;
    private String username,password;
    private String selectedImagePath;

    public static final String ACTION_GET_CONFIG = "com.trizelka.myforum.ForumService.GET_CONFIG";
    public static final String ACTION_LOGIN = "com.trizelka.myforum.ForumService.LOGIN";
    public static final String ACTION_SIGNUP = "com.trizelka.myforum.ForumService.SIGNUP";
    public static final String ACTION_GET_PUBLIC = "com.trizelka.myforum.ForumService.GET_PUBLIC";
    public static final String ACTION_GET_REGISTER_STATUS = "com.trizelka.myforum.ForumService.GET_REGISTER_STATUS";
    public static final String ACTION_GET_USERS = "com.trizelka.myforum.ForumService.GET_USERS";
    public static final String ACTION_GET_PROFILE = "com.trizelka.myforum.ForumService.GET_PROFILE";
    public static final String ACTION_LOGOUT = "com.trizelka.myforum.ForumService.LOGOUT";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "Service onCreate() called...");
        progress = new ProgressDialog(this);

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();

            if (action.equals(ACTION_GET_CONFIG)
                    || (action.equals((ACTION_LOGIN)))
                    || (action.equals((ACTION_SIGNUP)))
                    || (action.equals(ACTION_GET_PUBLIC))
                    || (action.equals(ACTION_GET_REGISTER_STATUS))
                    || (action.equals(ACTION_GET_USERS))
                    || (action.equals(ACTION_GET_PROFILE)
                    || (action.equals(ACTION_LOGOUT))))

            {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (action.equals(ACTION_GET_CONFIG)) handleGetConfigIntent(intent);
                        if (action.equals(ACTION_LOGIN)) handleLoginIntent(intent);
                        if (action.equals(ACTION_SIGNUP)) handleSignUpIntent(intent);
                        if (action.equals(ACTION_GET_PUBLIC)) handlegetPublicIntent(intent);
                        if (action.equals(ACTION_GET_REGISTER_STATUS)) handleGetRegisterStatusIntent(intent);
                        if (action.equals(ACTION_GET_USERS)) handleGetUsersIntent(intent);
                        if (action.equals(ACTION_GET_PROFILE)) handleGetProfileIntent(intent);
                        if (action.equals(ACTION_LOGOUT)) handleLogoutIntent(intent);

                    }
                });

            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w(TAG, "Service onStart() called...");
    }

    private void JSONObjectRequest(final String urls, final int method, final String action) {
        try {
            Log.w(TAG, "send http request to " + urls + ", " + method + ", " + action);
            JsonObjectRequest result = new JsonObjectRequest(method, urls, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    JSONObject data = response;
                    Log.w(TAG, "response: " + data.toString());
                    if (action == "getConfig") {
                        if (response != null) {
                            try {
                                JSONSharedPreferences.saveJSONObject(getApplicationContext(), "DATA_CONFIG", "_CONFIG", data);
                            }catch(Exception ee) {
                                Log.w("JSONGetConfig","error: "+ee);
                            }
                            Log.w("JSONGetConfig", "response: " + data.toString());
                        } else {
                            Log.w("JSONGetConfig", "ERROR GET DATA CONFIG");
                        }
                    }
                    if (action == "Login") {
                        if (response.equals("/")) {
                            Log.w("JSONLogin", "Login Successfully ");
                        } else {
                            Log.w("JSONLogin", "ERROR GET DATA LOGIN");
                        }
                    }
                    if (action == "getPublic") {
                        if (response != null) {
                            JSONSharedPreferences.saveJSONObject(getApplicationContext(), "DATA_PUBLIC", "_PUBLIC", data);
                        } else {
                            Log.w("JSONPublic", "ERROR GET DATA PUBLIC");
                        }
                    }
                    if (action == "getRegister") {
                        if (response != null) {
                            JSONSharedPreferences.saveJSONObject(getApplicationContext(), "DATA_REGISTER", "_REGISTER", data);
                        } else {
                            Log.w("JSONRegister", "ERROR GET DATA REGISTER");
                        }
                    }
                    if (action == "getUsers") {
                        if (response != null) {
                            JSONSharedPreferences.saveJSONObject(getApplicationContext(), "DATA_USERS", "_USERS", data);
                        } else {
                            Log.w("JSONUsers", "ERROR GET DATA USERS");
                        }
                    }
                    if (action == "getProfile") {
                        if (response != null) {
                            JSONSharedPreferences.saveJSONObject(getApplicationContext(), "DATA_PROFILE", "_PROFILE", data);
                        } else {
                            Log.w("JSONProfile", "ERROR GET DATA PROFILE");
                        }
                    }
                    if (action == "Logout") {
                        if (response != null) {
                            Log.w("JSONLogout", "LOGOUT Successfully");
                        } else {
                            Log.w("JSONLogout", "ERROR GET DATA LOGOUT");
                        }
                    }

                }
            }, responseErrorListener(action)) {
                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //    String credentials = localNumber + ":" + password;
                    //    Log.w("GummyPhoneService", "HeartBeat BasicAuth: " + credentials);
                    //    String encodedCredentials = android.util.Base64.encodeToString(credentials.getBytes(), android.util.Base64.NO_WRAP);
                    try {
                        JSONObject data = JSONSharedPreferences.loadJSONObject(getApplicationContext(), "DATA_CONFIG", "_CONFIG");
                        String token = data.optString("csrf_token");
                        headers.put("csrf_token", token);
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        Log.w(TAG, "header: " + headers);
                    }catch(Exception ee){
                        Log.w("JSONLogin","error: "+ee);
                    }

                    return headers;
                }
            };
            //;

            result.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            volleyController.getInstance().addToRequestQueue(result);
        } catch (Exception ee) {
            Log.w(TAG, "error: " + ee);
        }
    }

    private Response.ErrorListener responseErrorListener(final String action) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    try {
                        Log.w(TAG, "Network Error!, Please Check your Internet Connection " + error);
                        Intent i = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
                        i.putExtra(Constants.ONLINE_STATUS, "Offline");
                        getApplicationContext().sendBroadcast(i);
                    } catch (Exception ee) {
                        Log.w(TAG, "Network error: " + ee);
                    }
                } else if (error instanceof ServerError) {
                    try {
                        Log.w(TAG, "Request Error!, Server Error " + error);
                        Intent i = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
                        i.putExtra(Constants.ONLINE_STATUS, "Offline");
                        getApplicationContext().sendBroadcast(i);
                    } catch (Exception ee) {
                        Log.w(TAG, "request error: " + ee);
                    }
                } else if (error instanceof AuthFailureError) {
                    try {
                        Log.w(TAG, "Authentication Error!, Please Check username/password/token " + action + ", " + error);
                        Intent i = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
                        i.putExtra(Constants.ONLINE_STATUS, "Offline");
                        getApplicationContext().sendBroadcast(i);
                    } catch (Exception ee) {
                        Log.w(TAG, "authentication error: " + ee);
                    }
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    try {
                        Log.w(TAG, "Connection Error!, Please Check your Internet Connection " + error);
                        Intent i = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
                        i.putExtra(Constants.ONLINE_STATUS, "Offline");
                        getApplicationContext().sendBroadcast(i);
                    } catch (Exception ee) {
                        Log.w(TAG, "connection error: " + ee);
                    }
                } else if (error instanceof TimeoutError) {
                    try {
                        Log.w(TAG, "Timeout Error!, Connection Timeout " + error);
                        Intent i = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
                        i.putExtra(Constants.ONLINE_STATUS, "Offline");
                        getApplicationContext().sendBroadcast(i);
                    } catch (Exception ee) {
                        Log.w(TAG, "timeout error: " + ee);
                    }
                } else {
                    try {
                        Log.w(TAG, "Connection Error!, Connection Timeout " + error);
                    } catch (Exception ee) {
                        Log.w(TAG, "connection error: " + ee);
                    }
                }

                progress.dismiss();
            }
        };
    }

    public class ForumServiceBinder extends Binder {
        public ForumService getService() {
            return ForumService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        executor.shutdown();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private void handleGetConfigIntent(Intent intent){
        JSONObjectRequest(Release.UrlgetConfig, Request.Method.GET,"getConfig");
    }
    private void handleLoginIntent(Intent intent) {
        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");
        String url = Release.UrlLogin+"?username="+username+"&password="+password;
        JSONObjectRequest(url, Request.Method.POST,"Login");
    }
    private void handlegetPublicIntent(Intent intent){
        JSONObjectRequest(Release.UrlGetPublic, Request.Method.GET,"getPublic");
    }
    private void handleSignUpIntent(Intent intent){
        JSONObjectRequest(Release.UrlSignUp, Request.Method.POST,"SignUp");
    }
    private void handleGetRegisterStatusIntent(Intent intent){
        JSONObjectRequest(Release.UrlGetRegister, Request.Method.GET,"getRegister");
    }
    private void handleGetUsersIntent(Intent intent){
        JSONObjectRequest(Release.UrlGetUsers, Request.Method.GET,"getUsers");
    }
    private void handleGetProfileIntent(Intent intent){
        JSONObjectRequest(Release.UrlGetProfile, Request.Method.GET,"getProfile");
    }
    private void handleLogoutIntent(Intent intent){
        JSONObjectRequest(Release.UrlLogout, Request.Method.POST, "Logout");
    }

    private void openMainActivity(String status){
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.MAIN_STATUS, status);
        startActivity(i);
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}