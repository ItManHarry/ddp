package com.doosan.ddp.pm.controller.biz.report
import java.text.SimpleDateFormat

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.comm.utils.ConditionTypeEnum
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.biz.pro.ProgramStatusService
import com.doosan.ddp.pm.utils.excel.ExcelExportService
@Controller
@RequestMapping("/pm/biz/report/pro")
class ProgramReportController {
	
	final String WEB_URL = "biz/report"
	@Autowired
	ProgramMainService programMainService
	@Autowired
	ProgramStatusService programStatusService
	@Autowired
	SystemUserService systemUserService
	@Autowired
	ExcelExportService excelExportService
	/**
	 * 项目报表清单
	 * @return
	 */
	@RequestMapping("/index")
	def index(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/proreport"
	}
	
	/**
	 * 根据项目或code进行模糊分页查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @param conCd
	 * @param prCd
	 * @return
	 */
	@ResponseBody
	@GetMapping("/query")
	def queryForPM(Integer page, Integer limit, String name, String code, String conCd, String prCd, HttpServletRequest request){
		def count = programMainService.getCountForReport(name, code, conCd, prCd) ? programMainService.getCountForReport(name, code, conCd, prCd).intValue():0
		if(count == 0)
			return 	ServerResultJson.success([], 0)
		//项目主数据	
		def data = getProgramReportData(programMainService.getProListForReport(page, limit, name, code, conCd, prCd))	
		def export_data = getProgramReportData(programMainService.getProListForReportAll(name, code, conCd, prCd))
		request.getSession().setAttribute("export_program_data", export_data)
		return ServerResultJson.success(data, count)		
	}
	def getProgramReportData(List<ProgramMain> pros) {
		def reportData = []
		def data = [:]
		//获取项目状态数据
		def stsData = programStatusService.getAll()
		def stsMap = [:]
		stsData.each { 
			stsMap.put(it.getProgramid(), it)
		}
		pros.each { 
			it.setProStatus(stsMap.get(it.getTid()))
			data = [:]
			data.put('tid', it.getTid())
			data.put('company', (it.getProStatus()==null?'':it.getProStatus().getCompany()))
			data.put('newpro', (it.getProStatus()==null?'':it.getProStatus().getNewpro()==1?'是':'否'))
			data.put('name', it.getName())
			data.put('charger', (systemUserService.getUserByCode(it.getCharger())).getName())
			data.put('remark', it.getRemark())
			data.put('category', (it.getProStatus()==null?'':it.getProStatus().getCategory()==1?'C&SI服务卖出':'C&SI商品卖出'))
			data.put('status', (it.getProStatus()==null?'':getProgramStatus(it.getProStatus().getState())))
			data.put('possible', (it.getProStatus()==null?'':it.getProStatus().getPossible()+'%'))
			data.put('contractstart', (it.getProStatus()==null?'':it.getProStatus().getContractstart()))
			data.put('contractend', (it.getProStatus()==null?'':it.getProStatus().getContractend()))
			data.put('legalorg', (it.getProStatus()==null?'':it.getProStatus().getLegalorg()))
			data.put('legaldept', (it.getProStatus()==null?'':it.getProStatus().getLegaldept()))
			data.put('ddicdept', (it.getProStatus()==null?'':it.getProStatus().getDdicdept()))
			data.put('amount', it.getAmount())
			data.put('process', (it.getProStatus()==null?'':it.getProStatus().getProcess()))
			reportData << data
		}
		return reportData
	}
	
	def getProgramStatus(int sts) {
		def stsStr = ''
		switch(sts) {
			case 1:
				stsStr = '等待'
				break
			case 2:
				stsStr = '合同准备'
				break
			case 3:
				stsStr = '起案进行'
				break
			case 4:
				stsStr = '进行中'
				break
			case 5:
				stsStr = '结束'
				break
		}
		return stsStr
	}
	/**
	 * 数据导出
	 * @param response
	 * @return
	 */
	@RequestMapping("/export")
	@ResponseBody
	String export(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("application/binary;charset=UTF-8")
		try{
			ServletOutputStream out = response.getOutputStream()
			String fileName = new String(("PROJECT-REPORT-DATA"+ new SimpleDateFormat("yyyyMMdd").format(new Date())).getBytes(),"UTF-8")
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls")
			def titles = ["客户公司", "是否为新项目","项目名","项目担当","项目说明","分类","Status","项目执行可能性","合同(开始日期)","合同(结束日期)","法人","	客户公司主管部门","我司主管部门","事业预算","进行情况"]
			def data = [];
			def rows = request.getSession().getAttribute("export_program_data")
			rows.each{
				def rowData = [];
				rowData << it.getAt('company')
				rowData << it.getAt('newpro')
				rowData << it.getAt('name')
				rowData << it.getAt('charger')
				rowData << it.getAt('remark')
				rowData << it.getAt('category')
				rowData << it.getAt('status')
				rowData << it.getAt('possible')
				rowData << it.getAt('contractstart')
				rowData << it.getAt('contractend')
				rowData << it.getAt('legalorg')
				rowData << it.getAt('legaldept')
				rowData << it.getAt('ddicdept')
				rowData << it.getAt('amount')
				rowData << it.getAt('process')
				data << rowData
			}
			excelExportService.export(titles, data, out)
			return "success"
		} catch(Exception e){
			e.printStackTrace()
			return "Excel导出失败"
		}
	}
}