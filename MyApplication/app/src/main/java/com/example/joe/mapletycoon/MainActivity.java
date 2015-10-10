package com.example.joe.mapletycoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.example.joe.mapletycoon.R.*;

public class MainActivity extends AppCompatActivity {

    public String mUsername = "";
    public float totalCarbon = 0.0f;
    public int currentYear = 1800;
    public Store mStore = new Store();
    public BuyClickListener buyClickListener = new BuyClickListener(new WeakReference<Store>(mStore), this);
    public SellClickListener sellClickListener = new SellClickListener(new WeakReference<Store>(mStore), this);
    public DescriptionClickListener descClickListener = new DescriptionClickListener(new WeakReference<Store>(mStore), this);
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

    public boolean checkName() {

        EditText nameText = (EditText) findViewById(id.editName);
        if (nameText.getText().length() != 0) {
            setName(nameText.getText().toString());
            return true;
        } else
            return false;
    }


    public void setName(String username) {
        mUsername = username;
    }

    public void setTitle() {

        TextView maintitle = (TextView) findViewById(id.mainTitle);
        maintitle.setText(mUsername + ", " + currentYear);

    }

    public void updateResources() {
        float mMoney = mStore.getMoney();
        int mWorker = mStore.getWorkers();
        int mFurnace = mStore.getFurnaces();

        TextView money = (TextView) findViewById(id.moneynum);
        money.setText(Float.toString(mMoney));

        TextView worker = (TextView) findViewById(id.workernum);
        worker.setText(Integer.toString(mWorker));

        TextView furnace = (TextView) findViewById(id.furnacenum);
        furnace.setText(Integer.toString(mFurnace));

    }

    public void generateStoreControls() {
        ArrayList<StoreItem> items = mStore.getAvailabelItems();

        for (int i = 0; i < items.size(); i++) {
            GridLayout layout = (GridLayout) findViewById(id.gridLayout);

            TextView name = new TextView(this);
            name.setId(i + 100);
            name.setTextSize(12);
            name.setGravity(Gravity.CENTER);
            name.setTypeface(Typeface.MONOSPACE);
            name.setText(items.get(i).getName());
            layout.addView(name);

            TextView amount = new TextView(this);
            amount.setText("(" + Integer.toString(mStore.getItemAmount(i)) + ")");
            amount.setTextSize(12);
            amount.setTypeface(Typeface.MONOSPACE);
            amount.setId(i + 400);
            layout.addView(amount);

            Button buy = new Button(this);
            buy.setText("Buy");
            buy.setId(i + 200);
            buy.setOnClickListener(buyClickListener);
            buy.setBackgroundResource(drawable.buttonstyle);
            buy.setTextSize(12);
            buy.setTextColor(Color.parseColor("#FFFFFF"));
            layout.addView(buy);

            Button sell = new Button(this);
            sell.setText("Sell");
            sell.setId(i + 300);
            sell.setOnClickListener(sellClickListener);
            sell.setBackgroundResource(drawable.buttonstyle);
            sell.setTextColor(Color.parseColor("#FFFFFF"));
            sell.setTextSize(12);
            layout.addView(sell);

            Button description = new Button(this);
            description.setText("?");
            description.setId(i + 500);
            description.setBackgroundResource(drawable.buttonstyle);
            description.setTextColor(Color.parseColor("#FFFFFF"));
            description.setTextSize(12);
            description.setOnClickListener(descClickListener);
            layout.addView(description);

            ((TextView) findViewById(id.moneyAmount)).setText(Float.toString(mStore.getMoney()));
        }
    }

    public void startClick(View view) {

        if (checkName()) {
            start(view);
        } else {
            Toast.makeText(getApplicationContext(), "Make a Name !", Toast.LENGTH_SHORT).show();
        }
    }

    public void start(View view) {
        setContentView(R.layout.activity_main);
        updateResources();
        setTitle();
    }


    public void storeClick(View view) {

        setContentView(R.layout.activity_store);
        generateStoreControls();
    }


    public void storeExitClick(View view) {
        setContentView(R.layout.activity_main);
        updateResources();
        setTitle();
    }

    public void TreeClick(View view) {

        final Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        final Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(), anim.move);
        Animation animMoveTap = AnimationUtils.loadAnimation(getApplicationContext(), anim.movetap);
        Animation animFadeinTap = AnimationUtils.loadAnimation(getApplicationContext(), anim.fadeintap);
        final ImageView bucket = (ImageView) findViewById(id.bucket);
        ImageView tap = (ImageView) findViewById(id.tap);
        tap.startAnimation(animFadeinTap);
        tap.startAnimation(animMoveTap);


        animMoveTap.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {
            }

            public void onAnimationRepeat(Animation a) {
            }

