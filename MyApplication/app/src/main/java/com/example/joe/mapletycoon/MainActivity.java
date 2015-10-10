package com.example.joe.mapletycoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.joe.mapletycoon.R.*;

public class MainActivity extends AppCompatActivity {

    public String mUsername = "";
    public int currentYear = 1800;

    Store userStore = new Store();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.start_activity);

        Button startBtn = (Button) findViewById(id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(checkName()) {
                    setContentView(R.layout.activity_main);
                    setTitle();
                    Button storeBtn = (Button) findViewById(id.storeButton);
                    storeBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            setContentView(R.layout.activity_store);
                            updateResources();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Make a Name !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkName(){

        EditText nameText = (EditText) findViewById(id.editName);
        if (nameText.getText().length() != 0) {
            setName(nameText.getText().toString());
            return true;
        }
        else
            return false;
    }


    public void setName (String username)
    {
            mUsername = username;
    }

    public void setTitle (){

        TextView maintitle = (TextView) findViewById(id.mainTitle);
        maintitle.setText(mUsername + ", " + currentYear);

    }

    public void updateResources ()
    {
        TextView money = (TextView) findViewById(id.moneynum);
        money.setText(userStore.getMoney().ToString());

       // TextView worker = (TextView) findViewById(id.workernum);
       /// worker.setText(mUsername + ", " + currentYear);

       // TextView house = (TextView) findViewById(id.housenum);
       // house.setText(mUsername + ", " + currentYear);

    }

}
