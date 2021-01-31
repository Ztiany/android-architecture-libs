package me.ztiany.widget.insets;

import android.content.Context;
import android.util.AttributeSet;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-04-18 18:31
 */
public class NoInsetsCoordinatorLayout extends CoordinatorLayout {

    {
        try {
            Field mApplyWindowInsetsListener = CoordinatorLayout.class.getDeclaredField("mApplyWindowInsetsListener");
            mApplyWindowInsetsListener.setAccessible(true);
            mApplyWindowInsetsListener.set(this, (OnApplyWindowInsetsListener) (view, windowInsetsCompat) -> null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public NoInsetsCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public NoInsetsCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoInsetsCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
