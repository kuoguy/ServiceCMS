package clparker.service;

/**
 * Created by Clown on 28/10/2016.
 */

public class Item {

    private String id;
    private String name;
    private int stock;
    private String category;
    private String sub_category;
    private boolean isNew;
    private String amount;

    public String getAmount(){return amount;}
    public void setAmount(String amountNew){amount=amountNew;}

    public String getItemCategory() {
        return category;
    }

    public void setItemCategory(String itemCategory) {
        this.category = itemCategory;
    }

    public String getItemSubCategory() {
        return sub_category;
    }

    public void setItemSubCategory(String subCategoryNew) {
        this.sub_category = subCategoryNew;
    }

    public void setItemID(String idNew){this.id=idNew;}


    {
        name="";
        stock=0;
        isNew=false;
        category="";
        sub_category="";
    }

    public void setItemName(String nameNew){name=nameNew;}
    public void setItemStock(int stockNew){stock=stockNew;}
    public void setIsNew(boolean isNewValue){isNew=isNewValue;}
    public void setId(String nId){this.id=nId;}

    public String getItemName(){return name;}
    public int getItemStock(){return stock;}
    public boolean getIsNew(){return isNew;}
    public String getId(){return this.id;}
}
