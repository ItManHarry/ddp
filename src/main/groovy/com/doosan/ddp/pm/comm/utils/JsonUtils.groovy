package com.doosan.ddp.pm.comm.utils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.JSONPObject
/**
 * Json转换工具
 */
class JsonUtils {
	static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 对象转为字符串
	 * @param data
	 * @return
	 */
	static String objectToJson(Object data){
		try{
			String string = MAPPER.writeValueAsString(data);
			return string;
		}catch(JsonProcessingException e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将json字符串转换为对象
	 * @param jsonData
	 * @param clazz
	 * @return
	 */
	static <T> T jsonToBean(String jsonData, Class<T> clazz){
		try{
			T t = MAPPER.readValue(jsonData, clazz);
			return t;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将json字符串转换为pojo对象list
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	static <T>List<T> jsonToList(String jsonData, Class<T> beanType){
		JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
		try{
			List<T> list = MAPPER.readValue(jsonData, javaType);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 输出JSONP字符串
	 * @param functionName
	 * @param o
	 * @return
	 */
	String toJsonpString(String functionName, Object o){
		JSONPObject jsonp = new JSONPObject(functionName, o);
		return jsonp.toString();
	}
}