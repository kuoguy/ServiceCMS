package clparker.service;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clown on 28/10/2016.
 */

public class ItemHandler {

    public ArrayList<Category> getCategoryList() {
        return categoryList;
    }

    public ArrayList<Recipe_Category> getRecipeCategoryList() {
        return recipe_categoryList;
    }

    public ArrayList<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public ArrayList<Recipe_SubCategory> getRecipeSubCategoryList() {return recipe_subCategoryList;}


    public ArrayList<Item> getItemList()
    {
        return itemList;
    }

    public ArrayList<Recipe> getRecipeList(){ return recipeList; }

    public ArrayList<Recipe_Line> getRecipeLines(int element)
    {

        return recipeList.get(element).getRecipeLines();
    }

    public ArrayList<Recipe_Line> getNewRecipeLines(Recipe recipe)
    {
        return recipe.getNewRecipeLines();
    }


    public void addNewItem(Item item)
    {
        itemList.add(item);
    }
    public void addNewCategory(Category category)
    {
        categoryList.add(category);
    }
    public void addNewSubCategory(SubCategory subCategory)
    {
        subCategoryList.add(subCategory);
    }
    public void addNewRecipe(Recipe recipe){ recipeList.add(recipe); }
    public void addNewMeasure(Amount measure){measureList.add(measure);}
    public void addNewRecipeCategory(Recipe_Category category){recipe_categoryList.add(category);}
    public void addNewRecipeSubCategory(Recipe_SubCategory subCategory){recipe_subCategoryList.add(subCategory);}

    public ArrayList<Amount> getMeasureList()
    {
        return measureList;
    }

    public Amount getMeasure(String measureName)
    {
        Amount toReturn=null;
        for(int count=0; count<measureList.size(); count++)
        {
            if(measureList.get(count).getName()==measureName)
                toReturn=measureList.get(count);
        }

        return toReturn;
    }

    public ArrayList<Item> getNewItems()
    {
        ArrayList<Item> itemsToDB = new ArrayList<Item>();

        for(int count=0; count<itemList.size(); count++)
        {
            if(itemList.get(count).getIsNew()==true) {
                Log.d("sendingSave", "ItemDB"+count);
                itemsToDB.add(itemList.get(count));
                itemList.get(count).setIsNew(false);

            }
        }
        return itemsToDB;
    }

    public ArrayList<Item> getItemsOfCategory(Category category)
    {
        ArrayList<Item> items = new ArrayList<Item>();
        for(int count=0; count<itemList.size(); count++)
        {
            if(itemList.get(count).getItemCategory().equals(category.getCategoryName())) {
                //Log.d("sendingSave", "ItemDB"+count);
                items.add(itemList.get(count));
            }
        }
        return items;
    }
    public ArrayList<Item> getItemsOfSubCategory(SubCategory category)
    {
        ArrayList<Item> items = new ArrayList<Item>();
        for(int count=0; count<itemList.size(); count++)
        {
            if(itemList.get(count).getItemSubCategory().equals(category.getSubCategoryName())) {
                //Log.d("sendingSave", "ItemDB"+count);
                items.add(itemList.get(count));
            }
        }
        return items;
    }

    public ArrayList<SubCategory> getSubCategoryOfCategory(Category category)
    {
        ArrayList<SubCategory> items = new ArrayList<>();
        for(int count=0; count<subCategoryList.size(); count++)
        {
            if(subCategoryList.get(count).getCategoryName().equals(category.getCategoryName())) {
                //Log.d("sendingSave", "ItemDB"+count);
                items.add(subCategoryList.get(count));
            }
        }
        return items;
    }

    public ArrayList<Recipe_SubCategory> getRecipeSubCategoryOfCategory(Recipe_Category category)
    {
        ArrayList<Recipe_SubCategory> items = new ArrayList<>();
        for(int count=0; count<recipe_subCategoryList.size(); count++)
        {
            if(recipe_subCategoryList.get(count).getName().equals(category.getName())) {
                //Log.d("sendingSave", "ItemDB"+count);
                items.add(recipe_subCategoryList.get(count));
            }
        }
        return items;
    }

