package clparker.service;

/**
 * Created by Clown on 28/10/2016.
 */

public class SubCategory {

    {
        subCategoryId=0;
        name="";
        isNew=false;
        loaded=false;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }
    public String getCategoryName()
    {
        return categoryName;
    }
    public void setCategoryName(String newCategoryName)
    {
        categoryName=newCategoryName;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return name;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.name = subCategoryName;
    }

    public SubCategory(int id, String newName, String catName)
    {
        subCategoryId=id;
        name=newName;
        categoryName=catName;
    }

    public SubCategory(){};

    private int subCategoryId;
    private String name;
    private String categoryName;
    private boolean isNew;
    private String id;

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    private boolean loaded;
}
