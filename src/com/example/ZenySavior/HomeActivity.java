package com.example.ZenySavior;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
public class HomeActivity extends Activity {

    //consts
    private final int N_DIGITS = 6;
    private final String[] K_DIGITS_LABELS = {"thousands","hundreds", "tens", "ones","tenths","hundredths"};

    private HashMap<String,NumberPicker> numberPickers = new HashMap<String,NumberPicker>(N_DIGITS);


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //find a way to utilize iterations?
        numberPickers.put("thousands",(NumberPicker)findViewById(R.id.thousands));
        numberPickers.put("hundreds",(NumberPicker)findViewById(R.id.hundreds));
        numberPickers.put("tens",(NumberPicker)findViewById(R.id.tens));
        numberPickers.put("ones",(NumberPicker)findViewById(R.id.ones));
        numberPickers.put("tenths",(NumberPicker)findViewById(R.id.tenths));
        numberPickers.put("hundredths",(NumberPicker)findViewById(R.id.hundredths));

        //Initializes month
        ((TextView)findViewById(R.id.dateLabel)).setText(
                new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));



        initLimitsOfNumberPickers();
        //[Add] Button
        ((Button)findViewById(R.id.addValueToDaily)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testWidget(view);
            }
        });



    }

    //default the min and max vals for all numberpickers
    private void initLimitsOfNumberPickers(){
        for (NumberPicker picker : this.numberPickers.values()){
            picker.setMinValue(0);
            picker.setMaxValue(9);
        }
    }

    private void testWidget(View widgetView){
        Log.d("Testing Widget","Currently testing " + widgetView.toString());
    }

}
