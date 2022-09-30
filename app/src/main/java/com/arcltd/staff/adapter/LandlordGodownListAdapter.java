package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import com.arcltd.staff.activity.updateData.UpdateLandlordDetailsActivity;
import com.arcltd.staff.response.LandlordGodownListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LandlordGodownListAdapter extends RecyclerView.Adapter<LandlordGodownListAdapter.ViewHolder> {
    Context context;
    List<LandlordGodownListResponse.LandlordGodownList> list;
    RecyclerView menuList;
    private LandlordGodownListResponse wishList;
    private int deliveryPosition;


    public LandlordGodownListAdapter(FragmentActivity activity, List<LandlordGodownListResponse.LandlordGodownList>
            bookingLists, RecyclerView menuList, LandlordGodownListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_allmixed_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.branchCode.setText(list.get(position).getBranch_code());
        holder.item.setText(list.get(position).getLandlord_name());



        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_landlor_godown_list);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow()
                        .setLayout(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                try {
                    TextView tvGodownAddress,tvLandlordName, tvLndPan,tvLandLordAddress,tvCostCode,tvgdnofficeResd,
                            tvPeriodFrom, tvPeriodTo,tvRentAmt,tvGdnOffResArea,tvOpenAres;
                    ImageView edit;

                    tvGodownAddress = dialog.findViewById(R.id.tvGodownAddress);
                    tvLandlordName = dialog.findViewById(R.id.tvLandlordName);
                    tvLndPan = dialog.findViewById(R.id.tvLndPan);
                    tvLandLordAddress = dialog.findViewById(R.id.tvLandLordAddress);
                    tvgdnofficeResd = dialog.findViewById(R.id.tvgdnofficeResd);
                    tvPeriodFrom = dialog.findViewById(R.id.tvPeriodFrom);
                    tvPeriodTo = dialog.findViewById(R.id.tvPeriodTo);
                    tvRentAmt = dialog.findViewById(R.id.tvRentAmt);
                    tvGdnOffResArea = dialog.findViewById(R.id.tvGdnOffResArea);
                    tvOpenAres = dialog.findViewById(R.id.tvOpenAres);
                    tvCostCode = dialog.findViewById(R.id.tvCostCode);
                    edit = dialog.findViewById(R.id.edit);

                    tvGodownAddress.setText(list.get(position).getLocation_address());
                    tvLandlordName.setText(list.get(position).getLandlord_name());
                    tvLndPan.setText(list.get(position).getPan_no());
                    tvLandLordAddress.setText(list.get(position).getLandlord_address());
                    tvgdnofficeResd.setText(list.get(position).getOff_gdn_residence());
                    tvPeriodFrom.setText(list.get(position).getPeriod_from());
                    tvPeriodTo.setText(list.get(position).getPeriod_to());
                    tvRentAmt.setText(list.get(position).getRent_amount());
                    tvGdnOffResArea.setText(list.get(position).getOfgdn_area());
                    tvOpenAres.setText(list.get(position).getOpen_area());
                    tvCostCode.setText(list.get(position).getCost_code());

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String data = new Gson().toJson(list.get(position));
                            Log.e("TAG", "LandlordGodownList: "+data );
                            context.startActivity(new Intent(context, UpdateLandlordDetailsActivity.class)
                                    .putExtra("data",data));

                        }
                    });


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

        TextView branchCode,item;
        LinearLayout listRegion;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            branchCode = itemView.findViewById(R.id.branchCode);
            item = itemView.findViewById(R.id.item);
            listRegion = itemView.findViewById(R.id.listRegion);

        }
    }

}
