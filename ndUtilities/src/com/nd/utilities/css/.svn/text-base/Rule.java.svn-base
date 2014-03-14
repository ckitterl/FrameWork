package com.nd.utilities.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class Rule {
	private List<String> m_selectors = new ArrayList<String>();
	private List<String> m_propertys = new ArrayList<String>();
	private Map<String, String> m_values = new HashMap<String, String>();

	public void parserCssRule(String rule) {
		String[] tmp = rule.split("\\{");

		if (tmp.length == 2) {
			parserSelector(tmp[0]);
			parserPropertys(tmp[1]);
		} else {
			parserSelector(tmp[0]);
		}
	}

	public void parserJson(String selector, String json) {
		parserSelector(selector);
		try {
			JSONObject propertyJson = new JSONObject(json);
			JSONArray propertys = propertyJson.names();
			for (int i = 0; i < propertys.length(); i++) {
				String property = propertys.getString(i);
				String value = propertyJson.getString(property).trim();
				if (!TextUtils.isEmpty(value)) {
					m_propertys.add(property);
					m_values.put(property, value);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parserSelector(String str) {
		String[] selectors = str.trim().split(",");
		for (String selector : selectors) {
			selector = selector.trim();
			if (TextUtils.isEmpty(selector))
				continue;
			m_selectors.add(selector);
		}
	}

	private void parserPropertys(String str) {
		String[] propertys = str.trim().split(";");
		for (String property : propertys) {
			property = property.trim();
			if (TextUtils.isEmpty(property))
				continue;
			String[] tmp = property.split(":");
			if (tmp.length == 2) {
				m_propertys.add(tmp[0].trim());
				m_values.put(tmp[0].trim(), tmp[1].trim());
			}
		}
	}

	public List<String> getSelectors() {
		return m_selectors;
	}

	public List<String> getPropertys() {
		return m_propertys;
	}

	public String getPropertyValue(String property) {
		if (m_values.containsKey(property))
			return m_values.get(property);
		return "";
	}
}
