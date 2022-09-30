package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.MessEmpListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessEmpListAdapter extends RecyclerView.Adapter<MessEmpListAdapter.ViewHolder> {
    Context context;
    List<MessEmpListResponse.MessEmpList> list;
    private List<MessEmpListResponse.MessEmpList> beanList;
    RecyclerView menuList;
    private RerfreshWishList shWishList;



    public MessEmpListAdapter(Context context, List<MessEmpListResponse.MessEmpList>
            bookingLists, RecyclerView menuList) {
        this.context = context;
        this.menuList = menuList;
        this.beanList = bookingLists;
        this.list = bookingLists;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_messemp_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvEmpName.setText(list.get(position).getEmp_name());
        holder.tvEmpCode.setText(list.get(position).getEmp_code());
        holder.tvWorkBranch.setText(list.get(position).getEmp_branch());
        holder.tvRoomNo.setText(list.get(position).getRoom_no());
        String number=list.get(position).getEmp_mob();


        holder.tvCall.setOnClickListener(new View.OnClickListener() {
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


    public interface ListCallback {
    }

    public interface RerfreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessName,tvCall,tvEmpName,tvEmpCode,tvWorkBranch,tvRoomNo;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvCall = itemView.findViewById(R.id.tvCall);
            tvEmpName = itemView.findViewById(R.id.tvEmpName);
            tvEmpCode = itemView.findViewById(R.id.tvEmpCode);
            tvWorkBranch = itemView.findViewById(R.id.tvWorkBranch);
            tvRoomNo = itemView.findViewById(R.id.tvRoomNo);




        }
    }

}
