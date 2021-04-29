package com.doosan.ddp.pm.utils.svn
import org.springframework.stereotype.Component
import org.tmatesoft.svn.core.SVNCommitInfo
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNDirEntry
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNConflictChoice
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNUpdateClient
import org.tmatesoft.svn.core.wc.SVNWCUtil

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
	/**
	 * Checkout文件到本地磁盘
	 * @param clientManager
	 * @param url
	 * @param revision
	 * @param destPath
	 * @param isRecursive
	 * @return
	 * @throws SVNException
	 */
	void checkout(SVNClientManager clientManager, SVNURL url, SVNRevision revision, File destPath, boolean isRecursive ) throws SVNException {
		SVNUpdateClient updateClient = clientManager.getUpdateClient()
		updateClient.setIgnoreExternals(false)
		try {
			updateClient.doCheckout( url , destPath , revision , revision , isRecursive )
		} catch(Exception e) {
			throw new RuntimeException("检出SVN文件异常:" + e.getMessage())
		}
	}
	/**
	 * 递归检查不在版本控制的文件，并add到svn
	 * @param clientManager
	 * @param wcPath
	 * @param merge
	 * @throws SVNException
	 */
	void addVersionToSvn(SVNClientManager clientManager, File wcPath, boolean merge) throws SVNException {
		if(!SVNWCUtil.isVersionedDirectory(wcPath)) {
			try{
				addEntry(clientManager, wcPath, merge)
			}catch(SVNException e) {
				throw e	
			}
		}
		if(wcPath.isDirectory()) {
			for (File sub : wcPath.listFiles()) {
				if (sub.isDirectory() && sub.getName().equals(".svn")) {
					continue
				}
				addVersionToSvn(clientManager, sub, merge)
			}
		}
	}
	/**
	 * 添加到版本控制
	 * @param clientManager
	 * @param wcPath
	 * @throws SVNException
	 */
	void addEntry(SVNClientManager clientManager, File wcPath, boolean merge) throws SVNException {
		try {
			clientManager.getWCClient().doAdd(wcPath, false, false, false, true)
		} catch(SVNException  e) {
			e.printStackTrace()
			if(e.getMessage().contains('E155015')) {
				// 解决冲突
				// SVNConflictChoice.MERGED  是把本地和远程仓库文件合并，以本地为主
				// SVNConflictChoice.THEIRS_CONFLICT
				// SVNConflictChoice.THEIRS_FULL
				// SVNConflictChoice.MINE_CONFLICT
				if(merge) {
					clientManager.getWCClient().doResolve(wcPath, SVNDepth.INFINITY, SVNConflictChoice.MERGED)
				} else {
					throw e
				}
			}else if(e.getMessage().concat('E150002')){
				//already under version control
				println "already under version control , "+e.getMessage()
			}else {
				throw e
			}
		}
    }
	/**
	 * 更新SVN
	 * @param clientManager
	 * @param wcPath
	 * @param updateToRevision
	 * @param isRecursive
	 * @throws SVNException
	 */
	void update(SVNClientManager clientManager,File wcPath, SVNRevision updateToRevision , boolean isRecursive) throws SVNException{
		SVNUpdateClient updateClient = clientManager.getUpdateClient()
		updateClient.setIgnoreExternals(false)
		try {
			updateClient.doUpdate(wcPath, updateToRevision, isRecursive)
		} catch(Exception e) {
			throw new RuntimeException("SVN更新异常:" + e.getMessage())
		}
		
	}
	/**
	 * 文件提交
	 * @param clientManager
	 * @param wcPath
	 * @param keepLocks
	 * @param commitMessage
	 * @throws SVNException
	 */
	void commit(SVNClientManager clientManager, File wcPath, boolean keepLocks, String commitMessage) throws SVNException {
		def files = []
		files << wcPath
		try {
			clientManager.getCommitClient().doCommit((File[])files.toArray(), keepLocks, commitMessage, false, true)
		}catch(Exception e) {
			e.printStackTrace()
			throw new RuntimeException("SVN提交异常:" + e.getMessage())
		}
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