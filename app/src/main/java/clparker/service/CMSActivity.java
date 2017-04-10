package clparker.service;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.windowsazure.mobileservices.*;

import static android.R.attr.left;
import static clparker.service.R.id.editText1;

public class CMSActivity extends AppCompatActivity {

    //Global class variables
    private MobileServiceClient mClient;


    //Category chosen
    Category global_categorySelected;
    SubCategory global_subCategorySelected;

    //Items required for onscreen listviews
    ArrayAdapter<String> myAdapter1;
    ArrayAdapter<String> myAdapter2;
    ArrayAdapter<String> myAdapter3;
    ListView myList1;
    ListView myList2;
    ListView myList3;
    ArrayList<String> myStringList1 = new ArrayList<>();
    ArrayList<String> myStringList2 = new ArrayList<>();
    ArrayList<String> myStringList3 = new ArrayList<>();

    int itemSelectedID;

    //Instances of required classes
    ItemHandler itemHandlerLists = new ItemHandler();
    //DatabaseConnector dbConnector = new DatabaseConnector(getActivity());
    CloudDBConnector cdbConnector = new CloudDBConnector();

/*
    //Called by asynctask with jsonarray parameter containing query results for categories
    void grabCategories(JSONArray results)
    {
        itemHandlerLists.clearCategoryList();
        int count = 0;
        do {
            try {
                JSONObject temp = results.getJSONObject(count);
                Category newCategory = new Category();
                newCategory.setCategoryId(Integer.parseInt(temp.getString("id")));
                newCategory.setCategoryName(temp.getString("name"));

                itemHandlerLists.addNewCategory(newCategory);

            } catch (JSONException e) {
                Log.d("JSONError", "Error at getting temp obj");
            }
            count++;
        }
        while (count < results.length());

        populateStringList3();
    }
*/



/*
    //Called by asynctask with jsonarray parameter containing query results for subcategories
    void grabSubCategories(JSONArray results)
    {
        //itemHandlerLists.clearSubCategoryList();
        int count = 0;

            while (count<results.length()) {
                try {

                    JSONObject temp = results.getJSONObject(count);

                    if(itemHandlerLists.findSubCategoryExists(temp.getString("name"))==false)
                    {
                        SubCategory newSubCategory = new SubCategory();
                        newSubCategory.setSubCategoryId(Integer.parseInt(temp.getString("id")));
                        newSubCategory.setSubCategoryName(temp.getString("name"));
                        newSubCategory.setCategoryName(temp.getString("category"));

                        itemHandlerLists.addNewSubCategory(newSubCategory);
                    }
                    else
                        Log.d("NOTE", "SubCat already exists");


                } catch (JSONException e) {
                    Log.d("JSONError", "Error at getting temp obj");
                }
                count++;
            }
        //ArrayList<SubCategory> subCategoryList = itemHandlerLists.getSubCategoryList();
        populateStringList2(itemHandlerLists.getSubCategoryOfCategory(global_categorySelected));
    }
*/
    /*
    //Pulls all items from the database and adds to itemHandler lists
    void grabItems(JSONArray results)
    {
        //itemHandlerLists.clearItemList();
        int count = 0;

        while (count<results.length()) {
            try {
                JSONObject temp = results.getJSONObject(count);

                    Item newItem = new Item();
                    newItem.setItemID(Integer.parseInt(temp.getString("id")));
                    newItem.setItemName(temp.get("name").toString());
                    newItem.setItemStock(Integer.parseInt(temp.getString("stock")));
                    newItem.setItemCategory(temp.get("category").toString());
                    newItem.setItemSubCategory(temp.get("sub_category").toString());

                    itemHandlerLists.addNewItem(newItem);
            }
            catch (JSONException e)
            {
            Log.d("JSONError", "Error at getting temp obj");
            }
        count++;
        }

        populateStringList(itemHandlerLists.getItemsOfSubCategory(global_subCategorySelected));

        //Log.d("grabItems", "Leavinggrabitems");
    }
    */

