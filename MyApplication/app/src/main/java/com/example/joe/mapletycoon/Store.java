package com.example.joe.mapletycoon;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by colin on 10/9/2015.
 */
public class Store {

    private ArrayList<StoreItem> _availableItems;
    private HashMap<Integer, StoreItem> _futureItems; //maps items to the years they unlock
    private float _money = 2000.0f; //the amount of money that the user has
    private Context mParent;
    public Store(Context parent)
    {
        mParent = parent;
        //initialize availableItems
        _availableItems = new ArrayList<StoreItem>();

        _availableItems.add(0, new StoreItem(50, "Worker", -1, R.string.workerDescription, MainActivity.effect.sap, .5f,100,0.0f));
        _availableItems.get(0).buy();
        _availableItems.get(0).buy();
        _availableItems.get(0).buy();
        _availableItems.get(0).buy();
        _availableItems.get(0).buy();
        _availableItems.add(1, new StoreItem(1500, "Sugar House", -1, R.string.sugarHouseDescription, MainActivity.effect.sap, .0f,25,0.0f));
        _availableItems.get(1).buy();
        _availableItems.add(2, new StoreItem(400, "Vat", 3, R.string.vatDescription, MainActivity.effect.syrup, .1f,0,0.0f));
        _availableItems.add(3, new StoreItem(100, "Carriage", 5, R.string.carriageDescription, MainActivity.effect.money, .5f,25,0.0f));
        _availableItems.add(4, new StoreItem(400, "Wood Furnace", 10, R.string.woodFurnanceDescription, MainActivity.effect.syrup, .5f,50,0.0f));
        _availableItems.add(5, new StoreItem(500, "Oil Furnace", 10, R.string.oilFurnaceDescription, MainActivity.effect.syrup, 1.00f,175,2.0f));

        // initialize futureItems
        _futureItems = new HashMap<Integer, StoreItem>();
      //  _futureItems.put(1920, new StoreItem(10000, "Truck", 10));
        _futureItems.put(1940, new StoreItem(10000,"Trucks", 10, R.string.trucksDescription, MainActivity.effect.sap,1.5f,100,1.0f));
        _futureItems.put(1980, new StoreItem(10000,"Propane Furnace", 10, R.string.propaneFurnaceDescription, MainActivity.effect.sap,1.0f,100,1.5f));
        _futureItems.put(1980, new StoreItem(10000,"Natural Gas Furnace", 10, R.string.naturalGasFurnaceDescription, MainActivity.effect.sap,.5f,50,1.0f));
        _futureItems.put(1930, new StoreItem(10000,"Steam Furnace", 10, R.string.steamFurnaceDescription, MainActivity.effect.sap,.75f,50,1.75f));
        _futureItems.put(1970, new StoreItem(10000,"Plastic Tubing", 1, R.string.plasticTubingDescription, MainActivity.effect.sap,1.0f,25,0.0f));
        _futureItems.put(1970, new StoreItem(15000,"Vacuum Tubing", 1, R.string.vacuumPumpDescription, MainActivity.effect.sap,.75f,25,0.0f));
        _futureItems.put(1970, new StoreItem(10000,"Preheater", 3, R.string.preheatersDescription, MainActivity.effect.emmisions,.75f,75,-1.0f));
        _futureItems.put(1990, new StoreItem(10000,"Super Charged Preheater", 1, R.string.superChargedPreheatersDescription, MainActivity.effect.sap,1.0f,25,-2.0f));



    }

    //moves items from futureItems to availableItems
    //adds any money made in this year to your money amount
    //returns anyItems added to the store this update
    public ArrayList<StoreItem> updateYear(int year, float moneyMade)
    {
        _money += moneyMade;
        ArrayList<StoreItem> newItems = new ArrayList<StoreItem>();

        for (Map.Entry<Integer, StoreItem> entry : _futureItems.entrySet()) {
                //move it to the availableItems list
                if(entry.getKey() > year) {
                    _availableItems.add((StoreItem) entry.getValue());
                    newItems.add((StoreItem) entry.getValue());
                }
        }

        return newItems;
    }

    //buys 1 of the item in the list at index
    //returns false if you cant afford it
    public boolean BuyItem(int index)
    {
        if(_money >= _availableItems.get(index).getPrice()) {
            if(index == 1) // they are trying to buy a sugar house
            {
                if(_availableItems.get(0).getAmount()/5 <= _availableItems.get(index).getAmount())
                {
                    Toast.makeText(mParent, "You need more workers to construct a sugar house (5 workers per house)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            if(index == 2 || index == 4 || index == 5 ||index == 7 || index == 8 || index == 9 )
            {
                if(_availableItems.get(index).getAmount() >= _availableItems.get(1).getAmount()*2)
                {
                    Toast.makeText(mParent, "You need need another sugar house to build more of this type of equipment", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            _money -= _availableItems.get(index).getPrice();
            _availableItems.get(index).buy();
            return true;
        }
        return false;
    }

    public boolean SellItem(int index)
    {
        if(_availableItems.get(index).sell()) {
            _money += _availableItems.get(index).getPrice() / 2;
            return true;
        }

        return false;
    }

    public float getMoney()
    {
        return _money;
    }

    public ArrayList<StoreItem> getAvailabelItems()
    {
        return _availableItems;
    }

    public int getItemAmount(int index)
    {
        return _availableItems.get(index).getAmount();
    }

    public int getWorkers()
    {
        return _availableItems.get(0).getAmount();
    }

    public int getHouses()
    {
        return _availableItems.get(1).getAmount();
    }

    public int getFurnaces() {
        int total = 0;

        total += _availableItems.get(4).getAmount();
        total += _availableItems.get(5).getAmount();

        total += -_futureItems.get(1980).getAmount();
        total += -_futureItems.get(1930).getAmount();

        return total;
    }


    public void addMoney(float profit) { _money += profit;}

    public int getDescriptionId (int index)
    {
        return _availableItems.get(index).getDescriptionId();
    }

    public String getName (int index)
    {
        return _availableItems.get(index).getName();
    }
}
