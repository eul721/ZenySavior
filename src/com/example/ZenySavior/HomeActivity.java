package com.example.ZenySavior;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomeActivity extends Activity {

    //consts
    //private final int N_DIGITS = 6; //Limiting how many digits
    private final double MAXIMUM_FACTOR = 1000; //Multiplier factor
    private static DataHelper dataHelper;


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
        ImageButton addButton = (ImageButton)findViewById(R.id.addValueToDaily);
        ImageButton subtractButton = (ImageButton)findViewById(R.id.subtractValueDaily);
        final Button debugButton = (Button)findViewById(R.id.debugButton);

        //Initializes month

        Date curTime = Calendar.getInstance().getTime();
        String dateFormat = new SimpleDateFormat("yyyy/MM/dd").format(curTime);
        ((TextView)findViewById(R.id.dateLabel)).setText(
                dateFormat);

        //text Labels
        final TextView dailySpendingsLabel = (TextView)findViewById(R.id.dailySpendingsLabel);
        final TextView monthlySpendingsLabel = (TextView)findViewById(R.id.monthlySpendingsLabel);

        //get DB
        dataHelper = new DataHelper(this.getBaseContext());
        dataHelper.debugGetAllRows();



        dailySpendingsLabel.setText("Today's Spendings: $" + dataHelper.searchValueForDate(curTime));
        monthlySpendingsLabel.setText("This Month's Spendings: $" + dataHelper.getSumForMonth(curTime));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double finalVal = performModificationToDaily(getInputValue());
                dailySpendingsLabel.setText("Today's Spendings: $" + BigDecimal.valueOf(finalVal).setScale(2, BigDecimal.ROUND_UP).toString());
                monthlySpendingsLabel.setText("This Month's Spendings: $" + performModificationToMonth());
            }
        });
        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double finalVal = performModificationToDaily(-getInputValue());
                dailySpendingsLabel.setText("Today's Spendings: $" + BigDecimal.valueOf(finalVal).setScale(2, BigDecimal.ROUND_UP).toString());
                monthlySpendingsLabel.setText("This Month's Spendings: $" + performModificationToMonth());
            }
        });

        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DebugWindow debugWindow = new DebugWindow();
                debugWindow.show(getFragmentManager(),"dialog");
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.monthViewButton:{
                Intent intent = new Intent(getBaseContext(),MonthView.class);
                startActivity(intent);
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
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

    /*
    * Tester functions
    *
    private void testWidget(View widgetView){
        Log.d("Testing Widget","Currently testing " + widgetView.toString());
    }

    private void testWidget(View widgetView, String additionalMessage){
        Log.d("Testing Widget", "Currently testing " + widgetView.toString() + ".\nCustom Message : "+additionalMessage);
    }
    */

    protected double getInputValue(){
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
    private double performModificationToDaily(final double providedInput){
        assert (dataHelper!=null);
        Date curTime = Calendar.getInstance().getTime();
        double newVal = BigDecimal.valueOf(providedInput).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        try{
            newVal = dataHelper.insertValueForDate(curTime,newVal);
        }catch(Exception e){
            e.printStackTrace();
        }
        return newVal;

    }

    private double performModificationToMonth(){
        assert (dataHelper!=null);
        Date curTime = Calendar.getInstance().getTime();
        return dataHelper.getSumForMonth(curTime);
    }

    //FOR TESTING SUBCLASS
    protected List<NumberPicker> getNumberPickers(){
        return this.numberPickers;
    }




}
