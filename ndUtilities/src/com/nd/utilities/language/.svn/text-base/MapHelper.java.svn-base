package com.nd.utilities.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

public class MapHelper {
	private static final String TAG="MapHelper";
	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @return
	 */
	public static List<Object> getListByKey(Map<String, Object> map, String key) {
		List<Object> result = new ArrayList<Object>();
		try {
			String[] keys = key.split("\\.");
			for (int i = 0; i < keys.length; i++) {
				if (i != keys.length - 1) {
					if (map.containsKey(keys[i]))
						map = (Map<String, Object>) map.get(keys[i]);
					else
						return result;
				} else {
					if (map.containsKey(keys[i]))
						return (List<Object>) map.get(keys[i]);
					else
						return result;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "get value error:" + key);
		}
		return result;
	}

	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, Object> getMapByKey(Map<String, Object> map,
			String key) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String[] keys = key.split("\\.");
			for (int i = 0; i < keys.length; i++) {
				if (i != keys.length - 1) {
					if (map.containsKey(keys[i]))
						map = (Map<String, Object>) map.get(keys[i]);
					else
						return result;
				} else {
					if (map.containsKey(keys[i]))
						return (Map<String, Object>) map.get(keys[i]);
					else
						return result;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "get value error:" + key);
		}
		return result;
	}

	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @param def
	 *            default value,if not found the value of this key,return this
	 *            value
	 * @return
	 */
	public static String getStringValueByKey(Map<String, Object> map,
			String key, String def) {
		try {
			String[] keys = key.split("\\.");
			for (int i = 0; i < keys.length; i++) {
				if (i != keys.length - 1) {
					if (map.containsKey(keys[i]))
						map = (Map<String, Object>) map.get(keys[i]);
					else
						return def;
				} else {
					if (map.containsKey(keys[i])
							&& !TextUtils.isEmpty(map.get(keys[i]).toString()))
						return map.get(keys[i]).toString();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "get value error:" + key);
		}
		return def;
	}

	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @param def
	 *            default value,if not found the value of this key,return this
	 *            value
	 * @return
	 */
	public static int getIntValueByKey(Map<String, Object> map, String key,
			int def) {
		try {
			String[] keys = key.split("\\.");
			for (int i = 0; i < keys.length; i++) {
				if (i != keys.length - 1) {
					if (map.containsKey(keys[i]))
						map = (Map<String, Object>) map.get(keys[i]);
					else
						return def;
				} else {
					if (map.containsKey(keys[i]))
						return StringHelper.parseStringToInt(map.get(keys[i])
								.toString(), def);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "get value error:" + key);
		}
		return def;
	}

	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @param def
	 *            default value,if not found the value of this key,return this
	 *            value
	 * @return
	 */
	public static float getFloatValueByKey(Map<String, Object> map, String key,
			float def) {
		try {
			String[] keys = key.split("\\.");
			for (int i = 0; i < keys.length; i++) {
				if (i != keys.length - 1) {
					if (map.containsKey(keys[i]))
						map = (Map<String, Object>) map.get(keys[i]);
					else
						return def;
				} else {
					if (map.containsKey(keys[i])
							&& !TextUtils.isEmpty(map.get(keys[i]).toString()))
						return Float.parseFloat(map.get(keys[i]).toString());
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "get value error:" + key);
		}
		return def;
	}
}
