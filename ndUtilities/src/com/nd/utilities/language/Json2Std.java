package com.nd.utilities.language;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.utilities.language.MapHelper;

/**
 * 對json形式的參數進行解析
 * 
 * @author apple
 * 
 */
public class Json2Std {
	private Map<String, Object> mResultMap = new HashMap<String, Object>();
	private List<Object> mResultArray = new ArrayList<Object>();
	private JSONObject mJson;
	private boolean mParseFirstLevel = false;
	private String mSkipKey;

	public String getSkipKey() {
		return mSkipKey;
	}
	public void setSkipKey(String skipKey) {
		mSkipKey = skipKey;
	}

	public boolean onlyParseFirstLevel() {
		return mParseFirstLevel;
	}

	public void setParseFirstLevel(boolean parseFirstLevel) {
		mParseFirstLevel = parseFirstLevel;
	}

	/**
	 * 
	 * 只解析第一級
	 * @param json
	 * @param parseFirstLevel
	 */
	public Json2Std(String json, boolean parseFirstLevel) {
		if (TextUtils.isEmpty(json))
			return;
		setParseFirstLevel(parseFirstLevel);
		parseData(json);
	}

	/**
	 * 
	 * 解析到此key時，此key中的內容不再往下解析
	 * @param json
	 * @param skipKey
	 */
	public Json2Std(String json, String skipKey) {
		if (TextUtils.isEmpty(json))
			return;
		setSkipKey(skipKey);
		parseData(json);
	}

	public Json2Std(String json) {
		if (TextUtils.isEmpty(json))
			return;
		parseData(json);
	}
	private void parseData(String data) {
		try {
			mJson = new JSONObject(data);
			if (mParseFirstLevel)
				parseFirstLevel(mJson);
			else
				mResultMap = parseData(mJson);
		} catch (JSONException e) {
			try {
				JSONArray array = new JSONArray(data);
				if (mParseFirstLevel)
					parseFirstLevel(array);
				else
					mResultArray = parseArray(array);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void parseFirstLevel(JSONObject data) {
		try {
			Iterator<String> keys = data.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = data.get(key);
				mResultMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseFirstLevel(JSONArray data) {
		try {
			int length = data.length();
			for (int i = 0; i < length; i++) {
				mResultArray.add(data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getData() {
		if (mJson != null)
			return mJson.toString();
		return "";
	}

	public void addValue(String key, Object value) {
		mResultMap.put(key, value);
	}

	public JSONObject getJsonData() {
		return mJson;
	}

	public List<Object> getResultArray() {
		return mResultArray;
	}

	public Map<String, Object> getResultMap() {
		return mResultMap;
	}

	public boolean hasKey(String key) {
		return mResultMap.containsKey(key);
	}

	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @return
	 */
	public List<Object> getListByKey(String key) {
		return MapHelper.getListByKey(mResultMap, key);
	}

	/**
	 * key的形式 從外層key到指定曾 ,以"."隔開，如 a.b.c
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, Object> getMapByKey(String key) {
		return MapHelper.getMapByKey(mResultMap, key);
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
	public String getStringValueByKey(String key, String def) {
		return MapHelper.getStringValueByKey(mResultMap, key, def);
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
	public int getIntValueByKey(String key, int def) {
		return MapHelper.getIntValueByKey(mResultMap, key, def);
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
	public float getFloatValueByKey(String key, float def) {
		return MapHelper.getFloatValueByKey(mResultMap, key, def);
	}

	protected Map<String, Object> parseData(JSONObject p_data) {
		Map<String, Object> result = new HashMap<String, Object>();
		Iterator<String> keys = p_data.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if (!TextUtils.isEmpty(mSkipKey)
					&& key.equalsIgnoreCase(mSkipKey)) {
				try {
					result.put(key, p_data.getString(key));
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} else {
				try {
					JSONArray array = p_data.getJSONArray(key);
					result.put(key, parseArray(array));
					// Logger.i(getClass(), "array key:"+key);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					try {
						JSONObject tmp = p_data.getJSONObject(key);
						result.put(key, parseData(tmp));
						// Logger.i(getClass(), "object key:"+key);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						try {
							result.put(key, p_data.getString(key));
							// Logger.i(getClass(), "string key:"+key);
						} catch (JSONException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}
			}
		}
		return result;
	}

	private List<Object> parseArray(JSONArray p_data) {
		List<Object> result = new ArrayList<Object>();
		for (int i = 0; i < p_data.length(); i++) {
			try {
				result.add(parseArray(p_data.getJSONArray(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				try {
					result.add(parseData(p_data.getJSONObject(i)));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					try {
						result.add(p_data.getString(i));
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
		}
		return result;
	}
	public Map<String, Object> getDataMap() {
		return mResultMap;
	}
}
