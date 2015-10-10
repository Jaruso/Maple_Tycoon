package com.example.joe.mapletycoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.joe.mapletycoon.R.*;

public class MainActivity extends AppCompatActivity {

    public String mUsername = "";
    public int currentYear = 1800;
    public Store mStore = new Store();
    public BuyClickListener buyClickListener = new BuyClickListener(new WeakReference<Store>(mStore), this);
    public SellClickListener sellClickListener = new SellClickListener(new WeakReference<Store>(mStore), this);

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
                        }
                    });


                }
                else{
                    Toast.makeText(getApplicationContext(), "Make a Name !", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

   // @Override
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
        int mMoney = mStore.getMoney();
        int mWorker = mStore.getWorkers();


        TextView money = (TextView) findViewById(id.moneynum);
        money.setText(Integer.toString(mMoney) );

        TextView worker = (TextView) findViewById(id.workernum);
        worker.setText(Integer.toString(mWorker));

       // TextView house = (TextView) findViewById(id.housenum);
       // house.setText(mUsername + ", " + currentYear);

    }

    public void generateStoreControls()
    {
        ArrayList<StoreItem> items = mStore.getAvailabelItems();

        for(int i = 0; i < items.size(); i++)
        {
            LinearLayout layout = (LinearLayout) findViewById(id.storeLayout);
            LinearLayout newRow = new LinearLayout(this);
            newRow.setId(i);
            newRow.setOrientation(LinearLayout.HORIZONTAL);

            TextView name = new TextView(this);
            name.setId(i + 100);
            name.setText(items.get(i).getName());
            newRow.addView(name);

            Button buy = new Button(this);
            buy.setText("Buy");
            buy.setId(i + 200);
            buy.setOnClickListener(buyClickListener);

            newRow.addView(buy);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)buy.getLayoutParams();
            params.weight = 1.0f;
            params.gravity = Gravity.RIGHT;
            buy.setLayoutParams(params);

            Button sell = new Button(this);
            sell.setText("Sell");
            sell.setId(i + 300);
            sell.setOnClickListener(sellClickListener);
            newRow.addView(sell);
            LinearLayout.LayoutParams sellParams = (LinearLayout.LayoutParams)sell.getLayoutParams();
            sellParams.weight = 1.0f;
            sellParams.gravity = Gravity.RIGHT;
            sell.setLayoutParams(sellParams);

            TextView amount = new TextView(this);
            amount.setText(Integer.toString(mStore.getItemAmount(i)));
            amount.setId(i + 400);
            newRow.addView(amount);
            layout.addView(newRow);
        }
    }

    public void startClick(View view) {

        if (checkName()) {
            setContentView(R.layout.activity_main);
            setTitle();
        } else {
            Toast.makeText(getApplicationContext(), "Make a Name !", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeClick(View view) {

        setContentView(R.layout.activity_store);
        ((TextView) findViewById(id.moneyAmount)).setText(Float.toString(mStore.getMoney()));
        generateStoreControls();
    }
    public void storeExitClick(View view) {
        setContentView(R.layout.activity_main);
        updateResources();
    }

    public void TreeClick(View view){

        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        Animation animMove=AnimationUtils.loadAnimation(getApplicationContext(), anim.move);
        ImageView tap=(ImageView) findViewById(id.bucket);
        tap.setVisibility(View.VISIBLE);
        tap.startAnimation(animFadein);
        tap.startAnimation(animMove);


    }

}
