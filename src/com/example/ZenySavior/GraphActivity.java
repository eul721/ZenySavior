package com.example.ZenySavior;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jacky on 7/29/2014.
 */
public class GraphActivity extends Activity {
    private static DataHelper dataHelper;
    private Calendar calendar;

    final static int MAX_DATE_IN_MONTH = 31;
    private int[] days_in_month;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphactivitylayout);
        calendar = Calendar.getInstance();
        dataHelper = new DataHelper(getApplicationContext());

        GraphView graphView = getGraphView(null);

        LinearLayout container = (LinearLayout)findViewById(R.id.graphViewContainer);
        container.addView(graphView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private GraphView getGraphView(Date date){
        if(date!=null)
            calendar.setTime(date);

        GraphView.GraphViewData[] monthdata_G = getGraphDataSet();

        //initilize const days to read
        days_in_month = new int[31];
        for (int i=1;i<=MAX_DATE_IN_MONTH;i++){
            days_in_month[i-1] = i;
        }

        GraphViewSeries monthSeries = new GraphViewSeries(monthdata_G);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMMMMM yyyy");

        int totalDaysInMonth = monthdata_G.length;
        String[] dateLabels = new String[totalDaysInMonth];
        for (int i = 0; i < totalDaysInMonth;i++){
            dateLabels[i] = String.valueOf(i+1);
        }

        GraphView graphView = new LineGraphView(getBaseContext(),dateFormat.format(calendar.getTime()));
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isX) {
                if(isX){
                    return Arrays.binarySearch(days_in_month,(int)value)!=-1? String.valueOf((int) value) : "";
                }
                else return null;
            }
        });

        graphView.addSeries(monthSeries);
        // Personally I think this method can be written in a better way...
        // Eg. Associate each label to a pair of data.
        //graphView.setHorizontalLabels(dateLabels);
        graphView.getGraphViewStyle().setNumHorizontalLabels(8);
        graphView.setViewPort(1,7);
        graphView.setScrollable(true);
        //graphView.setScalable(true);

        return graphView;
    }

    /**
     * Get the corresponding dataset for a specific year-month
     * Indexed by days in the month. For example arr[0] -> (1st, value), arr[1] -> (2nd, value) ...arr[n-1] -> (last day, value)
     * @return the corresponding graph series array
     */
    private GraphView.GraphViewData[] getGraphDataSet(){
        int maximumDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        GraphView.GraphViewData[] monthdata_G = new GraphView.GraphViewData[maximumDaysInMonth];

        //initialize everyday to 0.0
        for (int day=1;day<=maximumDaysInMonth;day++){
            monthdata_G[day-1] = new GraphView.GraphViewData(day,0.0);
        }
        //get all datarows for selected month from DB
        ArrayList<DataTableRow> dataRowsForThisMonth = dataHelper.getAllRowsInMonth(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
        if(dataRowsForThisMonth!=null) {

           /* for (int i = 0; i < dataRowsForThisMonth.size(); i++) {
                DataTableRow currentDay = dataRowsForThisMonth.get(i);

                monthdata_G[currentDay.getDate()-1] = new GraphView.GraphViewData(
                        currentDay.getDate(),
                        currentDay.getspentValue()
                );
            }*/

            for (DataTableRow currentDay : dataRowsForThisMonth){
                monthdata_G[currentDay.getDate()-1] = new GraphView.GraphViewData(
                        currentDay.getDate(),
                        currentDay.getspentValue()
                );
            }
        }
        return monthdata_G;
    }


}