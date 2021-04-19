package com.doosan.ddp.pm.controller.biz.svn
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.tmatesoft.svn.core.io.SVNRepository

import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.comm.results.ServerResults
import com.doosan.ddp.pm.controller.biz.pro.issue.ProgramIssueController
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import com.doosan.ddp.pm.utils.svn.SVNUtil
import com.doosan.ddp.pm.utils.svn.SvnOperates
import com.doosan.ddp.pm.utils.svn.bean.SvnEntry
/**
 * 项目SVN管理
 */
@Controller
@RequestMapping("/pm/biz/pro/svn")
class ProgramSvnController {
	
	final String WEB_URL = "biz/pro/svn"
	@Autowired
	ProgramMainService programMainService
	@Autowired
	SystemUserService systemUserService
	@Autowired
	ProgramIssueController programIssueController
	
	/**
	 *	跳转项目SVN清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/list"
	}
		
	@ResponseBody
	@GetMapping("/prolist")
	def getProList(HttpServletRequest request) {
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//获取所有参与的项目
		List<ProgramMain> pros = programIssueController.getUserPros(userId)		
		return ServerResultJson.success(pros)
	}
	/**
	 * 获取svn文档
	 * @param path	--svn相对路径
	 * @param id	--树节点id
	 * @param proId	--项目id		
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/docs")
	def getSvnDocuments(String path,String id, String proId, HttpServletRequest request) {
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		SystemUser user = systemUserService.getUserByCode(userId)
		if(user.getSvncode() == null)
			return ServerResultJson.error(ServerResults.ERROR_SVN_CODE)
		if(user.getSvnpwd() == null)
			return ServerResultJson.error(ServerResults.ERROR_SVN_PWD)
		ProgramMain program = programMainService.getProgramById(proId)
		if(program.getSvnadd() == null)
			return ServerResultJson.error(ServerResults.ERROR_SVN_ADD)
		SVNUtil svn = new SVNUtil(user.getSvncode(), user.getSvnpwd(), program.getSvnadd())
		SVNRepository repository = null
		try {
			repository = svn.getSVNRepository()
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_AUTH) 
		}
		SvnOperates op = new SvnOperates()
		//获取svn节点
		SvnEntry root = null
		if(id == '0') {
			root = new SvnEntry(
				name:'Root',
				path:'/',
				leaf:false,
				parent:true,
				icon:"/static/images/ztree/folder_close.png",
				iconOpen:"/static/images/ztree/folder_open.png",
				iconClose:"/static/images/ztree/folder_close.png",
				open:true,
				id:id,
				pId:null)
			def nodes = op.listEntries(repository, path, id)
			root.setChildren(nodes)
			return ServerResultJson.success(root)
		}else {
			def nodes = op.listEntries(repository, path, id)
			return ServerResultJson.success(nodes)
		}		
	}
}