    //Populates the string list with given item list - usually gained using getItemsOfCategory()
    void populateStringList(ArrayList<Item> itemList)
    {

        myStringList3.clear();

        for(int countString=0; countString<itemList.size(); countString++)
        {
            myStringList3.add((itemList.get(countString).getItemName()));
        }
        myAdapter3.notifyDataSetChanged();
    }
    //SubCategory string list
    void populateStringList2(ArrayList<SubCategory> subCategoryList)
    {

        myStringList2.clear();

        for(int countString=0; countString<subCategoryList.size(); countString++)
        {
            myStringList2.add((subCategoryList.get(countString).getSubCategoryName()));
        }
        myAdapter2.notifyDataSetChanged();
    }
    //Category string list
    void populateStringList3()
    {
        ArrayList<Category> categoryList = itemHandlerLists.getCategoryList();

        myStringList1.clear();


        for(int countString=0; countString<categoryList.size(); countString++)
        {
            myStringList1.add((categoryList.get(countString).getCategoryName()));
        }
        myAdapter1.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cms);

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
        myAdapter3=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringList3);

        //Initialise Listview objects and assign them listview's from activity layout
        myList1=(ListView) findViewById(R.id.a4);
        myList1.setAdapter(myAdapter1);
        myList1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myList1.setOnItemClickListener(mMessageClickedHandler);
        myList2=(ListView) findViewById(R.id.a3);
        myList2.setAdapter(myAdapter2);
        myList2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myList2.setOnItemClickListener(mMessageClickedHandler);
        myList3=(ListView) findViewById(R.id.a2);
        myList3.setAdapter(myAdapter3);
        myList3.setOnItemClickListener(mMessageClickedHandler);


        //Load up initial list of all categories
        GetCategoriesTask taskItems = new GetCategoriesTask();
        Boolean[] params = {true};
        taskItems.execute(params);
    }


    //initialises stuff to do with the custom action bar - uses R.menu.menu as template
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Handles button clicks in the actionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //If save button is pressed from toolbar- runs an Asynctask to save new items to database
            case R.id.action_save:
                SetItemsTask SetTaskItems = new SetItemsTask();
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
                    if(getResources().getResourceEntryName(parent.getId()).equals("a4"))
                    {
                        //myList1.setSelection(position);
                        String categorySelectedName = myStringList1.get(position);
                        Category categorySelected[] = {itemHandlerLists.findCategory(categorySelectedName)};
                        global_categorySelected = categorySelected[0];
                        myStringList3.clear();
                        myAdapter3.notifyDataSetChanged();

                        if(global_categorySelected.isLoaded() == false) //Determines whether or not to load cats from database
                        {
                            //Executes other thread tasks to get items
                            global_categorySelected.setLoaded(true);
                            GetSubCategories getSubCategories = new GetSubCategories();
                            getSubCategories.execute(categorySelected);
                        }
                        else
                        {
                            //Otherwise just populates stringlinst with items held in non-persistent objects stored
                            populateStringList2(itemHandlerLists.getSubCategoryOfCategory(global_categorySelected));
                        }
                    }
                    else if(getResources().getResourceEntryName(parent.getId()).equals("a3")) //Subcategory listview pressed
                    {
                        //for items
                        String subCategorySelectedName = myStringList2.get(position);
                        SubCategory subCategorySelected[] = {itemHandlerLists.findSubCategory(subCategorySelectedName)};

                        global_subCategorySelected = subCategorySelected[0];
                        Log.d("Clive", global_subCategorySelected.getSubCategoryName());

                        if(global_subCategorySelected.isLoaded() == false)
                        {
                            Log.d("Clive", "In");
                            global_subCategorySelected.setLoaded(true);
                            GetItems getItems = new GetItems();
                            getItems.execute(subCategorySelected);
                        }
                        else
                        {
                            populateStringList(itemHandlerLists.getItemsOfSubCategory(global_subCategorySelected));
                        }
                    }

                }
    };

    //Listens for a button click- Deals with adding of new items
    public void sendMessage(View view)
    {
        TextView enteredText=(TextView) findViewById(editText1);
        String newName=enteredText.getText().toString();


        switch(getResources().getResourceEntryName(view.getId()))
        {
            case "addCategory1":
                Category newCategory = new Category();
                newCategory.setCategoryName(newName);
                newCategory.setIsNew(true);
                itemHandlerLists.addNewCategory(newCategory);
                populateStringList3();
                break;
            case "addSubCategory1":
                SubCategory newSubCategory = new SubCategory();
                newSubCategory.setSubCategoryName(newName);
                newSubCategory.setCategoryName(global_categorySelected.getCategoryName());
                newSubCategory.setIsNew(true);
                itemHandlerLists.addNewSubCategory(newSubCategory);
                populateStringList2(itemHandlerLists.getSubCategoryOfCategory(global_categorySelected));
                break;
            case "addItem1":
                Item newItem = new Item();
                newItem.setItemName(newName);
                newItem.setItemStock(0);
                newItem.setItemCategory(global_categorySelected.getCategoryName());
                newItem.setItemSubCategory(global_subCategorySelected.getSubCategoryName());
                Log.d("newEntity", global_subCategorySelected.getSubCategoryName());
                newItem.setIsNew(true);
                itemHandlerLists.addNewItem(newItem);
                populateStringList(itemHandlerLists.getItemsOfSubCategory(global_subCategorySelected));
                break;
            default:
                Log.d("newEntity", "Failed adding new entity");
                break;
        }
    }

    //Listens for clicking of the 'Save' button on the actionbar
    //Runs an asynctask that updates the database with any new object with isNew == true
    public void sendSave(View view)
    {
        Log.d("sendingSave", "Beginning task save");
        SetItemsTask SetTaskItems = new SetItemsTask();
        SetTaskItems.execute();
    }

    public Activity getActivity()
    {
        return this;
    }

    /*
    //Takes any Cursor results set and parses it into JSON objects in a JSON array
    JSONArray createJSON(Cursor result)
    {
        JSONArray resultSet = new JSONArray();

        result.moveToFirst();
        while (result.isAfterLast() == false) {

            int totalColumn = result.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( result.getColumnName(i) != null )
                {
                    try
                    {
                        if( result.getString(i) != null )
                        {
                            Log.d("JSONGen", result.getString(i) );
                            rowObject.put(result.getColumnName(i) ,  result.getString(i) );
                        }
                        else
                        {
                            rowObject.put( result.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("JSONGen", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            result.moveToNext();
        }
        result.close();
        return resultSet;
    }
    */

    //////ASYNC TASKS BELOW//////
    //Nested asynctask- Collects all subcategories of a certain category and passes to gradSubCats
    private class GetSubCategories extends AsyncTask<Category, Object, List<SubCategory>>
    {
        @Override
        protected List<SubCategory> doInBackground(Category...params)
        {
            //dbConnector.open();
            //return dbConnector.getSubCategoriesOfCategory(params[0]);
            return cdbConnector.getSubCategoriesOfCategory(mClient, params[0]);
            //return cdbConnector.getSubCategories(mClient);
        }

        @Override
        protected void onPostExecute(List<SubCategory> result)
        {
            //JSONArray resultSet = createJSON(result);
            //dbConnector.close();
            itemHandlerLists.grabSubCategories(result);
            populateStringList2(itemHandlerLists.getSubCategoryOfCategory(global_categorySelected));
        }
    }

    private class GetItems extends AsyncTask<SubCategory, Object, List<Item>>
    {
        @Override
        protected List<Item> doInBackground(SubCategory...params)
        {
            //dbConnector.open();
            //return dbConnector.getItemsOfSubCategory(params[0]);
            return cdbConnector.getItemsOfSubCategory(mClient, params[0]);
        }

        @Override
        protected void onPostExecute(List<Item> result)
        {
            //JSONArray resultSet = createJSON(result);
            //dbConnector.close();
            itemHandlerLists.grabItems(result);
            populateStringList(itemHandlerLists.getItemsOfSubCategory(global_subCategorySelected));

        }
    }
