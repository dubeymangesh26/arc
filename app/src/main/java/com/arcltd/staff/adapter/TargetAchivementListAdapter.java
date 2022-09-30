package com.arcltd.staff.adapter;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.TargetResponseResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TargetAchivementListAdapter extends RecyclerView.Adapter<TargetAchivementListAdapter.ViewHolder> {
    Context context;
    List<TargetResponseResponse.BookingDetails> list;
    List<TargetResponseResponse.BookingTarget> tlist;
    RecyclerView menuList;
    private TargetResponseResponse wishList;
    private int deliveryPosition;
    private int mYear, mMonth, mDay, mHour, mMinute;


    public TargetAchivementListAdapter(FragmentActivity activity, List<TargetResponseResponse.BookingDetails>
            bookingLists,List<TargetResponseResponse.BookingTarget>
            bookingTarget, RecyclerView menuList, TargetResponseResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;
        this.tlist = bookingTarget;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_target_achvement_list, parent, false);
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


        try {
            holder.tvCurrParcel.setText(list.get(position).getParcel());
            holder.tvCurrPart.setText(list.get(position).getPart());
            holder.tvCurrFtl.setText(list.get(position).getFtl());
            holder.tvCurrTotal.setText(list.get(position).getTotal());
            holder.tvCurrentMDate.setText(list.get(position).getDate());
        }catch (Exception e){
            Log.e(TAG, "onBindViewHolder: ",e );
        }





        /* holder.btBookingFig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "TragetListActivity: "+data );
                context.startActivity(new Intent(context, MessListActivity.class)
                        .putExtra("data", data));

            }
        });*/



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
        TextView tcPriviousMonthDate,tvPriParcelAmt,tvPartAmt,tvPriFtlAmt,tvTotalAmt,tvCurrentMDate,
                tvCurrParcel,tvCurrPart,tvCurrFtl,tvCurrTotal;
        LinearLayout listRegion,liViewAll;
        Button btBookingFig;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);



            tvCurrentMDate = itemView.findViewById(R.id.tvCurrentMDate);
            tvCurrParcel = itemView.findViewById(R.id.tvCurrParcel);
            tvCurrPart = itemView.findViewById(R.id.tvCurrPart);
            tvCurrFtl = itemView.findViewById(R.id.tvCurrFtl);
            tvCurrTotal = itemView.findViewById(R.id.tvCurrTotal);
            listRegion = itemView.findViewById(R.id.listRegion);
            btBookingFig = itemView.findViewById(R.id.btBookingFig);
            liViewAll = itemView.findViewById(R.id.liViewAll);



        }
    }

}
