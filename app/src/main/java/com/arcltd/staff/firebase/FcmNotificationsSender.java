package com.arcltd.staff.firebase;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arcltd.staff.networkhandler.WebConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

    String userFcmToken;
    String title;
    String body;
    String clickAction;
    String senderCode;
    Context mContext;
    Activity mActivity;


    private RequestQueue requestQueue;


    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public FcmNotificationsSender(String userFcmToken, String title, String body, String clickAction, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.clickAction = clickAction;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public FcmNotificationsSender(String userFcmToken, String title, String body, String clickAction, String senderCode, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.clickAction = clickAction;
        this.senderCode = senderCode;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", "icon");

            mainObj.put("notification", notiObject);

            JSONObject dataObject = new JSONObject();
            dataObject.put("title", title);
            dataObject.put("message", body);
            if (clickAction != null) {
                dataObject.put("click_action", clickAction);
            }
            if (senderCode != null) {
                dataObject.put("sender_code", senderCode);
            }
            mainObj.put("data", dataObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebConstants.fcmPostUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + WebConstants.fcmKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
