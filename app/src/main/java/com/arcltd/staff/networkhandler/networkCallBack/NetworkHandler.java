package com.arcltd.staff.networkhandler.networkCallBack;

public class NetworkHandler<T> {
    private static final String TAG = NetworkHandler.class.getSimpleName();
    private INetworkResult iNetworkResultListener;

    public NetworkHandler(INetworkResult iNetworkResultListener) {
        this.iNetworkResultListener = iNetworkResultListener;
    }
}
