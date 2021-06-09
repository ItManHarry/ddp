package com.doosan.ddp.pm.controller.sys.auth
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.auth.SystemAuth
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.sys.auth.SystemAuthService
import com.doosan.ddp.pm.service.sys.log.SystemLogService
import com.doosan.ddp.pm.service.sys.menu.SystemMenuService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
@Controller
@RequestMapping("/pm/sys/auth")
class SystemAuthController {
	
	@Autowired
	SystemLogService systemLogService
	@Autowired
	SystemAuthService systemAuthService
	@Autowired
	SystemMenuService systemMenuService
	@Autowired
	SystemUserService systemUserService
	
	//保存权限
	@RequestMapping("/save")
	@ResponseBody
	def saveAuth(HttpServletRequest request) {
		def userId = request.getSession().getAttribute("currentUser")
		SystemAuth auth = new SystemAuth()
		auth.setRoleid(request.getParameter("roleid"))
		auth.setMenuid(request.getParameter("menuid"))
		auth.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		auth.setCreateuserid(userId)
		systemAuthService.save(auth)
		return ServerResultJson.success()
	}
	//批量删除权限
	@RequestMapping("/del")
	@ResponseBody
	def deleteAuth(HttpServletRequest request) {
		SystemAuth auth = new SystemAuth()
		systemAuthService.batchDeleteByRoleId(request.getParameter("roleid"))
		SystemLog log = new SystemLog()
		def userId = request.getParameter("userid")
		log.setUserid(userId)
		log.setContent("批量删除授权")
		log.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		log.setCreateuserid(userId)
		systemLogService.save(log)
		return ServerResultJson.success()
	}
	/**
	 * 	获取已授权菜单
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/menus")
	def menus(HttpServletRequest request){
		def userId = request.getSession().getAttribute("currentUser")
		SystemUser user = systemUserService.getUserByCode(userId)
		String roleId = user.getUserrole()
		def authed = systemAuthService.getByRoleId(roleId)
		def authedMenu = []
		authed.each{
			authedMenu << it.getMenuid()
		}
		def menus = systemMenuService.getMenusByIds(authedMenu)
		def menuList=[]
		menus.each {
			if(it.status==1){
				menuList.add(it)
			}
		}
		return ServerResultJson.success(menuList)
	}
}