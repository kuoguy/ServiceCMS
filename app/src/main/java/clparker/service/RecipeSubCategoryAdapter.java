package clparker.service;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Clown on 11/04/2017.
 */

public class RecipeSubCategoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Recipe_SubCategory> items;

    public RecipeSubCategoryAdapter(Context context, int resource, ArrayList<Recipe_SubCategory> objects) {
        this.context=context;
        this.items=objects;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Recipe_SubCategory itemObj=items.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recipelist, null);

        TextView t1 = (TextView) view.findViewById(R.id.textView4);
        TextView t2 = (TextView) view.findViewById(R.id.textView1);

        String nameString = itemObj.getName();
        String idString = itemObj.getId();

        t1.setText(nameString);
        t2.setText(idString);

        return view;
    }
}