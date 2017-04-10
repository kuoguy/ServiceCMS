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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    CloudDBConnector cdbConnector = new CloudDBConnector();
    private MobileServiceClient mClient;

    ItemHandler itemHandler=new ItemHandler();
    ArrayAdapter<Recipe_Line> recipeArrayAdapter;
    ListView listView;

    ArrayAdapter<String> itemsArrayAdapter;
    ListView listViewItems;
    ArrayList<String> stringListItems = new ArrayList<>();

    int global_selectedListNav = 0;
    ArrayList<String> selectedChain = new ArrayList<>();

    Recipe global_recipeSelected;

    public void populateWithCategory()
    {
        ArrayList<Category> categoryList = itemHandler.getCategoryList();

        stringListItems.clear();

        for(int countString=0; countString<categoryList.size(); countString++)
        {
            stringListItems.add((categoryList.get(countString).getCategoryName()));
        }

        itemsArrayAdapter.notifyDataSetChanged();
    }

    void populateWithSubCategory(ArrayList<SubCategory> subCategoryList)
    {

        stringListItems.clear();

        for(int countString=0; countString<subCategoryList.size(); countString++)
        {
            stringListItems.add((subCategoryList.get(countString).getSubCategoryName()));
        }
        itemsArrayAdapter.notifyDataSetChanged();
    }

    void populateWithItems(ArrayList<Item> itemList)
    {

        stringListItems.clear();

        for(int countString=0; countString<itemList.size(); countString++)
        {
            stringListItems.add((itemList.get(countString).getItemName()));
        }
        itemsArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

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


        itemsArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, stringListItems);
        listViewItems = (ListView) findViewById(R.id.listViewItems);
        listViewItems.setAdapter(itemsArrayAdapter);
        listViewItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewItems.setOnItemClickListener(mMessageClickedHandler);
        listView= (ListView) findViewById(R.id.customListview);

        listView.setOnItemClickListener(mMessageClickedHandler);

        //GetRecipe getRecipeTask = new GetRecipe();
        //getRecipeTask.execute();

        GetCategoriesTask getCategories = new GetCategoriesTask();
        getCategories.execute();

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
            case R.id.action_save:
                //TextView recipeNameTV = (TextView) findViewById(R.id.editTextRecipeName);
                //String[] recipeName = new String[1]; // contains recipe name from onscreen editText
                //recipeName[0]=recipeNameTV.getText().toString();
                Recipe[] recipeParam = new Recipe[1];
                recipeParam[0]=global_recipeSelected;
                SetRecipeLinesTask setRecipeLinesTask = new SetRecipeLinesTask();
                setRecipeLinesTask.execute(recipeParam); //Passes parameter containing name
                return true;
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void initialiseAdapter(int recipeNumber) //Initialises listview adapter
    {
        recipeArrayAdapter=new RecipeAdapter(getActivity(), 0, itemHandler.getRecipeLines(recipeNumber));
        listView.setAdapter(recipeArrayAdapter);
        recipeArrayAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent,
                                View v,
                                int position,
                                long id) {
            //itemSelectedID=position;

            //If the categories listview is clicked
            if (getResources().getResourceEntryName(parent.getId()).equals("listViewItems") && global_selectedListNav==0)
            {
                if(selectedChain.size()<1)
                {
                    global_selectedListNav++;
                    selectedChain.add(stringListItems.get(position));
                    itemListFunctions();
                }
                else
                    selectedChain.set(0, stringListItems.get(position));


            }
            else if(getResources().getResourceEntryName(parent.getId()).equals("listViewItems") && global_selectedListNav==1)
            {
                if(selectedChain.size()<2)
                {
                    global_selectedListNav++;
                    selectedChain.add(stringListItems.get(position));
                    itemListFunctions();
                }
                else
                    selectedChain.set(1, stringListItems.get(position));

            }
            else if(getResources().getResourceEntryName(parent.getId()).equals("listViewItems") && global_selectedListNav==2)
            {
                if(selectedChain.size()<3)
                {
                    selectedChain.add(stringListItems.get(position));
                }
                else
                {
                    selectedChain.set(2, stringListItems.get(position));
                }

            }
        }
    };

    public void itemListFunctions()
    {

        if(global_selectedListNav==2)
        {
            SubCategory subCatSelected = itemHandler.findSubCategoryPrecise(selectedChain.get(1), selectedChain.get(0));

            GetItemsTask getItemsTask = new GetItemsTask();
            getItemsTask.execute(subCatSelected);
        }
        else if(global_selectedListNav==1)
        {
            Category catSelected = itemHandler.findCategory(selectedChain.get(0));

            GetSubCategories getSubCategoriesTask = new GetSubCategories();
            getSubCategoriesTask.execute(catSelected);
        }
        else
        {
            GetCategoriesTask getCategories = new GetCategoriesTask();
            getCategories.execute();
        }
    }

    public void sendMessage(View view)
    {
        switch(getResources().getResourceEntryName(view.getId()))
        {
            case "button_add":
                Item itemSelected;
                try
                {
                    if(global_recipeSelected!=null) {
                        itemSelected = itemHandler.findItemPrecise(selectedChain.get(2), selectedChain.get(1), selectedChain.get(0));
                        addItemToRecipe(itemSelected);
                        global_selectedListNav = 0;
                        selectedChain.clear();
                        itemListFunctions();
                    }
                    else
                    {
                        Toast.makeText(this, "Cannot Add Item: No Recipe Selected or Created Yet",
                                Toast.LENGTH_LONG).show();
                    }

                }
                catch(NullPointerException e)
                {
                    Log.d("Add_To_Recipe", "Item precise found is null"+e.getMessage());
                }
                break;
            case "button_new_recipe":
                TextView recipeNameTV = (TextView) findViewById(R.id.editTextRecipeSearch);
                String recipeNameString=recipeNameTV.getText().toString();
                createNewRecipe(recipeNameString);
                initialiseAdapter(0);
                break;
            case "button_back":
                if(global_selectedListNav>0)
                {
                    global_selectedListNav--;
                    selectedChain.remove(global_selectedListNav);

                    itemListFunctions();
                }
                break;
            case "buttonRecipeSearch":
                String[] recipeSearchText=new String[1];
                EditText recipeEditText = (EditText) findViewById(R.id.editTextRecipeSearch);
                recipeSearchText[0]=recipeEditText.getText().toString();
                Log.d("Recipe Search", "Searched name: "+recipeSearchText[0]);
                Log.d("Recipe Search", "pass");
                GetRecipe getRecipeTask = new GetRecipe();
                getRecipeTask.execute(recipeSearchText);

        }
    }

    public void addItemToRecipe(Item selected)
    {
        global_recipeSelected.setItem(selected);
        recipeArrayAdapter.notifyDataSetChanged();
    }

    public void createNewRecipe(String name)
    {
        Recipe newRecipe = new Recipe();
        newRecipe.setName(name);
        newRecipe.setIsNew(true);
        global_recipeSelected=newRecipe;
        itemHandler.addNewRecipe(newRecipe);
    }


    public Activity getActivity()
    {
        return this;
    }

    private class GetRecipe extends AsyncTask<String, Object, ArrayList<Item>>
    {
        @Override
        protected ArrayList<Item> doInBackground(String...params)
        {
            if(cdbConnector.getSingleRecipe(mClient, params[0])!=null)
            {
                global_recipeSelected=cdbConnector.getSingleRecipe(mClient, params[0]);
                Log.d("RecipeName", global_recipeSelected.getName());
                ArrayList<Recipe> tempRecipeArray = new ArrayList<>();
                tempRecipeArray.add(global_recipeSelected);

                itemHandler.grabRecipes(tempRecipeArray);
                //List<Recipe_Line> tempLines=cdbConnector.getRecipeLines(mClient, (itemHandler.getRecipeList()).get(0));
                List<Recipe_Line> tempLines = cdbConnector.getRecipeLines(mClient, global_recipeSelected);

                /*
                List<SubCategory> tempSubCats;
                tempSubCats = cdbConnector.getSubCategories(mClient);
                ArrayList<SubCategory> toLoadSubCats = new ArrayList<>();
                for (int count = 0; count < tempLines.size(); count++) {
                    String subCatName = tempLines.get(count).getItemSubCategory();
                    for (int count2 = 0; count2 < tempSubCats.size(); count2++) {
                        Log.d("Loaders", "SubCats : " + tempSubCats.get(count2).getSubCategoryName());
                        if (subCatName.equals(tempSubCats.get(count2).getSubCategoryName())) {
                            SubCategory subCat = tempSubCats.get(count2);
                            if (toLoadSubCats.contains(subCat)) {
                                //Nothing
                            } else
                                toLoadSubCats.add(subCat);
                        }

                    }
                }
                */
                ArrayList<Item> itemsToLoad;


                itemsToLoad=cdbConnector.getItemsFromRecipeLines(mClient, tempLines);


                /*
                for (int countCats = 0; countCats < toLoadSubCats.size(); countCats++) {
                    int amount = cdbConnector.getItemsOfSubCategory(mClient, toLoadSubCats.get(countCats)).size();
                    for (int countCats2 = 0; countCats2 < amount; countCats2++) {
                        itemsToLoad.add(cdbConnector.getItemsOfSubCategory(mClient, toLoadSubCats.get(countCats)).get(countCats2));
                        Log.d("Loaders", "Name and stuff is: " + itemsToLoad.get(countCats2).getItemName() + " " + itemsToLoad.get(countCats2).getItemSubCategory());
                    }
                }
                */
                itemHandler.grabItems(itemsToLoad);

                global_recipeSelected.setLines(tempLines, itemHandler);
                return itemsToLoad;
            }
            else {
                Toast.makeText(getActivity(), "Recipe Does not Exist",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Item> result)
        {
            //JSONArray resultSet = createJSON(result);
            //dbConnector.close();
            //ikram.setName("ikram");
            //itemHandler.grabItems(result);
            //ikram.setItems(itemHandler.getItemList());
            //itemHandler.addNewRecipe(ikram);

           // global_recipeSelected=ikram;
           // ikram.setIsNew(true);
           // initialiseAdapter(0);
            //global_recipeSelected=itemHandler.findRecipe("ikram");
            if(result!=null) {
                initialiseAdapter(0);
            }

        }
    }

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
            itemHandler.grabCategories(result);
            populateWithCategory();
        }

    }

    private class GetSubCategories extends AsyncTask<Category, Object, List<SubCategory>>
    {
        Category global_categorySelected;
        @Override
        protected List<SubCategory> doInBackground(Category...params)
        {
            global_categorySelected = params[0];
            return cdbConnector.getSubCategoriesOfCategory(mClient, params[0]);
        }

        @Override
        protected void onPostExecute(List<SubCategory> result)
        {
            itemHandler.grabSubCategories(result);
            populateWithSubCategory(itemHandler.getSubCategoryOfCategory(global_categorySelected));
        }
    }

    private class GetItemsTask extends AsyncTask<SubCategory, Object, List<Item>>
    {
        SubCategory global_subCategorySelected;
        @Override
        protected List<Item> doInBackground(SubCategory...params)
        {
            global_subCategorySelected=params[0];
            return cdbConnector.getItemsOfSubCategory(mClient, params[0]);
        }

        @Override
        protected void onPostExecute(List<Item> result)
        {
            //JSONArray resultSet = createJSON(result);
            //dbConnector.close();
            itemHandler.grabItems(result);
            populateWithItems(itemHandler.getItemsOfSubCategory(global_subCategorySelected));
        }
    }

    private class SetRecipeLinesTask extends AsyncTask<Recipe, Object, Boolean>
    {
        @Override
        protected Boolean doInBackground(Recipe...params)
        {
            Boolean completeState=true;
            if(params[0].getIsNew())
            {
                String recipeName=params[0].getName();
                //if(recipeName==null || recipeName.equals(""))
                //{
                //    Log.d("Recipe_Save", "Recipe name is null so will be default name");
                //    completeState=false;
                //}


                    if(cdbConnector.getSingleRecipe(mClient, recipeName)==null)
                    {
                        //global_recipeSelected.setName(recipeName);
                        cdbConnector.addRecipe(mClient ,params[0]);
                        completeState=true;
                    }
                    else
                        completeState=false;



            }

            if(completeState)
            {
                ArrayList<Recipe_Line> newLines = itemHandler.getNewRecipeLines(params[0]);

                for (int count = 0; count < newLines.size(); count++)
                    cdbConnector.addRecipeLine(mClient, newLines.get(count));
            }

            return completeState;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if(result)
            {
                Toast.makeText(getActivity(), "New Records Saved",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(), "Not Saved: Name not Unique",
                        Toast.LENGTH_LONG).show();
            }
            this.cancel(true);
        }
    }

}
