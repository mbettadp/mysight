package capstone.learn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Geocoder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener , AdapterView.OnItemClickListener {

    TextView mainTextView;
    Button mainButton;
    EditText editText;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList =new ArrayList();
    String old;
    ShareActionProvider mShareActionProvider;

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;


    private static final String PREFS= "prefs";
    private static final String PREF_NAME= "name";
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access the textview set in activity_main.xml and set it's text

        mainTextView=(TextView) findViewById(R.id.main_textview);
        mainTextView.setText("set in java");

        mainButton=(Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        editText=(EditText) findViewById(R.id.main_edittext);
        old=editText.getText().toString();

        mainListView=(ListView) findViewById(R.id.main_listview);
        mArrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,mNameList);
        mainListView.setAdapter(mArrayAdapter);
        mainListView.setOnItemClickListener(this);

    }
    @Override
    public void onClick(View v) {

        mainTextView.setText(editText.getText().toString());
        mNameList.add(editText.getText().toString());
        mArrayAdapter.notifyDataSetChanged();
        setShareIntent();
        Toast.makeText(this, "Navigating to, " + editText.getText().toString() + "...", Toast.LENGTH_LONG).show();

        Intent myIntent = new Intent(this, MapsActivity.class);
        myIntent.putExtra("key",editText.getText().toString() ); //Optional parameters
        this.startActivity(myIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);


        if (shareItem != null) {
            //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider();
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        // Create an Intent to share your content
        setShareIntent();
        displayWelcome();

        return true;
    }


    private void displayWelcome(){

        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = mSharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {

            // If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        }

        else{

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Welcome to iSite");
            alert.setMessage("What can we call you?");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    String inputName = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();

                    // Welcome the new user
                    Toast.makeText(getApplicationContext(), "Hello " + inputName + " :) ", Toast.LENGTH_LONG).show();
                }
            });
            alert.show();
        }
    }






    private void setShareIntent(){
        if(mShareActionProvider!=null)
            {
                Intent myIntent=new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
                myIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

                // Make sure the provider knows
                // it should work with that Intent
                mShareActionProvider.setShareIntent(myIntent);
            }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// Log the item's position and contents
        // to the console in Debug
        Log.d("omg android", position + ": " + mNameList.get(position));
        mainTextView.setText((CharSequence) mNameList.get(position));

    }
}
