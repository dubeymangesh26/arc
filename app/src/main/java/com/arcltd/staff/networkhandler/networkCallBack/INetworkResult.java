package com.arcltd.staff.networkhandler.networkCallBack;

public interface INetworkResult<T> {
    void onSuccess(String responseBody, int responseType);

    void onError(String message, int responseType, int errorCode);
}