            public void onAnimationEnd(Animation a) {
                bucket.startAnimation(animFadein);
                bucket.startAnimation(animMove);
            }

        });

        animMove.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {
            }

            public void onAnimationRepeat(Animation a) {
            }

            public void onAnimationEnd(Animation a) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runSimulation();
                    }
                }, 200);
            }

        });

    }

    public void createSummary(float totalMoney, float totalSap, float totalSyrup, float totalUpkeep) {

        float avgTemp = 0.0f;     //

        Random rand = new Random();

        int randomNum = rand.nextInt((28 - 0) + 1) + 0;

        Resources res = getResources();
        String[] facts = res.getStringArray(R.array.fact_array);

        String fact = facts[randomNum];


        TextView totalSapText = (TextView) findViewById(id.totalsap);
        totalSapText.setText("This year you produced " + String.format("%.2f", totalSap) + " gallons of sap.");


        TextView totalGalText = (TextView) findViewById(id.totalsyrup);
        totalGalText.setText("All that sap boiled down to " + String.format("%.2f", totalSyrup) + " gallons of syrup.");

        TextView Upkeep = (TextView) findViewById(id.totalUpkeep);
        Upkeep.setText("The cost to maintain your company is $" + String.format("%.2f", totalUpkeep) + ".");

        TextView totalText = (TextView) findViewById(id.moneymade);
        totalText.setText("After expenses, you earned $" + String.format("%.2f", (totalMoney-totalUpkeep)) + " from maple syrup this year.");

        TextView avgTempText = (TextView) findViewById(id.avgtemp);
        avgTempText.setText(Float.toString(avgTemp));

        TextView factText = (TextView) findViewById(id.syrupfact);
        factText.setText("\t\t\tRandom Maple Syrup Fact ! \n\n \t" + fact);

    }

    public enum effect {
        sap,
        syrup,
        emmisions,
        money
    }

    public void runSimulation() {
        //  climateScores.get(currentYear);
        float sapPerTree = 0.3f;
        int treePerHouse = 500;
        float totalSap = sapPerTree * treePerHouse * 1;
        float totalSyrup = totalSap / 35;
        float totalMoney = totalSyrup * 30;
        float totalUpkeep = 0;
        float carbonMade = 0.0f;
        for (StoreItem item : mStore.getAvailabelItems()) {
            effect e = item.getEffect();
            switch (e) {
                case sap:
                    totalSap *= Math.pow(item.getMultiplyer(), item.getAmount());
                    totalSyrup = totalSap / 35;
                    totalMoney = totalSyrup * 30;
                    totalUpkeep = item.getAmount() * item.getUpkeep() + totalUpkeep;
                    break;
                case syrup:
                    totalSyrup *= Math.pow(item.getMultiplyer(), item.getAmount());
                    totalMoney = totalSyrup * 30;
                    totalUpkeep = item.getAmount() * item.getUpkeep() + totalUpkeep;
                    break;
                case emmisions:
                    break;
                case money:
                    totalMoney *= Math.pow(item.getMultiplyer(), item.getAmount());
                    totalUpkeep = item.getAmount() * item.getUpkeep() + totalUpkeep;
                    break;
            }
            carbonMade += item.getCarbon()*item.getAmount();

        }

        LayoutInflater inflator = getLayoutInflater();
        View view = inflator.inflate(layout.summary_activity, null, false);
        view.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        Animation myAnim = view.getAnimation();
        myAnim.setDuration(3000);
        view.setAnimation(myAnim);
        setContentView(view);

        createSummary(totalMoney, totalSap, totalSyrup, totalUpkeep);
        totalMoney = totalMoney - totalUpkeep;
        mStore.addMoney(totalMoney);
        currentYear += 1;
        totalCarbon += carbonMade;
        gameEnd();

    }

    public void gameEnd(){

        if(mStore.getMoney()<0){
            setContentView(layout.end_activity);
            TextView emissions = (TextView) findViewById(id.climate);
            TextView money = (TextView) findViewById(id.earned);
            TextView endTitle=(TextView) findViewById(id.endTitle);
            endTitle.setText("You have gone backrupt!");
            emissions.setText("You have added " + String.format("%.2f", totalCarbon) + " pounds of carbon to the atmosphere.");
            money.setText("You ended with $" + String.format("%.2f", mStore.getMoney()) + ". ");
        }
        else if(currentYear==2015){
            setContentView(layout.end_activity);
            TextView emissions = (TextView) findViewById(id.climate);
            TextView money = (TextView) findViewById(id.earned);
            TextView endTitle=(TextView) findViewById(id.endTitle);
            endTitle.setText("You have reached the current year!");
            emissions.setText("You have added " + String.format("%.2f", totalCarbon) + " pounds of carbon to the atmosphere.");
            money.setText("You ended with $" + String.format("%.2f", mStore.getMoney()) + ". ");
        }



    }

}

