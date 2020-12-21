package com.doosan.ddp.pm.controller.biz.pro.group
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
@Controller
@RequestMapping("/pm/biz/pro/group")
class ProgramGroupController {
	
	@Autowired
	SystemUserService systemUserService
	@Autowired
	ProgramGroupService programGroupService
	/**
	 * 根据项目id获取项目成员
	 * @param proId
	 * @return
	 */
	@ResponseBody
	@GetMapping("/members")
	def getProgramGroupMembers(String proId) {
		//获取项目组成员
		def groupMembers = programGroupService.getProgramGroupByProId(proId)
		def members = []
		def pmCode = ''
		groupMembers.each {
			if(it.getGrouprole() == 1)
				pmCode = it.getUserid()
			members << it.getUserid()
		}
		println "Members : ${members.size()}, PM is : $pmCode"
		//获取所有的用户信息
		def allUsers = systemUserService.getAll()
		def users = []
		def user = null
		allUsers.each { 
			user = [:]
			user.put("label", it.getName())
			user.put("key", it.getCode())
			if(it.getCode() == pmCode)
				user.put("disabled", true)		//项目PM不可从项目组中移除
			else
				user.put("disabled", false)
			users << user
		}
		println "Users : ${users.size()}"		
		def data = [:]
		data.put("users", users)
		data.put("members", members)
		return ServerResultJson.success(data)
	}
}