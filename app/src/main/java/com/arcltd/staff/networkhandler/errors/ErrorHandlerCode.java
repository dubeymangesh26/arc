package com.arcltd.staff.networkhandler.errors;

import android.content.Context;

import com.arcltd.staff.R;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;


public class ErrorHandlerCode {
    private static final String TAG = ErrorHandlerCode.class.getSimpleName();
    private int mErrorCode;
    private String message;
    private Context mContext;

    public ErrorHandlerCode(Context context, int errorCode, String message) {
        this.mErrorCode = errorCode;
        this.message = message;
        this.mContext = context;
        showErrorMessage(mErrorCode);
    }

    public void showErrorMessage(int mErrorCode) {
        try {
            switch (mErrorCode) {
                case ErrorStatus.BAD_REQUEST:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.UN_AUTHORIZED:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.FORBIDDEN:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.PAGE_NOT_FOUND:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.METHOD_NOT_ALLOWED:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.INTERNAL_SERVER_ERROR:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.BAD_GATEWAY:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                case ErrorStatus.GATEWAY_TIME_OUT:
                    message = mContext.getResources().getString(R.string.server_error_message);
                    Infrastructure.showToastMessage(mContext, message);
                    break;
                default:
                    Infrastructure.showToastMessage(mContext, message);
                    break;
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in showErrorMessage", e);
        }
    }
}
