package com.example.joe.mapletycoon;

/**
 * Created by colin on 10/9/2015.
 */
public class StoreItem {

    private float _price; // price of the item in dollars, must always only have 2 digits
    private String _name;
    //icon?
    private int _amountOwned;
    private int _maxAmount; //-1 for infinite

    public StoreItem(float price, String name, int maxAmount)
    {
        _price = price;
        _name = name;
        _amountOwned = 0;
        _maxAmount = maxAmount;
    }

    public float getPrice()
    {
        return _price;
    }

    public void setPrice(float price)
    {
        _price = price;
    }

    public String getName()
    {
        return _name;
    }

    public int getMaxAmount()
    {
        return _maxAmount;
    }

    //buys one of this item
    public void buy()
    {
        _amountOwned++;
    }

    public boolean sell()
    {
        if(_amountOwned > 0) {
            _amountOwned--;
            return true;
        }

        return false;
    }

    public int getAmount()
    {
        return _amountOwned;
    }
}
