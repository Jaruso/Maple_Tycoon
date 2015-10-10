package com.example.joe.mapletycoon;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.Reference;

/**
 * Created by colin on 10/9/2015.
 */
public class SellClickListener implements View.OnClickListener {

    Reference<Store> mStore;
    MainActivity mParent;

    public SellClickListener(Reference<Store> store, MainActivity parent)
    {
        mStore = store;
        mParent = parent;
    }

    @Override
    public void onClick(View v) {
        int index = v.getId() - 300;
        if(!mStore.get().SellItem(index))
        {
            Toast.makeText(mParent.getApplicationContext(), "You Dont have any.", Toast.LENGTH_SHORT);
            return;
        }

        View moneyField = mParent.findViewById(R.id.moneyAmount);
        TextView money = (TextView) moneyField;
        money.setText(Float.toString(mStore.get().getMoney()));

        View field = mParent.findViewById(index+400);
        TextView amount = (TextView) field;
        if(amount == null)
        {
            return;
        }

        else{
            amount.setText( "(" + Integer.toString(mStore.get().getItemAmount(index)) + ")");
        }
    }
}
