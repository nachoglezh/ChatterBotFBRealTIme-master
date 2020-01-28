package com.example.chatterbot.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatterbot.R;
import com.example.chatterbot.model.data.BackupItem;

import java.util.ArrayList;
import java.util.List;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHolder> {
    private List<BackupItem> backupItemList = new ArrayList<>();

    @NonNull
    @Override
    public BackupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.backup_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull BackupAdapter.ViewHolder holder, int position) {

        BackupItem backupItem = backupItemList.get(position);

        holder.tvMessage.setText("sdafsaf");
        holder.tvTime.setText(backupItem.getTime());
    }

    @Override
    public int getItemCount() {
        int num = 0;
        if(backupItemList != null){
            num = backupItemList.size();
        }
        return num;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contentLinearLayout, messageLinearLayout;
        private TextView tvMessage, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentLinearLayout = itemView.findViewById(R.id.contentLinearLayout);
            messageLinearLayout = itemView.findViewById(R.id.messageLinearLayout);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public void addMessage(BackupItem backupItem) {
        backupItemList.add(backupItem);
        notifyItemInserted(backupItemList.size());
    }
}
