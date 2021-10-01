package com.android.base.rxjava;

import com.android.base.rxjava.rxkit.RxBus;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-18 16:39
 */
public class RxBusTest {

    private RxBus mRxBus = RxBus.newInstance();

    private A mA;
    private A mA1;
    private B mB;
    private B mB1;
    private C mC;
    private C mC1;

    private static class A {

        final String name;

        private A(String name) {
            this.name = "A  " + name;
        }

        @NotNull
        @Override
        public String toString() {
            return "A{" + "name='" + name + '\'' + '}'; }
    }

    private static class C extends A {
        private C(String name) {
            super(name);
        }

        @NotNull
        @Override
        public String toString() {
            return "C {" + "name='" + name + '\'' + '}';
        }
    }

    private static class B {
        private final String name;

        private B(String name) {
            this.name = "B  " + name;
        }

        @NotNull
        @Override
        public String toString() {
            return "B{" + "name='" + name + '\'' + '}';
        }
    }


    @Before
    public void start() {

        mRxBus.toObservable(A.class).subscribe(a -> {
            System.out.println(a);
            mA = a;
        });

        mRxBus.toObservable("A", A.class)
                .subscribe(a -> {
                    System.out.println(" has id " + a);
                    mA1 = a;
                });

        mRxBus.toObservable(B.class).subscribe(b -> {
            System.out.println(b);
            mB = b;
        });

        mRxBus.toObservable("B", B.class)
                .subscribe(b -> {
                    System.out.println(" has id " + b);
                    mB1 = b;
                });

        mRxBus.toObservable(C.class).subscribe(c -> {
            System.out.println(c);
            mC = c;
        });

        mRxBus.toObservable("C", C.class)
                .subscribe(c -> {
                    System.out.println(" has id " + c);
                    mC1 = c;
                });
    }

    @Test
    public void send() {
        A a = new A("A01");
        mRxBus.send(a);
        Assert.assertSame(a, mA);
        Assert.assertNotSame(a, mA1);

        B b = new B("B01");
        mRxBus.send("B", b);
        Assert.assertNotSame(b, mB);
        Assert.assertSame(b, mB1);

        C c = new C("C01");
        mRxBus.send("C", c);
        Assert.assertNotSame(c, mC);
        Assert.assertSame(c, mC1);

        mRxBus.send("A", c);
        mRxBus.send(c);
    }

}