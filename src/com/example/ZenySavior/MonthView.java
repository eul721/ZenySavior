package com.example.ZenySavior;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jacky on 7/24/2014.
 */
public class MonthView extends FragmentActivity {

    private Date curDate;

    private TextView currentlySelectedDay;
    private TextView selectedDaySpendingLabel;
    private TextView currentMonthSpendingLabel;

    private Calendar cal;
    private static DataHelper dataHelper;

    private boolean changeDaily = false;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthview);

        dataHelper = new DataHelper(getBaseContext());

        cal = Calendar.getInstance();
        currentlySelectedDay = (TextView)findViewById(R.id.curDateLabel);
        currentlySelectedDay.setTextColor(getResources().getColor(R.color.blue));
        currentlySelectedDay.setShadowLayer((float)2.0,(float)1.0,(float)1.0,getResources().getColor(R.color.black));
        selectedDaySpendingLabel = (TextView)findViewById(R.id.selectedDaySpendingLabel);
        currentMonthSpendingLabel = (TextView)findViewById(R.id.currentMonthSpendingLabel);



        //calFragment.setCaldroidListener(this.getClickListeners());

        final CalendarUICaldroidFragment calFragment = new CalendarUICaldroidFragment(this,cal);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.caldroidWrapper, calFragment);
        transaction.commit();



    }

    public void changeDate(Date date){
        changeDaily = true;
        this.curDate = date;
        updateLabels(date);
    }

    public void changeMonth(int year, int month){
        changeDaily = false;
        cal.set(year,month,1);
        this.curDate = cal.getTime();
        updateLabels(this.curDate);
    }

    private void updateLabels(Date date){
        if(cal.getTime()!=date)
            cal.setTime(date);

        if(changeDaily){
            currentlySelectedDay.setText(DateFormat.getDateInstance().format(cal.getTime()));
            selectedDaySpendingLabel.setText(getString(R.string.daily_spending_prefix) + String.valueOf(dataHelper.searchValueForDate(cal.getTime())));
        }
        currentMonthSpendingLabel.setText(getString(R.string.monthly_spending_prefix) + String.valueOf(dataHelper.getSumForMonth(cal.getTime())));
    }

}
