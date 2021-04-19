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
		println 'User svn code is : ' + user.getSvncode() + ', password is : ' + user.getSvnpwd()
		println 'SVN path is : ' + path + ', parent node id is : ' + id + ', Program id is : ' + proId
		ProgramMain program = programMainService.getProgramById(proId)
		if(program.getSvnadd() == null)
			return ServerResultJson.error(ServerResults.ERROR_SVN_ADD)
		println 'Svn address is : ' + program.getSvnadd()
		SVNUtil svn = new SVNUtil(user.getSvncode(), user.getSvnpwd(), program.getSvnadd())
		SVNRepository repository = null
		try {
			repository = svn.getSVNRepository()
		}catch(Exception e) {
			println "SVN创建库连接失败" + e.getMessage()
			return ServerResultJson.error(ServerResults.ERROR_SVN_AUTH) 
		}
		SvnOperates op = new SvnOperates()
		def es = op.listEntries(repository, path, id)
		return ServerResultJson.success(es)
	}
}