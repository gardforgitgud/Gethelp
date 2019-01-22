package gr.mmv.gethelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by iceberg on 3/6/2018.
 */

    //here for this class we are using a singleton pattern

    public class SharedPrefManager {

        //the constants
        private static final String SHARED_PREF_NAME = "gethelpsharedpref";
        private static final String KEY_EMAIL = "keyemail";
        private static final String KEY_NAME = "keyname";
        private static final String KEY_TEL = "keytel";
        private static final String KEY_ADDRESS = "keyaddress";
        private static final String KEY_NATIONAL_ID = "keynationalid";
        private static final String KEY_ID = "keyid";
        private static final String KEY_ROLE = "keyrole";

        private static SharedPrefManager mInstance;
        private static Context mCtx;


        private SharedPrefManager(Context context) {

            mCtx = context;

        }

        public static synchronized SharedPrefManager getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new SharedPrefManager(context);
            }
            return mInstance;
        }

        //method to let the user login
        //this method will store the user data in shared preferences
        public void userLogin(User user) {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_ID, user.getId());
            editor.putString(KEY_EMAIL, user.getEmail());
            editor.putString(KEY_NAME, user.getName());
            editor.putString(KEY_TEL, user.getTel());
            editor.putString(KEY_ADDRESS, user.getAddress());
            editor.putString(KEY_NATIONAL_ID, user.getNational_id());
            editor.apply();
        }

        //method to let the officer login
        //this method will store the officer data in shared preferences
         public void officerLogin(Officer officer) {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_ID, officer.getId());
            editor.putString(KEY_EMAIL, officer.getEmail());
            editor.putString(KEY_NAME, officer.getName());
            editor.putString(KEY_NATIONAL_ID, officer.getNational_id());
            editor.putString(KEY_ROLE, officer.getRole());
            editor.putString(KEY_TEL, officer.getTel());
            editor.apply();
        }

        //this method will checker whether user is already logged in or not
        public boolean isLoggedIn() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_EMAIL, null) != null;
        }

        //this method will checker whether officer is already logged in or not
        public boolean isOffLoggedIn() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_EMAIL, null) != null;
        }

        //this method will give the logged in user
        public User getUser() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return new User(
                    sharedPreferences.getInt(KEY_ID, -1),
                    sharedPreferences.getString(KEY_EMAIL, null),
                    sharedPreferences.getString(KEY_NAME, null),
                    sharedPreferences.getString(KEY_TEL, null),
                    sharedPreferences.getString(KEY_ADDRESS, null),
                    sharedPreferences.getString(KEY_NATIONAL_ID, null)
            );
        }
        //this method will give the logged in officer
        public Officer getOfficer() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return new Officer(
                    sharedPreferences.getInt(KEY_ID, -1),
                    sharedPreferences.getString(KEY_EMAIL, null),
                    sharedPreferences.getString(KEY_NAME, null),
                    sharedPreferences.getString(KEY_NATIONAL_ID, null),
                    sharedPreferences.getString(KEY_ROLE, null),
                    sharedPreferences.getString(KEY_TEL, null)

            );
        }

        //this method will logout the user
        public void logout() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            mCtx.startActivity(new Intent(mCtx, MainActivity.class));
        }
    }

