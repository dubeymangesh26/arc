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
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.InsCMPAmountActivity;
import com.arcltd.staff.response.InsCompanyResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InsCompanyListAdapter extends RecyclerView.Adapter<InsCompanyListAdapter.ViewHolder> {
    Context context;
    List<InsCompanyResponse.InsidentalList> list;
    private List<InsCompanyResponse.InsidentalList> beanList;
    RecyclerView menuList;
    private RerfreshWishList shWishList;


    public InsCompanyListAdapter(Context context, List<InsCompanyResponse.InsidentalList>
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
        view = LayoutInflater.from(context).inflate(R.layout.item_ins_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.name.setText(list.get(position).getCompany_name());
        holder.text.setText(list.get(position).getBranch_code());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = new Gson().toJson(list.get(position));
                Log.e("TAG", "saddLayoutCallBack: "+data );
                context.startActivity(new Intent(context, InsCMPAmountActivity.class).putExtra("data", data));

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
        TextView name,text;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            listRegion = itemView.findViewById(R.id.listRegion);
            text = itemView.findViewById(R.id.text);


        }
    }

}