    public ArrayList<Category> getNewCategories()
    {
        ArrayList<Category> categoriesToDB = new ArrayList<Category>();

        for(int count=0; count<categoryList.size(); count++)
        {
            if(categoryList.get(count).getIsNew()==true) {
                Log.d("sendingSave", "ItemDB"+count);
                categoriesToDB.add(categoryList.get(count));
                categoryList.get(count).setIsNew(false);

            }
        }
        return categoriesToDB;
    }
    public ArrayList<SubCategory> getNewSubCategories()
    {
        ArrayList<SubCategory> subCategoriesToDB = new ArrayList<SubCategory>();

        for(int count=0; count<subCategoryList.size(); count++)
        {
            if(subCategoryList.get(count).getIsNew()==true) {
                Log.d("sendingSave", "ItemDB"+count);
                subCategoriesToDB.add(subCategoryList.get(count));
                subCategoryList.get(count).setIsNew(false);

            }
        }
        return subCategoriesToDB;
    }

    public ArrayList<Recipe_Category> getNewRecipeCategories()
    {
        ArrayList<Recipe_Category> categoriesToDB = new ArrayList<Recipe_Category>();

        for(int count=0; count<recipe_categoryList.size(); count++)
        {
            if(recipe_categoryList.get(count).isNew()) {
                Log.d("sendingSave", "ItemDB"+count);
                categoriesToDB.add(recipe_categoryList.get(count));
                recipe_categoryList.get(count).setNew(false);

            }
        }
        return categoriesToDB;
    }

    public ArrayList<Recipe_SubCategory> getNewRecipeSubCategories()
    {
        ArrayList<Recipe_SubCategory> categoriesToDB = new ArrayList<Recipe_SubCategory>();

        for(int count=0; count<recipe_subCategoryList.size(); count++)
        {
            if(recipe_subCategoryList.get(count).isNew()) {
                Log.d("sendingSave", "ItemDB"+count);
                categoriesToDB.add(recipe_subCategoryList.get(count));
                recipe_subCategoryList.get(count).setNew(false);

            }
        }
        return categoriesToDB;
    }

    public ArrayList<Amount> getNewMeasures()
    {
        ArrayList<Amount> measuresToDB = new ArrayList<>();

        for(int count=0; count<measureList.size(); count++)
        {
            if(measureList.get(count).getIsNew())
            {
                measureList.get(count).setNew(false);
                measuresToDB.add(measureList.get(count));
            }
        }
        return measuresToDB;
    }

    public Category findCategory(String nameFind)
    {
        for(int searchCount=0; searchCount<categoryList.size(); searchCount++)
        {
            if(categoryList.get(searchCount).getCategoryName().equals(nameFind))
                return categoryList.get(searchCount);
        }
        Log.d("findCategory", "Category name not found");
        return null;
    }

    public Recipe_Category findRecipeCategory(String nameFind)
    {
        for(int searchCount=0; searchCount<recipe_categoryList.size(); searchCount++)
        {
            if(recipe_categoryList.get(searchCount).getName().equals(nameFind))
                return recipe_categoryList.get(searchCount);
        }
        Log.d("findCategory", "Category name not found");
        return null;
    }

    public SubCategory findSubCategory(String nameFind)
    {
        for(int searchCount=0; searchCount<subCategoryList.size(); searchCount++)
        {
            if(subCategoryList.get(searchCount).getSubCategoryName().equals(nameFind))
                return subCategoryList.get(searchCount);
        }
        Log.d("findSubCategory", "SubCategory name not found");
        return null;
    }

    public SubCategory findSubCategoryPrecise(String subCatName, String catName)
    {
        for(int searchCount=0; searchCount<subCategoryList.size(); searchCount++)
        {
            if(subCategoryList.get(searchCount).getCategoryName().equals(catName))
                if(subCategoryList.get(searchCount).getSubCategoryName().equals(subCatName))
                    return subCategoryList.get(searchCount);
        }
        return null;
    }

    public Item findItem(String nameFind)
    {
        for(int searchCount=0; searchCount<itemList.size(); searchCount++)
        {
            if(itemList.get(searchCount).getItemName().equals(nameFind))
                return itemList.get(searchCount);
        }
        Log.d("findItem", "Item name not found");
        return null;
    }

