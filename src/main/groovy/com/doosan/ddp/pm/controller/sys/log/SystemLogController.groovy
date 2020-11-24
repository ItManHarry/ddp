package com.doosan.ddp.pm.controller.sys.log
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog
import com.doosan.ddp.pm.service.sys.log.SystemLogService
@Controller
@RequestMapping("/pm/sys/log")
class SystemLogController {
	
	@Autowired
	SystemLogService systemLogService
	
	@RequestMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request) {
		SystemLog log = new SystemLog()
		log.setUserid(request.getParameter("userid"))
		log.setContent(request.getParameter("content"))
		systemLogService.save(log)		
	}
}