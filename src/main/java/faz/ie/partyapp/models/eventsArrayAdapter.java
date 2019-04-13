package faz.ie.partyapp.models;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import faz.ie.partyapp.R;
import faz.ie.partyapp.models.User;

public class eventsArrayAdapter extends ArrayAdapter<Event>
{
    Context context;

    public eventsArrayAdapter(Context context, int resourceId, List<Event> items)
    {
        super(context,resourceId,items);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Event event_item = getItem(position);


        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item,parent,false);
        }

        TextView EventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView HostName = (TextView) convertView.findViewById(R.id.hostName);
        ImageView image = (ImageView) convertView.findViewById(R.id.eventImage);


        //update information...

        EventName.setText(event_item.getEventName());
        HostName.setText(event_item.getHostName());

        switch (event_item.getProfileImageUrl())
        {
            case "defaultUserImage":
                Glide.with(convertView.getContext()).load(R.mipmap.user).into(image);
                break;
            default:
                //Glide.clear(image); //makes sure the image is cleared before placing a new one
                Glide.with(convertView.getContext()).load(event_item.getProfileImageUrl()).into(image);
                break;
        }
        //Glide.with(getContext()).load(user_item.getProfileImageUrl()).into(image);
        //image.setImageResource(R.mipmap.user);

        return convertView;

    }
}