package com.doosan.ddp.pm.utils.svn
import org.springframework.stereotype.Component
import org.tmatesoft.svn.core.SVNDirEntry
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.io.SVNRepository

import com.doosan.ddp.pm.utils.svn.bean.SvnEntry
/**
 * SVN操作
 */
class SvnOperates {
	
	List<SVNDirEntry> listEntries(SVNRepository repository, String path)	throws SVNException {
		def r_entries = []
		//获取版本库的path目录下的所有条目。参数－1表示是最新版本。
		Collection entries = repository.getDir(path, -1, null,(Collection)null)
		Iterator iterator = entries.iterator()
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry)iterator.next()
			//System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName())
			r_entries << entry
			/*if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName(): path + "/" + entry.getName())
			}*/
		}
		return r_entries
	}
	
	static void main(String[] args) {
		println "Now do the svn test"
		SVNUtil svn = new SVNUtil("chengguoqian","chengguoqian", "https://10.40.128.191/svn/diccvendor")
		println "svn repository initialized!!!"
		SVNRepository repository = svn.getSVNRepository()
		println "SVN catalog"
		SvnOperates op = new SvnOperates()
		def path = "/"
		def entries = op.listEntries(repository, path)
		println "Catalog size is : " + entries.size()
		for(SVNDirEntry entry : entries) {
			//println "Catalog name : " + entry.getName()
			//println "URI Path :" + entry.getRepositoryRoot().getURIEncodedPath() 
			//println "URI Host :" + entry.getRepositoryRoot().getHost()
			//println "URI Path :" + entry.getRepositoryRoot().getPath()
			//println "URI Port :" + entry.getRepositoryRoot().getProtocol()
			//println "Catalog svn path is : " + (entry.getRepositoryRoot().getProtocol()+"://"+entry.getRepositoryRoot().getHost()+entry.getRepositoryRoot().getPath()+"/"+entry.getRelativePath())
		}
		println '-' * 80
		path = "/08. 运营指南"
		entries = op.listEntries(repository, path)
		println "Catalog size is : " + entries.size()
		def es = [] 	//SVN实体集
		for(SVNDirEntry entry : entries) 
			es << new SvnEntry(name:entry.getName(),
				path:(entry.getRepositoryRoot().getProtocol()+"://"+entry.getRepositoryRoot().getHost()+entry.getRepositoryRoot().getPath()+path+"/"+entry.getRelativePath()),
				isDir:entry.getKind()==SVNNodeKind.DIR?1:0)
		for(SvnEntry entry : es) {
			println "SVN Catalog name : " + entry.getName()
			println "SVN Catalog path : " + entry.getPath() 
			println "SVN Catalog is directory ? " + entry.getIsDir()
			println '-' * 80
		}
	}
}