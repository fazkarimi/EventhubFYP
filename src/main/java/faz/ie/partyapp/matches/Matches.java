package faz.ie.partyapp.matches;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import faz.ie.partyapp.R;

public class Matches extends AppCompatActivity {
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mMatchesAdapter;
    public RecyclerView.LayoutManager mMatchesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        System.out.print("ffffffdsgfdgsg"+mRecyclerView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(Matches.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getdataSetMatches(), Matches.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        for(int i =0; i<10;i++)
        {
            MatchesObject obj = new MatchesObject(Integer.toString(i));
            resultMatches.add(obj);
        }

        mMatchesAdapter.notifyDataSetChanged();

    }

    private ArrayList<MatchesObject> resultMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getdataSetMatches()
    {
        return resultMatches;
    }

}
