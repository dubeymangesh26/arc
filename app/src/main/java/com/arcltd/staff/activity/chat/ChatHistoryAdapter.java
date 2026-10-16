package com.arcltd.staff.activity.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.sqlliteDatabase.ChatLocalDBHelper;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHistoryAdapter
        extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<EmployeeListResponse.EmployeeList> list;
    private final ChatLocalDBHelper dbHelper;
    private final String myCode;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

    public ChatHistoryAdapter(
            Context context,
            List<EmployeeListResponse.EmployeeList> list
    ) {
        this.context = context;
        this.list = list;
        this.dbHelper = ChatLocalDBHelper.getInstance(context);
        this.myCode = RetrofitClient.getStringUserPreference(context, Constants.EMP_CODE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_chat_history,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            @SuppressLint("RecyclerView") int position
    ) {
        if (list == null || position < 0 || position >= list.size()) {
            return;
        }

        EmployeeListResponse.EmployeeList user = list.get(position);

        if (user == null) {
            return;
        }

        // =====================================================
        // USER NAME
        // =====================================================

        final String finalEmployeeName = (user.getEmp_name() != null && !user.getEmp_name().trim().isEmpty()) ? user.getEmp_name() : "Employee";
        holder.name.setText(finalEmployeeName);

        // =====================================================
        // PROFILE PHOTO
        // =====================================================

        String profilePicUrl = user.getProfilepic();

        Glide.with(context)
                .load(profilePicUrl != null && !profilePicUrl.trim().isEmpty() ? profilePicUrl : R.drawable.arcround)
                .placeholder(R.drawable.arcround)
                .error(R.drawable.arcround)
                .into(holder.profilePic);

        // =====================================================
        // LAST MESSAGE, UNREAD BADGE & STATUS TICKS
        // =====================================================

        ChatMessage lastMessage = dbHelper.getLastMessage(myCode, user.getEmp_code());
        int localUnread = dbHelper.getUnreadCount(myCode, user.getEmp_code());
        int unread = (localUnread > 0) ? localUnread : user.getUnread_count();

        if (lastMessage != null) {
            String lastText = lastMessage.getDisplayMessage();
            holder.lastMessage.setText(lastText != null ? lastText : "");
            holder.time.setText(formatTimestamp(lastMessage.getTimestamp()));

            if (lastMessage.isSent()) {
                holder.statusTicks.setVisibility(View.VISIBLE);
                if (lastMessage.getStatus() == 2) {
                    holder.statusTicks.setText("✓✓");
                    holder.statusTicks.setTextColor(Color.parseColor("#34B7F1")); // WhatsApp Blue Tick
                } else if (lastMessage.getStatus() == 1) {
                    holder.statusTicks.setText("✓✓");
                    holder.statusTicks.setTextColor(Color.parseColor("#8696A0")); // Grey Double Tick
                } else {
                    holder.statusTicks.setText("✓");
                    holder.statusTicks.setTextColor(Color.parseColor("#8696A0")); // Grey Single Tick
                }
            } else {
                holder.statusTicks.setVisibility(View.GONE);
            }
        } else if (user.getLast_message() != null && !user.getLast_message().trim().isEmpty()) {
            holder.lastMessage.setText(user.getLast_message());
            holder.time.setText(user.getLast_seen() != null ? user.getLast_seen() : "");
            holder.statusTicks.setVisibility(View.GONE);
        } else {
            holder.lastMessage.setText("Start a conversation");
            holder.time.setText("");
            holder.statusTicks.setVisibility(View.GONE);
        }

        // =====================================================
        // UNREAD COUNT & TIME COLOR
        // =====================================================

        if (unread > 0) {
            holder.unreadCount.setVisibility(View.VISIBLE);
            holder.unreadCount.setText(String.valueOf(unread));
            holder.time.setTextColor(Color.parseColor("#25D366")); // WhatsApp Green Time
        } else {
            holder.unreadCount.setVisibility(View.GONE);
            holder.time.setTextColor(Color.parseColor("#667781")); // Standard Grey Time
        }

        // =====================================================
        // OPEN CHAT
        // =====================================================

        holder.layoutItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userName", finalEmployeeName);
            intent.putExtra("emp_code", user.getEmp_code() != null ? user.getEmp_code() : "");
            intent.putExtra("fcmToken", user.getToken() != null ? user.getToken() : "");
            context.startActivity(intent);
        });
    }

    private String formatTimestamp(long timestamp) {
        if (Infrastructure.isToday(timestamp)) {
            return timeFormat.format(new Date(timestamp));
        }
        return dateFormat.format(new Date(timestamp));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, lastMessage, time, unreadCount, statusTicks;
        CircleImageView profilePic;
        LinearLayout layoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            lastMessage = itemView.findViewById(R.id.tv_last_message);
            time = itemView.findViewById(R.id.tv_time);
            unreadCount = itemView.findViewById(R.id.tv_unread_count);
            statusTicks = itemView.findViewById(R.id.tv_status_ticks);
            profilePic = itemView.findViewById(R.id.profile_image);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
}
