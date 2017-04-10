package clparker.service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Clown on 21/11/2016.
 */

public class CloudDBConnector {

    void addCategory(MobileServiceClient mClient, Category newCategory)
    {
        mClient.getTable(Category.class).insert(newCategory, new TableOperationCallback<Category>() {
             public void onCompleted(Category entity, Exception exception, ServiceFilterResponse response) {
                 if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
               } else {
                   Log.d("DBCloud", exception.getMessage());
              }
             }
            });
    }

    void addSubCategory(MobileServiceClient mClient, SubCategory newSubCategory)
    {
        newSubCategory.setLoaded(false);
        mClient.getTable("sub_category", SubCategory.class).insert(newSubCategory, new TableOperationCallback<SubCategory>() {
            public void onCompleted(SubCategory entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
        newSubCategory.setLoaded(true);
    }

    void addRecipeCategory(MobileServiceClient mClient, Recipe_Category newCategory)
    {
        mClient.getTable(Recipe_Category.class).insert(newCategory, new TableOperationCallback<Recipe_Category>() {
            public void onCompleted(Recipe_Category entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
    }

    void addRecipeSubCategory(MobileServiceClient mClient, Recipe_SubCategory newSubCategory)
    {
        newSubCategory.setLoaded(false);
        mClient.getTable("recipe_subcategory", Recipe_SubCategory.class).insert(newSubCategory, new TableOperationCallback<Recipe_SubCategory>() {
            public void onCompleted(Recipe_SubCategory entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
        newSubCategory.setLoaded(true);
    }

    void addItem(MobileServiceClient mClient, Item newItem)
    {
        mClient.getTable(Item.class).insert(newItem, new TableOperationCallback<Item>() {
            public void onCompleted(Item entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
    }
    public void addMeasure(MobileServiceClient mClient, Amount newItem)
    {
        mClient.getTable("measure", Amount.class).insert(newItem, new TableOperationCallback<Amount>() {
            public void onCompleted(Amount entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
    }

    List<Category> getCategories(MobileServiceClient mClient)
    {
        List<Category> catList;
        try {
            catList = mClient.getTable(Category.class).execute().get();
        }
        catch(Exception e)
        {
            catList=null;
            Log.d("CloudDBConnector", "getCat failed: "+e.getMessage());
        }

        return catList;

    }

    List<SubCategory> getSubCategories(MobileServiceClient mClient)
    {
        List<SubCategory> subCatList;
        try {
            subCatList = mClient.getTable("sub_category", SubCategory.class).execute().get();
        }
        catch(Exception e)
        {
            subCatList=null;
            Log.d("CloudDBConnector", "getSubCat failed: "+e.getMessage());
        }

        return subCatList;

    }

    List<SubCategory> getSubCategoriesOfCategory(MobileServiceClient mClient, Category parent)
    {
        List<SubCategory> subCatList;
        try{
            subCatList = mClient.getTable("sub_category", SubCategory.class).where()
                    .field("categoryName").eq(parent.getCategoryName())
                    .execute().get();
            Log.d("CloudDBConnector", "cat= "+parent.getCategoryName());
        }
        catch(Exception e)
        {
            subCatList=null;
            Log.d("CloudDBConnector", "getSubCatofCat failed: "+e.getMessage());
        }
        return subCatList;
    }

    List<Item> getItems(MobileServiceClient mClient)
    {
        List<Item> itemList;
        try {
            itemList = mClient.getTable(Item.class).execute().get();
        }
        catch(Exception e)
        {
            itemList=null;
            Log.d("CloudDBConnector", "getItem failed: "+e.getMessage());
        }

        return itemList;

    }

    ArrayList<Item> getItemsFromRecipeLines(MobileServiceClient mClient, List<Recipe_Line> lines)
    {
        List<Item> itemList=null;
        ArrayList<Item> toReturn=new ArrayList<Item>();
        for(int count=0; count<lines.size(); count++)
        {
            try {
                itemList = mClient.getTable(Item.class).where()
                        .field("id").eq(lines.get(count).getItem_id())
                        .execute().get();

                toReturn.add(itemList.get(0));
                lines.get(count).setItem(itemList.get(0));
            }
            catch(Exception e)
            {
                itemList=null;
                Log.d("CloudDBConnector", "getItemFromRecipeLines failed: "+e.getMessage());
            }
        }
        return toReturn;
    }

    List<Item> getItemsOfSubCategory(MobileServiceClient mClient, SubCategory parent)
    {
        List<Item> itemList;
        try {
            itemList = mClient.getTable(Item.class).where()
                    .field("sub_category").eq(parent.getSubCategoryName())
                    .execute().get();
        }
        catch(Exception e)
        {
            itemList=null;
            Log.d("CloudDBConnector", "getItemofSubCat failed: "+e.getMessage());
        }

        return itemList;

    }

    public List<Amount> getMeasures(MobileServiceClient mClient)
    {
        List<Amount> measureList;
        try{
            measureList = mClient.getTable("measure", Amount.class).execute().get();
        }
        catch (Exception e)
        {
            measureList=null;
            Log.d("CloudDBConnector", "Get Measures List Failed: "+e.getMessage());
        }
        return measureList;
    }

    public List<Recipe> getRecipes(MobileServiceClient mClient)
    {
        List<Recipe> recipeList;
        try{
            recipeList = mClient.getTable(Recipe.class).execute().get();
        }
        catch(Exception e)
        {
            recipeList=null;
            Log.d("CloudDBConnector", "Get Recipe List failed: "+e.getMessage());
        }
        return recipeList;
    }

    public Recipe getSingleRecipe(MobileServiceClient mClient, String recipeName)
    {
        List<Recipe> toReturn;
        Recipe toReturnRecipe;
        try{
            toReturn = mClient.getTable(Recipe.class).where().field("name")
                    .eq(recipeName).execute().get();
            toReturnRecipe=toReturn.get(0);
        }
        catch(Exception e)
        {
            toReturnRecipe=null;
            Log.d("CloudDBConnector", "Get Recipe by name failed: "+e.getMessage());
        }

        return toReturnRecipe;
    }

    public List<Recipe_Line> getRecipeLines(MobileServiceClient mClient, Recipe recipe)
    {
        List<Recipe_Line> recipeLineList;
        try{
            recipeLineList = mClient.getTable(Recipe_Line.class).where().field("recipe_id")
                    .eq(recipe.getRecipe_id()).execute().get();
        }
        catch(Exception e)
        {
            recipeLineList=null;
            Log.d("CloudDBConnector", "Get Recipe Line List failed: "+e.getMessage());
        }
        return recipeLineList;
    }

    public void addRecipe(MobileServiceClient mClient, Recipe newRecipe)
    {
        MobileServiceJsonTable recipeTable = mClient.getTable("Recipe");
        JsonObject recipeObject = new JsonObject();
        recipeObject.addProperty("name", newRecipe.getName());
        recipeObject.addProperty("recipe_category", newRecipe.getRecipe_category());
        recipeObject.addProperty("id", newRecipe.getId());
        recipeObject.addProperty("recipe_id", newRecipe.getRecipe_id());
        try
        {
            recipeTable.insert(recipeObject).get();
        }
        catch(Exception exception)
        {
            Log.d("DBCloud", exception.getMessage());
        }

        /*
        Log.d("DBCloud", "Recipe is: "+newRecipe.getName());
        mClient.getTable(Recipe.class).insert(newRecipe, new TableOperationCallback<Recipe>() {
            public void onCompleted(Recipe entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
        */
    }

    public void addRecipeLine(MobileServiceClient mClient, Recipe_Line newRecipe)
    {
        MobileServiceJsonTable recipeLineTable = mClient.getTable("recipe_line");
        JsonObject recipeLineObject = new JsonObject();
        //recipeLineObject.addProperty("recipe_name",newRecipe.getRecipeName());
        //recipeLineObject.addProperty("item_name", newRecipe.getItem().getItemName());
        //recipeLineObject.addProperty("item_sub_category", newRecipe.getItem().getItemSubCategory());
        recipeLineObject.addProperty("amount_name", newRecipe.getAmount());
        recipeLineObject.addProperty("amount_value", newRecipe.getValue());
        recipeLineObject.addProperty("recipe_id", newRecipe.getRecipeId());
        recipeLineObject.addProperty("item_id", newRecipe.getItem_id());

        try
        {
            recipeLineTable.insert(recipeLineObject).get();
        }
        catch(Exception exception)
        {
            Log.d("DBCloud", exception.getMessage());
        }

        /*
        Log.d("DBCloud", "Recipe_Line is: "+newRecipe.getItem());
        mClient.getTable(Recipe_Line.class).insert(newRecipe, new TableOperationCallback<Recipe_Line>() {
            public void onCompleted(Recipe_Line entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    Log.d("DBCloud", "Succeeded");
                } else {
                    Log.d("DBCloud", exception.getMessage());
                }
            }
        });
        */
    }

    List<Recipe_Category> getRecipeCategories(MobileServiceClient mClient)
    {
        List<Recipe_Category> catList;
        try {
            catList = mClient.getTable(Recipe_Category.class).execute().get();
        }
        catch(Exception e)
        {
            catList=null;
            Log.d("CloudDBConnector", "getRecipeCat failed: "+e.getMessage());
        }

        return catList;

    }

    List<Recipe_SubCategory> getRecipeSubCategoriesOfCategory(MobileServiceClient mClient, Recipe_Category parent)
    {
        List<Recipe_SubCategory> subCatList;
        try{
            subCatList = mClient.getTable("sub_category", Recipe_SubCategory.class).where()
                    .field("recipe_category_id").eq(parent.getRecipe_category_id())
                    .execute().get();
            Log.d("CloudDBConnector", "Recipecat= "+parent.getName());
        }
        catch(Exception e)
        {
            subCatList=null;
            Log.d("CloudDBConnector", "getSubCatofCat failed: "+e.getMessage());
        }
        return subCatList;
    }


}
