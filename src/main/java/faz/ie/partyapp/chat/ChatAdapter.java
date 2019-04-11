package faz.ie.partyapp.chat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import faz.ie.partyapp.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>
{

    private List<ChatObject> chatlist;
    private Context context;

    public ChatAdapter(List<ChatObject>matcheslist, Context context)
    {
        this.chatlist = matcheslist;
        this.context = context;
    }
    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position)
    {
        holder.mMessage.setText(chatlist.get(position).getMessage());
        if(chatlist.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#2DB4C8")); // textcolor for currentuser
           // holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
            //holder.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
            holder.mContainer.setHorizontalGravity(Gravity.RIGHT);

            //setting textback ground for current user
        }else
            {
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#ffffff"));
           // holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
            holder.mContainer.setHorizontalGravity(Gravity.LEFT);
            holder.mContainer.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }

    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }
}
