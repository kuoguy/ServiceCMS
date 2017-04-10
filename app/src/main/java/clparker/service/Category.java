package clparker.service;

/**
 * Created by Clown on 28/10/2016.
 */

public class Category
{
    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    {
        categoryId=0;
        name="";
        isNew=false;
        loaded=false;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String categoryName) {
        this.name = categoryName;
    }



    private int categoryId;
    private String id;
    private String name;
    private boolean isNew;

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    private boolean loaded;


}
