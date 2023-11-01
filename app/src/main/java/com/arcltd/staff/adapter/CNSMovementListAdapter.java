package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.BookingListResponse;
import com.arcltd.staff.response.ConsignmentMovementListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CNSMovementListAdapter extends RecyclerView.Adapter<CNSMovementListAdapter.ViewHolder> {
    Context context;
    List<ConsignmentMovementListResponse.ConsignmentList> list;
    RecyclerView menuList;
    private ConsignmentMovementListResponse wishList;
    private int deliveryPosition;


    public CNSMovementListAdapter(FragmentActivity activity, List<ConsignmentMovementListResponse.ConsignmentList>
            bookingLists, RecyclerView menuList, ConsignmentMovementListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_cns_movement_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.moveDate.setText(list.get(position).getMove_date());
        holder.moveFrom.setText(list.get(position).getMove_From());
        holder.moveTo.setText(list.get(position).getMove_To());


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
        TextView moveDate,moveFrom,moveTo;
        LinearLayout listMovement;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            moveDate = itemView.findViewById(R.id.moveDate);
            moveFrom = itemView.findViewById(R.id.moveFrom);
            moveTo = itemView.findViewById(R.id.moveTo);
            listMovement = itemView.findViewById(R.id.listMovement);



        }
    }

}
