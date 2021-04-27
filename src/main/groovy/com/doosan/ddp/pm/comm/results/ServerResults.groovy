package com.doosan.ddp.pm.comm.results
import java.io.Serializable

public enum ServerResults {
	
	SUCCESS(200, "执行成功"),
	NOTFOUND(0, "无数据"),
	NOTACCESS(403, "拒绝请求"),
	ERROR(500, "内部错误"),
	ERROR_KEY(600, "数据库主键冲突"),
	ERROR_TOKEN(502, "用户Token错误"),
	ERROR_TIMEOUT(503, "连接超时"),
	ERROR_UNKNOWN(555, "未知错误"),
	ERROR_USERNOTFOUND(700, "用户不存在"),
	ERROR_WRONGPWD(701, "密码错误"),
	ERROR_SVN_CODE(800, "用户svn账号没有维护!"),
	ERROR_SVN_PWD(801, "用户svn密码为空!"),
	ERROR_SVN_ADD(802, "系统svn地址没有维护!"),
	ERROR_SVN_AUTH(803, "SVN创建连接失败,SVN地址有误或账号无权限,请联系管理员!"),
	ERROR_SVN_FOLDER(804, "SVN新增目录失败,请联系管理员!"),
	ERROR_SVN_DELETE(805, "SVN目录/文件删除失败,请联系管理员!"),
	ERROR_SVN_CHECKOUT(806, "SVN文件检出失败,请联系管理员!")
	
	private int status
	private String message
	
	ServerResults(int status, String message){
		this.status = status
		this.message = message
	}

	public int getStatus() {
		return status
	}

	public void setStatus(int status) {
		this.status = status
	}

	public String getMessage() {
		return message
	}

	public void setMessage(String message) {
		this.message = message
	}	
}