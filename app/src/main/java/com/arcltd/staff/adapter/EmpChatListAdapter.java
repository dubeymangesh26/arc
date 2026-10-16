package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.response.UserChatListResponse;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmpChatListAdapter extends RecyclerView.Adapter<EmpChatListAdapter.ViewHolder> {
    Context context;
    List<UserChatListResponse.EmployeeList> list;
    RecyclerView menuList;
    private UserChatListResponse wishList;
    private int deliveryPosition;


    public EmpChatListAdapter(FragmentActivity activity, List<UserChatListResponse.EmployeeList>
            bookingLists, RecyclerView menuList, UserChatListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @NonNull
    @Override
    public EmpChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_emplst_list, parent, false);
        return new EmpChatListAdapter.ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(EmpChatListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getName());
        holder.branchCode.setText(list.get(position).getBranch_code());
        
        String profilePicUrl = list.get(position).getProfilepic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            Glide.with(context)
                    .load(profilePicUrl) // image url
                    .placeholder(R.drawable.arcround) // any placeholder to load at start
                    .error(R.drawable.arcround)  // any image in case of error
                    .override(200, 200) // resizing
                    .centerCrop()
                    .into(holder.profilepic);
        } else {
            holder.profilepic.setImageResource(R.drawable.arcround);
        }




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
        TextView region,branchCode;
        CardView listRegion;
        CircleImageView profilepic;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);
            branchCode = itemView.findViewById(R.id.branchCode);
            profilepic = itemView.findViewById(R.id.profilePicture);



        }
    }
}
