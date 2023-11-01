package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.PdfViewerReaderActivity;
import com.arcltd.staff.response.InchargeListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InchargeListAdapter extends RecyclerView.Adapter<InchargeListAdapter.ViewHolder> {
    Context context;
    List<InchargeListResponse.InchargeList> list;
    RecyclerView menuList;
    private InchargeListResponse wishList;
    private int deliveryPosition;


    public InchargeListAdapter(FragmentActivity activity, List<InchargeListResponse.InchargeList>
            bookingLists, RecyclerView menuList, InchargeListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_region_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getName());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = list.get(position).getLink();
                /*Uri path = Uri.parse(list.get(position).getLink());
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(path, "application/pdf");
                try {
                    context.startActivity(pdfOpenintent);
                }
                catch (ActivityNotFoundException e) {

                }*/
                context.startActivity(new Intent(context, PdfViewerReaderActivity.class).putExtra("link",path));

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
        TextView region;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);



        }
    }

}
