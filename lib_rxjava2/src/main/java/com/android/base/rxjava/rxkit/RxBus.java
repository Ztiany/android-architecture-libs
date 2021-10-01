package com.android.base.rxjava.rxkit;


import androidx.annotation.NonNull;

import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;


/**
 * 不要跨大范围的使用 RxBus，比较推荐的模式为针对特定模块定义特定的事件分发类：
 * <pre>
 *     {@code
 *      public class FooEvents{
 *
 *                  public Observable<FooEvents> subscribeFooEvents(){
 *                             return RxBus.getDefault().toObservable(FooEvents.class);
 *                  }
 *
 *                  public void sendFooEvent(FooEvents event){
 *                            RxBus.getDefault().send(event);
 *                  }
 *              }
 *     }
 * </pre>
 */
public class RxBus {

    private static final RxBus BUS = new RxBus();

    public static RxBus getDefault() {
        return BUS;
    }

    public static RxBus newInstance() {
        return new RxBus();
    }

    //All other Publisher and Subject methods are thread-safe by design.
    private static final PublishProcessor<Object> mBus = PublishProcessor.create();

    private final String COMMON_EVENT_IDENTIFY;

    private RxBus() {
        COMMON_EVENT_IDENTIFY = UUID.randomUUID().toString();
    }

    public boolean hasObservers() {
        return mBus.hasSubscribers();
    }

    public void send(@NonNull final Object event) {
        mBus.onNext(new ObjectHolder(COMMON_EVENT_IDENTIFY, event));
    }

    public <T> Flowable<T> toObservable(Class<T> tClass) {
        return toObservable(COMMON_EVENT_IDENTIFY, tClass);
    }

    public void send(@NonNull String identify, @NonNull final Object event) {
        mBus.onNext(new ObjectHolder(identify, event));
    }

    public <T> Flowable<T> toObservable(@NonNull final String identify, final Class<T> tClass) {
        return mBus.ofType(ObjectHolder.class)
                .filter(objectHolder -> objectHolder.identify.equals(identify) && tClass == objectHolder.event.getClass())
                .map(objectHolder -> tClass.cast(objectHolder.event));
    }

    private static class ObjectHolder {

        private final String identify;
        private final Object event;

        ObjectHolder(String identify, Object event) {
            this.identify = identify;
            this.event = event;
        }

    }

}