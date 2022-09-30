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
import com.arcltd.staff.response.EmployeeListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmployeeMSGListAdapter extends RecyclerView.Adapter<EmployeeMSGListAdapter.ViewHolder> {
    Context context;
    List<EmployeeListResponse.EmployeeList> list;
    RecyclerView menuList;
    private EmployeeListResponse wishList;
    private int deliveryPosition;
    private SearchAddressCallBack searchAddressCallBack;


    public EmployeeMSGListAdapter(FragmentActivity activity, List<EmployeeListResponse.EmployeeList>
            bookingLists, RecyclerView menuList, EmployeeListResponse activeRerfreshWishList,
                                  SearchAddressCallBack addressCallBack) {
        this.context = activity;
        this.menuList = menuList;
        this.wishList = activeRerfreshWishList;
        this.list = bookingLists;
        this.searchAddressCallBack = addressCallBack;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_emplst_list, parent, false);
        return new ViewHolder(view);


    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.region.setText(list.get(position).getEmp_name());
        holder.branchCode.setText(list.get(position).getBranch_code());

        holder.listRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddressCallBack.searchdAddressCallBack(
                        list.get(position).getEmp_name(),
                        list.get(position).getEmp_code(),
                        list.get(position).getBranch_code(),
                        list.get(position).getRegion_id(),
                        list.get(position).getRegion_name(),
                        list.get(position).getDivision_id(),
                        list.get(position).getDivision_name(),
                        list.get(position).getEmp_desig(),
                        list.get(position).getToken());

            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface SearchAddressCallBack {
        void searchdAddressCallBack(String empName, String empCode, String branchCode, String region_id, String region_name,
                                    String division_id, String division_name, String design,String token);
    }
    public interface ListCallback {
    }

    public interface RerfreshWishList {
        void refresh(Boolean b);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView region,branchCode;
        LinearLayout listRegion;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            region = itemView.findViewById(R.id.region);
            listRegion = itemView.findViewById(R.id.listRegion);
            branchCode = itemView.findViewById(R.id.branchCode);



        }
    }

}
