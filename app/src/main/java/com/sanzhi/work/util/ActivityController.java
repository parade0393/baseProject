package com.sanzhi.work.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/***
 *author: parade岁月
 *date:  2020/4/24 11:17
 *description：
 */
public class ActivityController {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        if (!activities.contains(activity))
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
