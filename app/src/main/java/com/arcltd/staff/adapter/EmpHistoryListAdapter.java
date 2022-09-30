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
import com.arcltd.staff.response.EmployeeTransferHistoryResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringTokenizer;

public class EmpHistoryListAdapter extends RecyclerView.Adapter<EmpHistoryListAdapter.ViewHolder> {
    Context context;
    List<EmployeeTransferHistoryResponse.EmpTrHistoryList> list;
    RecyclerView menuList;
    private EmployeeTransferHistoryResponse wishList;
    private int deliveryPosition;


    public EmpHistoryListAdapter(FragmentActivity activity, List<EmployeeTransferHistoryResponse.EmpTrHistoryList>
            bookingLists, RecyclerView menuList, EmployeeTransferHistoryResponse activeRerfreshWishList) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_empall_history_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String date=list.get(position).getTransfer_date();
        StringTokenizer tk = new StringTokenizer(date);
        String showDate = tk.nextToken();  // <---  yyyy-mm-dd
        String time = tk.nextToken();  // <---  hh:mm:ss
        String[] items1 = showDate.split("-");
        String date1=items1[2];
        String month=items1[1];
        String year=items1[0];

        holder.transDate.setText(date1+"-"+month+"-"+year);
        holder.fromBranch.setText(list.get(position).getFrom_branch());
        holder.toBranch.setText(list.get(position).getTo_branch());


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
        TextView transDate,fromBranch,toBranch;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            transDate = itemView.findViewById(R.id.transDate);
            fromBranch = itemView.findViewById(R.id.fromBranch);
            toBranch = itemView.findViewById(R.id.toBranch);



        }
    }

}
