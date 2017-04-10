package clparker.service;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static clparker.service.R.id.editText1;

public class RecipeCategories extends AppCompatActivity {

    //Global class variables
    private MobileServiceClient mClient;

    //Category chosen
    Recipe_Category global_categorySelected;
    Recipe_SubCategory global_subCategorySelected;

    //Items required for onscreen listviews
    ArrayAdapter<String> myAdapter1;
    ArrayAdapter<String> myAdapter2;
    ListView myList1;
    ListView myList2;
    ArrayList<String> myStringList1 = new ArrayList<>();
    ArrayList<String> myStringList2 = new ArrayList<>();

    int itemSelectedID;

    //Instances of required classes
    ItemHandler itemHandlerLists = new ItemHandler();
    //DatabaseConnector dbConnector = new DatabaseConnector(getActivity());
    CloudDBConnector cdbConnector = new CloudDBConnector();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_categories);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        try {
            mClient = new MobileServiceClient(
                    "https://cparkertest1.azurewebsites.net",
                    this
            );
        }catch(MalformedURLException e)
        {
            Log.d("CloudDB", "Failed to create mClient object using URL provided"+e.getMessage());
        }

        //Initialise arrayAdapters for onscreen listviews
        myAdapter1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, myStringList1);
        myAdapter2=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, myStringList2);

        //Initialise Listview objects and assign them listview's from activity layout
        myList1=(ListView) findViewById(R.id.a3);
        myList1.setAdapter(myAdapter1);
        myList1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myList1.setOnItemClickListener(mMessageClickedHandler);
        myList2=(ListView) findViewById(R.id.a2);
        myList2.setAdapter(myAdapter2);
        myList2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myList2.setOnItemClickListener(mMessageClickedHandler);

        GetRecipeCategoriesTask getCategories = new GetRecipeCategoriesTask();
        getCategories.execute();

    }

    //initialises stuff to do with the custom action bar - uses R.menu.menu as template
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Listens for a button click- Deals with adding of new items
    public void sendMessage(View view) {
        TextView enteredText = (TextView) findViewById(editText1);
        String newName = enteredText.getText().toString();


        switch (getResources().getResourceEntryName(view.getId())) {
            case "addCategory1":
                Recipe_Category newCategory = new Recipe_Category();
                newCategory.setName(newName);
                newCategory.setNew(true);
                itemHandlerLists.addNewRecipeCategory(newCategory);
                populateStringList(itemHandlerLists.getRecipeCategoryList());
                break;
            case "addSubCategory1":
                Recipe_SubCategory newSubCategory = new Recipe_SubCategory();
                newSubCategory.setName(newName);
                newSubCategory.setRecipe_category_name(global_categorySelected.getName());
                newSubCategory.setNew(true);
                itemHandlerLists.addNewRecipeSubCategory(newSubCategory);
                populateStringList2(itemHandlerLists.getRecipeSubCategoryOfCategory(global_categorySelected));
                break;
            default:
                break;
        }
    }

    //Handles button clicks in the actionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //If save button is pressed from toolbar- runs an Asynctask to save new items to database
            case R.id.action_save:
                RecipeCategories.SetItemsTask SetTaskItems = new RecipeCategories.SetItemsTask();
                SetTaskItems.execute(); //Runs async task that saves all new item additions
                return true;
            //If home button is pressed- should return user to main menu activity and end current activity
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Handles any clicks from onscreen lists through listAdapters
    AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView parent,
                                View v,
                                int position,
                                long id) {
            itemSelectedID=position;

            //If the categories listview is clicked
            if(getResources().getResourceEntryName(parent.getId()).equals("a3"))
            {
                //myList1.setSelection(position);
                String categorySelectedName = myStringList1.get(position);
                Recipe_Category categorySelected[] = {itemHandlerLists.findRecipeCategory(categorySelectedName)};
                global_categorySelected = categorySelected[0];
                Log.d("Clive", "Name: "+global_categorySelected.getName());
                myStringList2.clear();
                myAdapter2.notifyDataSetChanged();

                //if(global_categorySelected.isLoaded() == false) //Determines whether or not to load cats from database
                //{
                    //Executes other thread tasks to get items
                    global_categorySelected.setLoaded(true);
                    RecipeCategories.GetRecipeSubCategoriesTask getSubCategories = new RecipeCategories.GetRecipeSubCategoriesTask();
                    getSubCategories.execute(categorySelected);
                //}
                //else
                //{
                    //Otherwise just populates stringlinst with items held in non-persistent objects stored
                 //   populateStringList2(itemHandlerLists.getRecipeSubCategoryOfCategory(global_categorySelected));
                //}
            }
            else if(getResources().getResourceEntryName(parent.getId()).equals("a2")) //Subcategory listview pressed
            {
                //for items
                //String subCategorySelectedName = myStringList2.get(position);
                //Recipe_SubCategory subCategorySelected[] = {itemHandlerLists.findSubCategory(subCategorySelectedName)};

                //global_subCategorySelected = subCategorySelected[0];
                //Log.d("Clive", global_subCategorySelected.getSubCategoryName());

                //if(global_subCategorySelected.isLoaded() == false)
                //{
                //    Log.d("Clive", "In");
                //    global_subCategorySelected.setLoaded(true);
                //    CMSActivity.GetItems getItems = new CMSActivity.GetItems();
                //    getItems.execute(subCategorySelected);
                //}
               // else
                //{
                 //   populateStringList(itemHandlerLists.getItemsOfSubCategory(global_subCategorySelected));
                //}
            }

        }
    };

    //Populates the string list with recipe categories
    void populateStringList(ArrayList<Recipe_Category> itemList)
    {

        myStringList1.clear();

        for(int countString=0; countString<itemList.size(); countString++)
        {
            myStringList1.add((itemList.get(countString).getName()));
        }
        myAdapter1.notifyDataSetChanged();
    }
    //SubCategory string list
    void populateStringList2(ArrayList<Recipe_SubCategory> subCategoryList)
    {

        myStringList2.clear();

        for(int countString=0; countString<subCategoryList.size(); countString++)
        {
            myStringList2.add((subCategoryList.get(countString).getName()));
        }
        myAdapter2.notifyDataSetChanged();
    }

    private class GetRecipeCategoriesTask extends AsyncTask<Boolean, Object, List<Recipe_Category>>
    {
        @Override
        protected List<Recipe_Category> doInBackground(Boolean...params)
        {
            return cdbConnector.getRecipeCategories(mClient);
        }

        @Override
        protected void onPostExecute(List<Recipe_Category> result)
        {
            itemHandlerLists.grabRecipeCategories(result);
            populateStringList(itemHandlerLists.getRecipeCategoryList());
        }

    }
    private class GetRecipeSubCategoriesTask extends AsyncTask<Recipe_Category, Object, List<Recipe_SubCategory>>
    {
        @Override
        protected List<Recipe_SubCategory> doInBackground(Recipe_Category...params)
        {
            //dbConnector.open();
            //return dbConnector.getSubCategoriesOfCategory(params[0]);
            return cdbConnector.getRecipeSubCategoriesOfCategory(mClient, params[0]);
            //return cdbConnector.getSubCategories(mClient);
        }

        @Override
        protected void onPostExecute(List<Recipe_SubCategory> result)
        {
            //JSONArray resultSet = createJSON(result);
            //dbConnector.close();
            itemHandlerLists.grabRecipeSubCategories(result);
            populateStringList2(itemHandlerLists.getRecipeSubCategoryOfCategory(global_categorySelected));
        }
    }

    public Activity getActivity(){return this;}

    private class SetItemsTask extends AsyncTask<Object, Object, Boolean>
    {
        @Override
        protected Boolean doInBackground(Object...params)
        {
            //dbConnector.open();

            ArrayList<Recipe_Category> toAddDBCat = itemHandlerLists.getNewRecipeCategories();
            ArrayList<Recipe_SubCategory> toAddDBSubCat = itemHandlerLists.getNewRecipeSubCategories();


            for (int count = 0; count < toAddDBCat.size(); count++)
            {
                Log.d("sendingSave", "Cat " + count + "" + toAddDBCat.get(count).getName());
                //dbConnector.addCategory(toAddDBCat.get(count));
                toAddDBCat.get(count).setLoaded(false);
                cdbConnector.addRecipeCategory(mClient, toAddDBCat.get(count));
            }
            for (int count = 0; count < toAddDBSubCat.size(); count++)
            {
                Log.d("sendingSave", "SubCat" + count);
                //dbConnector.addSubCategory(toAddDBSubCat.get(count));
                toAddDBSubCat.get(count).setLoaded(false);
                cdbConnector.addRecipeSubCategory(mClient, toAddDBSubCat.get(count));
            }

            //dbConnector.close();
            Log.d("sendingSave", "Ending task save");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Toast.makeText(getActivity(), "New Records Saved",
                    Toast.LENGTH_LONG).show();
            this.cancel(true);
        }

    }

}
