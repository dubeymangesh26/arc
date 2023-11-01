package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.listData.AdminBranchListActivity;
import com.arcltd.staff.response.BookingListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {
    Context context;
    List<BookingListResponse.BookingList> list;
    RecyclerView menuList;
    private BookingListResponse wishList;
    private int deliveryPosition;


    public BookingListAdapter(FragmentActivity activity, List<BookingListResponse.BookingList>
            bookingLists, RecyclerView menuList, BookingListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_booking_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.branchCode.setText(list.get(position).getBranch_code());
        holder.preParcel.setText(list.get(position).getPrevious_m_parcel());
        holder.preTotal.setText(list.get(position).getPrevious_m_total());
        holder.curParcel.setText(list.get(position).getCurrent_m_parcel());
        holder.curTotal.setText(list.get(position).getCurrent_m_total());
        holder.difParcel.setText(list.get(position).getDiff_in_parcel());
        holder.difTotal.setText(list.get(position).getDiff_in_total());

       /* holder.listBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "BookingList: "+data );
                context.startActivity(new Intent(context, AdminBranchListActivity.class)
                        .putExtra("div_id", list.get(position).getBkg_id())
                        .putExtra("A","D"));

            }
        });*/


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
        TextView branchCode,preParcel,preTotal,curParcel,curTotal,difParcel,difTotal;
        LinearLayout listBooking;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            branchCode = itemView.findViewById(R.id.branchCode);
            preParcel = itemView.findViewById(R.id.preParcel);
            preTotal = itemView.findViewById(R.id.preTotal);
            curParcel = itemView.findViewById(R.id.curParcel);
            curTotal = itemView.findViewById(R.id.curTotal);
            difParcel = itemView.findViewById(R.id.difParcel);
            difTotal = itemView.findViewById(R.id.difTotal);
            listBooking = itemView.findViewById(R.id.listBooking);



        }
    }

}
