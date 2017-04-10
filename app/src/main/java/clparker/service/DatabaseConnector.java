package clparker.service;
import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Clown on 24/10/2016.
 */

public class DatabaseConnector extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Service";

    private SQLiteDatabase database;


    // table nameS
    private static final String TABLE_ITEMS = "item";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_SUB_CATEGORY = "sub_category";
    private static final String TABLE_ITEMS4 = "item4";
    // Items Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_CAT = "category";
    private static final String KEY_SUB_CAT = "sub_category";
    // CATEGORY TABLE NAMES
    private static final String KEY_CAT_ID = "id";
    private static final String KEY_CAT_NAME = "name";
    // SUB_CATEGORY TABLE NAMES
    private static final String KEY_SUB_CAT_ID = "id";
    private static final String KEY_SUB_CAT_NAME = "name";
    //private static final String KEY_CAT = "category";
    // Other Items Table Columns names - items2, items3, items4
    //Same as items +
    //private static final String FOREIGN_KEY_ID = "parent";

    public DatabaseConnector(Context context) //Constructor for class
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //Creates new Database
        //databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException
    {
        //database=databaseOpenHelper.getWritableDatabase();
        database=this.getWritableDatabase();
    }

    public void close()
    {
        if(database!=null)
            database.close();
    }

    //public void addItem(String name, int stock)
    //{
     //   ContentValues newItem = new ContentValues();
     //   newItem.put(KEY_NAME, name);
     //   newItem.put(KEY_STOCK, stock);

     //   database.insert(TABLE_ITEMS, null, newItem);

   // }

    public void addItem(Item item)
    {
        ContentValues newItem = new ContentValues();
        newItem.put(KEY_NAME, item.getItemName());
        newItem.put(KEY_STOCK, item.getItemStock());
        newItem.put(KEY_CAT, item.getItemCategory());
        newItem.put(KEY_SUB_CAT, item.getItemSubCategory());

        database.insert(TABLE_ITEMS, null, newItem);
        Log.d("SQLAdds", "Item Added SQL" );

    }

    public void addCategory(Category category)
    {
        ContentValues newCategory = new ContentValues();
        //newCategory.put(KEY_CAT_ID, category.getCategoryId());
        newCategory.put(KEY_CAT_NAME, category.getCategoryName());
        database.insert(TABLE_CATEGORY, null, newCategory);
        Log.d("SQLAdds", "Category Added SQL" );

        //Cloud Database Add//


    }

    public void addSubCategory(SubCategory subCategory)
    {
        ContentValues newSubCategory = new ContentValues();
        //newSubCategory.put(KEY_CAT_ID, subCategory.getSubCategoryId());
        newSubCategory.put(KEY_SUB_CAT_NAME, subCategory.getSubCategoryName());
        newSubCategory.put(KEY_CAT, subCategory.getCategoryName());
        database.insert(TABLE_SUB_CATEGORY, null, newSubCategory);
        Log.d("SQLAdds", "Sub-Category Added SQL" );
    }

    public Cursor getAllItems()
    {
        return database.query(TABLE_ITEMS, new String[]{KEY_ID, KEY_NAME, KEY_STOCK, KEY_CAT, KEY_SUB_CAT}, null, null, null, null, KEY_NAME);
    }
    public Cursor getAllCategories()
    {
        return database.query(TABLE_CATEGORY, new String[]{KEY_CAT_ID, KEY_CAT_NAME}, null, null, null, null, KEY_CAT_NAME);
    }
    public Cursor getAllSubcategories()
    {
        return database.query(TABLE_SUB_CATEGORY, new String[]{KEY_SUB_CAT_ID, KEY_SUB_CAT_NAME, KEY_CAT}, null, null, null, null, KEY_SUB_CAT_NAME);
    }

    public Cursor getSubCategoriesOfCategory(Category category)
    {
        String query = "SELECT " + KEY_SUB_CAT_NAME + "," + KEY_SUB_CAT_ID + "," + KEY_CAT +
                " FROM " + TABLE_SUB_CATEGORY +
                " WHERE " + KEY_CAT + "=" + "'" + category.getCategoryName() + "'";

        Log.d("Ikramon", category.getCategoryName());

        return database.rawQueryWithFactory(null, query, null, KEY_SUB_CAT_NAME, null);
    }

    public Cursor getItemsOfSubCategory(SubCategory subCategory)
    {
        String query = "SELECT " + KEY_NAME + "," + KEY_ID + "," + KEY_CAT + "," + KEY_STOCK + "," + KEY_SUB_CAT +
                " FROM " + TABLE_ITEMS +
                " WHERE " + KEY_SUB_CAT + "=" + "'" + subCategory.getSubCategoryName() + "'";

        Log.d("Ikramon", subCategory.getSubCategoryName());

        return database.rawQueryWithFactory(null, query, null, KEY_NAME, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("Ikramon", "onCreateEntered");


        //Database creation//
        //Items table//
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                 + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_STOCK + " TEXT," + KEY_CAT + " TEXT," + KEY_SUB_CAT + " TEXT" + ")";

        //Category Table
        String CREATE_CAT_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "(" + KEY_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CAT_NAME + " TEXT" + ")";

        //Sub_Category Table
        String CREATE_SUB_CAT_TABLE = "CREATE TABLE " + TABLE_SUB_CATEGORY + "(" + KEY_SUB_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUB_CAT_NAME + " TEXT," + KEY_CAT + " TEXT" + ")";

        //Execute create queries
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_SUB_CAT_TABLE);

        Log.d("Ikramon", "onCreateLeaving");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("Ikramon", "EnteredUpgrade");

        //Complete upgrade to new version here

        Log.d("Ikramon", "CompleteUpgrade");

    }

}

