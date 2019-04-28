package faz.ie.partyapp.chat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import faz.ie.partyapp.R;
import faz.ie.partyapp.chat.Chat;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;

    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);

    }

    @Override
    public void onClick(View view)
    {

    }
}
