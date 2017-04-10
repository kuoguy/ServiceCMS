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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static clparker.service.R.id.editText1;

public class MeasuresActivity extends AppCompatActivity {
    private MobileServiceClient mClient;
    ArrayAdapter<String> measuresArrayAdapter;
    ListView measuresListView;
    ArrayList<String> measuresArrayList;
    ItemHandler itemHandler = new ItemHandler();
    CloudDBConnector cdbConnector = new CloudDBConnector();

    public Activity getActivity()
    {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);

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
        measuresArrayList=new ArrayList<String>();
        measuresArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, measuresArrayList);
        measuresListView=(ListView) findViewById(R.id.measuresListView);
        measuresListView.setAdapter(measuresArrayAdapter);

        GetMeasures getMeasuresTask = new GetMeasures();
        getMeasuresTask.execute();

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
                //CMSActivity.SetItemsTask SetTaskItems = new CMSActivity.SetItemsTask();
                //SetTaskItems.execute(); //Runs async task that saves all new item additions
                SetMeasures setMeasuresTask = new SetMeasures();
                setMeasuresTask.execute();
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

    public void sendMessage(View view) {
        TextView enteredText = (TextView) findViewById(R.id.editTextMeasureName);
        String newName = enteredText.getText().toString();


        switch (getResources().getResourceEntryName(view.getId()))
        {
            case "button_measure_add":
                if(itemHandler.getMeasure(newName)==null)
                    createNewMeasure(newName);
                else
                {
                    Toast.makeText(this, "Cannot Add Measure: Already Exists", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void createNewMeasure(String name)
    {
        Amount measure = new Amount();
        measure.setName(name);
        measure.setNew(true);
        itemHandler.addNewMeasure(measure);
        populateList();
    }

    public void populateList()
    {
        measuresArrayList.clear();
        ArrayList<Amount> result = itemHandler.getMeasureList();
        for(int count=0; count<result.size(); count++)
        {
            measuresArrayList.add(result.get(count).getName());
        }
        measuresArrayAdapter.notifyDataSetChanged();
    }

    private class GetMeasures extends AsyncTask<Object, Object, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params)
        {
            List<Amount> resultAmounts=cdbConnector.getMeasures(mClient);
            itemHandler.grabMeasures(resultAmounts);
            if(resultAmounts!=null)
                return true;
            else
                return false;
            //return true;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            populateList();
        }
    }

    private class SetMeasures extends AsyncTask<Object, Object, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params)
        {
            ArrayList<Amount> toAddDB = itemHandler.getNewMeasures();
            Boolean haveAdded=false;

            for(int count=0; count<toAddDB.size(); count++)
            {
                cdbConnector.addMeasure(mClient, toAddDB.get(count));
                haveAdded=true;
            }
            //return true;
            return haveAdded;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if(result)
                Toast.makeText(getActivity(), "New Records Saved", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Not Saved: Nothing new to save", Toast.LENGTH_LONG).show();
        }
    }

}
