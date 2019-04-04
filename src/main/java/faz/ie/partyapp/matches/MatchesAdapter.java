package faz.ie.partyapp.matches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import faz.ie.partyapp.R;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders>
{

    private List<MatchesObject> matcheslist;
    private Context context;

    public MatchesAdapter(List<MatchesObject>matcheslist, Context context)
    {
        this.matcheslist = matcheslist;
        this.context = context;
    }
    @Override
    public MatchesViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolders rcv = new MatchesViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MatchesViewHolders holder, int position)
    {
        holder.matchesIDTextView.setText(matcheslist.get(position).getUserid());
    }

    @Override
    public int getItemCount() {
        return matcheslist.size();
    }
}
