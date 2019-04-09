package faz.ie.partyapp.chat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import faz.ie.partyapp.R;
import faz.ie.partyapp.chat.Chat;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener
{


    public ChatViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

    }
}
