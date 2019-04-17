package faz.ie.partyapp.matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import faz.ie.partyapp.R;
import faz.ie.partyapp.chat.Chat;


public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId, mMatchName, mLastMessage;
    public ImageView mMatchImage;
    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.matchId);
        mMatchName = (TextView) itemView.findViewById(R.id.matchName);
        mMatchImage = (ImageView) itemView.findViewById(R.id.matchImage);
        mLastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
    }

    @Override
    public void onClick(View view) {

        String matchID = mMatchId.getText().toString();
        String lastMessage = mLastMessage.getText().toString();
        Intent intent = new Intent(view.getContext(), Chat.class);
        Bundle bundle = new Bundle();
        bundle.putString("matchId", matchID);
        bundle.putString("lastMessage", lastMessage);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
