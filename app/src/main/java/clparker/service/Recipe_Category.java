package clparker.service;

/**
 * Created by Clown on 10/04/2017.
 */

public class Recipe_Category {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id;
    String name;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    boolean isNew=false;

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    boolean isLoaded=false;

    public String getRecipe_category_id() {
        return recipe_category_id;
    }

    public void setRecipe_category_id(String recipe_category_id) {
        this.recipe_category_id = recipe_category_id;
    }

    String recipe_category_id;
}