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
import com.arcltd.staff.response.HolidaysResponse;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HolidaysListAdapter extends RecyclerView.Adapter<HolidaysListAdapter.ViewHolder> {
    Context context;
    List<HolidaysResponse.HolidysList> list;
    RecyclerView menuList;
    private HolidaysResponse wishList;
    private int deliveryPosition;


    public HolidaysListAdapter(FragmentActivity activity, List<HolidaysResponse.HolidysList>
            bookingLists, RecyclerView menuList, HolidaysResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_holidays_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.tvFestvalName.setText(list.get(position).getFestval());

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr=list.get(position).getFstDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String outputDateStr = outputFormat.format(date);

        holder.tvFestDate.setText(outputDateStr);


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
        TextView tvFestvalName,tvFestDate;
        LinearLayout listholidays;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvFestvalName = itemView.findViewById(R.id.tvFestvalName);
            tvFestDate = itemView.findViewById(R.id.tvFestDate);
            listholidays = itemView.findViewById(R.id.listholidays);




        }
    }

}
