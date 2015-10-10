package com.example.joe.mapletycoon;

/**
 * Created by colin on 10/9/2015.
 */
public class StoreItem {

    private int mPrice; // price of the item in dollars, must always only have 2 digits
    private String mName;
    private int mDescriptionId;
    private int mAmountOwned;
    private int mMaxAmount; //-1 for infinite
    private MainActivity.effect mMyEffect;
    private float mMultiplyer;
    private float mUpkeep;
    public StoreItem(int price, String name, int maxAmount, int descriptionId, MainActivity.effect myEffect, float multiplyer, float upkeep)
    {
        mPrice = price;
        mName = name;
        mAmountOwned = 0;
        mMaxAmount = maxAmount;
        mDescriptionId = descriptionId;
        mMyEffect=myEffect;
        mMultiplyer=multiplyer;
        mUpkeep=upkeep;
    }

    public int getPrice()
    {
        return mPrice;
    }

    public void setPrice(int price)
    {
        mPrice = price;
    }

    public String getName()
    {
        return mName;
    }

    public int getMaxAmount()
    {
        return mMaxAmount;
    }

    //buys one of this item
    public void buy()
    {
        if( mMaxAmount == -1 || mAmountOwned < mMaxAmount)
            mAmountOwned++;
    }

    public boolean sell()
    {
        if(mAmountOwned > 0) {
            mAmountOwned--;
            return true;
        }

        return false;
    }

    public int getAmount()
    {
        return mAmountOwned;
    }

    public float getMultiplyer(){ return mMultiplyer;}

    public float getUpkeep(){ return mUpkeep;};

    public MainActivity.effect getEffect (){ return mMyEffect;}

    public int getDescriptionId()
    {
        return mDescriptionId;
    }
}
