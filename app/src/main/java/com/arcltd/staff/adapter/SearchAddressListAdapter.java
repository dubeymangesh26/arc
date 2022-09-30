package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.BranchListResponse;

import java.util.List;

public class SearchAddressListAdapter extends RecyclerView.Adapter<SearchAddressListAdapter.ViewHolder> {

    Context context;
    List<BranchListResponse.BranchList> list;
    private SearchAddressCallBack searchAddressCallBack;

    public SearchAddressListAdapter(Context context, List<BranchListResponse.BranchList> list,
                                    SearchAddressCallBack addressCallBack) {
        this.context = context;
        this.list = list;
        this.searchAddressCallBack = addressCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_branch_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvAddress.setText(list.get(position).getBranch_name());

        holder.liAddressSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddressCallBack.searchAddressCallBack(list.get(position));
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface SearchAddressCallBack {
        void searchAddressCallBack(BranchListResponse.BranchList searchAddressModel);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        CardView liAddressSearch;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvBranchName);
           liAddressSearch = itemView.findViewById(R.id.listRegion);

        }
    }
}