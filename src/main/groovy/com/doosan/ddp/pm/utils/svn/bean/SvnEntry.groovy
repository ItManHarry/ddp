package com.doosan.ddp.pm.utils.svn.bean
/**
 * SVN实体Bean
 */
class SvnEntry {
	
	String id				//节点ID	
	String pId 				//父节点ID
	String name				//SVN目录名称
	String path				//SVN全路径(用于下载文件及加载子目录)
	boolean open			//是否展开
	String  icon			//图标
	String 	iconOpen		//展开时的图标
	String 	iconClose		//闭合时的图标
	boolean leaf			//是否是文件(是:true 否:false)
	boolean parent			//是否是父节点(是:true 否:false)
	boolean hasChildren		//是否有子节点
	List<SvnEntry> children	//子节点
	
}
