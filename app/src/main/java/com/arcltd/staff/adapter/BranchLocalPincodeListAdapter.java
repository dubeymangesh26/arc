package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.detailsData.BranchPicodeDetailsActivity;
import com.arcltd.staff.response.PincodeBranchListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BranchLocalPincodeListAdapter extends RecyclerView.Adapter<BranchLocalPincodeListAdapter.ViewHolder> {
    Context context;
    private ArrayList<PincodeBranchListResponse> list;
    private ArrayList<PincodeBranchListResponse> beanList;
    RecyclerView menuList;
    private final BranchLocalPincodeListAdapter.RerfreshWishList shWishList;



    public BranchLocalPincodeListAdapter(Context context, ArrayList<PincodeBranchListResponse> bookingLists,
                                  RecyclerView menuList, BranchLocalPincodeListAdapter.RerfreshWishList branchRerfreshWishList) {
        this.context = context;
        this.menuList = menuList;
        this.list = bookingLists;
        this.beanList = new ArrayList<>(list);
        this.shWishList = branchRerfreshWishList;


    }

    @Override
    public BranchLocalPincodeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_branch_list, parent, false);
        return new BranchLocalPincodeListAdapter.ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(BranchLocalPincodeListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {



        if (list.get(position).getSc_branch_name() != null && !list.get(position).getSc_branch_name().equals("")) {
            holder.tvBranchName.setText(list.get(position).getSc_branch_name());
        }
        String number=list.get(position).getSc_branch_mngr_mob();

        holder.btnCallManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                context.startActivity(intent);

            }
        });
        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "saddLayoutCallBack: "+data );
                context.startActivity(new Intent(context, BranchPicodeDetailsActivity.class).putExtra("data", data));

            }
        });


    }
    @Override
    public int getItemCount() {

        return list.size();
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<PincodeBranchListResponse> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(beanList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (PincodeBranchListResponse item : beanList) {
                        if (item.getSc_service_pincode().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((List) results.values);
                notifyDataSetChanged();
            }

        };
    }
    public interface ListCallback {
    }

    public interface RerfreshWishList {

        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBranchName;
        LinearLayout listRegion;
        Button btnCallManger,btnViewDetails;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvBranchName = itemView.findViewById(R.id.tvBranchName);
            listRegion = itemView.findViewById(R.id.listRegion);
            btnCallManger = itemView.findViewById(R.id.btnCallManger);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);



        }
    }
}
