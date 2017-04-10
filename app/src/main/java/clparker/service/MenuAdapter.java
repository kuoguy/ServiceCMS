package clparker.service;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clown on 29/11/2016.
 */

public class MenuAdapter extends ArrayAdapter<MenuItemObj> {
    private Context context;
    private List<MenuItemObj> items;

    public MenuAdapter(Context context, int resource, ArrayList<MenuItemObj> objects) {
        super(context, resource, objects);

        this.context = context;
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        MenuItemObj itemObj = items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menulist, null);

        TextView nameText = (TextView) view.findViewById(R.id.textViewName);
        TextView descText = (TextView) view.findViewById(R.id.textViewDescription);
        ImageView iconImage = (ImageView) view.findViewById(R.id.menuItemIcon);

        String nameTextString = itemObj.getName();
        String descTextString = itemObj.getDescription();
        int imageIDInt = itemObj.getImageID();

        nameText.setText(nameTextString);
        nameText.setTextSize(22);
        descText.setText(descTextString);
        descText.setTextSize(16);
        iconImage.setImageResource(imageIDInt);

        return view;
    }
}
