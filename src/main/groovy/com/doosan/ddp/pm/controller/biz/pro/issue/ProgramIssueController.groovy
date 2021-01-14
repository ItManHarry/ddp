package com.doosan.ddp.pm.controller.biz.pro.issue
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import com.doosan.ddp.pm.service.biz.issue.ProgramIssueService
/**
 * 项目issue事项
 */
@Controller
@RequestMapping("/pm/biz/pro/issue")
class ProgramIssueController {
	
	final String WEB_URL = "biz/pro/issue"
	@Autowired
	ProgramIssueService programIssueService
	
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
	
	
}