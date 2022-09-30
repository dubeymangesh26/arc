package com.arcltd.staff.base;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.arcltd.staff.R;
import com.arcltd.staff.networkhandler.errors.ErrorStatus;
import com.arcltd.staff.networkhandler.networkCallBack.IResultView;
import com.arcltd.staff.networkhandler.presentor.ApiPresenter;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.networkhandler.remote.WebService;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment implements IResultView {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static WebService apiService;
    public CompositeDisposable disposable;
    public ApiPresenter apiPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            apiService = RetrofitClient.getClient(getActivity()).create(WebService.class);
            disposable = new CompositeDisposable();
            apiPresenter = new ApiPresenter((BaseActivity) getActivity(), apiService, this);
        } catch (Exception e) {
            ELog.e(TAG, "Exception in onCreate", e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (disposable == null || disposable.isDisposed()) {
                disposable = new CompositeDisposable();
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in onStart", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        try {
            if (disposable != null) {
                disposable.dispose();
            }
            Infrastructure.hideSoftKeyboard(getActivity());
        } catch (Exception e) {
            ELog.e(TAG, "Exception in onStop", e);
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void finish() {

    }

    @Override
    public void showResult(Object genericObj, int callAPIId) {
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
    }

    @Override
    public void showArrayResult(List genericObj, int callAPIId) {
    }

    // Call this function while success i.e statusCode = 200
    public void setInfoViewSuccess(View mDataView, View mErrorView) {
        try {
            setInfoView(ErrorStatus.OK, mDataView, mErrorView, null, null,
                    null, null,
                    0, "", "", false);
        } catch (Exception e) {
            ELog.e(TAG, "Exception in setInfoViewSuccess", e);
        }
    }

    // Call this function while error with specific message
    public void setInfoView(int statusCode, View mDataView, View mErrorView, ImageView mImgError, TextView mTvMessage,
                            TextView mTvSubMessage, View mLlRetry,
                            int mErrorIconId, String mMessage, String mSubMessage, boolean isRetry) {
        try {
            switch (statusCode) {
                case ErrorStatus.OK:
                    mDataView.setVisibility(View.VISIBLE);
                    mErrorView.setVisibility(View.GONE);
                    mErrorIconId = 0;
                    mMessage = "";
                    mSubMessage = "";
                    break;
                case ErrorStatus.BAD_REQUEST:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.UN_AUTHORIZED:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.FORBIDDEN:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.PAGE_NOT_FOUND:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.METHOD_NOT_ALLOWED:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.INTERNAL_SERVER_ERROR:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.BAD_GATEWAY:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                case ErrorStatus.GATEWAY_TIME_OUT:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorIconId = R.drawable.ic_server_error;
                    mMessage = getString(R.string.server_error_message);
                    mSubMessage = getString(R.string.server_error_sub_message);
                    break;
                default:
                    mDataView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                    break;
            }
            if (mImgError != null) {
                mImgError.setImageResource(mErrorIconId);
            }
            if (mTvMessage != null) {
                if (mMessage != null && !mMessage.equals("")) {
                    mTvMessage.setVisibility(View.VISIBLE);
                    mTvMessage.setText(mMessage);
                } else {
                    mTvMessage.setVisibility(View.GONE);
                }
            }
            if (mTvSubMessage != null) {
                if (mSubMessage != null && !mSubMessage.equals("")) {
                    mTvSubMessage.setVisibility(View.VISIBLE);
                    mTvSubMessage.setText(mSubMessage);
                } else {
                    mTvSubMessage.setVisibility(View.GONE);
                }
            }
            if (mLlRetry != null) {
                if (isRetry) {
                    mLlRetry.setVisibility(View.VISIBLE);
                } else {
                    mLlRetry.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in setInfoView", e);
        }
    }

}
