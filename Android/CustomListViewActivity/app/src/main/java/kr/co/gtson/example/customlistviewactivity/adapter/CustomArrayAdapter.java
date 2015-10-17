package kr.co.gtson.example.customlistviewactivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.gtson.example.customlistviewactivity.R;
import kr.co.gtson.example.customlistviewactivity.item.ListItem;

/**
 * Created by genteelson on 2015-04-07.
 */
public class CustomArrayAdapter extends ArrayAdapter<ListItem> {

    public CustomArrayAdapter(Context context, int textViewResourceId, ArrayList<ListItem> listItems) {
        super(context, textViewResourceId, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListItem listItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_custom_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.setImage((ImageView) convertView.findViewById(R.id.custom_list_image));
            viewHolder.setMainTitle((TextView) convertView.findViewById(R.id.custom_list_title_main));
            viewHolder.setSubTitle((TextView) convertView.findViewById(R.id.custom_list_title_sub));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.getImage().setImageResource(listItem.getImageId());
        viewHolder.getMainTitle().setText(listItem.getMainTitle());
        viewHolder.getSubTitle().setText(listItem.getSubTitle());
        // Return the completed view to render on screen
        return convertView;
    }
}
