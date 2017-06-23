# SharedPreferencesDemo
SharedPreferences Demo 记住密码
Android存储方式有使用SharedPreferences存储数据；文件存储数据；SQLite数据库存储数据；使用ContentProvider存储数据；网络存储数据。

首先说明SharedPreferences存储方式，它是 Android提供的用来存储一些简单配置信息的一种机制，例如：登录用户的用户名与密码。其采用了Map数据结构来存储数据，以键值的方式存储，可以简单的读取与写入。SharedPreferences存储文件的目录是/data/data/Package Name/Shared_Pref,安卓手机root后可以查看。

1、得到SharedPreferences对象
2、调用SharedPreferences对象的edit()方法来获取一个SharedPreferences.Editor对象。
3、向SharedPreferences.Editor对象中添加数据。
4、调用commit方法将添加的数据提交。

public SharedPreferences getSharedPreferences(String name, int mode) {
        return mBase.getSharedPreferences(name, mode);
    }

name为本组件的配置文件名( 自己定义，也就是一个文件名)
mode为操作模式，默认的模式为0或MODE_PRIVATE，还可以使用MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE

mode指定为MODE_PRIVATE，则该配置文件只能被自己的应用程序访问。
mode指定为MODE_WORLD_READABLE，则该配置文件除了自己访问外还可以被其它应该程序读取。
mode指定为MODE_WORLD_WRITEABLE，则该配置文件除了自己访问外还可以被其它应该程序读取和写入

现在比较通用的是将SharePreferences的使用封装成一个工具类SPUtil，代码如下：

package com.ada.packageapplication;

import android.content.Context;
import android.content.SharedPreferences;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Ada on 2017/6/20.
 */

public class SPUtil {
    private static String FILE_NAME="Ada_SharePreferences";

    private SPUtil() {
        throw new Error("Do not need instantiate");
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


使用时直接调用SPUtil进行数据的存储和读取

SPUtil.saveData(this,"isRemember",cbxRemember.isChecked());
SPUtil.saveData(this,"userName",userName);
SPUtil.saveData(this,"password",password);

boolean isRemember= (boolean) SPUtil.getData(this,"isRemember",false);
String userName= (String) SPUtil.getData(this,"userName","");
String password= (String) SPUtil.getData(this,"password","");
