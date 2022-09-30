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
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.InsAmoutResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InsCMPAmountListAdapter extends RecyclerView.Adapter<InsCMPAmountListAdapter.ViewHolder> {
    Context context;
    List<InsAmoutResponse.InsidentalList> list;
    private List<InsAmoutResponse.InsidentalList> beanList;
    RecyclerView menuList;
    private RerfreshWishList shWishList;


    public InsCMPAmountListAdapter(Context context, List<InsAmoutResponse.InsidentalList>
            bookingLists, RecyclerView menuList) {
        this.context = context;
        this.menuList = menuList;
        this.beanList = bookingLists;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_ins_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.name.setText(list.get(position).getMonth());
        holder.text.setText("RS. : "+list.get(position).getAmount_paid());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_incidental_details_list);
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
                    TextView tvBranchCode,tvPartyCode, tvFRTAmt,tvInsAmt,tvAmtPaid,tvPaidTo
                            ,tvMobileNo,tvPaidBy;

                    tvPartyCode = dialog.findViewById(R.id.tvPartyCode);
                    tvBranchCode = dialog.findViewById(R.id.tvBranchCode);
                    tvFRTAmt = dialog.findViewById(R.id.tvFRTAmt);
                    tvInsAmt = dialog.findViewById(R.id.tvInsAmt);
                    tvAmtPaid = dialog.findViewById(R.id.tvAmtPaid);
                    tvPaidTo = dialog.findViewById(R.id.tvPaidTo);
                    tvMobileNo = dialog.findViewById(R.id.tvMobileNo);
                    tvPaidBy = dialog.findViewById(R.id.tvPaidBy);


                    tvPartyCode.setText(list.get(position).getCompany_code());
                    tvBranchCode.setText(list.get(position).getBranch_code());
                    tvFRTAmt.setText(list.get(position).getFright_amount());
                    tvInsAmt.setText(list.get(position).getIncidental_amount());
                    tvAmtPaid.setText(list.get(position).getAmount_paid());
                    tvPaidTo.setText(list.get(position).getPaid_to());
                    tvMobileNo.setText(list.get(position).getPaid_tomobile_no());
                    tvPaidBy.setText(list.get(position).getPaid_by());

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
        TextView name,text;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            listRegion = itemView.findViewById(R.id.listRegion);
            text = itemView.findViewById(R.id.text);



        }
    }

}
