package clparker.service;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<MenuItemObj> menuItemObjArrayAdapter;
    ListView menuItemListView;
    ArrayList<MenuItemObj> menuItemObjArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuItemListView=(ListView) findViewById(R.id.menuItemListView);
        generateMenuArray();

        menuItemObjArrayAdapter=new MenuAdapter(getActivity(), 0, menuItemObjArrayList);
        menuItemListView.setOnItemClickListener(mMessageClickedHandler);

        menuItemListView.setAdapter(menuItemObjArrayAdapter);
        menuItemObjArrayAdapter.notifyDataSetChanged();
    }

    public void generateMenuArray()
    {
        menuItemObjArrayList=new ArrayList<>();
        MenuItemObj addMenuItem = new MenuItemObj();
        addMenuItem.setName("Add");
        addMenuItem.setDescription("A place to add Categories, Sub-Categories, and Items           ");
        addMenuItem.setImageID(R.drawable.additemicon);
        menuItemObjArrayList.add(addMenuItem);
        MenuItemObj recipeMenuItem = new MenuItemObj();
        recipeMenuItem.setName("Recipe");
        recipeMenuItem.setDescription("A place to add, modify, and delete Recipes and Recipe Lines");
        recipeMenuItem.setImageID(R.drawable.recipeicon);
        menuItemObjArrayList.add(recipeMenuItem);
        MenuItemObj menuItem3 = new MenuItemObj();
        menuItem3.setName("Measures");
        menuItem3.setDescription("A place to add and alter item measures");
        menuItem3.setImageID(R.drawable.measuresicon);
        menuItemObjArrayList.add(menuItem3);

        MenuItemObj menuItem4 = new MenuItemObj();
        menuItem4.setName("RecipeCategories");
        menuItem4.setDescription("A place to add Recipe Categories and SubCategories");
        menuItem4.setImageID(R.drawable.additemicon);
        menuItemObjArrayList.add(menuItem4);
    }

    AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            if(getResources().getResourceEntryName(parent.getId()).equals("menuItemListView"))
            {
                if(menuItemObjArrayList.get(position).getName().equals("Add"))
                {
                    Intent intent = new Intent(getActivity(), CMSActivity.class);
                    startActivity(intent);
                }
                else if(menuItemObjArrayList.get(position).getName().equals("Recipe"))
                {
                    Intent intent = new Intent(getActivity(), RecipeActivity.class);
                    startActivity(intent);
                }
                else if(menuItemObjArrayList.get(position).getName().equals("Measures"))
                {
                    Intent intent = new Intent(getActivity(), MeasuresActivity.class);
                    startActivity(intent);
                }
                else if(menuItemObjArrayList.get(position).getName().equals("RecipeCategories"))
                {
                    Intent intent = new Intent(getActivity(), RecipeCategories.class);
                    startActivity(intent);
                }
            }
        }
    };

    public Activity getActivity()
    {
        return this;
    }

    public void sendMessage(View view)
    {

    }
}
