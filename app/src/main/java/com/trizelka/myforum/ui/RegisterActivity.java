package com.trizelka.myforum.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import com.trizelka.myforum.Constants;
import com.trizelka.myforum.Release;
import com.trizelka.myforum.ForumService;
import com.trizelka.myforum.utils.SettingsHelper;
import com.trizelka.myforum.utils.UserData;
import com.trizelka.myforum.controller.VolleyController;

import com.trizelka.myforum.R;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterUser";

    private Toolbar mToolbar;
    private Button bRegister, bBack, bNext, bPhoto;
    private Spinner spGender, spCountry, spProfesi;
    private TextView tvToolbar;
    private ImageView imgProfile;
    private CheckBox cbAccept;

    private EditText tFirst, tLast, tEmail, tPassword;
    private TextView tBirth;
    final String tgl = "";

    private String selectedImagePath;
    private String first, last, gender, birth, email, password, country, profesi, imgg;

    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormat;

    private int year;
    private int month;
    private int day;

    private String currentDate;
    private Context context;

    ProgressDialog progress;
    String tokenSignUp;
    VolleyController volleyController = new VolleyController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        //bBack = (Button) mToolbar.findViewById(R.id.btnBackToolbar);
        //bNext = (Button) mToolbar.findViewById(R.id.btnNextToolbar);
        //tvToolbar = (TextView) mToolbar.findViewById(R.id.tvToolbar);
        bPhoto = (Button) findViewById(R.id.btnEditReg);
        imgProfile = (ImageView) findViewById(R.id.imgProfileReg);


        setSupportActionBar(mToolbar);

        bBack.setText("Cancel");
        bNext.setText("");
        bNext.setEnabled(false);
        tvToolbar.setText("Create Profile");

        progress = new ProgressDialog(this);

        initialComponent();

        bRegister.setEnabled(false);
        bRegister.setBackgroundResource(R.drawable.btn_register_off);

        setAdapterGender();
        setAdapterCountry();
        setAdapterProfesi();

        actionReg();
    }

    private void initialComponent(){

        bRegister = (Button) findViewById(R.id.btnRegister);
        spGender = (Spinner) findViewById(R.id.spGenderReg);
        spCountry = (Spinner) findViewById(R.id.spCountryREg);
        spProfesi = (Spinner) findViewById(R.id.spProfesiReg);
        cbAccept = (CheckBox) findViewById(R.id.cbRead);
        tFirst = (EditText) findViewById(R.id.txFirstReg);
        tFirst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tFirst.setHint("");
                return false;
            }
        });

        tFirst.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tFirst.setHint("First Name");
                }
            }
        });
        tLast = (EditText) findViewById(R.id.txLasttReg);
        tLast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tLast.setHint("");
                return false;
            }
        });

        tLast.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tLast.setHint("Last Name");
                }
            }
        });
        tBirth = (TextView) findViewById(R.id.txBirthReg);
        tEmail = (EditText) findViewById(R.id.txEmailReg);
        tPassword = (EditText) findViewById(R.id.txPasswordReg);
    }


    private void actionReg(){
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = tFirst.getText().toString();
                last = tLast.getText().toString();
                email = tEmail.getText().toString();
                password = tPassword.getText().toString();
                birth = tBirth.getText().toString();
                country = spCountry.getSelectedItem().toString();
                gender = spGender.getSelectedItem().toString();
                profesi = spProfesi.getSelectedItem().toString();

                if(first.equals("")){
                    notif("First name cannot empty");
                    return;
                }
                if(last.equals("")){
                    notif("Last name cannot empty");
                    return;
                }
                if(gender.equals("Select")){
                    notif("Please select gender");
                    return;
                }
                if(birth.equals("")||birth.equals("DD-MM-YYYY")){
                    notif("Birth Date cannot empty");
                    return;
                }
                if(birth.trim().length()!=10){
                    notif("Please check Birth Date");
                    return;
                }

                if(email.equals("")||email.equals("your@email.com")){
                    notif("Email cannot empty");
                    return;
                }
                if(password.equals("")){
                    notif("Password name cannot empty");
                    return;
                }
                if(password.trim().length()<6){
                    notif("Password must have at least 6 characters");
                    return;
                }

                if(country.equals("Select")){
                    notif("Please select country");
                    return;
                }
                if(profesi.equals("Select")){
                    notif("Please select profession");
                    return;
                }

                //new JSONSignUp().execute();
                Intent intent = new Intent(getApplicationContext(), ForumService.class);
                intent.setAction(ForumService.ACTION_SIGNUP);
                intent.putExtra(Constants.FIRSTNAME, first);
                intent.putExtra(Constants.LASTNAME, last);
                intent.putExtra(Constants.EMAIL, email);
                intent.putExtra(Constants.PASSWORD, password);
                intent.putExtra(Constants.BIRTHDATE, birth);
                intent.putExtra(Constants.GENDER, gender);
                intent.putExtra(Constants.PROFESSION, profesi);
                intent.putExtra(Constants.COUNTRY, country);
                getApplicationContext().startService(intent);

                /*if(mydb.insertContact(first,last,gender,email,birth,password,country,profesi,selectedImagePath))
                {
                    tost("Profile has been registered");
                    openLogin();
                    finish();
                }else
                {
                    tost("Error register profile");
                }*/
            }
        });
        bPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        cbAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    bRegister.setEnabled(true);
                    bRegister.setBackgroundResource(R.drawable.btn_register);
                }else {
                    bRegister.setEnabled(false);
                    bRegister.setBackgroundResource(R.drawable.btn_register_off);
                }
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        tEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ViewGroup.LayoutParams params= tEmail.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tEmail.setLayoutParams(params);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                showDialog(1);
            }
        });
    }

    private void updateDisplay() {

        if(month>9) {
            if(day>9) {
                currentDate = new StringBuilder().append(year).append("-")
                        .append(month + 1).append("-").append(day).toString();
            }else {
                currentDate = new StringBuilder().append(year).append("-")
                        .append(month + 1).append("-").append("0").append(day).toString();
            }
        }else
        {
            if(day>9) {
                currentDate = new StringBuilder().append(year).append("-0")
                        .append(month + 1).append("-").append(day).toString();
            }else {
                currentDate = new StringBuilder().append(year).append("-0")
                        .append(month + 1).append("-").append("0").append(day).toString();
            }
        }

        Log.i("DATE", currentDate);
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {

            year = i;
            month = j;
            day = k;
            updateDisplay();
            tBirth.setText(currentDate);
        }
    };



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new DatePickerDialog(this, myDateSetListener, year, month,
                        day);
        }
        return null;
    }

    private void tost(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }



    private void setAdapterGender(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, R.layout.simple_spinner_i);
        adapter.setDropDownViewResource(R.layout.simple_spinner_d);
        spGender.setAdapter(adapter);
    }

    private void setAdapterCountry(){

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_i, countries);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               //R.array.country, R.layout.simple_spinner_i);
        adapter.setDropDownViewResource(R.layout.simple_spinner_d);
        spCountry.setAdapter(adapter);
    }

    private void setAdapterProfesi(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.profesi, R.layout.simple_spinner_i);
        adapter.setDropDownViewResource(R.layout.simple_spinner_d);
        spProfesi.setAdapter(adapter);
    }

    private void openLogin(){
        UserData.saveUri(this,selectedImagePath);
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(this,selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                imgProfile.setImageURI(selectedImageUri);
            }
        }
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
            //Things to Do
            warnExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void warnExit() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setMessage("Cancel Registration ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openLogin();
                                finish();

                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
