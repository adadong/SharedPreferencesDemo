package com.ada.packageapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Ada on 2017/6/20.
 */
public class SP_Util {
    private static String FILE_NAME="Ada_SharePreferences";

    private SP_Util() {
        throw new Error("Do not need instantiate");
    }

    public void setFILE_NAME(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }

    /**
     *保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param value
     */
    public static void saveData(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if(value instanceof Boolean) {
            edit.putBoolean(key, ((Boolean)value).booleanValue());
        } else if(value instanceof String) {
            edit.putString(key, (String)value);
        } else if(value instanceof Integer) {
            edit.putInt(key, ((Integer)value).intValue());
        } else if(value instanceof Float) {
            edit.putFloat(key, ((Float)value).floatValue());
        } else if(value instanceof Long) {
            edit.putLong(key, ((Long)value).longValue());
        }

        SP_Util.SharedPreferencesCompat.apply(edit);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getData(Context context, String key, Object defaultObject)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     * @param context
     * @param key
     */
    public static void removeData(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        SP_Util.SharedPreferencesCompat.apply(edit);
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clearData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        SP_Util.SharedPreferencesCompat.apply(edit);
    }

    /**
     * 查询某个key是否已经存在
     * @param context
     * @param key
     * @return
     */
    public static boolean containsData(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     *
     */
    private static class SharedPreferencesCompat {
        private static final Method mApplyMethod = findApplyMethod();

        private SharedPreferencesCompat() {
        }

        private static Method findApplyMethod() {
            try {
                Class e = SharedPreferences.Editor.class;
                return e.getMethod("apply", new Class[0]);
            } catch (Exception var1) {
                var1.printStackTrace();
                return null;
            }
        }

        public static void apply(SharedPreferences.Editor editor) {
            try {
                if(mApplyMethod != null) {
                    mApplyMethod.invoke(editor, new Object[0]);
                    return;
                }
            } catch (IllegalAccessException var2) {
                var2.printStackTrace();
            } catch (InvocationTargetException var3) {
                var3.printStackTrace();
            }

            editor.commit();
        }
    }

}
