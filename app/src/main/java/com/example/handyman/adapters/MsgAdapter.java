package com.example.handyman.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyman.R;

import org.w3c.dom.Text;

import java.util.List;

// to show messages
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder>{

    private List<String> messages;
    private String ownerName;


    public MsgAdapter(List<String> messages, String ownerName) {
        this.messages = messages;
        this.ownerName = ownerName;
    }
    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_message, parent, false);
        return new MsgViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        String message = messages.get(position);
        holder.textMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MsgViewHolder extends RecyclerView.ViewHolder{
        TextView textMessage;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.txtMessageContent);
        }

        public TextView getTextMessage() {return textMessage;}
        public void setTextMessage(TextView textMessage) {this.textMessage = textMessage;}

    }
}

