package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddMoterCycleActivity;
import com.arcltd.staff.response.BookingListResponse;
import com.arcltd.staff.response.RateCardListResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RateCardListAdapter extends RecyclerView.Adapter<RateCardListAdapter.ViewHolder> {
    Context context;
    List<RateCardListResponse.RatecardList> list;
    RecyclerView menuList;
    private RateCardListResponse wishList;
    private int deliveryPosition;


    public RateCardListAdapter(FragmentActivity activity, List<RateCardListResponse.RatecardList>
            bookingLists, RecyclerView menuList, RateCardListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_ratecard_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.fromBranch.setText(list.get(position).getBranch_code());
        holder.dest.setText("To : "+list.get(position).getDest_code());
        holder.rate.setText("\u20B9 "+list.get(position).getBooking_rate());

        holder.listRetecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(R.layout.item_ratecard_details);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow()
                        .setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );


                try {
                    TextView tvFromBranchCode,tvToBranch,tvToBranchName,tvBookingRate,tvDeliveryRate,tvTransitDay
                            ,tvTransitRoot;


                    tvFromBranchCode = dialog.findViewById(R.id.tvFromBranchCode);
                    tvToBranch = dialog.findViewById(R.id.tvToBranch);
                    tvToBranchName = dialog.findViewById(R.id.tvToBranchName);
                    tvBookingRate = dialog.findViewById(R.id.tvBookingRate);
                    tvDeliveryRate = dialog.findViewById(R.id.tvDeliveryRate);
                    tvTransitDay = dialog.findViewById(R.id.tvTransitDay);
                    tvTransitRoot = dialog.findViewById(R.id.tvTransitRoot);


                    tvFromBranchCode.setText(list.get(position).getBranch_code());
                    tvToBranch.setText(list.get(position).getDest_code());
                    tvToBranchName.setText(list.get(position).getDest_name());
                    tvBookingRate.setText("\u20B9 "+list.get(position).getBooking_rate());
                    tvDeliveryRate.setText("\u20B9 "+list.get(position).getDelivery_rate());
                    tvTransitDay.setText(list.get(position).getTransit_days());
                    tvTransitRoot.setText(list.get(position).getTransit_root());

                } catch (Exception e) {
                    Log.e("TAG", "onClick: ", e);
                }

                ImageView close = (ImageView) dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
        TextView fromBranch,dest,rate;
        LinearLayout listRetecard;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            fromBranch = itemView.findViewById(R.id.fromBranch);
            dest = itemView.findViewById(R.id.dest);
            rate = itemView.findViewById(R.id.rate);
            listRetecard = itemView.findViewById(R.id.listRetecard);



        }
    }

}
