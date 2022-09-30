package com.arcltd.staff.networkhandler.networkCallBack;

import java.util.List;

public interface IResultView<T> {
    void finish();

    void showResult(T data, int callAPIId);
    void showArrayResult(List<String> genericObj, int callAPIId);
    void onDisplayMessage(String message, int callAPIId, int errorCode);
}
