package com.arcltd.staff.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;


import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;

import java.util.Calendar;

public class Infrastructure {

    private static String TAG = Infrastructure.class.getSimpleName();
    private static Dialog dialog;
    private static BaseActivity baseActivity = null;
    private static FragmentActivity baseFragment = null;


    private static BaseActivity getBaseActivity() {
        return baseActivity;
    }

    public static void setBaseActivity(BaseActivity baseParentActivity) {
        baseActivity = baseParentActivity;
    }
    public static void setBaseFragment(FragmentActivity baseParentFragment) {
        baseFragment = baseParentFragment;
    }


    public static void showToastMessage(Context context, String string) {
        try {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            ELog.e(TAG, "Exception in showToastMessage", e);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                View view = activity.getCurrentFocus();
                if (view == null) {
                    view = new View(activity);
                }
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in hideSoftKeyboard", e);
        }
    }

    public static boolean isInternetPresent() {
        boolean isInternetPresent = false;
        try {
            ConnectionDetector cd = new ConnectionDetector(getBaseActivity());
            isInternetPresent = cd.isConnectingToInternet();
            ELog.d(TAG, "isInternetPresent = " + isInternetPresent);
        } catch (Exception e) {
            ELog.e(TAG, "Exception in isInternetPresent", e);
        }
        return isInternetPresent;
    }

    public static boolean isInternetPresent(Context context) {
        boolean isInternetPresent = false;
        try {
            ConnectionDetector cd = new ConnectionDetector(context);
            isInternetPresent = cd.isConnectingToInternet();
            ELog.d(TAG, "isInternetPresent = " + isInternetPresent);
        } catch (Exception e) {
            ELog.e(TAG, "Exception in isInternetPresent", e);
        }
        return isInternetPresent;
    }


    public static void showProgressDialog(Context mContext) {
        ELog.d(TAG, "showProgressDialog: ");
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new Dialog(mContext, R.style.loaderDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.rount_loader);
            dialog.setTitle("Please Wait");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (NullPointerException e) {
            ELog.e(TAG, "Null Pointer Exception in showProgressDialog", e);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in showProgressDialog", e);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in dismissProgressDialog", e);
        }
    }

    public static boolean isToday(long timestamp) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(timestamp);
        return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }
}
