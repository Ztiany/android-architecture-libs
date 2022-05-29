package com.android.base.utils.android.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.base.utils.BaseUtils;

import java.lang.reflect.Method;

import timber.log.Timber;

/**
 * modified from <a href='https://github.com/hongyangAndroid/SpCache'>hongyangAndroid/SpCache</a>.
 */
@SuppressWarnings("unused")
public class SpCache {

    private static final String TAG = SpCache.class.getSimpleName();

    private final SharedPreferences mSharedPreferences;
    private final boolean mUseApply;

    public SpCache(String prefFileName) {
        this(BaseUtils.getAppContext(), prefFileName, true);
    }

    public SpCache(String prefFileName, boolean useApply) {
        this(BaseUtils.getAppContext(), prefFileName, useApply);
    }

    public SpCache(Context context, String prefFileName, boolean useApply) {
        if (TextUtils.isEmpty(prefFileName)) {
            throw new NullPointerException("SpCache get fileName = null");
        }
        mSharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        mUseApply = useApply;
    }

    //put
    public SpCache putInt(String key, int val) {
        return put(key, val);
    }

    public SpCache putLong(String key, long val) {
        return put(key, val);
    }

    public SpCache putString(String key, String val) {
        return put(key, val);
    }

    public SpCache putBoolean(String key, boolean val) {
        return put(key, val);
    }

    public SpCache putFloat(String key, float val) {
        return put(key, val);
    }

    //get
    public int getInt(String key, int defaultVal) {
        return (int) (get(key, defaultVal));
    }

    public long getLong(String key, long defaultVal) {
        return (long) (get(key, defaultVal));
    }

    public String getString(String key, String defaultVal) {
        return (String) (get(key, defaultVal));
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        return (boolean) (get(key, defaultVal));
    }

    public float getFloat(String key, float defaultVal) {
        return (float) (get(key, defaultVal));
    }

    //contains
    public boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    //remove
    public SpCache remove(String key) {
        return _remove(key);
    }

    private SpCache _remove(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor, mUseApply);
        return this;
    }

    //clear
    public SpCache clear() {
        return _clear();
    }

    private SpCache _clear() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor, mUseApply);
        return this;
    }

    private <T> SpCache put(String key, T t) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if (t instanceof String) {
            editor.putString(key, (String) t);
        } else if (t instanceof Integer) {
            editor.putInt(key, (Integer) t);
        } else if (t instanceof Boolean) {
            editor.putBoolean(key, (Boolean) t);
        } else if (t instanceof Float) {
            editor.putFloat(key, (Float) t);
        } else if (t instanceof Long) {
            editor.putLong(key, (Long) t);
        } else {
            Timber.d("you may be put a invalid object :%s", t.toString());
            editor.putString(key, t.toString());
        }
        SharedPreferencesCompat.apply(editor, mUseApply);
        return this;
    }


    private Object readDisk(String key, Object defaultObject) {
        Timber.e("readDisk");
        SharedPreferences sp = getSharedPreferences();

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        Timber.e("you can not read object , which class is %s", defaultObject.getClass().getSimpleName());
        return null;

    }

    private Object get(String key, Object defaultVal) {
        return readDisk(key, defaultVal);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(final SharedPreferences.Editor editor, boolean useApply) {
            if (useApply) {
                try {
                    if (sApplyMethod != null) {
                        sApplyMethod.invoke(editor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    editor.commit();
                }
            } else {
                editor.commit();
            }
        }
    }

    private SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

}