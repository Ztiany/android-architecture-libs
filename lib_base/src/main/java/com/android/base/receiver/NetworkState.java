package com.android.base.receiver;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

public enum NetworkState {

    STATE_WIFI,
    STATE_GPRS,
    STATE_NONE;

    private static final BehaviorProcessor<NetworkState> PROCESSOR = BehaviorProcessor.create();

    public static Flowable<NetworkState> observableState() {
        return PROCESSOR;
    }

    public boolean isConnected() {
        return this != STATE_NONE;
    }

    static void notify(NetworkState networkState) {
        PROCESSOR.onNext(networkState);
    }


}
