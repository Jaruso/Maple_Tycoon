package com.example.joe.mapletycoon;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.example.joe.mapletycoon.R.*;

public class MainActivity extends AppCompatActivity {

    public String mUsername = "";
    public float totalCarbon = 0.0f;
    public int currentYear = 1900;
    public int maxTrees = 400;
    public Store mStore = new Store(this);
    public ShopItemClickListener shopClickListener = new ShopItemClickListener(new WeakReference<Store>(mStore), this);
    public HashMap<Integer, Integer> climateScores;
    public float climateLosses = 0;
    public Typeface gloria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.start_activity);
//        gloria = Typeface.createFromAsset(getAssets(), "fonts/GloriaHallelujah.ttf");

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
        int mHouse = mStore.getHouses();

        TextView money = (TextView) findViewById(id.moneynum);
        money.setText(String.format("$ %.2f", mMoney));

        TextView worker = (TextView) findViewById(id.workernum);
        worker.setText(Integer.toString(mWorker));

        TextView house = (TextView) findViewById(id.housenum);
        house.setText(Integer.toString(mHouse));

        TextView furnace = (TextView) findViewById(id.furnacenum);
        furnace.setText(Integer.toString(mFurnace));

    }

    public void generateStoreControls() {
        ArrayList<StoreItem> items = mStore.getAvailabelItems();
        ListView listView = (ListView) findViewById(id.listView);
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(shopClickListener);
        for (int i = 0; i < items.size(); i++) {

            adapter.add(items.get(i).getName() + " - $" + Integer.toString(items.get(i).getPrice()));


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


        double avgTemp = 0.0f;     //
        int goodDays = 0;
        int badDays = 0;

        WeatherMan wm = new WeatherMan(getApplicationContext());
        try {
            Season endSeason = wm.computeScore(currentYear);
            avgTemp = endSeason.avgTemp;
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            avgTemp = (avgTemp * 1.8) + 32;
            goodDays = endSeason.dayQuality[2];
            badDays = endSeason.dayQuality[0];
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random rand = new Random();

        int randomNum = rand.nextInt(28 + 1);

        Resources res = getResources();
        String[] facts = res.getStringArray(R.array.fact_array);

        String fact = facts[randomNum];


        TextView totalSapText = (TextView) findViewById(id.totalsap);
        totalSapText.setText("\tThis year you produced " + String.format("%.2f", totalSap) + " gallons of sap.");


        TextView totalGalText = (TextView) findViewById(id.totalsyrup);
        totalGalText.setText("\tAll that sap boiled down to " + String.format("%.2f", totalSyrup) + " gallons of syrup.");

        TextView Upkeep = (TextView) findViewById(id.totalUpkeep);
        Upkeep.setText("\tThe cost to maintain your company is $" + String.format("%.2f", totalUpkeep) + ".");


        TextView totalText = (TextView) findViewById(id.moneymade);
        totalText.setText("After expenses, you earned $" + String.format("%.2f", (totalMoney - totalUpkeep)) + " from maple syrup this year.");

        NumberFormat tnum = new DecimalFormat("#.00");
        TextView avgTempText = (TextView) findViewById(id.avgtemp);
        avgTempText.setText("Average temperature during the season was " + tnum.format(avgTemp) + " (F)");

        TextView goodDayText = (TextView) findViewById(id.gooddays);
        goodDayText.setText("# of good flow days: " + goodDays);

        TextView badDayText = (TextView) findViewById(id.baddays);
        badDayText.setText("# of bad flow days: " + badDays);

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
        double climateMod = 1;
        WeatherMan wm = new WeatherMan(getApplicationContext());

        try {
            climateMod = wm.computeScore(currentYear).climateMod;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        climateMod = climateMod / 100;
        float sapPerTree = 10;
        int treePerHouse = 80;
        float totalSap = ((sapPerTree * treePerHouse) / 5)*mStore.getHouses();
        totalSap = (float)climateMod * totalSap;
        float totalSyrup = totalSap / 45;
        float totalMoney = totalSyrup * 30;
        float totalUpkeep = 0;
        float carbonMade = 0.0f;
        for (StoreItem item : mStore.getAvailabelItems()) {
            effect e = item.getEffect();
            switch (e) {
                case sap:
                    totalSap *= item.getMultiplyer()*item.getAmount()+1;
                    totalSyrup = totalSap / 35;
                    totalMoney = totalSyrup * 30;
                    totalUpkeep = item.getAmount() * item.getUpkeep() + totalUpkeep;
                    break;
                case syrup:
                    totalSyrup *=item.getMultiplyer()*item.getAmount()+1;
                    totalMoney = totalSyrup * 30;
                    totalUpkeep = item.getAmount() * item.getUpkeep() + totalUpkeep;
                    break;
                case emmisions:
                    break;
                case money:
                    totalMoney *= item.getMultiplyer()*item.getAmount()+1;
                    totalUpkeep = item.getAmount() * item.getUpkeep() + totalUpkeep;
                    break;
            }
            carbonMade += item.getCarbon()*item.getAmount();

        }

        if(currentYear < 1970){
            totalSap *= 10;
            totalSyrup *= 10;
            totalMoney *= 10;
            totalUpkeep *= 10;
        }
        else if(currentYear < 2000) {
            totalSap *= 5;
            totalSyrup *= 5;
            totalMoney *= 5;
            totalUpkeep *= 5;
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
        if(currentYear < 1970)
            currentYear += 10;
        else if(currentYear < 2000)
            currentYear += 5;
        else
            currentYear +=1;

        mStore.updateYear(currentYear, totalMoney);
        totalCarbon += carbonMade;
        gameEnd();

    }

    public void gameEnd(){

        if (mStore.getMoney()<0){
            setContentView(layout.end_activity);
            TextView emissions = (TextView) findViewById(id.climate);
            TextView money = (TextView) findViewById(id.earned);
            TextView endTitle=(TextView) findViewById(id.endTitle);
            endTitle.setText("You have gone bankrupt!");
            emissions.setText("You have added " + String.format("%.2f", totalCarbon) + " pounds of carbon to the atmosphere.");
            money.setText("You ended with $" + String.format("%.2f", mStore.getMoney()) + ". ");
        } else if (currentYear ==2015){
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

