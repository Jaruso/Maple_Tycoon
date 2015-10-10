package com.example.joe.mapletycoon;

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
    private int _money = 10000; //the amount of money that the user has

    public Store()
    {
        //initialize availableItems
        _availableItems = new ArrayList<StoreItem>();
        _availableItems.add(0, new StoreItem(100, "Worker", -1, R.string.workerDescription, MainActivity.effect.sap, 1.1f));
        _availableItems.add(1, new StoreItem(200, "Vat", 3, R.string.vatDescription, MainActivity.effect.syrup, 1.25f));
        _availableItems.add(2, new StoreItem(100, "Carriage", 5, R.string.carriageDescription, MainActivity.effect.money, 1.5f));
        _availableItems.add(3, new StoreItem(500, "Wood Furnace", 10, R.string.woodFurnanceDescription,MainActivity.effect.syrup, 1.25f ));
       // initialize futureItems
        _futureItems = new HashMap<Integer, StoreItem>();
      //  _futureItems.put(1920, new StoreItem(10000, "Truck", 10));
    }

    //moves items from futureItems to availableItems
    //adds any money made in this year to your money amount
    //returns anyItems added to the store this update
    public ArrayList<StoreItem> updateYear(int year, float moneyMade)
    {
        _money += moneyMade;
        ArrayList<StoreItem> newItems = new ArrayList<StoreItem>();

        Iterator it = _futureItems.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            if((Integer)pair.getKey() > year)
            {
                //move it to the availableItems list
                _availableItems.add((StoreItem)pair.getValue());
                newItems.add((StoreItem)pair.getValue());
                _futureItems.remove((Integer)pair.getKey());
            }
        }

        return newItems;
    }

    //buys 1 of the item in the list at index
    //returns false if you cant afford it
    public boolean BuyItem(int index)
    {
        if(_money > _availableItems.get(index).getPrice()) {
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

    public int getMoney()
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

    public int getFurnaces() { return _availableItems.get(3).getAmount();}
}
