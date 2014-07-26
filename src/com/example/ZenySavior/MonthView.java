package com.example.ZenySavior;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jacky on 7/24/2014.
 */
public class MonthView extends FragmentActivity {

    private Date curDate;

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
        selectedDaySpendingLabel = (TextView)findViewById(R.id.selectedDaySpendingLabel);
        currentMonthSpendingLabel = (TextView)findViewById(R.id.currentMonthSpendingLabel);

        final MonthView instance = this;

        //CalendarUIWidget calWidget = new CalendarUIWidget(cal);
        CaldroidFragment calFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        calFragment.setArguments(args);
        CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onCaldroidViewCreated(){
                changeDaily = true;
                instance.changeDate(cal.getTime());
            }

            @Override
            public void onSelectDate(Date date, View view) {
                //MonthView monthview = (MonthView)view.getContext();
                changeDaily = true;
                instance.changeDate(date);
            }
            @Override
            public void onChangeMonth(int month, int year){
                cal.set(year,month-1,1); //the month here is the ACTUAL MONTH, not the 0-indexed month. Must adjust.
                changeDaily = false;
                instance.changeDate(cal.getTime());
            }
        };
        calFragment.setCaldroidListener(listener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.caldroidWrapper, calFragment);
        transaction.commit();







       /* calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i2, int i3) {
                cal.set(i,i2,i3);
                //cal.set(Calendar.MONTH,i+1);
                //cal.set(Calendar.DATE,i);

                Logger.logWithTag("Selected Month Focus Color",calendarView.getFocusedChild().getClass().toString());
                Logger.logWithTag("Day Changed", String.valueOf(i)+", "+String.valueOf(i2)+", "+String.valueOf(i3));
                selectedDaySpendingLabel.setText(getString(R.string.daily_spending_prefix) + String.valueOf(dataHelper.searchValueForDate(cal.getTime())));
                currentMonthSpendingLabel.setText(getString(R.string.monthly_spending_prefix) + String.valueOf(dataHelper.getSumForMonth(cal.getTime())));
            }
        });*/
        //calView.setOn


    }

    public void changeDate(Date date){
        this.curDate = date;
        updateLabels(date);
    }

    private void updateLabels(Date date){
        if(cal.getTime()!=date)
            cal.setTime(date);

        if(changeDaily)
            selectedDaySpendingLabel.setText(getString(R.string.daily_spending_prefix) + String.valueOf(dataHelper.searchValueForDate(cal.getTime())));
        currentMonthSpendingLabel.setText(getString(R.string.monthly_spending_prefix) + String.valueOf(dataHelper.getSumForMonth(cal.getTime())));
    }
}
