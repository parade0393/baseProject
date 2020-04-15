package com.sanzhi.work.util.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by parade岁月 on 2019/8/21 9:23
 */
public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

    public static boolean isRegistered(Object subscriber){
      return EventBus.getDefault().isRegistered(subscriber);
    }
}
