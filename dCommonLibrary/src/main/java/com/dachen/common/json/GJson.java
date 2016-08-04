package com.dachen.common.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LMC
 *
 */
public class GJson {

	private static final String TAG = GJson.class.getSimpleName();
	private static boolean DEBUG = false;

	public enum Mode {
		NOT, // 什么都不做
		TRIM // 删除两旁空格
	}

	private String jsonString = null;

	/**
	 * @param jsonObjectStringOrJsonArrayString
	 */
	public GJson(String jsonObjectStringOrJsonArrayString) {
		init(jsonObjectStringOrJsonArrayString);
	}

	public GJson(Object jsonObject) {
		init(jsonObject);
	}

	/**
	 * 字符串初始化(验证通过)
	 * 
	 * @param jsonObjectStringOrJsonArrayString
	 */
	private void init(String jsonObjectStringOrJsonArrayString) {
		//		if (isEmptyString(jsonString)) {
		//			throw new java.lang.NullPointerException( "字符串不能为空" );
		//		}
		this.jsonString = jsonObjectStringOrJsonArrayString;
	}

	/**
	 * 对象初始化(验证通过)
	 * 
	 * @param object
	 */
	private void init(Object object) {
		//		if (object == null) {
		//			throw new java.lang.NullPointerException( "对象不能为空指针" );
		//		}
		Gson gson = new Gson();
		init( gson.toJson(object) );
	}

	/**
	 * 设置调试开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 *
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 将String两旁空格删除(测试通过)
	 * 
	 */
	public static String trimString(String text) {
		if (isEmptyString(text)) {
			return text;
		} else {
			return text.trim();
		}
	}

