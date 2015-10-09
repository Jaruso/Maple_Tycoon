package com.example.joe.mapletycoon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class StoreActivity extends AppCompatActivity {

    private Store _store; // our store data

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        _store = new Store();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);


    }

    private void generateStoreControls()
    {
        _store.getAvailabelItems();
    }
}
