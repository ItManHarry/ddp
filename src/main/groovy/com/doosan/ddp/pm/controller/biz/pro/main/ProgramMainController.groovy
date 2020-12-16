package com.doosan.ddp.pm.controller.biz.pro.main
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
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/biz/pro")
class ProgramMainController {

	final String WEB_URL = "biz/pro/main"
	@Autowired
	ProgramMainService programMainService
	@Autowired
	SystemUserService systemUserService
	@Autowired
	ProgramGroupService programGroupService
		
	/**
	 *	跳转项目清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		return WEB_URL + "/list"
	}
	
	/**
	 * 根据姓名和code进行模糊分页查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	@ResponseBody
	@GetMapping("/query")
	def query(Integer page, Integer limit, String name, String code, HttpServletRequest request){
		//session获取用户账号
		def userCode = request.getSession().getAttribute("currentUser")
		SystemUser user = systemUserService.getUserByCode(userCode)
		String userId = user ? user.getTid() : ''
		if(userId) {
			//根据用户id获取所属的项目组
			List<ProgramGroup> groups = programGroupService.getProgramGroupByUserId(userId)
			def proIds = []
			groups.each { 
				proIds << it.getProgramid()
			}
			def count = programMainService.getCountByNameAndCode(name, code, proIds) ? systemUserService.getCountByNameAndCode(name, code, proIds).intValue() : 0
			def data = programMainService.getByNameAndCode(page, limit, name, code, proIds)
			return ServerResultJson.success(data, count)
		}else {
			return ServerResultJson.success([], 0)
		}
	}
	
	/**
	 * 	保存项目
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//传递参数
		println 'Parameters : \t' + params
		Random rand = new Random()
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		//生成规则PRO+YYYYMMDD+四位随机数
		String code = "PRO"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)
		String name = json.get("name").asString
		int status = json.get("status").asInt
		//String user = json.get("user").asString
		ProgramMain pro = new ProgramMain()
		if(id) {
			pro = programMainService.getProgramById(id);
			pro.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			pro.setModifyuserid(userId)
		}else {
			pro.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			pro.setCreateuserid(userId)
		}
		pro.setCode(code.toUpperCase())
		pro.setName(name)
		pro.setStatus(status)
		systemUserService.save(pro)
		return ServerResultJson.success()
	}
	
	static void main(String[] args) {
		Random rand = new Random()
		String proNo = "PRO"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)
		println "Program code is : $proNo"
	}
}