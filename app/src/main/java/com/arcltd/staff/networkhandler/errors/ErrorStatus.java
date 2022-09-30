package com.arcltd.staff.networkhandler.errors;

public class ErrorStatus {
    public static final int BAD_REQUEST = 400;
    public static final int UN_AUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int PAGE_NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int GATEWAY_TIME_OUT = 504;
    public static final int NO_INTERNET = 600;
    public static final int OK = 200;
    public static final int NO_RECORD = 902; // No Data Found
    public static final int SERVER_ERROR = 901; // Status 'exception' or Exception while parsing
}
