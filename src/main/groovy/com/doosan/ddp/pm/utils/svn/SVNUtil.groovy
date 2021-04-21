package com.doosan.ddp.pm.utils.svn
import org.springframework.stereotype.Component
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
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
	 * SVN身份认证
	 * @return
	 */
	ISVNAuthenticationManager getAuthManager() throws Exception{
		ISVNAuthenticationManager authManager = null
		try {
			authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password)
		} catch (Exception e) {
			throw new RuntimeException("SVN身份认证失败：" + e.getMessage())
		}
		return authManager
	}
	/**
	* 获取SVN仓库
	*/
	SVNRepository getSVNRepository() throws SVNException{
	   SVNRepository repository = null
	   ISVNAuthenticationManager authManager = null
	   try {
		   repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl))
		   authManager =  getAuthManager()
		   repository.setAuthenticationManager(authManager)
	   }catch(Exception e) {
		   throw new RuntimeException("SVN创建库连接失败:" + e.getMessage())
	   }	   
	   return repository
	}
	/**
        * 获取客户端管理器
    * @return
    */
   	SVNClientManager getSVNClientManager(SVNRepository repository) throws Exception{
	    SVNClientManager clientManager = null
	    ISVNOptions options = SVNWCUtil.createDefaultOptions(true)
		try{		
			clientManager = SVNClientManager.newInstance(options, repository.getAuthenticationManager())
		}catch(Exception e) {
			throw new RuntimeException("SVN客户端管理器获取失败:" + e.getMessage())
		}
		return clientManager
	}   		   
}