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
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramInvoice
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
import com.doosan.ddp.pm.service.biz.pro.ProgramInvoiceService
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.biz.pro.ProgramStatusService
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
	@Autowired
	ProgramStatusService programStatusService
	@Autowired
	ProgramInvoiceService programInvoiceService
		
	/**
	 *	跳转项目清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/list"
	}
	
	/**
	 * 根据项目或code进行模糊分页查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	@ResponseBody
	@GetMapping("/pm/query")
	def queryForPM(Integer page, Integer limit, String name, String code, HttpServletRequest request){
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		if(userId) {
			//根据用户id获取所属的项目组
//			List<ProgramGroup> groups = programGroupService.getProgramGroupByUserId(userId)
//			def proIds = []
//			groups.each { 
//				proIds << it.getProgramid()
//			}
			def count = programMainService.getCountByNameAndCodeForPM(name, code, userId) ? programMainService.getCountByNameAndCodeForPM(name, code, userId).intValue() : 0
			def data = programMainService.getByNameAndCodeForPM(page, limit, name, code, userId)
			return ServerResultJson.success(data, count)
		}else {
			return ServerResultJson.success([], 0)
		}
	}
	/**
	 * 获取项目状态信息
	 * @param proId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/status")
	def status(String proId, HttpServletRequest request){
		List<ProgramStatus> sts = programStatusService.getProgramStatusByProId(proId)
		if(sts) {
			return ServerResultJson.success(sts.get(0))
		}else {
			return ServerResultJson.success()
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
		//session获取用户账号&uuid
		def userCd = request.getSession().getAttribute("currentUser")
		//def userId = request.getSession().getAttribute("currentUserId")
		//传递参数
		println 'Parameters : \t' + params
		Random rand = new Random()
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		//生成规则PR-YYYYMMDD-四位随机数
		String code = "PR-"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+"-"+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)
		String name = json.get("name").asString
		String remark = json.get("remark").asString
		String startdate = json.get("startdate").asString
		String enddate = json.get("enddate").asString
		double amount = json.get("amount").asDouble
		String contractno = json.get("contractno").asString
		String prno = json.get("prno").asString
		int status = json.get("status").asInt
		//String user = json.get("user").asString
		ProgramMain pro = new ProgramMain()
		if(id) {
			pro = programMainService.getProgramById(id)
			pro.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			pro.setModifyuserid(userCd)
		}else {
			pro.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			pro.setCreateuserid(userCd)
		}
		pro.setCode(code) 
		pro.setName(name)
		pro.setRemark(remark)
		pro.setCharger(userCd)
		pro.setStartdate(startdate)
		pro.setEnddate(enddate)
		pro.setAmount(amount)
		pro.setContractno(contractno)
		pro.setPrno(prno)
		pro.setStatus(status)
		programMainService.save(pro)
		//新建的项目要新增项目成员(PM)
		if(!id) {
			println "Program id is : ${pro.getTid()}"
			ProgramGroup group = new ProgramGroup(programid:pro.getTid(),userid:userCd,grouprole:1)
			group.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			group.setCreateuserid(userCd)
			programGroupService.save(group)
		}
		return ServerResultJson.success()
	}
	/**
	 * 	保存项目状态
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/status/save")
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
	/**
	 * 	保存项目发票
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/invoice/save")
	@ResponseBody
	def invoiceSave(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号&uuid
		def userCd = request.getSession().getAttribute("currentUser")
		//def userId = request.getSession().getAttribute("currentUserId")
		//传递参数
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		String programid = json.get("proId").asString
		int stage = json.get("stage").asInt
		int percent = json.get("percent").asInt
		String invoicedt = json.get("invoicedt").asString
		//String user = json.get("user").asString
		ProgramInvoice invoice = new ProgramInvoice()
		if(id) {
			invoice = programInvoiceService.getById(id)
			invoice.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			invoice.setModifyuserid(userCd)
		}else {
			invoice.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			invoice.setCreateuserid(userCd)
		}
		invoice.setProgramid(programid)
		invoice.setStage(stage)
		invoice.setPercent(percent)
		invoice.setInvoicedt(invoicedt)
		programInvoiceService.save(invoice)
		return ServerResultJson.success()
	}
	/**
	 * 获取项目发票信息
	 * @param proId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/invoices")
	def invoices(String proId, HttpServletRequest request){
		List<ProgramInvoice> invoices = programInvoiceService.getProgramInvoiceByProId(proId)
		invoices.each { 
			switch(it.getStage()) {
				case 1:
					it.setStageStr('首付款')
					break
				case 2:
					it.setStageStr('中期款')
					break
				case 3:
					it.setStageStr('尾款')
					break
				default:
					it.setStageStr('Null')
					break
			}
		}
		if(invoices) {
			return ServerResultJson.success(invoices)
		}else {
			return ServerResultJson.success()
		}
	}
	
	static void main(String[] args) {
		Random rand = new Random()
		String proNo = "PRO"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)+rand.nextInt(10)
		println "Program code is : $proNo"
	}
}