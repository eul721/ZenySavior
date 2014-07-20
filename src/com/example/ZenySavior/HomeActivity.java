package com.example.ZenySavior;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomeActivity extends Activity {

    //consts
    private final int N_DIGITS = 6;
    private final double MAXIMUM_FACTOR = 1000;


    private List<NumberPicker> numberPickers = new ArrayList<NumberPicker>();


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Create and set Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Initialize Number Pickers
        initNumberPickers();
        //[Add] Button
        //The resource has been added to the xml
        ImageButton addButton = ((ImageButton)findViewById(R.id.addValueToDaily));

        //Initializes month
        Date curTime = Calendar.getInstance().getTime();
        String dateFormat = new SimpleDateFormat("yyyy/MM/dd").format(curTime);
        ((TextView)findViewById(R.id.dateLabel)).setText(
                dateFormat);

        //text Labels
        final TextView dailySpendingsLabel = (TextView)findViewById(R.id.dailySpendingsLabel);
        final TextView monthlySpendingsLabel = (TextView)findViewById(R.id.monthlySpendingsLabel);

        //Init the labels
        //if (has_entry_for_today)
        //set label as that
        //else
        dailySpendingsLabel.setText("Today's Spendings: $0.00");
        monthlySpendingsLabel.setText("This Month's Spendings: $0.00");




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                * If entry for today does not exist
                *   Add entry to DB
                * Else
                *   Modify entry
                * */
                dailySpendingsLabel.setText("Today's Spendings: $" + BigDecimal.valueOf(getInputValue()).setScale(2,BigDecimal.ROUND_UP).toString());

            }
        });



    }

    //default the min and max vals for all numberpickers
    private void initNumberPickers(){
        // Future:
        // Have program automatically create the NumPickers?
        assert (numberPickers!=null);
        numberPickers.add((NumberPicker)findViewById(R.id.thousands));
        numberPickers.add((NumberPicker)findViewById(R.id.hundreds));
        numberPickers.add((NumberPicker)findViewById(R.id.tens));
        numberPickers.add((NumberPicker)findViewById(R.id.ones));
        numberPickers.add((NumberPicker)findViewById(R.id.tenths));
        numberPickers.add((NumberPicker)findViewById(R.id.hundredths));

        for (NumberPicker picker : this.numberPickers){
            picker.setMinValue(0);
            picker.setMaxValue(9);
        }
    }

    private void testWidget(View widgetView){
        Log.d("Testing Widget","Currently testing " + widgetView.toString());
    }

    private void testWidget(View widgetView, String additionalMessage){
        Log.d("Testing Widget", "Currently testing " + widgetView.toString() + ".\nCustom Message : "+additionalMessage);
    }

    private double getInputValue(){
        /*
        [0] = *1000
        [1] = *100
        [2] = *10
        [3] = *1
        [4] = *.1
        [5] = *.01
        * */
        double curVal = 0.0;
        double curValAtPicker;
        for (int i=0;i<this.numberPickers.size();i++){
            NumberPicker picker = this.numberPickers.get(i);
            curValAtPicker = picker.getValue();
            curValAtPicker = curValAtPicker * MAXIMUM_FACTOR / Math.pow(10.0,i);
            curVal += curValAtPicker;
        }
        return curVal;
    }

}
