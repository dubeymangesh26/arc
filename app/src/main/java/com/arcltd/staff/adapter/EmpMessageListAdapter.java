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
import com.arcltd.staff.response.EmpMessageListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmpMessageListAdapter extends RecyclerView.Adapter<EmpMessageListAdapter.ViewHolder> {
    Context context;
    List<EmpMessageListResponse.EmployeeMessage> list;
    RecyclerView menuList;
    private EmpMessageListResponse wishList;
    private int deliveryPosition;


    public EmpMessageListAdapter(FragmentActivity activity, List<EmpMessageListResponse.EmployeeMessage>
            bookingLists, RecyclerView menuList, EmpMessageListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_renark_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.region.setText(list.get(position).getMessage());
        holder.date.setText(list.get(position).getCreated_date());
        holder.empcode.setText(list.get(position).getEmp_code());


        /*holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "MessOfficeGodownList: "+data );
                context.startActivity(new Intent(context, AdminBranchListActivity.class)
                        .putExtra("div_id", list.get(position).getDiv_id())
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
        TextView region,date,empcode;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            date = itemView.findViewById(R.id.date);
            empcode = itemView.findViewById(R.id.empcode);



        }
    }

}
