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
    private float _money; //the amount of money that the user has

    public Store()
    {
        //initialize availableItems
        _availableItems = new ArrayList<StoreItem>();
        _availableItems.add(0, new StoreItem(100.00f, "Worker", -1));
        _availableItems.add(1, new StoreItem(200f, "Vat", 3));
        _availableItems.add(2, new StoreItem(100f, "Carriage", 5));
        _availableItems.add(3, new StoreItem(500f, "Wood Furnace", 10));

       // initialize futureItems
        _futureItems.put(1920, new StoreItem(10000f, "Truck", 10));
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
    public void BuyItem(int index)
    {
        _money -= _availableItems.get(index).getPrice();
        _availableItems.get(index).buy();
    }

    public void SellItem(int index)
    {
        _money += _availableItems.get(index).getPrice()/2;
        _availableItems.get(index).sell();
    }

    public float getMoney()
    {
        return _money;
    }

    public ArrayList<StoreItem> getAvailabelItems()
    {
        return _availableItems;
    }
}
