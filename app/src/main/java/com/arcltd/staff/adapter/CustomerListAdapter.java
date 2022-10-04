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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddUpdateCustomerActivity;
import com.arcltd.staff.activity.detailsData.CustomerDetailsActivity;
import com.arcltd.staff.response.CustomerListResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    Context context;
    List<CustomerListResponse.CustomerList> list;
    RecyclerView menuList;
    private CustomerListResponse wishList;
    private int deliveryPosition;


    public CustomerListAdapter(FragmentActivity activity, List<CustomerListResponse.CustomerList>
            bookingLists, RecyclerView menuList, CustomerListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_customer_list, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.region.setText(list.get(position).getCompany_name());
        holder.branchCode.setText(list.get(position).getBranch_code());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = new Gson().toJson(list.get(position));
                    Log.e("TAG", "saddLayoutCallBack: " + data);
                    context.startActivity(new Intent(context, AddUpdateCustomerActivity.class)
                            .putExtra("data", data));

                }
            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final BottomSheetDialog dialog = new BottomSheetDialog(context);
                    dialog.setContentView(R.layout.item_customer_detail);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    dialog.getWindow()
                            .setLayout(
                                    ViewGroup.LayoutParams.FILL_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );


                    try {

                        TextView tvCustomerType = dialog.findViewById(R.id.tvCustomerType);
                        tvCustomerType.setText(list.get(position).getCust_type());

                        TextView tvNoofVisit = dialog.findViewById(R.id.tvNoofVisit);
                        tvNoofVisit.setText(list.get(position).getNoof_visit());

                        TextView tvCustEmailID = dialog.findViewById(R.id.tvCustEmailID);
                        tvCustEmailID.setText(list.get(position).getCust_email());

                        TextView tvCustomerDesign = dialog.findViewById(R.id.tvCustomerDesign);
                        tvCustomerDesign.setText(list.get(position).getCust_design());

                        TextView tvCustomerMobile = dialog.findViewById(R.id.tvCustomerMobile);
                        tvCustomerMobile.setText(list.get(position).getCust_mobile());

                        TextView tvCustomerName = dialog.findViewById(R.id.tvCustomerName);
                        tvCustomerName.setText(list.get(position).getCust_name());

                        TextView tvCompanyName = dialog.findViewById(R.id.tvCompanyName);
                        tvCompanyName.setText(list.get(position).getCompany_name());

                        TextView tvVisitPerson = dialog.findViewById(R.id.tvVisitPerson);
                        tvVisitPerson.setText(list.get(position).getM_name());

                        TextView tvBranch = dialog.findViewById(R.id.tvBranch);
                        tvBranch.setText(list.get(position).getBranch_code());
                    } catch (Exception e) {
                        Log.e("TAG", "onClick: ", e);
                    }
                    Button viewFeedback = dialog.findViewById(R.id.viewFeedback);
                    viewFeedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String data = new Gson().toJson(list.get(position));
                            Log.e("TAG", "saddLayoutCallBack: " + data);
                            Intent intent=new Intent(context,CustomerDetailsActivity.class)
                                    .putExtra("data", data);
                            dialog.getContext().startActivity(intent);

                        }
                    });


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
        TextView region,branchCode;
        Button edit,view;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            branchCode = itemView.findViewById(R.id.branchCode);
            edit = itemView.findViewById(R.id.edit);
            view = itemView.findViewById(R.id.view);


        }
    }

}
