package com.arcltd.staff.networkhandler.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.arcltd.staff.networkhandler.WebConstants;
import com.arcltd.staff.utility.ELog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitClient {
    private static final String USER_PREFERENCE = "UserPreferences";
    private static final String SETTING_PREFERENCE = "SettingPreferences";
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(Context context) {
        int cacheSize = 5 * 1024 * 1024;
        Cache myCache = new Cache(context.getCacheDir(), cacheSize);
        initOkHttp(context,myCache);
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(WebConstants.baseURL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } catch (Exception e) {
            ELog.e(TAG, "Exception in Retrofit getClient", e);
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context, Cache myCache) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder().cache(myCache)
                .connectTimeout(7, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES);
        okHttpClient = httpClient.build();
    }

    public static String getStringUserPreference(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public static void saveUserPreference(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearAllPreference(Context context) {
        SharedPreferences sharedUserPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences sharedSettingPref = context.getSharedPreferences(SETTING_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedUserPref.edit().clear().commit()) {
            sharedSettingPref.edit().clear().apply();
        }
    }
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat(String datetime) {
        if (datetime != null)
            try { // Expiry date
                String s;
                DateFormat month = new SimpleDateFormat("MMM dd, yyyy");
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                //get month no. from given date
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);

                Date date = sdf.parse(datetime);

                if (sdf.format(date).equals(sdf.format(Calendar.getInstance().getTime()))) {
                    s = "Today";
                } else if (sdf.format(date).equals(sdf.format(c.getTime()))) {
                    s = "Yesterday";
                } else {
                    s = month.format(date);
                }

                return s;
            } catch (Exception e) {
                Log.e("AppUtilDateFormat", "dateFormat: Parsing exception ", e);
            }
        return null;
    }
}
