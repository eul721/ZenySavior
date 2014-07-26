package com.example.ZenySavior;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.ZenySavior.HomeActivityTest \
 * com.example.ZenySavior.tests/android.test.InstrumentationTestRunner
 */
public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    private HomeActivity pActivity;
    private List<NumberPicker> pNumpicks;
    private TextView pTodaySpendings;

    public HomeActivityTest() {
        super("com.example.ZenySavior", HomeActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        pActivity = getActivity();
        pNumpicks = pActivity.getNumberPickers();
        pTodaySpendings = (TextView)pActivity.findViewById(com.example.ZenySavior.R.id.dailySpendingsLabel);


    }
    public void testPreCons(){
        for(int i=0;i<pNumpicks.size();i++){
            NumberPicker numpic = pNumpicks.get(i);
            assertEquals("Numberpicker["+i+"] should be defaulted to 0",numpic.getValue(),0);
        }
        assertEquals("Label should be defaulted",pTodaySpendings.getText(),"Today's Spendings: $0.0");
    }

    @UiThreadTest
    public void testInput(){
        //Value: $1234.56
        int[] value = new int[]{1,2,3,4,5,6};
        for (int i = 0; i < pNumpicks.size(); i++) {
            NumberPicker numpic = pNumpicks.get(i);
            numpic.setValue(value[i]);
        }
        assertEquals("inputs should work",pActivity.getInputValue(),1234.56);
    }


}