	/**
	 * 将对象里的String属性删除两旁空格(测试通过)
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T trimObjectString(T entity, Class<T> clazz) {
		if (entity == null) {
			return null;
		}

		if (clazz == null) {
			return null;
		}

		// getFields()获得某个类的所有的公共（public）的字段，包括父类。 
		// getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，
		// 但是不包括父类的申明字段。 
		// 得到属性数组
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null) {
			return null;
		}

		int len = fields.length;
		//		if (D) LogUtil.e(TAG, "length:"+len);
		if (len <= 0) {
			return null;
		}

		for(int i = 0 ; i < len; i++) {

			Field field = fields[i];

			field.setAccessible(true); //设置些属性是可以访问的
			if (DEBUG) Log.w(TAG, "field name:"+field.getName());

			// 判断：是String类型，并且没有final修饰符
			if ( field.getType() == String.class ) {
				if (DEBUG) Log.w(TAG, "== String.class");
				if ( java.lang.reflect.Modifier.isFinal(field.getModifiers()) ) {
					if (DEBUG) Log.w(TAG, "== Final");
					continue;
				} else {
					if (DEBUG) Log.w(TAG, "!= Final");
					try {
						Object objectValue = field.get(entity);
						if (objectValue != null) {
							String value = objectValue.toString();
							if (isEmptyString(value) == false) {
								if (DEBUG) Log.w(TAG, "execute trim.....");
								value = value.trim(); // 删除两旁空格
								// 设置回去
								field.set(entity, value);
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (DEBUG) Log.w(TAG, "!= String.class");
				continue;
			}
		}

		return entity;
	}

	/**
	 * 判断键名是否存在(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isKeyExists(String keyName) {
		if (isEmptyString(keyName)) {
			return false;
		}

		try {

			JsonParser jsonParser = new JsonParser();

			JsonElement jsonElement = jsonParser.parse( toJsonString() );
			if (jsonElement == null) {
				return false;
			}

			// 将文本转成JsonObject
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject == null) {
				return false;
			}

			return jsonObject.has(keyName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 通过jsonObjectString和keyName得到JsonElement(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	private JsonElement getJsonElement(String jsonObjectString, String keyName) {

		if (isEmptyString(jsonObjectString)) {
			return null;
		}

		if (isEmptyString(keyName)) {
			return null;
		}

		try {

			JsonParser jsonParser = new JsonParser();

			JsonElement jsonElement = jsonParser.parse(jsonObjectString);
			if (jsonElement == null) {
				return null;
			}

			if (jsonElement.isJsonObject() == false) {
				return null;
			}

			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject == null) {
				return null;
			}

			JsonElement __jsonElement = jsonObject.get(keyName);

			return __jsonElement;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过keyName键名得到JsonElement元素(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public JsonElement getJsonElement(String keyName) {
		return getJsonElement(toJsonString(), keyName);
	}

	/**
	 * 判断键是否为Object类型(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isObject(String keyName) {

		JsonElement jsonElement = getJsonElement(toJsonString(), keyName);
		if (jsonElement == null) {
			return false;
		}

		return jsonElement.isJsonObject();
	}

	/**
	 * 判断本类是否为Object类型(测试通过)
	 * 
	 * @return
	 */
	public boolean isObject() {

		if (isEmptyString( toJsonString() )) {
			return false;
		}

		try {

			JsonParser jsonParser = new JsonParser();

			JsonElement jsonElement = jsonParser.parse( toJsonString() );

			return jsonElement.isJsonObject();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 判断键是否为Array类型(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isArray(String keyName) {

		JsonElement jsonElement = getJsonElement(toJsonString(), keyName);
		if (jsonElement == null) {
			return false;
		}

		return jsonElement.isJsonArray();
	}

	/**
	 * 判断本类是否为Array类型(测试通过)
	 * 
	 * @return
	 */
	public boolean isArray() {

		if (isEmptyString( toJsonString() )) {
			return false;
		}

		try {

			JsonParser jsonParser = new JsonParser();

			JsonElement jsonElement = jsonParser.parse( toJsonString() );

			return jsonElement.isJsonArray();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 判断键是否为Primitive类型(测试通过)
	 * 
	 * 什么是Primitive类型？
	 * 除了object array null的，其余的都是Primitive类型
	 * 也就是说string number boolean integer long float double是属于Primitive类型
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isPrimitive(String keyName) {

		JsonElement jsonElement = getJsonElement(toJsonString(), keyName);
		if (jsonElement == null) {
			return false;
		}

		return jsonElement.isJsonPrimitive();
	}

	/**
	 * 判断键是否为Null类型(测试通过)
	 * 
	 * 这类"Rows": null		就是null类型
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isNull(String keyName) {

		JsonElement jsonElement = getJsonElement(toJsonString(), keyName);
		if (jsonElement == null) {
			return false;
		}

		return jsonElement.isJsonNull();
	}

	/**
	 * 通过jsonObjectString和keyName得到JsonPrimitive(测试通过)
	 * 
	 * @param jsonObjectString
	 * @param keyName
	 * @return
	 */
	private JsonPrimitive getJsonPrimitive(String jsonObjectString, String keyName) {

		if (isEmptyString(jsonObjectString)) {
			return null;
		}

		if (isEmptyString(keyName)) {
			return null;
		}

		try {

			JsonParser jsonParser = new JsonParser();

			JsonElement jsonElement = jsonParser.parse(jsonObjectString);
			if (jsonElement == null) {
				return null;
			}

			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject == null) {
				return null;
			}

			JsonElement __jsonElement = jsonObject.get(keyName);

			// 判断此元素是否为Primitive
			if (__jsonElement.isJsonPrimitive() == false) {
				return null;
			}

			return __jsonElement.getAsJsonPrimitive();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过keyName键名得到JsonElement元素(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public JsonPrimitive getJsonPrimitive(String keyName) {
		return getJsonPrimitive(toJsonString(), keyName);
	}

	/**
	 * 判断keyName是否为string类型(测试通过)
	 * 
	 * 以下两种都是isString()  
	 *  "ResultMsg": "",
    	"ResultMsg2": "dddd",

    	"Rows": null 不是isString()

	 * @param keyName
	 * @return
	 */
	public boolean isString(String keyName) {

		JsonPrimitive jsonPrimitive = getJsonPrimitive(toJsonString(), keyName);
		if (jsonPrimitive == null) {
			return false;
		}

		return jsonPrimitive.isString();
	}

	/**
	 * 判断keyName是否为integer/long/float/double类型(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isNumber(String keyName) {

		JsonPrimitive jsonPrimitive = getJsonPrimitive(toJsonString(), keyName);
		if (jsonPrimitive == null) {
			return false;
		}

		return jsonPrimitive.isNumber();
	}

	/**
	 * 判断keyName是否为boolean类型 (测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isBoolean(String keyName) {

		JsonPrimitive jsonPrimitive = getJsonPrimitive(toJsonString(), keyName);
		if (jsonPrimitive == null) {
			return false;
		}

		return jsonPrimitive.isBoolean();
	}

	/**
	 * 判断keyName是否为float/double类型(测试通过)
	 * 
	 * gson.jar没有找到isDouble()相关方法,
	 * 解决方法：得到此值，转成字符串，查找是否含有.
	 * 如有.则为double，否则为long
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isDouble(String keyName) {

		if (isNumber(keyName) == false) { // 判断是否为number类型
			return false;
		}

		// 得JsonPrimitive
		JsonPrimitive jsonPrimitive = getJsonPrimitive(toJsonString(), keyName);
		if (jsonPrimitive == null) {
			return false;
		}

		// 取值
		String value = jsonPrimitive.getAsString();
		if (isEmptyString(value)) {
			return false;
		}

		// 查找从第二位开始是否含有.
		if (value.indexOf('.') > 1) {
			// 找到
			return true;
		}

		return false;
	}

	/**
	 * 判断keyName是否为integer/long类型(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean isLong(String keyName) {

		if (isNumber(keyName) == false) { // 判断是否为number类型
			return false;
		}

		return !isDouble(keyName);
	}

	/**
	 * 通过keyName键名获取JsonObject(验证通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public JsonObject getJsonObject(String keyName) {

		JsonElement jsonElement = getJsonElement(toJsonString(), keyName);
		if (jsonElement == null) {
			return null;
		}

		// 判断jsonElement是不是object类型
		if (jsonElement.isJsonObject() == false) {
			return null;
		}

		return jsonElement.getAsJsonObject();
	}

	/**
	 * 通过keyName键名获取JsonArray(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public JsonArray getJsonArray(String keyName) {

		JsonElement jsonElement = getJsonElement(toJsonString(), keyName);
		if (jsonElement == null) {
			return null;
		}

		// 判断jsonElement是不是array类型
		if (jsonElement.isJsonArray() == false) {
			return null;
		}

		return jsonElement.getAsJsonArray();
	}

	/**
	 * 得到值(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public String getString(String keyName) {

		JsonPrimitive jsonPrimitive = getJsonPrimitive(keyName);
		if (jsonPrimitive == null) {
			return null;
		}

		if (jsonPrimitive.isString() == false) {
			return null;
		}

		return jsonPrimitive.getAsString();
	}

	/**
	 * 得到值(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public long getLong(String keyName) {

		if (isLong(keyName) == false) {
			return 0L;
		}

		JsonPrimitive jsonPrimitive = getJsonPrimitive(keyName);
		if (jsonPrimitive == null) {
			return 0L;
		}

		return jsonPrimitive.getAsLong();
	}

	/**
	 * 得到值(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public double getDouble(String keyName) {

		if (isDouble(keyName) == false) {
			return 0.0D;
		}

		JsonPrimitive jsonPrimitive = getJsonPrimitive(keyName);
		if (jsonPrimitive == null) {
			return 0.0D;
		}

		return jsonPrimitive.getAsDouble();
	}

	/**
	 * 得到值(测试通过)
	 * 
	 * @param keyName
	 * @return
	 */
	public boolean getBoolean(String keyName) {

		if (isBoolean(keyName) == false) {
			return false;
		}

		JsonPrimitive jsonPrimitive = getJsonPrimitive(keyName);
		if (jsonPrimitive == null) {
			return false;
		}

		return jsonPrimitive.getAsBoolean();
	}

	/**
	 *
	 * @return
	 */
	public boolean isJson() {
		return isJson( toJsonString() );
	}

	/**
	 * 返回构造函数传输进去的Json字符串(验证通过)
	 * 
	 * @return
	 */
	public String toJsonString() {
		return this.jsonString;
	}

	/**
	 * 判断string是否为标准的JSON格式 (验证通过)
	 * 
	 * @param jsonObjectStringOrjsonArrayString
	 * @return
	 */
	public static boolean isJson(String jsonObjectStringOrjsonArrayString) {
		if (isEmptyString(jsonObjectStringOrjsonArrayString)) {
			return false;
		}

		boolean isJson = false;

		try {

			JsonParser jsonParser = new JsonParser();

			JsonElement jsonElement = jsonParser.parse(jsonObjectStringOrjsonArrayString);
			if (jsonElement != null) {
				isJson = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isJson;
	}

	/**
	 *
	 * @param Object
	 * @return
	 */
	public static String toJsonString(Object Object) {
		if (Object == null) {
			return null;
		}
		Gson gson = new Gson();
		return gson.toJson(Object);
	}

	/**
	 *
	 * @param jsonElement
	 * @return
	 */
	public static String toJsonString(JsonElement jsonElement) {
		if (jsonElement == null) {
			return null;
		}
		Gson gson = new Gson();
		return gson.toJson(jsonElement);
	}

	/**
	 * @param keyName
	 * @param jsonObjectString
	 * @param clszz
	 * @param mode
	 * @return
	 */
	public static <T> T parseObject(String jsonObjectString, String keyName, Class<T> clszz, Mode mode) {

		if (isEmptyString(jsonObjectString)) {
			return null;
		}

		if (isEmptyString(keyName)) {
			return null;
		}

		if (clszz == null) {
			return null;
		}

		if (mode == null) {
			return null;
		}

		try {

			Gson gson = new Gson();

			JsonParser jsonParser = new JsonParser();

			// 字符串jsonObjectString转成JsonElement
			JsonElement jsonElement = jsonParser.parse(jsonObjectString);
			if (jsonElement == null) {
				return null;
			}

			// 判断元素JsonElement是否为对象JsonObject
			if (jsonElement.isJsonObject() == false) {
				return null;
			}

			// 得到对象JsonObject
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject == null) {
				return null;
			}

			// 由键名得到元素JsonElement
			JsonElement __sub__jsonElement = jsonObject.get(keyName);
			if (__sub__jsonElement == null) {
				return null;
			}

			// 判断元素JsonElement是否为数组JsonArray
			if (__sub__jsonElement.isJsonObject() == false) {
				return null;
			}

			// 将元素JsonElement转成对象entity
			T entity = gson.fromJson(__sub__jsonElement, clszz);

			if (mode == Mode.TRIM) {
				// 删除两旁空格
				trimObjectString(entity, clszz);
			}

			return entity;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 *
	 * @param jsonObjectString
	 * @param keyName
	 * @param clszz
	 * @return
	 */
	public static <T> T parseObject(String jsonObjectString, String keyName, Class<T> clszz) {
		return parseObject(jsonObjectString, keyName, clszz, Mode.NOT);
	}

	/**
	 * @param jsonObjectString
	 * @param clszz
	 * @param mode
	 * @return
	 */
	public static <T> T parseObject(String jsonObjectString, Class<T> clszz, Mode mode) {
		if (isEmptyString(jsonObjectString)) {
			return null;
		}

		if (clszz == null) {
			return null;
		}

		if (mode == null) {
			return null;
		}

		try {

			Gson gson = new Gson();

			JsonParser jsonParser = new JsonParser();

			// 字符串jsonObjectString转成JsonElement
			JsonElement jsonElement = jsonParser.parse(jsonObjectString);
			if (jsonElement == null) {
				return null;
			}

			// 判断元素JsonElement是否为对象JsonObject
			if(jsonElement.isJsonObject() == false) {
				return null;
			}

			// 将元素JsonElement转成对象entity
			T entity = gson.fromJson(jsonElement, clszz);

			if (mode == Mode.TRIM) {
				// 删除两旁空格
				trimObjectString(entity, clszz);
			}

			return entity;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param jsonObjectString
	 * @param clszz
	 * @return
	 */
	public static <T> T parseObject(String jsonObjectString, Class<T> clszz) {
		return parseObject(jsonObjectString, clszz, Mode.NOT);
	}

	/**
	 *
	 * @param jsonObjectString
	 * @param keyName
	 * @param clszz
	 * @param mode
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonObjectString, String keyName, Class<T> clszz, Mode mode) {

		if (isEmptyString(jsonObjectString)) {
			return null;
		}

		if (isEmptyString(keyName)) {
			return null;
		}

		if (clszz == null) {
			return null;
		}

		if (mode == null) {
			return null;
		}

		try {

			Gson gson = new Gson();
			JsonParser jsonParser = new JsonParser();

			// 字符串jsonObjectString转成JsonElement
			JsonElement jsonElement = jsonParser.parse(jsonObjectString);
			if (jsonElement == null) {
				return null;
			}

			// 判断元素JsonElement是否为对象JsonObject
			if (jsonElement.isJsonObject() == false) {
				return null;
			}

			// 得到对象JsonObject
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject == null) {
				return null;
			}

			// 由键名得到元素JsonElement
			JsonElement __sub__jsonElement = jsonObject.get(keyName);
			if (__sub__jsonElement == null) {
				return null;
			}

			// 判断元素JsonElement是否为数组JsonArray
			if (__sub__jsonElement.isJsonArray() == false) {
				return null;
			}

			// 得到数组JsonArray
			JsonArray jsonArray = __sub__jsonElement.getAsJsonArray();
			if (jsonArray == null) {
				return null;
			}

			int len = jsonArray.size();

			// 创建容器List<T>
			List<T> list = new ArrayList<T>( len );

			// 类型
			//		Type type = new TypeToken<T>(){}.getType();
			//		if (type == null) {
			//			return null;
			//		}

			for (int n = 0; n < len; n++) {
				JsonElement __for__jsonElement = jsonArray.get(n);
				if (jsonElement != null) {
					//			T t = gson.fromJson(jsonElement, type); // 会出错
					// 将元素JsonElement转成对象entity
					T entity = gson.fromJson(__for__jsonElement, clszz);
					if (entity != null) {
						if (mode == Mode.TRIM) {
							// 删除两旁空格
							trimObjectString(entity, clszz);
						}
						list.add(entity);
					}
				}
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 *
	 * @param <T>
	 * @param jsonObjectString
	 * @param keyName
	 * @param clszz
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonObjectString, String keyName, Class<T> clszz) {
		return parseArray(jsonObjectString, keyName, clszz, Mode.NOT);
	}

	/**
	 *
	 * @param <T>
	 * @param jsonArrayString
	 * @param clszz
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonArrayString, Class<T> clszz, Mode mode) {
		if (isEmptyString(jsonArrayString)) {
			return null;
		}

		if (clszz == null) {
			return null;
		}

		if (mode == null) {
			return null;
		}

		try {

			Gson gson = new Gson();

			JsonParser jsonParser = new JsonParser();

			// 字符串jsonObjectString转成JsonElement
			JsonElement jsonElement = jsonParser.parse(jsonArrayString);
			if (jsonElement == null) {
				return null;
			}

			// 判断元素JsonElement是否为数组JsonArray
			if(jsonElement.isJsonArray() == false) {
				return null;
			}

			// 得到数组JsonArray
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			if (jsonArray == null) {
				return null;
			}

			int len = jsonArray.size();

			List<T> list = new ArrayList<T>( len );

			for (int n = 0; n < len; n++) {
				JsonElement __for__jsonElement = jsonArray.get(n);
				if (jsonElement != null) {
					//			T t = gson.fromJson(jsonElement, type); // 会出错
					// 将元素JsonElement转成对象entity
					T entity = gson.fromJson(__for__jsonElement, clszz);
					if (entity != null) {
						if (mode == Mode.TRIM) {
							// 删除两旁空格
							trimObjectString(entity, clszz);
						}
						list.add(entity);
					}
				}
			}

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 *
	 * @param <T>
	 * @param jsonArrayString
	 * @param clszz
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonArrayString, Class<T> clszz) {
		return parseArray(jsonArrayString, clszz, Mode.NOT);
	}

	/**
	 * @param array
	 * @return
	 */
	public static <T> List<T> asList(T... array) {
		if (array == null) {
			return null;
		}
		return new ArrayList<T>(Arrays.asList(array));
	}
	
	public static <T> ArrayList<T> asArrayList(T... array) {
		if (array == null) {
			return null;
		}
		return new ArrayList<T>(Arrays.asList(array));
	}
	
}
