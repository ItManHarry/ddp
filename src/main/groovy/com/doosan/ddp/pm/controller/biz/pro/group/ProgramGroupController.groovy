package com.doosan.ddp.pm.controller.biz.pro.group
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramGroup
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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
	
	/**
	 * 保存项目组成员
	 * @param params
	 * @param request
	 * @param map
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号&uuid
		def userCd = request.getSession().getAttribute("currentUser")
		//def userId = request.getSession().getAttribute("currentUserId")
		//传递参数
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String groupProId = json.get("groupProId").asString
		//删除项目组现有普通成员
		def groupMembers = programGroupService.getProgramGroupByProId(groupProId)
		def pmCode = ''
		groupMembers.each {
			if(it.getGrouprole() == 1)
				pmCode = it.getUserid()
			else
				programGroupService.deleteGroupMember(it)
		}
		println "Program group is : $groupProId"
		String members = json.get("members").asString
		//新建项目组普通成员
		members.split(",").each { 
			println "User code is : $it"
			if(it != pmCode) {
				ProgramGroup group = new ProgramGroup(programid:groupProId,userid:it,grouprole:2)
				group.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				group.setCreateuserid(userCd)
				programGroupService.save(group)
			}				
		}
		return ServerResultJson.success()
	}
}