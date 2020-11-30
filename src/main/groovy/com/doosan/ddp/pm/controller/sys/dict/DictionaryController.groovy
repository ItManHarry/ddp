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
import com.doosan.ddp.pm.service.sys.dict.SystemDictionaryService
import com.doosan.ddp.pm.service.sys.dict.SystemEnumerationService
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/sys/dict")
class DictionaryController {
	
	final String WEB_URL = "sys/dict"
	@Autowired
	SystemDictionaryService systemDictionaryService
	@Autowired
	SystemEnumerationService systemEnumerationService
	
	/**
	 * 	跳转字典清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def total = systemDictionaryService.getCount()
		if(total)
			map.put("total", total)
		else
			map.put("total", 0)
		return WEB_URL + "/list"
	}
	/**
	 * 	获取数据
	 * 	@param order
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		def count = systemDictionaryService.getCount() ? systemDictionaryService.getCount().intValue() : 0
		def data = systemDictionaryService.getAllByPages(page, limit)
		data.each { 
			if(it.getStatus().equals("1"))
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	保存字典
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(@RequestBody String params, HttpServletRequest request, Map map){
		println 'Parameters : \t' + params 
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String code = json.get("code").asString
		String name = json.get("name").asString
		String user = json.get("user").asString
		print("Code is : $code, name is : $name, user is : $user")
		//session获取用户账号
		//def user = request.getSession().getAttribute("currentUser")
		SystemDictionary dict = new SystemDictionary()
		String id = request.getParameter("id")
		if(id) {
			dict = systemDictionaryService.getDictById(id)
			dict.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			dict.setModifyuserid(user)
		}else {
			def d = systemDictionaryService.getDictByCode(code.toUpperCase())
			if(d != null)
				return ServerResultJson.error(0, "字典代码已存在,请勿重复添加!", "")
			dict.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			dict.setCreateuserid(user)
			dict.setStatus(1)
		}
		dict.setCode(code)
		dict.setName(name)
		systemDictionaryService.save(dict)
		return ServerResultJson.success()
	}
	/**
	 * 	根据字典Code获取下拉列表
	 * 	@param code
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/enums")
	def enums(String code){
		def dict = systemDictionaryService.getDictByCode(code.toUpperCase())
		if(dict)
			return ServerResultJson.success(systemEnumerationService.findByDictionary(dict.getTid()))
		else
			return ServerResultJson.success([])
	}
}