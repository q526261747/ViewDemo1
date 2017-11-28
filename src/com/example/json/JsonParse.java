package com.example.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParse {
	public static List<String> Parse(String jsonstr) throws JSONException{
		List<String> list = new ArrayList<String>();
		JSONObject json1 = new JSONObject(jsonstr);
		String key = json1.getString("serverinfo");
		JSONObject json2 = new JSONObject(key);
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = json2.keys();
		while(iterator.hasNext()){
			String value = json2.getString(iterator.next());
			list.add(value);
		}
		return list;
	}
}
