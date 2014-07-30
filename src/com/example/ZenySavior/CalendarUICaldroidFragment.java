package com.example.ZenySavior;

import android.os.Bundle;
import android.view.View;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jacky on 7/26/2014.
 */
public class CalendarUICaldroidFragment extends CaldroidFragment{

    private MonthView parentView;
    private Calendar cal;

    private Date currentlyEnabledDate;



    public CalendarUICaldroidFragment(MonthView parentView,Calendar cal){
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        this.setArguments(args);
        this.parentView = parentView;
        this.cal = cal;
        this.setupListners();


    }

    private void setupListners(){
        final CaldroidFragment caldroidFragment = this;

        this.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onCaldroidViewCreated(){
                //changeDaily = true;
                parentView.changeDate(cal.getTime());
            }

            @Override
            public void onSelectDate(Date date, View view) {
                parentView.changeDate(date);

                if(currentlyEnabledDate!=null)
                    caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white,currentlyEnabledDate);
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, date);
                currentlyEnabledDate = date;
                caldroidFragment.refreshView();
            }
            @Override
            public void onChangeMonth(int month, int year) {
                //cal.set(year, month - 1, 1); //the month here is the ACTUAL MONTH, not the 0-indexed month. Must adjust.
                //changeDaily = false;
                parentView.changeMonth(year, month-1);
            }
        });
    }
}
