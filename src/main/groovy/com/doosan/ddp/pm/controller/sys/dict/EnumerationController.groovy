package com.doosan.ddp.pm.controller.sys.dict
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemEnumeration
import com.doosan.ddp.pm.service.sys.dict.SystemEnumerationService
/**
 * 字典枚举
 */
@Controller
@RequestMapping("/pm/sys/enums")
class EnumerationController {
	
	@Autowired
	SystemEnumerationService systemEnumerationService
	/**
	 * 根据字典ID获取所有的枚举
	 * @param dict
	 * @return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(String dict){
		def data = systemEnumerationService.findByDictionary(dict)
		return ServerResultJson.success(data)
	}
	/**
	 * 新增修改枚举数据
	 * @param request
	 * @param map
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		//session获取用户账号
		//def userId = request.getSession().getAttribute("currentUser")
		//参数传递用户账号
		def userId = request.getParameter("userId")
		SystemEnumeration em = new SystemEnumeration()
		String id = request.getParameter("id")
		if(id) {
			em = systemEnumerationService.getEnumerationById(id)
			em.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			em.setModifyuserid(userId)
		}else {
			em.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			em.setCreateuserid(userId)
			em.setStatus(1)
		}
		em.setDict(request.getParameter("dict"))
		em.setValue(request.getParameter("value"))
		em.setView(request.getParameter("view"))
		systemEnumerationService.save(em)
		return ServerResultJson.success()
	}
}