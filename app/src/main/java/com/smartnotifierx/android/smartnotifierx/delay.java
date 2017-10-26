package com.smartnotifierx.android.smartnotifierx;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by sv300_000 on 11-Oct-17.
 */

public class delay extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        SystemClock.sleep(2000);
    }
}
