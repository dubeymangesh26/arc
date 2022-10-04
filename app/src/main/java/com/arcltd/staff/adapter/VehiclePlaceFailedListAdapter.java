package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import com.arcltd.staff.response.VehicleFailListResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VehiclePlaceFailedListAdapter extends RecyclerView.Adapter<VehiclePlaceFailedListAdapter.ViewHolder> {
    Context context;
    List<VehicleFailListResponse.PlaceFaildList> list;
    RecyclerView menuList;
    private VehicleFailListResponse wishList;
    private int deliveryPosition;


    public VehiclePlaceFailedListAdapter(FragmentActivity activity, List<VehicleFailListResponse.PlaceFaildList>
            bookingLists, RecyclerView menuList, VehicleFailListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_vehicle_palce_faild_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.branchCode.setText(list.get(position).getBranch_code());
        holder.item.setText(list.get(position).getDate());



        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(R.layout.item_faildplc_vehicle_list);
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
                    TextView tvDate,tvBranchCode, tvVehicleType,tvRemark,tvEmpCode,tvEmpName;

                    tvDate = dialog.findViewById(R.id.tvDate);
                    tvBranchCode = dialog.findViewById(R.id.tvBranchCode);
                    tvVehicleType = dialog.findViewById(R.id.tvVehicleType);
                    tvRemark = dialog.findViewById(R.id.tvRemark);
                    tvEmpCode = dialog.findViewById(R.id.tvEmpCode);
                    tvEmpName = dialog.findViewById(R.id.tvEmpName);


                    tvDate.setText(list.get(position).getDate());
                    tvBranchCode.setText(list.get(position).getBranch_code());
                    tvVehicleType.setText(list.get(position).getVehicle_type());
                    tvRemark.setText(list.get(position).getRemark());
                    tvEmpCode.setText(list.get(position).getEmp_code());
                    tvEmpName.setText(list.get(position).getEmp_name());

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
