package com.arcltd.staff.sqlliteDatabase;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arcltd.staff.response.PincodeBranchListResponse;
import com.arcltd.staff.utility.ELog;

import java.util.ArrayList;

public class PincodeLocalDBHelper extends SQLiteOpenHelper {
    private static PincodeLocalDBHelper sInstance;

    private static final String DB_NAME = "pincodelist";
    // below int is our database version
    private static final int DB_VERSION = 1;
    private static final String ID_COL = "id";
    private static final String TABLE_NAME = "pincodelist";
    private static final String BRANCH_ID = "branch_id";
    private static final String BRANCH_NAME = "branch_name";
    private static final String BRANCH_CODE = "branch_code";
    private static final String REGION_ID = "region_id";
    private static final String REGION_NAME = "region_name";
    private static final String DIVISION_ID = "division_id";
    private static final String DIVISION_NAME = "division_name";
    private static final String ACCOUNT_BRANCH = "ac_branch";
    private static final String BRANCH_ADDRESS = "branch_address";
    private static final String BRANCH_M_MOBILE = "branch_mngr_mob";
    private static final String STD_CODE = "std_code";
    private static final String BPHONE_NUMBER = "phone_number";
    private static final String BRANCH_MOBILE_NO = "branch_mobileno";
    private static final String EMAIL_ID = "email_id";
    private static final String UPDATED_DATE = "updated_date";
    private static final String BRANCH_PINCODE = "branch_pincode";
    private static final String SERVICE_PINCODE = "service_pincode";


    public static PincodeLocalDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new PincodeLocalDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public PincodeLocalDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BRANCH_ID + " TEXT,"
                + BRANCH_NAME + " TEXT,"
                + BRANCH_CODE + " TEXT,"
                + REGION_ID + " TEXT,"
                + REGION_NAME + " TEXT,"
                + DIVISION_ID + " TEXT,"
                + DIVISION_NAME + " TEXT,"
                + ACCOUNT_BRANCH + " TEXT,"
                + BRANCH_ADDRESS + " TEXT,"
                + BRANCH_M_MOBILE + " TEXT,"
                + STD_CODE + " TEXT,"
                + BPHONE_NUMBER + " TEXT,"
                + BRANCH_MOBILE_NO + " TEXT,"
                + EMAIL_ID + " TEXT,"
                + UPDATED_DATE + " TEXT,"
                + BRANCH_PINCODE + " TEXT,"
                + SERVICE_PINCODE + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    public void addNewList(String sc_branch_id, String sc_branch_name, String sc_branch_code, String sc_region_id,
                             String sc_region_name, String sc_division_id, String sc_division_name,
                             String sc_ac_branch, String sc_branch_address, String sc_branch_mngr_mob,
                             String sc_std_code, String sc_phone_number, String sc_branch_mobileno,
                             String sc_email_id, String sc_updated_date, String sc_branch_pincode,
                             String sc_service_pincode) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        try {
            SQLiteDatabase db = this.getWritableDatabase();


        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        values.put(BRANCH_ID, sc_branch_id);
        values.put(BRANCH_NAME, sc_branch_name);
        values.put(BRANCH_CODE, sc_branch_code);
        values.put(REGION_ID, sc_region_id);
        values.put(REGION_NAME, sc_region_name);
        values.put(DIVISION_ID, sc_division_id);
        values.put(DIVISION_NAME, sc_division_name);
        values.put(ACCOUNT_BRANCH, sc_ac_branch);
        values.put(BRANCH_ADDRESS, sc_branch_address);
        values.put(BRANCH_M_MOBILE, sc_branch_mngr_mob);
        values.put(STD_CODE, sc_std_code);
        values.put(BPHONE_NUMBER, sc_phone_number);
        values.put(BRANCH_MOBILE_NO, sc_branch_mobileno);
        values.put(EMAIL_ID, sc_email_id);
        values.put(UPDATED_DATE, sc_updated_date);
        values.put(BRANCH_PINCODE, sc_branch_pincode);
        values.put(SERVICE_PINCODE, sc_service_pincode);


            db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        }catch (Exception e){
            Log.e(TAG, "addNewList: ",e );
        }
    }



    // we have created a new method for reading all the courses.
    public ArrayList<PincodeBranchListResponse> readCourses() {
        // on below line we are creating a
        // database for reading our database.

            SQLiteDatabase db = this.getReadableDatabase();

            // on below line we are creating a cursor with query to read data from database.
            Cursor cursorList = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            // on below line we are creating a new array list.
            ArrayList<PincodeBranchListResponse> courseModalArrayList = new ArrayList<>();

            // moving our cursor to first position.
            if (cursorList.moveToFirst()) {

                do {
                    // on below line we are adding the data from cursor to our array list.
                    courseModalArrayList.add(new PincodeBranchListResponse(
                            cursorList.getString(1),
                            cursorList.getString(2),
                            cursorList.getString(3),
                            cursorList.getString(4),
                            cursorList.getString(5),
                            cursorList.getString(6),
                            cursorList.getString(7),
                            cursorList.getString(8),
                            cursorList.getString(9),
                            cursorList.getString(10),
                            cursorList.getString(11),
                            cursorList.getString(12),
                            cursorList.getString(13),
                            cursorList.getString(14),
                            cursorList.getString(15),
                            cursorList.getString(16),
                            cursorList.getString(17)));
                } while (cursorList.moveToNext());
                // moving our cursor to next.
            }
            // at last closing our cursor
            // and returning our array list.
            cursorList.close();
            return courseModalArrayList;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}