    public Item findItemPrecise(String itemName, String subCatName, String catName)
    {
        for(int searchCount=0; searchCount<itemList.size(); searchCount++)
        {
            if(itemList.get(searchCount).getItemCategory().equals(catName))
                if(itemList.get(searchCount).getItemSubCategory().equals(subCatName))
                    if(itemList.get(searchCount).getItemName().equals(itemName))
                        return itemList.get(searchCount);
        }
        return null;
    }

    public Item findItemPrecise(String itemName, String subCatName)
    {
        for(int searchCount=0; searchCount<itemList.size(); searchCount++)
        {

                if(itemList.get(searchCount).getItemSubCategory().equals(subCatName))
                    if(itemList.get(searchCount).getItemName().equals(itemName))
                        return itemList.get(searchCount);
        }
        return null;
    }

    public boolean findSubCategoryExists(String name)
    {
        for(int searchCount=0; searchCount<subCategoryList.size(); searchCount++)
        {
            //Log.d("findSubCategory", subCategoryList.get(searchCount).getSubCategoryName());
            if (subCategoryList.get(searchCount).getSubCategoryName().equals(name))
                return true;
        }
        Log.d("findSubCategory", "SubCategory name not found");
        return false;
    }

    public Recipe findRecipe(String nameFind)
    {
        for(int searchCount=0; searchCount<recipeList.size(); searchCount++)
        {
            if(recipeList.get(searchCount).getName().equals(nameFind))
                return recipeList.get(searchCount);
        }
        Log.d("findRecipe", "Recipe name not found");
        return null;
    }

    void grabCategories(List<Category> result)
    {
        this.clearCategoryList();

        for(int count=0; count<result.size(); count++)
        {
            this.addNewCategory(result.get(count));
        }

    }

    void grabRecipeCategories(List<Recipe_Category> result)
    {
        this.clearCategoryList();

        for(int count=0; count<result.size(); count++)
        {
            this.addNewRecipeCategory(result.get(count));
        }

    }

    void grabMeasures(List<Amount> result)
    {
        this.clearMeasureList();

        for(int count=0; count<result.size(); count++)
        {
            this.addNewMeasure(result.get(count));
        }

    }

    void grabRecipes(List<Recipe> result)
    {
        recipeList.clear();

        for(int count=0; count<result.size(); count++)
        {
            Log.d("RecipeIn", "Name is: "+result.get(count).getName());
            this.addNewRecipe(result.get(count));
        }
    }


    void grabSubCategories(List<SubCategory> result)
    {
        //itemHandlerLists.clearCategoryList();
        for(int count=0; count<result.size(); count++)
        {
            if(this.findSubCategoryExists(result.get(count).getSubCategoryName())==false)
            {
                this.addNewSubCategory(result.get(count));
            }
            else
                Log.d("NOTE", "SubCat already exists");
        }

    }

    void grabRecipeSubCategories(List<Recipe_SubCategory> result)
    {
        //itemHandlerLists.clearCategoryList();
        for(int count=0; count<result.size(); count++)
        {
            if(this.findSubCategoryExists(result.get(count).getName())==false)
            {
                this.addNewRecipeSubCategory(result.get(count));
            }
            else
                Log.d("NOTE", "SubCat already exists");
        }

    }

    void grabItems(List<Item> result)
    {
        //itemHandlerLists.clearCategoryList();

        for(int count=0; count<result.size(); count++)
        {
            Log.d("2Loaders", result.get(count).getItemName());
            //if(this.findItemPrecise(result.get(count).getItemName(), result.get(count).getItemSubCategory(), result.get(count).getItemCategory())==null)
            this.addNewItem(result.get(count));
        }
    }

    public void clearSubCategoryList() {subCategoryList.clear();}
    public void clearCategoryList(){categoryList.clear();}
    public void clearItemList(){itemList.clear();}
    public void clearMeasureList(){measureList.clear();}


    ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<Category> categoryList = new ArrayList<>();
    ArrayList<SubCategory> subCategoryList = new ArrayList<>();
    ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    ArrayList<Boolean> isLoadedList = new ArrayList<>();
    ArrayList<Amount> measureList = new ArrayList<>();
    ArrayList<Recipe_Category> recipe_categoryList = new ArrayList<>();
    ArrayList<Recipe_SubCategory> recipe_subCategoryList = new ArrayList<>();


}
