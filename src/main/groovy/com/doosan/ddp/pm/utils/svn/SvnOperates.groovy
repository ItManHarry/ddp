package com.doosan.ddp.pm.utils.svn
import org.springframework.stereotype.Component
import org.tmatesoft.svn.core.SVNCommitInfo
import org.tmatesoft.svn.core.SVNDirEntry
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.wc.SVNClientManager

import com.doosan.ddp.pm.utils.svn.bean.SvnEntry
/**
 * SVN操作
 */
class SvnOperates {
	/**
	 * 获取svn某个路径下的所有目录/文件清单
	 * @param repository
	 * @param path
	 * @param parentId
	 * @return
	 * @throws SVNException
	 */
	List<SvnEntry> listEntries(SVNRepository repository, String path, String parentId)	throws SVNException {
		//SVN实体节点
		def es = [] 			
		//获取版本库的path目录下的所有条目。参数"-1"表示是最新版本。
		Collection entries = repository.getDir(path, -1, null,(Collection)null)
		Iterator iterator = entries.iterator()
		SvnEntry node = null
		println "Parent id is : >>>>>>>>>>>>>>>>>>>>>>>>>>>" + parentId
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry)iterator.next()
			node = new SvnEntry(
				name:entry.getName(),
				path:(parentId=="0"?path+entry.getRelativePath():path+"/"+entry.getRelativePath()),				
				leaf:entry.getKind()==SVNNodeKind.DIR?false:true,
				parent:entry.getKind()==SVNNodeKind.DIR?true:false,
				hasChildren:entry.getKind()==SVNNodeKind.DIR?true:false,
				icon:"/static/images/ztree/folder_close.png",
				iconOpen:"/static/images/ztree/folder_open.png",
				iconClose:"/static/images/ztree/folder_close.png",
				open:true,
				id:UUID.randomUUID().toString().replaceAll("-",""),
				pId:parentId)
			es << node			
		}		
		return es
	}
	/**
	 * 	创建目录
	 * @param clientManager
	 * @param url
	 * @param commitMessage
	 * @return
	 * @throws SVNException
	 */
	SVNCommitInfo makeDirectory(SVNClientManager clientManager, SVNURL url , String commitMessage ) throws SVNException {
		def urls = []		
		urls << url
		return clientManager.getCommitClient().doMkDir((SVNURL[])urls.toArray(), commitMessage)
	}
	/**
	 * 删除目录/文件
	 * @param clientManager
	 * @param url
	 * @param commitMessage
	 * @return
	 * @throws SVNException
	 */
	SVNCommitInfo delete(SVNClientManager clientManager, SVNURL url , String commitMessage ) throws SVNException {
		def urls = []
		urls << url
		return clientManager.getCommitClient().doDelete((SVNURL[])urls.toArray(), commitMessage)
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
			println "SVN Catalog is directory ? " + (entry.getLeaf()==1?'Y':'N')
			println "SVN Catalog id : " + entry.getId()
			println "SVN Parent id : " + entry.getpId()
			println '-' * 80
		}
	}
}