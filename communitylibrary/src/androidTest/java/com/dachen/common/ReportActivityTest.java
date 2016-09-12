package com.dachen.common;

import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;

import com.dachen.community.activity.ReportActivity;
import com.dachen.community.views.GridRadioGroup;

import java.util.Arrays;

/**
 * Created by pqixi on 2016/9/12 0012.
 */
public class ReportActivityTest extends ActivityInstrumentationTestCase2<ReportActivity> {

    private ReportActivity activity;

    private boolean isQueit;

    private int method = 0;

    public ReportActivityTest() {
        super(ReportActivity.class);
    }

    public ReportActivityTest(Class<ReportActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testString() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setTitleText("monkey", "jsdafhkadhkfjasfjdak fjkdhasjkdfhskajfhsjk");
            }
        });
        end();
    }

    public void testRadioButton() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GridRadioGroup mRadioGroup = (GridRadioGroup) activity.findViewById(com.dachen.community.R.id.rg_radiogroups);
                mRadioGroup.setmSelects(Arrays.asList(new String[]{"选项1", "选项1", "选项1", "选项1", "选项1", "选项1", "选项1", "选项1", "选项1"}));
            }
        });
        end();
    }


    public void end() {
        method++;
        if (method == 2) {
            SystemClock.sleep(30000);
        }
    }


//    public void testEnd() {
//        SystemClock.sleep(30000);
//    }
}
