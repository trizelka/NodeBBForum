package com.trizelka.myforum.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trizelka.myforum.Constants;
import com.trizelka.myforum.ForumService;
import com.trizelka.myforum.utils.SettingsHelper;
import com.trizelka.myforum.utils.UserData;
import com.trizelka.myforum.controller.VolleyController;
import com.trizelka.myforum.R;

public class LoginActivity extends AppCompatActivity {

    public final static String USER_NAME = "userlogin";
    private static final String TAG = "Login";

    private Button bLogin, bSign;
    private EditText tUser, tPassword;

    private int cek;

    String token, tokenSignUp;

    public ProgressDialog progress;
    VolleyController volleyController = new VolleyController();

    private String _id,name,first, last, gender, birth, email, password, country, profesi, imgg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //bFb = (Button) findViewById(R.id.btnFbLogin);
        bLogin = (Button) findViewById(R.id.btnLogin);
       // bTwit = (Button) findViewById(R.id.btnTwitLogin);
        bSign = (Button) findViewById(R.id.btnSignUp);
        tUser = (EditText) findViewById(R.id.txUserLogin);
        tUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tUser.setHint("");
                return false;
            }
        });
        //progress = new ProgressDialog(this);
        tUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tUser.setHint("email");
                }
            }
        });
        tPassword = (EditText) findViewById(R.id.txPasswordLogin);
        tPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tPassword.setHint("");
                return false;
            }
        });

        tPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tPassword.setHint("password");
                }
            }
        });
        //bFb.setVisibility(View.INVISIBLE);
        //bTwit.setVisibility(View.INVISIBLE);

        //mydb = new DBHelper(this);
        actionLogin();
        progress = new ProgressDialog(this);

    }

    private void actionLogin(){


        bSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
                // new JSONSignUp().execute();
            }
        });
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cek = mydb.findUser(tUser.getText().toString(), tPassword.getText().toString());
               /* switch (cek) {
                    case 0:
                        notif("Username not found");
                        break;
                    case 1:

                        openMain();
                        break;
                    case 2:
                        notif("Wrong Password");
                        break;

                }*/
                //new JSONLogin().execute();
                String email = tUser.getText().toString();
                String pass = tPassword.getText().toString();
                Intent intent = new Intent(getApplicationContext(), ForumService.class);
                intent.setAction(ForumService.ACTION_LOGIN);
                intent.putExtra(Constants.LOGIN_EMAIL, email);
                intent.putExtra(Constants.LOGIN_PASSWORD, pass);
                getApplicationContext().startService(intent);
                Log.w(TAG, "send Request Login to service,...");
                progress.setMessage("Loging in...");
                progress.setCancelable(false);
                progress.show();
                //try {
                //    JSONObject data = new JSONObject();
                //    data.put("email", email);
                //    data.put("password", pass);
                //    progress.setMessage("Loging in...");
                //    progress.setCancelable(false);
                //    progress.show();
                //    JSONLogin(Release.UrlLogin, Request.Method.POST, data, "Login");
                //} catch (JSONException e) {
                //    Log.w(TAG, "Registration Error: " + e.toString());
                //}

            }
        });

    }

    private void saveUserData(){
        UserData.saveEmail(this,email);
        UserData.saveName(this,name);
        UserData.saveToken(this, token);
        UserData.saveId(this, _id);
    }


    private void loadJSON(){
/*
        if(mydb.insertContact(first,last,gender,email,birth,password,country,profesi,""))
                {
                    tost("Insert DB OK");
                    finish();
                }else
                {
                    tost("Error insert DB");
                }*/
        //SettingsHelper.saveUsername(this,getD(tUser.getText().toString()));
        //SettingsHelper.saveUri(this, mydb.getImg(tUser.getText().toString()));

        //SettingsHelper.saveEmail(this, tUser.getText().toString());
        SettingsHelper.saveUser(this,"user");
        UserData.saveName(this, tUser.getText().toString());
        UserData.saveToken(this, token);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        //progress.show();
        //JSONGetProfile(Release.UrlGetProfile, Request.Method.GET, "getProfile");
        //Intent i = new Intent(this, LoadJson.class);
       // i.putExtra(USER_NAME, tUser.getText().toString());
        //startActivity(i);
        //finish();
        //notif("Success");
        Intent intent1 = new Intent(this, ForumService.class);
        intent1.setAction(ForumService.ACTION_GET_PUBLIC);
        this.startService(intent1);
    }

//     private String getD(String user){
//        Cursor c = mydb.getField(user);
//        c.moveToFirst();
//        return  c.getString(c.getColumnIndex(DBHelper.COLUMN_FIRSTNAME)) + " " +
//                c.getString(c.getColumnIndex(DBHelper.COLUMN_LASTNAME));
//    }

    private void notif(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder

                .setMessage(message)
                .setCancelable(false)

                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void tost(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openRegister(){
        Intent i = new Intent(this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    private void openMain(){
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
