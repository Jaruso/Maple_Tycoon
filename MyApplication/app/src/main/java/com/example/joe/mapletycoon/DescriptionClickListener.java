package com.example.joe.mapletycoon;

import android.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.Reference;

/**
 * Created by colin on 10/10/2015.
 */
public class DescriptionClickListener implements View.OnClickListener {

    Reference<Store> mStore;
    MainActivity mParent;

    public DescriptionClickListener(Reference<Store> store, MainActivity parent)
    {
        mStore = store;
        mParent = parent;
    }

    @Override
    public void onClick(View v) {
        int index = v.getId() - 500;

        AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
        builder.setMessage(mParent.getResources().getString(mStore.get().getDescriptionId(index)))
                .setTitle((mStore.get().getName(index)));
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
