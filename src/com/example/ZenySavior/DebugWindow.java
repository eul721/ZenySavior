package com.example.ZenySavior;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

/**
 * Created by Jacky on 7/21/2014.
 * This class serves as the debug window fragment
 */
public class DebugWindow extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.debugwindow,null);

        TableLayout table = (TableLayout)view.findViewById(R.id.dbTable);
        for (int i = 0; i < 5; i++) {
            table.setColumnStretchable(i, true);
        }

        DataHelper dataHelper = new DataHelper(view.getContext());
        ArrayList<TableRow> listOfPopulatedTableRows = dataHelper.getAllRowsInTableRowArrayList(view.getContext());
        for (TableRow row : listOfPopulatedTableRows){
            table.addView(row);
        }


        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


        //populate Table



        return builder.create();
    }



}
