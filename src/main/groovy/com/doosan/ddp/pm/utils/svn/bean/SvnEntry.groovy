package com.doosan.ddp.pm.utils.svn.bean
/**
 * SVN实体Bean
 */
class SvnEntry {
	
	String name		//SVN目录名称
	String path		//SVN全路径(用于下载文件及加载子目录)
	int isDir		//是否是目录(0:否 1:是)
	
}
