package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.EmpRemarkListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmployeeRemarkListAdapter extends RecyclerView.Adapter<EmployeeRemarkListAdapter.ViewHolder> {
    Context context;
    List<EmpRemarkListResponse.EmployeeRemarkList> list;
    RecyclerView menuList;
    private EmpRemarkListResponse wishList;
    private int deliveryPosition;
    private DeleteRemark deleteRemark;


    public EmployeeRemarkListAdapter(FragmentActivity activity, List<EmpRemarkListResponse.EmployeeRemarkList>
            bookingLists, RecyclerView menuList, EmpRemarkListResponse activeRerfreshWishList,DeleteRemark remark) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;
        this.deleteRemark = remark;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_empremark_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getRemark());
        holder.date.setText(list.get(position).getCreated_date());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRemark.remark(list.get(position).getR_id());

            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface ListCallback {
    }
    public interface DeleteRemark {
       // void remark(EmpRemarkListResponse.EmployeeRemarkList remarkbackList);

        void remark(String r_id);
    }
    public interface RerfreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView region,date;
        LinearLayout listRegion;
        ImageView delete;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);



        }
    }

}
