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


public class arrayAdapter extends ArrayAdapter<User>
{
  Context context;

  public arrayAdapter(Context context,int resourceId,List<User>items)
  {
      super(context,resourceId,items);
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {
      User user_item = getItem(position);


      if(convertView == null)
      {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
      }

      TextView FullName = (TextView) convertView.findViewById(R.id.userName);
      TextView Age = (TextView) convertView.findViewById(R.id.userAge);
      TextView Gender = (TextView) convertView.findViewById(R.id.userGender);


      //update information...


      ImageView image = (ImageView)convertView.findViewById(R.id.userImage);

      FullName.setText(user_item.getFullName());
      Age.setText(user_item.getAge());
      Gender.setText(user_item.getGender());

      switch (user_item.getProfileImageUrl())
      {
          case "default":
              Glide.with(convertView.getContext()).load(R.mipmap.user).into(image);
              break;
          default:
                Glide.clear(image);
              Glide.with(convertView.getContext()).load(user_item.getProfileImageUrl()).into(image);
              break;
      }
      //Glide.with(getContext()).load(user_item.getProfileImageUrl()).into(image);
      //image.setImageResource(R.mipmap.user);

      return convertView;

  }
}
