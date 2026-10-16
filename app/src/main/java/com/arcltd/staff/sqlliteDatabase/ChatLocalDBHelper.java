package com.arcltd.staff.sqlliteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arcltd.staff.activity.chat.ChatMessage;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.response.GetChatHistoryUsersResponse;
import com.arcltd.staff.response.UserChatListResponse;

import java.util.ArrayList;
import java.util.List;

public class ChatLocalDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "chat_db";

    private static final int DB_VERSION = 7;

    private static final String TABLE_NAME = "messages";

    private static final String ID_COL = "id";
    private static final String SENDER_CODE = "sender_code";
    private static final String RECEIVER_CODE = "receiver_code";
    private static final String SERVER_ID = "server_id";
    private static final String MESSAGE_TEXT = "message_text";
    private static final String IS_SENT = "is_sent";
    private static final String TIMESTAMP = "timestamp";
    private static final String IS_READ = "is_read";
    private static final String MESSAGE_STATUS = "message_status";

    private static final String USERS_TABLE_NAME = "chat_users";
    private static final String USER_EMP_CODE = "emp_code";
    private static final String USER_EMP_NAME = "emp_name";
    private static final String USER_PROFILE_PIC = "profile_pic";
    private static final String USER_BRANCH_CODE = "branch_code";
    private static final String USER_BRANCH_NAME = "branch_name";
    private static final String USER_DIVISION_NAME = "division_name";
    private static final String USER_REGION_NAME = "region_name";
    private static final String USER_TOKEN = "token";

    private static ChatLocalDBHelper instance;

    public static synchronized ChatLocalDBHelper getInstance(Context context) {

        if (instance == null) {
            instance = new ChatLocalDBHelper(
                    context.getApplicationContext()
            );
        }

        return instance;
    }

    private ChatLocalDBHelper(Context context) {
        super(
                context,
                DB_NAME,
                null,
                DB_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SENDER_CODE + " TEXT, " +
                        RECEIVER_CODE + " TEXT, " +
                        SERVER_ID + " TEXT UNIQUE, " +
                        MESSAGE_TEXT + " TEXT, " +
                        IS_SENT + " INTEGER, " +
                        TIMESTAMP + " LONG, " +
                        IS_READ + " INTEGER DEFAULT 0, " +
                        MESSAGE_STATUS + " INTEGER DEFAULT 0" +
                        ")";

        db.execSQL(query);

        String createUsersQuery =
                "CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + " (" +
                        USER_EMP_CODE + " TEXT PRIMARY KEY, " +
                        USER_EMP_NAME + " TEXT, " +
                        USER_PROFILE_PIC + " TEXT, " +
                        USER_BRANCH_CODE + " TEXT, " +
                        USER_BRANCH_NAME + " TEXT, " +
                        USER_DIVISION_NAME + " TEXT, " +
                        USER_REGION_NAME + " TEXT, " +
                        USER_TOKEN + " TEXT" +
                        ")";

        db.execSQL(createUsersQuery);
    }

    // =========================================================
    // ADD MESSAGE
    // =========================================================

    public void addMessage(
            String senderCode,
            String receiverCode,
            String serverId,
            String message,
            boolean isSent,
            long timestamp,
            boolean isRead,
            int status) {

        if (senderCode == null ||
                receiverCode == null ||
                serverId == null) {
            return;
        }

        senderCode =
                senderCode.trim().toUpperCase();

        receiverCode =
                receiverCode.trim().toUpperCase();

        if (status < 0) status = 0;
        if (status > 2) status = 2;

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                SENDER_CODE,
                senderCode
        );

        values.put(
                RECEIVER_CODE,
                receiverCode
        );

        values.put(
                SERVER_ID,
                serverId
        );

        values.put(
                MESSAGE_TEXT,
                message == null ? "" : message
        );

        values.put(
                IS_SENT,
                isSent ? 1 : 0
        );

        values.put(
                TIMESTAMP,
                timestamp
        );

        values.put(
                IS_READ,
                isRead ? 1 : 0
        );

        values.put(
                MESSAGE_STATUS,
                status
        );

        db.insertWithOnConflict(
                TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    // =========================================================
    // UPDATE MESSAGE STATUS
    // =========================================================

    public void updateMessageStatus(
            String serverId,
            int status) {

        if (serverId == null) {
            return;
        }

        if (status < 0) status = 0;
        if (status > 2) status = 2;

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                MESSAGE_STATUS,
                status
        );

        if (status == 2) {
            values.put(IS_READ, 1);
        }

        db.update(
                TABLE_NAME,
                values,
                SERVER_ID + "=?",
                new String[]{serverId}
        );
    }

    // =========================================================
    // MESSAGE EXISTS
    // =========================================================

    public boolean messageExists(
            String serverId) {

        if (serverId == null) {
            return false;
        }

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID_COL},
                    SERVER_ID + "=?",
                    new String[]{serverId},
                    null,
                    null,
                    null
            );

            return cursor != null &&
                    cursor.getCount() > 0;

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // =========================================================
    // MARK RECEIVED MESSAGES READ
    // =========================================================

    public void markAsRead(
            String myCode,
            String senderCode) {

        if (myCode == null ||
                senderCode == null) {
            return;
        }

        myCode =
                myCode.trim().toUpperCase();

        senderCode =
                senderCode.trim().toUpperCase();

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(IS_READ, 1);
        values.put(MESSAGE_STATUS, 2);

        db.update(
                TABLE_NAME,
                values,

                "UPPER(TRIM(" + RECEIVER_CODE + "))=?" +
                        " AND UPPER(TRIM(" +
                        SENDER_CODE + "))=?",

                new String[]{
                        myCode,
                        senderCode
                }
        );
    }

    // =========================================================
    // UNREAD COUNT
    // =========================================================

    public int getUnreadCount(
            String myCode,
            String senderCode) {

        if (myCode == null ||
                senderCode == null) {
            return 0;
        }

        myCode =
                myCode.trim().toUpperCase();

        senderCode =
                senderCode.trim().toUpperCase();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor = null;

        try {

            String query =
                    "SELECT COUNT(*) FROM " +
                            TABLE_NAME +

                            " WHERE UPPER(TRIM(" +
                            RECEIVER_CODE + "))=?" +

                            " AND UPPER(TRIM(" +
                            SENDER_CODE + "))=?" +

                            " AND " +
                            IS_READ + "=0";

            cursor = db.rawQuery(
                    query,
                    new String[]{
                            myCode,
                            senderCode
                    }
            );

            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return 0;
    }

    // =========================================================
    // GET CHAT MESSAGES
    // =========================================================

    public List<ChatMessage> getMessages(
            String myCode,
            String targetCode) {

        List<ChatMessage> list =
                new ArrayList<>();

        if (myCode == null ||
                targetCode == null) {

            return list;
        }

        myCode =
                myCode.trim().toUpperCase();

        targetCode =
                targetCode.trim().toUpperCase();

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT * FROM " +
                        TABLE_NAME +

                        " WHERE (UPPER(TRIM(" +

                        SENDER_CODE + "))=?" +
                        " AND UPPER(TRIM(" +
                        RECEIVER_CODE + "))=?)" +

                        " OR (UPPER(TRIM(" +

                        SENDER_CODE + "))=?" +
                        " AND UPPER(TRIM(" +
                        RECEIVER_CODE + "))=?)" +

                        " ORDER BY " +
                        TIMESTAMP +
                        " ASC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{
                        myCode,
                        targetCode,
                        targetCode,
                        myCode
                }
        );

        try {

            int serverIdIndex =
                    cursor.getColumnIndex(SERVER_ID);

            int messageIndex =
                    cursor.getColumnIndex(MESSAGE_TEXT);

            int isSentIndex =
                    cursor.getColumnIndex(IS_SENT);

            int timestampIndex =
                    cursor.getColumnIndex(TIMESTAMP);

            int statusIndex =
                    cursor.getColumnIndex(MESSAGE_STATUS);

            while (cursor.moveToNext()) {

                String serverId =
                        cursor.getString(serverIdIndex);

                String message =
                        cursor.getString(messageIndex);

                boolean isSent =
                        cursor.getInt(isSentIndex) == 1;

                long timestamp =
                        cursor.getLong(timestampIndex);

                int status =
                        cursor.getInt(statusIndex);

                list.add(
                        new ChatMessage(
                                serverId,
                                message,
                                isSent,
                                timestamp,
                                status
                        )
                );
            }

        } finally {

            cursor.close();
        }

        return list;
    }

    // =========================================================
    // LAST MESSAGE
    // =========================================================

    public ChatMessage getLastMessage(
            String myCode,
            String targetCode) {

        if (myCode == null ||
                targetCode == null) {

            return null;
        }

        myCode =
                myCode.trim().toUpperCase();

        targetCode =
                targetCode.trim().toUpperCase();

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT * FROM " +
                        TABLE_NAME +

                        " WHERE (UPPER(TRIM(" +

                        SENDER_CODE + "))=?" +
                        " AND UPPER(TRIM(" +
                        RECEIVER_CODE + "))=?)" +

                        " OR (UPPER(TRIM(" +

                        SENDER_CODE + "))=?" +
                        " AND UPPER(TRIM(" +
                        RECEIVER_CODE + "))=?)" +

                        " ORDER BY " +
                        TIMESTAMP +
                        " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{
                        myCode,
                        targetCode,
                        targetCode,
                        myCode
                }
        );

        try {

            if (cursor.moveToFirst()) {

                int serverIdIndex =
                        cursor.getColumnIndex(SERVER_ID);

                int messageIndex =
                        cursor.getColumnIndex(MESSAGE_TEXT);

                int isSentIndex =
                        cursor.getColumnIndex(IS_SENT);

                int timestampIndex =
                        cursor.getColumnIndex(TIMESTAMP);

                int statusIndex =
                        cursor.getColumnIndex(MESSAGE_STATUS);

                return new ChatMessage(
                        cursor.getString(serverIdIndex),
                        cursor.getString(messageIndex),
                        cursor.getInt(isSentIndex) == 1,
                        cursor.getLong(timestampIndex),
                        cursor.getInt(statusIndex)
                );
            }

        } finally {

            cursor.close();
        }

        return null;
    }

    // =========================================================
    // DELETE ONE MESSAGE
    // =========================================================

    public void deleteMessage(
            String messageId) {

        if (messageId == null) {
            return;
        }

        SQLiteDatabase db =
                getWritableDatabase();

        db.delete(
                TABLE_NAME,
                SERVER_ID + "=?",
                new String[]{messageId}
        );
    }

    // =========================================================
    // SYNC SERVER MESSAGES (PURGE MESSAGES DELETED ON SERVER)
    // =========================================================

    public void syncServerMessages(
            String myCode,
            String targetCode,
            List<String> activeServerIds) {

        if (myCode == null || activeServerIds == null) {
            return;
        }

        myCode = myCode.trim().toUpperCase();
        SQLiteDatabase db = getWritableDatabase();

        List<String> toDelete = new ArrayList<>();

        if (targetCode != null && !targetCode.trim().isEmpty()) {
            targetCode = targetCode.trim().toUpperCase();

            String query = "SELECT " + SERVER_ID + " FROM " + TABLE_NAME +
                    " WHERE (" + SERVER_ID + " NOT LIKE 'temp_%')" +
                    " AND ((" + SENDER_CODE + "=? AND " + RECEIVER_CODE + "=?)" +
                    " OR (" + SENDER_CODE + "=? AND " + RECEIVER_CODE + "=?))";

            Cursor cursor = db.rawQuery(query, new String[]{myCode, targetCode, targetCode, myCode});
            try {
                int idIdx = cursor.getColumnIndex(SERVER_ID);
                while (cursor.moveToNext()) {
                    String localId = cursor.getString(idIdx);
                    if (localId != null && !activeServerIds.contains(localId)) {
                        toDelete.add(localId);
                    }
                }
            } finally {
                cursor.close();
            }
        } else {
            String query = "SELECT " + SERVER_ID + " FROM " + TABLE_NAME +
                    " WHERE (" + SERVER_ID + " NOT LIKE 'temp_%')" +
                    " AND (" + SENDER_CODE + "=? OR " + RECEIVER_CODE + "=?)";

            Cursor cursor = db.rawQuery(query, new String[]{myCode, myCode});
            try {
                int idIdx = cursor.getColumnIndex(SERVER_ID);
                while (cursor.moveToNext()) {
                    String localId = cursor.getString(idIdx);
                    if (localId != null && !activeServerIds.contains(localId)) {
                        toDelete.add(localId);
                    }
                }
            } finally {
                cursor.close();
            }
        }

        for (String deleteId : toDelete) {
            deleteMessage(deleteId);
        }
    }

    // =========================================================
    // CLEAR TEMP MESSAGES
    // =========================================================

    public void clearTempMessages(
            String myCode,
            String targetCode) {

        if (myCode == null ||
                targetCode == null) {
            return;
        }

        myCode =
                myCode.trim().toUpperCase();

        targetCode =
                targetCode.trim().toUpperCase();

        SQLiteDatabase db =
                getWritableDatabase();

        db.delete(
                TABLE_NAME,

                SERVER_ID +
                        " LIKE 'temp_%'" +

                        " AND ((" +

                        SENDER_CODE + "=?" +
                        " AND " +
                        RECEIVER_CODE + "=?" +

                        ") OR (" +

                        SENDER_CODE + "=?" +
                        " AND " +
                        RECEIVER_CODE + "=?" +

                        "))",

                new String[]{
                        myCode,
                        targetCode,
                        targetCode,
                        myCode
                }
        );
    }

    // =========================================================
    // CLEAR CHAT
    // =========================================================

    public void clearChat(
            String myCode,
            String targetCode) {

        if (myCode == null ||
                targetCode == null) {
            return;
        }

        myCode =
                myCode.trim().toUpperCase();

        targetCode =
                targetCode.trim().toUpperCase();

        SQLiteDatabase db =
                getWritableDatabase();

        db.delete(
                TABLE_NAME,

                "(" +
                        SENDER_CODE + "=?" +
                        " AND " +
                        RECEIVER_CODE + "=?" +

                        ") OR (" +

                        SENDER_CODE + "=?" +
                        " AND " +
                        RECEIVER_CODE + "=?" +

                        ")",

                new String[]{
                        myCode,
                        targetCode,
                        targetCode,
                        myCode
                }
        );
    }

    // =========================================================
    // CHAT USER CODES
    // =========================================================

    public List<String> getChatUserCodes(
            String myCode) {

        List<String> codes =
                new ArrayList<>();

        if (myCode == null) {
            return codes;
        }

        myCode =
                myCode.trim().toUpperCase();

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT DISTINCT " + SENDER_CODE +
                " FROM " + TABLE_NAME +
                " WHERE UPPER(TRIM(" + RECEIVER_CODE + ")) = UPPER(TRIM(?))" +
                " UNION " +
                "SELECT DISTINCT " + RECEIVER_CODE +
                " FROM " + TABLE_NAME +
                " WHERE UPPER(TRIM(" + SENDER_CODE + ")) = UPPER(TRIM(?))";

        Cursor cursor =
                db.rawQuery(
                        query,
                        new String[]{
                                myCode,
                                myCode
                        }
                );

        try {

            while (cursor.moveToNext()) {

                String code =
                        cursor.getString(0);

                if (code != null &&
                        !code.equalsIgnoreCase(myCode) &&
                        !codes.contains(code)) {

                    codes.add(code);
                }
            }

        } finally {

            cursor.close();
        }

        return codes;
    }

    // =========================================================
    // SAVE OR UPDATE USER DETAILS TO LOCAL DB
    // =========================================================

    public void saveUser(
            String empCode,
            String empName,
            String profilePic,
            String branchCode,
            String branchName,
            String divisionName,
            String regionName,
            String token) {

        if (empCode == null || empCode.trim().isEmpty()) {
            return;
        }

        empCode = empCode.trim().toUpperCase();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_EMP_CODE, empCode);

        if (empName != null && !empName.trim().isEmpty() && !empName.startsWith("Employee ")) {
            values.put(USER_EMP_NAME, empName.trim());
        }
        if (profilePic != null && !profilePic.trim().isEmpty()) {
            values.put(USER_PROFILE_PIC, profilePic.trim());
        }
        if (branchCode != null && !branchCode.trim().isEmpty()) {
            values.put(USER_BRANCH_CODE, branchCode.trim());
        }
        if (branchName != null && !branchName.trim().isEmpty()) {
            values.put(USER_BRANCH_NAME, branchName.trim());
        }
        if (divisionName != null && !divisionName.trim().isEmpty()) {
            values.put(USER_DIVISION_NAME, divisionName.trim());
        }
        if (regionName != null && !regionName.trim().isEmpty()) {
            values.put(USER_REGION_NAME, regionName.trim());
        }
        if (token != null && !token.trim().isEmpty()) {
            values.put(USER_TOKEN, token.trim());
        }

        db.insertWithOnConflict(
                USERS_TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public void saveUsers(List<EmployeeListResponse.EmployeeList> users) {
        if (users == null || users.isEmpty()) return;
        for (EmployeeListResponse.EmployeeList user : users) {
            if (user != null) {
                saveUser(
                        user.getEmp_code(),
                        user.getEmp_name(),
                        user.getProfilepic(),
                        user.getBranch_code(),
                        user.getBranch_name(),
                        user.getDivision_name(),
                        user.getRegion_name(),
                        user.getToken()
                );
            }
        }
    }

    public void saveChatHistoryUsers(List<GetChatHistoryUsersResponse.UserItem> users) {
        if (users == null || users.isEmpty()) return;
        for (GetChatHistoryUsersResponse.UserItem user : users) {
            if (user != null) {
                saveUser(
                        user.getEmpCode(),
                        user.getEmpName(),
                        user.getProfilePic(),
                        user.getBranchCode(),
                        user.getBranchName(),
                        user.getDivisionName(),
                        user.getRegionName(),
                        user.getToken()
                );
            }
        }
    }

    public void saveUserChatList(List<UserChatListResponse.EmployeeList> users) {
        if (users == null || users.isEmpty()) return;
        for (UserChatListResponse.EmployeeList user : users) {
            if (user != null) {
                saveUser(
                        user.getEmp_code(),
                        user.getName(),
                        user.getProfilepic(),
                        user.getBranch_code(),
                        user.getBranch_name(),
                        user.getDivision_name(),
                        user.getRegion_name(),
                        ""
                );
            }
        }
    }

    // =========================================================
    // GET USER DETAILS FROM LOCAL DB
    // =========================================================

    public EmployeeListResponse.EmployeeList getUser(String empCode) {
        if (empCode == null || empCode.trim().isEmpty()) {
            return null;
        }

        empCode = empCode.trim().toUpperCase();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    USERS_TABLE_NAME,
                    null,
                    "UPPER(TRIM(" + USER_EMP_CODE + "))=?",
                    new String[]{empCode},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                EmployeeListResponse.EmployeeList user = new EmployeeListResponse.EmployeeList();

                int codeIdx = cursor.getColumnIndex(USER_EMP_CODE);
                int nameIdx = cursor.getColumnIndex(USER_EMP_NAME);
                int picIdx = cursor.getColumnIndex(USER_PROFILE_PIC);
                int bCodeIdx = cursor.getColumnIndex(USER_BRANCH_CODE);
                int bNameIdx = cursor.getColumnIndex(USER_BRANCH_NAME);
                int divIdx = cursor.getColumnIndex(USER_DIVISION_NAME);
                int regIdx = cursor.getColumnIndex(USER_REGION_NAME);
                int tokenIdx = cursor.getColumnIndex(USER_TOKEN);

                if (codeIdx != -1) user.setEmp_code(cursor.getString(codeIdx));
                if (nameIdx != -1) user.setEmp_name(cursor.getString(nameIdx));
                if (picIdx != -1) user.setProfilepic(cursor.getString(picIdx));
                if (bCodeIdx != -1) user.setBranch_code(cursor.getString(bCodeIdx));
                if (bNameIdx != -1) user.setBranch_name(cursor.getString(bNameIdx));
                if (divIdx != -1) user.setDivision_name(cursor.getString(divIdx));
                if (regIdx != -1) user.setRegion_name(cursor.getString(regIdx));
                if (tokenIdx != -1) user.setToken(cursor.getString(tokenIdx));

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return null;
    }

    // =========================================================
    // SEARCH USERS FROM LOCAL DB
    // =========================================================

    public List<EmployeeListResponse.EmployeeList> searchUsers(String query) {
        List<EmployeeListResponse.EmployeeList> list = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return list;
        }

        query = query.trim();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    USERS_TABLE_NAME,
                    null,
                    USER_EMP_NAME + " LIKE ? OR " + USER_EMP_CODE + " LIKE ? OR " + USER_BRANCH_NAME + " LIKE ?",
                    new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"},
                    null, null, null
            );

            while (cursor != null && cursor.moveToNext()) {
                EmployeeListResponse.EmployeeList user = new EmployeeListResponse.EmployeeList();

                int codeIdx = cursor.getColumnIndex(USER_EMP_CODE);
                int nameIdx = cursor.getColumnIndex(USER_EMP_NAME);
                int picIdx = cursor.getColumnIndex(USER_PROFILE_PIC);
                int bCodeIdx = cursor.getColumnIndex(USER_BRANCH_CODE);
                int bNameIdx = cursor.getColumnIndex(USER_BRANCH_NAME);
                int divIdx = cursor.getColumnIndex(USER_DIVISION_NAME);
                int regIdx = cursor.getColumnIndex(USER_REGION_NAME);
                int tokenIdx = cursor.getColumnIndex(USER_TOKEN);

                if (codeIdx != -1) user.setEmp_code(cursor.getString(codeIdx));
                if (nameIdx != -1) user.setEmp_name(cursor.getString(nameIdx));
                if (picIdx != -1) user.setProfilepic(cursor.getString(picIdx));
                if (bCodeIdx != -1) user.setBranch_code(cursor.getString(bCodeIdx));
                if (bNameIdx != -1) user.setBranch_name(cursor.getString(bNameIdx));
                if (divIdx != -1) user.setDivision_name(cursor.getString(divIdx));
                if (regIdx != -1) user.setRegion_name(cursor.getString(regIdx));
                if (tokenIdx != -1) user.setToken(cursor.getString(tokenIdx));

                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return list;
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        onCreate(db);
    }
}