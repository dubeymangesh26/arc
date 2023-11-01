package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.FeedbackListResponse;
import com.arcltd.staff.utility.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.ViewHolder> {
    Context context;
    List<FeedbackListResponse.FeedbackList> list;
    RecyclerView menuList;
    private FeedbackListResponse wishList;
    private int deliveryPosition;
    private SearchAddressCallBack searchAddressCallBack;
    private AddRemark addRemark;


    public FeedbackListAdapter(FragmentActivity activity, List<FeedbackListResponse.FeedbackList>
            bookingLists, RecyclerView menuList, FeedbackListResponse activeRerfreshWishList,
                               SearchAddressCallBack addressCallBack, AddRemark addddRemark) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;
        this.addRemark = addddRemark;
        this.searchAddressCallBack = addressCallBack;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_allfeedback_list, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getFeedback());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddressCallBack.searchAddressCallBack(list.get(position));
            }
        });
          if (RetrofitClient.getStringUserPreference(context, Constants.LOGIN_TYPE).equals("DGM")){
              holder.cvAdd.setVisibility(View.VISIBLE);
        }else{
              holder.cvAdd.setVisibility(View.GONE);
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemark.addRemarksdd(list.get(position));
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

    public interface SearchAddressCallBack {
        void searchAddressCallBack(FeedbackListResponse.FeedbackList feedbackList);
    }

    public interface AddRemark {
        void addRemarksdd(FeedbackListResponse.FeedbackList feedbackList);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView region;
        LinearLayout listRegion;
        CardView cvAdd;
        Button add;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);
            add = itemView.findViewById(R.id.add);
            cvAdd = itemView.findViewById(R.id.cvAdd);


        }
    }

}
