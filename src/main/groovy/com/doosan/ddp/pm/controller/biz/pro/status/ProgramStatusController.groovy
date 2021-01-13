package com.doosan.ddp.pm.controller.biz.pro.status
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
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus
import com.doosan.ddp.pm.service.biz.pro.ProgramStatusService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/biz/pro/status")
class ProgramStatusController {
	
	@Autowired
	ProgramStatusService programStatusService
	
	/**
	 * 获取项目状态信息
	 * @param proId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/info")
	def status(String proId, HttpServletRequest request){
		List<ProgramStatus> sts = programStatusService.getProgramStatusByProId(proId)
		if(sts) {
			return ServerResultJson.success(sts.get(0))
		}else {
			return ServerResultJson.success()
		}
	}
	
	/**
	 * 	保存项目状态
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def statusSave(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号&uuid
		def userCd = request.getSession().getAttribute("currentUser")
		//def userId = request.getSession().getAttribute("currentUserId")
		//传递参数
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		String programid = json.get("proId").asString
		String company = json.get("company").asString
		int newpro = json.get("newpro").asInt
		int category = json.get("category").asInt
		int state = json.get("state").asInt
		int possible = json.get("possible").asInt
		String contractstart = json.get("contractstart").asString
		String contractend = json.get("contractend").asString
		String legalorg = json.get("legalorg").asString
		String legaldept = json.get("legaldept").asString
		String ddicdept = json.get("ddicdept").asString
		double budget = json.get("budget").asDouble
		String process = json.get("process").asString
		//String user = json.get("user").asString
		ProgramStatus sts = new ProgramStatus()
		if(id) {
			sts = programStatusService.getById(id)
			sts.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			sts.setModifyuserid(userCd)
		}else {
			sts.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			sts.setCreateuserid(userCd)
		}
		sts.setProgramid(programid)
		sts.setCompany(company)
		sts.setNewpro(newpro)
		sts.setCategory(category)
		sts.setState(state)
		sts.setPossible(possible)
		sts.setContractstart(contractstart)
		sts.setContractend(contractend)
		sts.setLegalorg(legalorg)
		sts.setLegaldept(legaldept)
		sts.setDdicdept(ddicdept)
		sts.setBudget(budget)
		sts.setProcess(process)
		programStatusService.save(sts)
		return ServerResultJson.success()
	}
}