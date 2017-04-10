package clparker.service;

/**
 * Created by Clown on 25/11/2016.
 */

public class Recipe_Line {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private Item item;
    private String item_id;

    public String getItem_id(){return item_id;}
    public void setItem_id(String nId){item_id=nId;}

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    private String item_name;

    public String getItemSubCategory() {
        return item_sub_category;
    }

    public void setItemSubCategory(String itemSubCategory) {
        this.item_sub_category = itemSubCategory;
    }

    private String item_sub_category;
    private String amount_name;
    private int amount_value;
    private String recipe_name;
    private String recipe_id;
    private boolean isNew;

    public boolean getIsNew(){return isNew;}
    public void setIsNew(boolean isNewB){isNew=isNewB;}

    //Recipe_Line(Item itemNew)
    //{
    //    item=itemNew;
    //}

    public String getRecipeName(){return recipe_name;}
    public void setRecipeName(String recipeNameNew){recipe_name=recipeNameNew;}

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setRecipId(String nId){this.recipe_id = nId;}

    public String getAmount() {
        return amount_name;
    }

    public void setAmount(String amount) {
        this.amount_name = amount;
    }

    public int getValue() {
        return amount_value;
    }

    public void setValue(int value) {
        this.amount_value = value;
    }

    public String getRecipeId(){return this.recipe_id;}


}
