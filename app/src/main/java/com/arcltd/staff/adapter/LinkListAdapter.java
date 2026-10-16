package com.arcltd.staff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.PdfViewerReaderActivity;
import com.arcltd.staff.response.LinkListResponse;
import com.arcltd.staff.response.PDFListResponse;
import com.arcltd.staff.web_view.WebViewpoppupActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LinkListAdapter extends RecyclerView.Adapter<LinkListAdapter.ViewHolder> {
    Context context;
    List<LinkListResponse.PdfList> list;
    RecyclerView menuList;
    private LinkListResponse wishList;
    private int deliveryPosition;


    public LinkListAdapter(FragmentActivity activity, List<LinkListResponse.PdfList>
            bookingLists, RecyclerView menuList, LinkListResponse activeRerfreshWishList) {
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

                    context.startActivity(new Intent(context, WebViewpoppupActivity.class)
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
