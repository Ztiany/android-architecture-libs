package com.android.base.concurrent;

import java.util.concurrent.Executor;


/**
 * Smart执行器
 *
 * @see <a href="https://github.com/litesuits/android-lite-go">android-lite-go</a>
 */
public class JobExecutor {

    private static SmartExecutor smartExecutor;

    public static void setSchedulePolicy(SchedulePolicy policy) {
        if (smartExecutor != null) {
            smartExecutor.setSchedulePolicy(policy);
        }
    }

    public static void setOverloadPolicy(OverloadPolicy policy) {
        if (smartExecutor != null) {
            smartExecutor.setOverloadPolicy(policy);
        }
    }

    static {
        smartExecutor = new SmartExecutor();
        smartExecutor.setSchedulePolicy(SchedulePolicy.LastInFirstRun);
        smartExecutor.setOverloadPolicy(OverloadPolicy.DiscardOldTaskInQueue);
    }

    public static Executor getJobExecutor() {
        return smartExecutor;
    }

}
