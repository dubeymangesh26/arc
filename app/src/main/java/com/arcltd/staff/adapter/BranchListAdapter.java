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
import com.arcltd.staff.activity.detailsData.BranchDetailsActivity;
import com.arcltd.staff.response.BranchListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.ViewHolder> {
    Context context;
    List<BranchListResponse.BranchList> list;
    private List<BranchListResponse.BranchList> beanList;
    RecyclerView menuList;
    private RerfreshWishList shWishList;



    public BranchListAdapter(Context context, List<BranchListResponse.BranchList>
            bookingLists, RecyclerView menuList,
                             RerfreshWishList branchRerfreshWishList) {
        this.context = context;
        this.menuList = menuList;
        this.beanList = bookingLists;
        this.list = bookingLists;
        this.shWishList = branchRerfreshWishList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_branch_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.tvBranchName.setText(list.get(position).getBranch_name());
        String number=list.get(position).getBranch_mngr_mob();

        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "saddLayoutCallBack: "+data );
                context.startActivity(new Intent(context, BranchDetailsActivity.class).putExtra("data", data));

            }
        });

        holder.btnCallManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                context.startActivity(intent);

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
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = beanList;
                } else {
                    ArrayList<BranchListResponse.BranchList> filteredList = new ArrayList<>();
                    for (BranchListResponse.BranchList data : beanList) {
                        if (data.getBranch_name().toLowerCase().contains(charString) ||
                                data.getBranch_name().toUpperCase().contains(charString) ||
                                data.getBranch_code().toUpperCase().contains(charString) ||
                                data.getBranch_address().toUpperCase().contains(charString)) {
                            filteredList.add(data);
                        }
                    }
                    list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<BranchListResponse.BranchList>) filterResults.values;

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
