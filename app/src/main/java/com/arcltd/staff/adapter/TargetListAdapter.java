package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddDailyBusinessAmountActivity;
import com.arcltd.staff.activity.detailsData.TargetAchivementDetailsActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.TargetResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TargetListAdapter extends RecyclerView.Adapter<TargetListAdapter.ViewHolder> {
    Context context;
    List<TargetResponseResponse.BookingTarget> list;
    RecyclerView menuList;
    private TargetResponseResponse wishList;
    private int deliveryPosition;
    private int mYear, mMonth, mDay, mHour, mMinute;


    public TargetListAdapter(FragmentActivity activity, List<TargetResponseResponse.BookingTarget>
            bookingLists,RecyclerView menuList, TargetResponseResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_target_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

       /* tcPriviousMonthDate,tvPriParcelAmt,tvPartAmt,tvPriFtlAmt,tvTotalAmt,tvCurrentMDate,
                tvCurrParcel,tvCurrPart,tvCurrFtl,tvCurrTotal*/
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        String m= String.valueOf(mMonth+1);
        String current_date =mDay + "-" + (mMonth+1) + "-" + mYear;
        String privious_m_date =mDay + "-" + mMonth + "-" + mYear;
        Log.e("DateSelector", "current_date: "+current_date+" privious_m_date " +privious_m_date);

        if (RetrofitClient.getStringUserPreference(context, Constants.LOGIN_TYPE).equals("BM")){
            holder.addBusiness.setVisibility(View.VISIBLE);
            holder.imagevehicle.setVisibility(View.GONE);
        }else {
            holder.addBusiness.setVisibility(View.GONE);
            holder.imagevehicle.setVisibility(View.VISIBLE);
        }

        holder.tvBranch.setText(list.get(position).getBranch_name());

        holder.listtarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "TragetListActivity: "+data );
                context.startActivity(new Intent(context, TargetAchivementDetailsActivity.class)
                        .putExtra("data", data));

            }
        });

        holder.addBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddDailyBusinessAmountActivity.class)
                        .putExtra("OFFICETYPE","STAFF RESIDENCE");
                context.startActivity(intent);
            }
        });



    }
    @Override
    public int getItemCount() {
        return list.size();

    }




    public interface ListCallback {
    }

    public interface RerfreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBranch;
        LinearLayout listtarget;
        Button addBusiness;
        ImageView imagevehicle;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


            tvBranch = itemView.findViewById(R.id.tvBranch);
            addBusiness = itemView.findViewById(R.id.addBusiness);
            listtarget = itemView.findViewById(R.id.listtarget);
            imagevehicle = itemView.findViewById(R.id.imagevehicle);



        }
    }

}
