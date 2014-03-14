package com.nd.utilities.css;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.nd.utilities.logger.Logger;

public class CssParser {
	private List<Rule> m_rules = new ArrayList<Rule>();

	public void parserCss(String css) {
		if (TextUtils.isEmpty(css.trim()))
			return;
		// 去除Css中的注释
		css = css
				.replaceAll(
						"\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*\\/",
						"");

		// 將Css切割成屬性塊
		String[] blocks = css.split("\\}");
		for (String block : blocks) {
			block = block.trim();
			if (TextUtils.isEmpty(block))
				continue;
			Rule rule = new Rule();
			rule.parserCssRule(block);
			m_rules.add(rule);
		}
	}

	public void parserFile(String path) {
		parserCss(readFile(path));
	}

	public void parserJson(String css) {
		if (TextUtils.isEmpty(css.trim()))
			return;
		try {
			JSONObject json = new JSONObject(css);
			JSONArray names = json.names();
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				Rule rule = new Rule();
				rule.parserJson(name, json.getString(name));
				m_rules.add(rule);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String readFile(String path) {
		try {
			File file = new File(path);
			if (file.isFile() && file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String str = "";
				String r = br.readLine();
				while (r != null) {
					str += r;
					r = br.readLine();
				}
				br.close();
				return str;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public List<Rule> getRule() {
		return m_rules;
	}
	
	public static String convertMap2Css(Map<String, Object> style){
		Logger.d(CssParser.class, "before convert:"+style);
		StringBuffer result=new StringBuffer();
		if(style!=null && style.size()!=0){
			Set<String> set=style.keySet();
			Iterator<String> keys=set.iterator();			
			while(keys.hasNext()){
				String key=keys.next();
				result.append(key).append("{");
				Map<String, Object> item=(Map<String, Object>) style.get(key);
				Iterator<String> itemKeys=item.keySet().iterator();
				while(itemKeys.hasNext()){
					String itemKey=itemKeys.next();
					result.append(itemKey).append(":")
						.append(item.get(itemKey)).append(";");
				}
				result.append("}");
			}
		}
		Logger.d(CssParser.class, result.toString());
		return result.toString();
	}
}
