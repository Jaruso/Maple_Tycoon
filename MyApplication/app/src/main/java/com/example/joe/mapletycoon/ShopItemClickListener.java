package com.example.joe.mapletycoon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.Reference;

/**
 * Created by colin on 10/10/2015.
 */
public class ShopItemClickListener implements ListView.OnItemClickListener {

    Reference<Store> mStore;
    MainActivity mParent;

    public ShopItemClickListener(Reference<Store> store, MainActivity parent)
    {
        mStore = store;
        mParent = parent;
    }

    @Override
    public void onItemClick(AdapterView<?> list, View view, int position, long id) {
        final int index = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
        builder.setMessage(mParent.getResources().getString(mStore.get().getDescriptionId(index)) + "\n\nAmount Owned: " + mStore.get().getItemAmount(index))
        .setTitle((mStore.get().getName(index)));
        builder.setNegativeButton("Buy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int num) {
                //buy the item at index
                mStore.get().BuyItem(index);
                ((TextView) mParent.findViewById(R.id.moneyAmount)).setText(String.format("$ %.2f", mStore.get().getMoney()));
            }
        });

        builder.setPositiveButton("Sell", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int num) {
                //sell the item at index
                mStore.get().SellItem(index);
                ((TextView) mParent.findViewById(R.id.moneyAmount)).setText(String.format("$ %.2f", mStore.get().getMoney()));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
