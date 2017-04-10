package clparker.service;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clown on 25/11/2016.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe_Line>
{

    private Context context;
    private List<Recipe_Line> recipeInfo;

    //constructor, call on creation
    public RecipeAdapter(Context context, int resource, ArrayList<Recipe_Line> objects) {
        super(context, resource, objects);

        this.context = context;
        this.recipeInfo = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Recipe_Line recipe = recipeInfo.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recipelist, null);

        TextView category = (TextView) view.findViewById(R.id.textView1);
        TextView subCategory = (TextView) view.findViewById(R.id.textView2);
        TextView item = (TextView) view.findViewById(R.id.textView3);
        TextView listNumber = (TextView) view.findViewById(R.id.textView4);
        //TextView amount = (TextView) view.findViewById(R.id.textView5);
        //TextView amountValue = (TextView) view.findViewById(R.id.textView6);

        String categoryString = recipe.getItem().getItemCategory();
        category.setText(categoryString);
        String subCategoryString = recipe.getItem().getItemSubCategory();
        subCategory.setText(subCategoryString);
        String itemString = recipe.getItem().getItemName();
        item.setText(itemString);
        String listNumberString = Integer.toString(position);
        listNumber.setText(listNumberString);
        //String amountString = recipe.getAmount();
        //amount.setText(amountString);
        //String amountValueString = Integer.toString(recipe.getValue());
        //amountValue.setText(amountValueString);

        return view;

    }

}
