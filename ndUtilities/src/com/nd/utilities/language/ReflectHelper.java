package com.nd.utilities.language;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import com.nd.utilities.logger.Logger;

import android.net.Uri;
import android.text.TextUtils;

public class ReflectHelper {
	/**
	 * 反射取得方法
	 * 
	 * @param clazz
	 * @param name
	 * @param argClasses
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String name,
			Class<?>... argClasses) {
		try {
			return clazz.getMethod(name, argClasses);
		} catch (NoSuchMethodException nsme) {
			// OK
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}

	/**
	 * 
	 * @param method
	 * @param instance
	 * @param args
	 * @return
	 */
	public static Object invoke(Method method, Object instance, Object... args) {
		try {
			Logger.i(ReflectHelper.class, "method:" + method);
			return method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return null;
		}
	}
}
