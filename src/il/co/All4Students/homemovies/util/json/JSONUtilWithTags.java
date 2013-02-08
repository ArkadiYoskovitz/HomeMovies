package il.co.All4Students.homemovies.util.json;

/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */

import android.annotation.SuppressLint;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * JSONUtilWithTags allows converting an object into a JSONObject with TAGs so
 * that we can parse the JSON in a standard way
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-05
 */

@SuppressLint("DefaultLocale")
public class JSONUtilWithTags {

	public static String object2json(Object obj, String objetTag) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("{");
			json.append("\"").append(objetTag).append("\"").append(" : ");
			json.append("\"").append(string2json(obj.toString())).append("\"");
			json.append("}");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj, objetTag));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj, objetTag));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj, objetTag));
		}
		return json.toString();
	}

	public static String array2json(Object[] array, String arrayTag) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj, arrayTag));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String list2json(List<?> list, String listTag) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj, listTag));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(JSONUtil.object2json(key));
				json.append(":");
				json.append(JSONUtil.object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	public static String set2json(Set<?> set, String setTag) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj, setTag));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	@SuppressLint("DefaultLocale")
	public static String string2json(String string) {
		if (string == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase(Locale.US));
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}