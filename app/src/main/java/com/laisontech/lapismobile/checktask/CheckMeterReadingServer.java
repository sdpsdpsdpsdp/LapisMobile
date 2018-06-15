package com.laisontech.lapismobile.checktask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ..................................................................
 * .         The Buddha said: I guarantee you have no bug!          .
 * .                                                                .
 * .                            _ooOoo_                             .
 * .                           o8888888o                            .
 * .                           88" . "88                            .
 * .                           (| -_- |)                            .
 * .                            O\ = /O                             .
 * .                        ____/`---'\____                         .
 * .                      .   ' \\| |// `.                          .
 * .                       / \\||| : |||// \                        .
 * .                     / _||||| -:- |||||- \                      .
 * .                       | | \\\ - /// | |                        .
 * .                     | \_| ''\---/'' | |                        .
 * .                      \ .-\__ `-` ___/-. /                      .
 * .                   ___`. .' /--.--\ `. . __                     .
 * .                ."" '< `.___\_<|>_/___.' >'"".                  .
 * .               | | : `- \`.;`\ _ /`;.`/ - ` : | |               .
 * .                 \ \ `-. \_ __\ /__ _/ .-` / /                  .
 * .         ======`-.____`-.___\_____/___.-`____.-'======          .
 * .                            `=---='                             .
 * ..................................................................
 * Created by SDP on 2018/6/15.
 */

public class CheckMeterReadingServer extends Service {
    private static final String ACTION_START_BUILD_CHECK_TASK = "com.laisontech.server.check_task";
    private static final String ACTION_STOP_BUILD_CHECK_TASK = "com.laisontech.server.stop_task";
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && !action.isEmpty()) {
                switch (action) {
                    case ACTION_START_BUILD_CHECK_TASK:
                        openCheckTimer();
                        break;
                    case ACTION_STOP_BUILD_CHECK_TASK:
                        closeTimer();
                        break;
                }
            }
        }
        return START_STICKY;
    }


    //开启一个定时器
    private void openCheckTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().postSticky(new CheckTaskMessage("startTimer" + System.currentTimeMillis()));
            }
        }, 0, 1000);
    }

    private void closeTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public static void startCheckServer(Context context) {
        Intent intent = new Intent(context, CheckMeterReadingServer.class);
        intent.setAction(ACTION_START_BUILD_CHECK_TASK);
        context.startService(intent);
    }

    public static void stopCheckServer(Context context) {
        Intent intent = new Intent(context, CheckMeterReadingServer.class);
        intent.setAction(ACTION_STOP_BUILD_CHECK_TASK);
        context.stopService(intent);
    }
}
