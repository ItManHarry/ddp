package com.doosan.ddp.pm.controller.biz.pro.issue
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
import com.doosan.ddp.pm.controller.sys.dict.EnumerationController
import com.doosan.ddp.pm.dao.domain.biz.issue.IssueHandleRecord
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramGroup
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.biz.issue.IssueHandleService
import com.doosan.ddp.pm.service.biz.issue.ProgramIssueService
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
/**
 * 项目issue事项
 */
@Controller
@RequestMapping("/pm/biz/pro/issue")
class ProgramIssueController {
	
	final String WEB_URL = "biz/pro/issue"
	@Autowired
	ProgramIssueService programIssueService
	@Autowired
	ProgramGroupService programGroupService
	@Autowired
	ProgramMainService programMainService
	@Autowired
	SystemUserService systemUserService
	@Autowired
	EnumerationController enumerationController
	@Autowired
	IssueHandleService issueHandleService
	
	/**
	 *	跳转项目issue清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/list"
	}
	@ResponseBody
	@GetMapping("/prolist")
	def getProList(HttpServletRequest request) {
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//获取所有项目
		List<ProgramMain> pros = getUserPros(userId)
		println "Program list size is : " + pros.size()
		return ServerResultJson.success(pros)
	}
	@ResponseBody
	@GetMapping("/gm")
	def getGroupMembers(String proId, HttpServletRequest request) {
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		def ul = getUsers(proId)
		return ServerResultJson.success(ul)
	}
	/**
	 * 	保存issue
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
		String programid = json.get("programid").asString
		int issuetype = json.get("issuetype").asInt
		int issuegrade = json.get("issuegrade").asInt
		String issueremark = json.get("issueremark").asString
		String handler = json.get("handler").asString
		int state = json.get("state").asInt
		String finishdate = json.get("finishdate").asString
		//String user = json.get("user").asString
		ProgramIssue issue = new ProgramIssue()
		if(id) {
			issue = programIssueService.getIssueById(id)
			issue.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			issue.setModifyuserid(userCd)
		}else {
			issue.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			issue.setCreateuserid(userCd)
		}
		issue.setProgramid(programid)
		issue.setIssuetype(issuetype)
		issue.setIssuegrade(issuegrade)
		issue.setIssueremark(issueremark)
		issue.setHandler(handler)
		issue.setState(state)
		issue.setFinishdate(finishdate)
		programIssueService.save(issue)
		//新增issue处理履历
		IssueHandleRecord record = new IssueHandleRecord()
		record.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		record.setCreateuserid(userCd)
		record.setIssueid(issue.getTid())
		def stateMap = enumerationController.getOptions('D009')
		record.setState(stateMap.getAt(state+""))
		issueHandleService.save(record)
		return ServerResultJson.success()
	}
	/**
	 * 根据项目ID分页查询issue
	 * @param page
	 * @param limit
	 * @param proId
	 * @return
	 */
	@ResponseBody
	@GetMapping("/query")
	def query(Integer page, Integer limit, String proId, HttpServletRequest request){
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//获取所有项目
		List<ProgramMain> pros = getUserPros(userId)
		def proMap = [:]
		pros.each { 
			proMap.put(it.getTid(), it.getName())
		}
		//获取用户清单
		def ul = getUsers(proId)
		def userMap = [:]
		ul.each { 
			userMap.put(it.getAt("code"), it.getAt("name"))
		}
		def count = programIssueService.getProgramIssuesById(proId).size()
		def data = programIssueService.getProgramIssuesById(proId)
		def typeMap = enumerationController.getOptions('D007')
		def gradeMap = enumerationController.getOptions('D008')
		def stateMap = enumerationController.getOptions('D009')
		data.each { 
			it.setProgram(proMap.get(it.getProgramid()))
			it.setType(typeMap.getAt(it.getIssuetype()+""))
			it.setGrade(gradeMap.getAt(it.getIssuegrade()+""))
			it.setStateStr(stateMap.getAt(it.getState()+""))
			it.setHandlerNm(userMap.get(it.getHandler()))
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 根据用户账号获取参与的项目清单
	 * @param userId
	 * @return
	 */
	def getUserPros(String userId) {
		List<ProgramGroup> groups = programGroupService.getProgramGroupByUserId(userId)
		def proIds = []
		groups.each {
			proIds << it.getProgramid()
		}
		println "Program ids are : $proIds"
		//获取所有项目
		List<ProgramMain> pros = programMainService.getProListForUser(proIds)
		return pros
	}
	/**
	 * 根据项目ID获取项目成员
	 * @param proId
	 * @return
	 */
	def getUsers(String proId) {
		List<ProgramGroup> groups = programGroupService.getProgramGroupByProId(proId)
		List<SystemUser> users = systemUserService.getAll()
		def um = [:]
		users.each { 
			um.put(it.getCode(), it.getName())			
		}
		def ul = [], user = [:]
		groups.each { 
			user = [:]
			user.put("code", it.getUserid())
			user.put("name", um.get(it.getUserid()))
			ul << user
		}
		return ul
	}
	
	/**
	 * 	首页Issue现况图表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/charts")
	def charts(HttpServletRequest request) {
		def data = [:]
		def sc = enumerationController.getOptions('D009')	//issue状态枚举数据
		def pros = programMainService.getAll()
		def xdata = []										//项目名称(x轴数据)
		def pis = []										//项目ID
		pros.each {
			xdata << it.getName()
			pis << it.getTid()
		}
		def item = null, subItem = null
		def ydata = []
		sc.each { k, v ->
			item = [:]
			item.put('name', v)
			item.put('type', 'bar')
			subItem = []
			pis.each{
				subItem << programIssueService.getProgramIssuesByPidAndStat(it, Integer.parseInt(k)).size()
			}
			item.put("data", subItem)
			ydata << item
		}
		data.put("xdata", xdata)
		data.put("ydata", ydata)
		println 'Program chart data : >>>>>>>>>>>>>>>>>>>>>>>>>>>>>' + data
		return ServerResultJson.success(data)
	}
}