/*
    //Other thread task to access the database
    private class GetCategoriesTask extends AsyncTask<Boolean, Object, Cursor>
    {
        @Override
        protected Cursor doInBackground(Boolean...params)
        {
            Log.d("Catlist", "Entered");
            List<Category> catList;

            //MobileServiceTable<Category> mCategoryTable = mClient.getTable(Category.class);
            //Log.d("Catlist", "Entered2");
            try {
                catList = mClient.getTable(Category.class).execute().get();
                Log.d("Catlist", catList.get(1).getCategoryName());
            }
            catch(Exception e)
            {
                Log.d("DBCloud", "Catlist failed: "+e.getMessage());
            }
            dbConnector.open();
            return dbConnector.getAllCategories();
        }

        @Override
        protected void onPostExecute(Cursor result)
        {
            //Save the results to a JSON object
           JSONArray resultSet = createJSON(result);
            Log.d("TAG_NAME", resultSet.toString() );

            dbConnector.close();

            grabCategories(resultSet);
        }

    }
*/
    private class GetCategoriesTask extends AsyncTask<Boolean, Object, List<Category>>
    {
        @Override
        protected List<Category> doInBackground(Boolean...params)
        {
            return cdbConnector.getCategories(mClient);
        }

        @Override
        protected void onPostExecute(List<Category> result)
        {
            itemHandlerLists.grabCategories(result);
            populateStringList3();
        }

    }

    private class SetItemsTask extends AsyncTask<Object, Object, Boolean>
    {
        @Override
        protected Boolean doInBackground(Object...params)
        {
            //dbConnector.open();

            ArrayList<Item> toAddDB = itemHandlerLists.getNewItems();
            ArrayList<Category> toAddDBCat = itemHandlerLists.getNewCategories();
            ArrayList<SubCategory> toAddDBSubCat = itemHandlerLists.getNewSubCategories();


            for (int count = 0; count < toAddDB.size(); count++)
            {
                Log.d("sendingSave", "Item" + count);
                //dbConnector.addItem(toAddDB.get(count));

                cdbConnector.addItem(mClient, toAddDB.get(count));
            }
            for (int count = 0; count < toAddDBCat.size(); count++)
            {
                Log.d("sendingSave", "Cat " + count + "" + toAddDBCat.get(count).getCategoryName());
                //dbConnector.addCategory(toAddDBCat.get(count));
                toAddDBCat.get(count).setLoaded(false);
                cdbConnector.addCategory(mClient, toAddDBCat.get(count));
            }
            for (int count = 0; count < toAddDBSubCat.size(); count++)
            {
                Log.d("sendingSave", "SubCat" + count);
                //dbConnector.addSubCategory(toAddDBSubCat.get(count));
                toAddDBSubCat.get(count).setLoaded(false);
                cdbConnector.addSubCategory(mClient, toAddDBSubCat.get(count));
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
