package com.arcltd.staff.activity.messDetails;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.MessEmpListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.MessEmpListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MessEmployeeDeetailsActivity extends BaseActivity {
    RecyclerView list;
    String mess_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_employee_deetails);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

       // ((AppCompatActivity)this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=findViewById(R.id.list);
        mess_id = Objects.requireNonNull(getIntent().getExtras()).getString("me_id");
        messEmpList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void messEmpList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.messEMPList(disposable, Constants.ApiRequestCode.DIVISION_LIST,
                        mess_id);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getData messEmpListResponse", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            MessEmpListResponse messEmpListResponse = (MessEmpListResponse) object;
            getMessList(messEmpListResponse);
        }
    }

    private void getMessList(MessEmpListResponse messEmpListResponse) {
        try {
            Log.e(TAG, "messEmpListResponse: " + new GsonBuilder().create().toJson(messEmpListResponse));
            if (messEmpListResponse != null) {
                if (messEmpListResponse.getMess_Emp_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, true));
                    MessEmpListAdapter listAdapter = new MessEmpListAdapter(this,
                            messEmpListResponse.getMess_Emp_list()
                            , list);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, messEmpListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in messEmpListResponse" + messEmpListResponse.getResponseCode());
                Toast.makeText(this, messEmpListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in messEmpListResponse", e);
        }
    }
    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

}