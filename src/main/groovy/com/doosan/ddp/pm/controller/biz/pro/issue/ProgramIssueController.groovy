package com.doosan.ddp.pm.controller.biz.pro.issue
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramGroup
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.service.biz.issue.ProgramIssueService
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
/**
 * 项目issue事项
 */
@Controller
@RequestMapping("/pm/biz/pro/issue")
class ProgramIssueController {
	
	final String WEB_URL = "biz/pro/issue"
	@Autowired
	ProgramIssueService programIssueService
	@Autowired
	ProgramGroupService programGroupService
	@Autowired
	ProgramMainService programMainService
	
	/**
	 *	跳转项目issue清单
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
		List<ProgramGroup> groups = programGroupService.getProgramGroupByUserId(userId)
		def proIds = []
		groups.each { 
			proIds << it.getProgramid()
		}
		println "Program ids are : $proIds"
		//获取所有项目
		List<ProgramMain> pros = programMainService.getProListForUser(proIds)
		println "Program list size is : " + pros.size()
		return ServerResultJson.success(pros)
	}
}