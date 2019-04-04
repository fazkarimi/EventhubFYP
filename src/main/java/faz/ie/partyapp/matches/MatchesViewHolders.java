package faz.ie.partyapp.matches;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import faz.ie.partyapp.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView matchesIDTextView;

    public ImageView matchImageView;

    public MatchesViewHolders(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        matchesIDTextView = (TextView) itemView.findViewById(R.id.matchID);
    }

    @Override
    public void onClick(View view) {

    }
}
