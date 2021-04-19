package com.doosan.ddp.pm.utils.svn
import org.springframework.stereotype.Component
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions
import org.tmatesoft.svn.core.wc.ISVNOptions
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNUpdateClient
import org.tmatesoft.svn.core.wc.SVNWCUtil
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
/**
 * SVN初始化库
 */
class SVNUtil {  
	
	String svnUrl
	String username
	String password
	
	SVNUtil(String username, String password, String svnUrl) {		
		this.username = username
		this.password = password
		this.svnUrl = svnUrl
	}
	
	
   /**
	* 获取SVN仓库
	*/
   SVNRepository getSVNRepository() throws SVNException{
	   SVNRepository repository = null
	   ISVNAuthenticationManager authManager = null
	   try {
		   repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl))
		   authManager =  SVNWCUtil.createDefaultAuthenticationManager(username, password)
		   repository.setAuthenticationManager(authManager)
	   }catch(Exception e) {
		   throw new RuntimeException("SVN创建库连接失败:" + e.getMessage())
	   }	   
	   return repository
   }
   /**
    * 初始化库
    */
   void setupLibrary() {
	   DAVRepositoryFactory.setup()
	   SVNRepositoryFactoryImpl.setup()
	   FSRepositoryFactory.setup()
   }
   /**
    * 获取SVN更新实例
    * @return
    */
   SVNUpdateClient getSVNUpdateClient(){
	   //声明SVN客户端管理类
	   SVNClientManager ourClientManager = null   
	   //初始化库。 必须先执行此操作。
	   setupLibrary()
	   ISVNOptions options = SVNWCUtil.createDefaultOptions(true)
	   //实例化客户端管理类
	   ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions)options, username, password)   
	   //通过客户端管理类获得updateClient类的实例。
	   SVNUpdateClient updateClient = ourClientManager.getUpdateClient()   
	   updateClient.setIgnoreExternals(false)   
	   return updateClient   
   }
   /**
    * 获取客户端管理器
    * @return
    */
   SVNClientManager getSVNClientManager(){
	   DAVRepositoryFactory.setup()
	   ISVNOptions options = SVNWCUtil.createDefaultOptions(true)
	   //实例化客户端管理类
	   return SVNClientManager.newInstance((DefaultSVNOptions)options, username, password)
   }
   
}