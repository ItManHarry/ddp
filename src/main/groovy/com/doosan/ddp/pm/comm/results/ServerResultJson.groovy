package com.doosan.ddp.pm.comm.results
import java.io.Serializable
/**
 * 返回Json数据
 * @author 20112004
 *
 */
public class ServerResultJson implements Serializable{

	/**
	 * 无结果返回成功
	 * @return
	 */
	public static ServerResultObject success(){
		return success(null)
	}
	/**
	 * 有数据返回成功
	 * @param object
	 * @return
	 */
	public static ServerResultObject success(Object object){
		ServerResultObject resultOject = new ServerResultObject(ServerResults.SUCCESS)
		resultOject.setData(object)
		return resultOject
	}
	/**
	 * 有数据返回成功
	 * @param object
	 * @return
	 */
	public static ServerResultObject success(Object object, Integer number){
		ServerResultObject resultOject = new ServerResultObject(ServerResults.SUCCESS)
		resultOject.setData(object)
		resultOject.setNumber(number)
		return resultOject
	}
	/**
	 * 返回错误
	 * @param results
	 * @return
	 */
	public static ServerResultObject error(ServerResults results){
		ServerResultObject resultOject = new ServerResultObject(results)
		return resultOject
	}
	
	/**
	 * 返回错误
	 * @param results
	 * @return
	 */
	public static ServerResultObject error(Integer status, String message, String exceptionURL){
		ServerResultObject resultOject = new ServerResultObject(status, message, exceptionURL)
		return resultOject
	}
}
