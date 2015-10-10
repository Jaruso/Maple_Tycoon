package com.example.joe.mapletycoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.joe.mapletycoon.R.*;

public class MainActivity extends AppCompatActivity {

    public String mUsername = "";
    public int currentYear = 1800;
    public Store mStore = new Store();
    public BuyClickListener buyClickListener = new BuyClickListener(new WeakReference<Store>(mStore), this);
    public SellClickListener sellClickListener = new SellClickListener(new WeakReference<Store>(mStore), this);
    public HashMap<Integer, Integer> climateScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.start_activity);

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
            GridLayout layout = (GridLayout) findViewById(id.gridLayout);

            TextView name = new TextView(this);
            name.setId(i + 100);
            name.setTextSize(14);
            name.setGravity(Gravity.CENTER);
            name.setTypeface(Typeface.MONOSPACE);
            name.setText(items.get(i).getName());
            layout.addView(name);

            TextView amount = new TextView(this);
            amount.setText("(" + Integer.toString(mStore.getItemAmount(i)) + ")");
            amount.setTextSize(14);
            amount.setGravity(Gravity.CENTER);
            amount.setTypeface(Typeface.MONOSPACE);
            amount.setId(i + 400);
            layout.addView(amount);

            Button buy = new Button(this);
            buy.setText("Buy");
            buy.setId(i + 200);
            buy.setOnClickListener(buyClickListener);
            buy.setBackgroundResource(drawable.buttonstyle);
            buy.setTextColor(Color.parseColor("#FFFFFF"));
            layout.addView(buy);

            Button sell = new Button(this);
            sell.setText("Sell");
            sell.setId(i + 300);
            sell.setOnClickListener(sellClickListener);
            sell.setBackgroundResource(drawable.buttonstyle);
            sell.setTextColor(Color.parseColor("#FFFFFF"));
            layout.addView(sell);

            Button description = new Button(this);
            description.setText("?");
            description.setId(i + 500);
            description.setBackgroundResource(drawable.buttonstyle);
            description.setTextColor(Color.parseColor("#FFFFFF"));
            layout.addView(description);


        }
    }

    public void startClick(View view) {

        if (checkName()) {
            setContentView(R.layout.activity_main);
            updateResources();
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

        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        Animation animMove=AnimationUtils.loadAnimation(getApplicationContext(), anim.move);
        Animation animMoveTap=AnimationUtils.loadAnimation(getApplicationContext(), anim.movetap);
        Animation animFadeinTap=AnimationUtils.loadAnimation(getApplicationContext(), anim.fadeintap);
        ImageView bucket=(ImageView) findViewById(id.bucket);
        ImageView tap=(ImageView) findViewById(id.tap);
        tap.startAnimation(animFadeinTap);
        tap.startAnimation(animMoveTap);
        bucket.startAnimation(animFadein);
        bucket.startAnimation(animMove);

        runSimulation();


    }

    public void createSummary(float totalMoney, float totalSap, float totalSyrup){


        float avgTemp = 0.0f;     //
        String fact = "";         //


        TextView avgGalText = (TextView) findViewById(id.totalsap);
        avgGalText.setText(Float.toString(totalSap));

        TextView totalGalText = (TextView) findViewById(id.totalsyrup);
        totalGalText.setText(Float.toString(totalSyrup));

        TextView totalText = (TextView) findViewById(id.moneymade);
        totalText.setText(Float.toString(totalMoney));

        TextView avgTempText = (TextView) findViewById(id.avgtemp);
        avgTempText.setText(Float.toString(avgTemp));

        TextView factText = (TextView) findViewById(id.syrupfact);
        factText.setText(fact);

    }

    public enum effect{
        sap,
        syrup,
        emmisions,
        money
    }

    public void runSimulation(){
      //  climateScores.get(currentYear);
        float sapPerTree=0.3f;
        int treePerHouse=500;
        float totalSap=sapPerTree*treePerHouse*1;
        float totalSyrup=totalSap/35;
        float totalMoney=totalSyrup*30;
       for(StoreItem item: mStore.getAvailabelItems()){
           effect e=item.getEffect();
           switch(e) {
               case sap:
                   totalSap *= Math.pow(item.getMultiplyer(), item.getAmount());
                   totalSyrup = totalSap / 35;
                   totalMoney = totalSyrup * 30;
                   break;
               case syrup:
                   totalSyrup *= Math.pow(item.getMultiplyer(), item.getAmount());
                   totalMoney = totalSyrup * 30;
                   break;
               case emmisions:
                   break;
               case money:
                   totalMoney *= Math.pow(item.getMultiplyer(), item.getAmount());
                   break;
           }
           setContentView(layout.summary_activity);
           createSummary(totalMoney, totalSap, totalSyrup);



           }
       }




}
