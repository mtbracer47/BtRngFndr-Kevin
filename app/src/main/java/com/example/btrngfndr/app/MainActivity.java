package com.example.btrngfndr.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

//whatever

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    //allows access to Topmost button
    Button main_Top_Button;

    //allows access to list
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();

    //detects if a button was touched more than once
    boolean secondClick=false;

    // variables concerning the bluetooth adapter
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    Intent setDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_main);

        BA = BluetoothAdapter.getDefaultAdapter();
        if(!BA.isEnabled()){
        startActivityForResult(turnOn, 0);
        }

        main_Top_Button = (Button) findViewById(R.id.Top_Button);
        main_Top_Button.setOnClickListener(this);

        mainListView = (ListView) findViewById(R.id.Listview);
        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mNameList);
        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(secondClick){
            main_Top_Button.setText("Search for BT Devices");
            if(BA.isDiscovering()){
            BA.cancelDiscovery();
            }
            setProgressBarIndeterminateVisibility(false);
            secondClick = false;
        }else{
            if(!BA.isEnabled()){
                startActivityForResult(turnOn, 0);
            }
            if(!BA.isDiscovering()){
                startActivityForResult(setDiscoverable, 0);
            }
            main_Top_Button.setText("Searching...\nTap Again to Cancel");
            setProgressBarIndeterminateVisibility(true);
            pairedDevices = BA.getBondedDevices();
            ArrayList list = new ArrayList();
            for(BluetoothDevice bt : pairedDevices)
                list.add(bt.getName());
            final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            mainListView.setAdapter(adapter);
            secondClick = true;
        }
    }
}
