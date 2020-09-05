package com.sanzhi.work.util.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author : parade
 * date : 2020/9/4
 * description :
 */
class KtxLifeCycleCallBack: Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityLifcycleManager.pushActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityLifcycleManager.popActivity(activity)
    }

}