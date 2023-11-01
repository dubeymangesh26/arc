package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddUpdateCustomerActivity;
import com.arcltd.staff.activity.detailsData.CustomerDetailsActivity;
import com.arcltd.staff.activity.otherAndMain.PdfViewerReaderActivity;
import com.arcltd.staff.response.CustomerListResponse;
import com.arcltd.staff.response.PDFListResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PDFListAdapter extends RecyclerView.Adapter<PDFListAdapter.ViewHolder> {
    Context context;
    List<PDFListResponse.PdfList> list;
    RecyclerView menuList;
    private PDFListResponse wishList;
    private int deliveryPosition;


    public PDFListAdapter(FragmentActivity activity, List<PDFListResponse.PdfList>
            bookingLists, RecyclerView menuList, PDFListResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.pdf_list_item, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvSubTitel.setText(list.get(position).getSub_title());

            holder.rePdfList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, PdfViewerReaderActivity.class)
                            .putExtra("link", list.get(position).getLink()));

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
        TextView tvTitle,tvSubTitel;
       RelativeLayout rePdfList;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rePdfList = itemView.findViewById(R.id.rePdfList);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubTitel = itemView.findViewById(R.id.tvSubTitel);


        }
    }

}
