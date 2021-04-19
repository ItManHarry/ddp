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
	
	List<SvnEntry> listEntries(SVNRepository repository, String path, String parentId)	throws SVNException {
		def es = [] 			//SVN实体节点
		//获取版本库的path目录下的所有条目。参数－1表示是最新版本。
		Collection entries = repository.getDir(path, -1, null,(Collection)null)
		Iterator iterator = entries.iterator()
		SvnEntry node = null
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry)iterator.next()
			node = new SvnEntry(name:entry.getName(),
				path:(path+"/"+entry.getRelativePath()),
				isDir:entry.getKind()==SVNNodeKind.DIR?1:0,
				id:UUID.randomUUID().toString().replaceAll("-",""))
			node.setParentId(parentId)
			es << node			
		}		
		return es
	}
	
	static void main(String[] args) {
		println "Now do the svn test"
		SVNUtil svn = new SVNUtil("chengguoqian","chengguoqian", "https://10.40.128.191/svn/diccvendor")
		println "svn repository initialized!!!"
		SVNRepository repository = svn.getSVNRepository()
		println "SVN catalog"
		SvnOperates op = new SvnOperates()
		def path = "/"		
		println '-' * 80
		def es = op.listEntries(repository, path, '0')		
		for(SvnEntry entry : es) {
			println "SVN Catalog name : " + entry.getName()
			println "SVN Catalog path : " + entry.getPath() 
			println "SVN Catalog is directory ? " + (entry.getIsDir()==1?'Y':'N')
			println "SVN Catalog id : " + entry.getId()
			println "SVN Parent id : " + entry.getParentId()
			println '-' * 80
		}
	}
}