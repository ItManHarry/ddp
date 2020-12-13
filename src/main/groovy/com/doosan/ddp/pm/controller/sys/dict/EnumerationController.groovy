package com.doosan.ddp.pm.controller.sys.dict
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
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemDictionary
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemEnumeration
import com.doosan.ddp.pm.service.sys.dict.SystemDictionaryService
import com.doosan.ddp.pm.service.sys.dict.SystemEnumerationService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
/**
 * 字典枚举
 */
@Controller
@RequestMapping("/pm/sys/enum")
class EnumerationController {
	
	@Autowired
	SystemEnumerationService systemEnumerationService
	@Autowired
	SystemDictionaryService systemDictionaryService
	
	/**
	 * 根据字典ID获取所有的枚举 
	 * @param dict
	 * @return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(String dict){
		def data = systemEnumerationService.getByDictionary(dict)
		return ServerResultJson.success(data)
	}
	
	/**
	 * 根据字典CODE获取所有的枚举
	 * @param code
	 * @return
	 */
	@ResponseBody
	@GetMapping("/options")
	def options(String code){
		SystemDictionary dict = systemDictionaryService.getDictByCode(code)
		def data = systemEnumerationService.getByDictionary(dict.getTid())
		return ServerResultJson.success(data)
	}
	
	/**
	 * 新增修改枚举数据
	 * @param params
	 * @param map
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号
		def user = request.getSession().getAttribute("currentUser")
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		String dict = json.get("dict").asString
		String value = json.get("value").asString
		String view = json.get("view").asString
		//String user = json.get("user").asString
		SystemEnumeration em = new SystemEnumeration()
		if(id) {
			em = systemEnumerationService.getEnumerationById(id)
			em.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			em.setModifyuserid(user)
		}else {
			em.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			em.setCreateuserid(user)
			em.setStatus(1)
		}
		em.setDict(dict)
		em.setValue(value)
		em.setView(view)
		systemEnumerationService.save(em)
		return ServerResultJson.success()
	}
}