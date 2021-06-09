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
import com.doosan.ddp.pm.controller.sys.dict.EnumerationController
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus
import com.doosan.ddp.pm.service.biz.pro.ProgramInvoiceService
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.utils.excel.ExcelExportService
@Controller
@RequestMapping("/pm/biz/report/pro/invoice")
class ProgramInvoiceReportController {
	
	final String WEB_URL = "biz/report"
	@Autowired
	ProgramMainService programMainService
	@Autowired
	ProgramInvoiceService programInvoiceService
	@Autowired
	SystemUserService systemUserService
	@Autowired
	ExcelExportService excelExportService
	@Autowired
	EnumerationController enumerationController
	/**
	 * 项目发票报表清单
	 * @return
	 */
	@RequestMapping("/index")
	def index(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/invoicereport"
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
		request.getSession().setAttribute("export_program_invoice_data", export_data)
		return ServerResultJson.success(data, count)		
	}
	def getProgramReportData(List<ProgramMain> pros) {
		def im = enumerationController.getOptions('D006')	//项目发票区分
		println "Invoice map : " + im
		def reportData = []
		def data = [:]
		pros.each { 
			//发票信息
			def invoices = programInvoiceService.getProgramInvoiceByProId(it.getTid())
			if(invoices) {
				println it.getName() + "'s Invoice items : " + invoices.size()
				def pro = it
				int cnt = 0
				invoices.each{
					cnt += 1
					println "Count is >>>>>>>>>>>>>>>>>>>>>>> $cnt" 
					data = [:]
					//主表信息
					data.put('tid', pro.getTid())
					data.put('name', pro.getName())
					data.put('properiod', pro.getStartdate() + "~" + pro.getEnddate())
					data.put('amount', pro.getAmount())
					data.put('charger', (systemUserService.getUserByCode(pro.getCharger())).getName())
					//发票信息
					data.put('stage', im.getAt(it.getStage()+'')+'('+it.getPercent()+'%)')
					data.put('amountpart', pro.getAmount()*it.getPercent()/100)
					data.put('makeoutStr', it.getMakeout()==1?'是':'否')
					data.put('invoicedt', (it.getInvoicedt()==null?'-':it.getInvoicedt()))
					data.put('checkeddt', (it.getCheckeddt()==null?'-':it.getCheckeddt()))
					data.put('remark', (it.getRemark()==null?'-':it.getRemark()))
					//行数(供合并单元格)
					data.put('rowspan', cnt==1?invoices.size():0)
					reportData << data
				}
			}else {
				println it.getName() + "'s Invoice items is zero "
				data = [:]
				//主表信息
				data.put('tid', it.getTid())
				data.put('name', it.getName())
				data.put('properiod', it.getStartdate() + "~" + it.getEnddate())
				data.put('amount', it.getAmount())
				data.put('charger', (systemUserService.getUserByCode(it.getCharger())).getName())
				//发票信息
				data.put('stage', '-')
				data.put('amountpart', '-')
				data.put('makeoutStr', '-')
				data.put('invoicedt', '-')
				data.put('checkeddt', '-')
				data.put('remark', '-')
				data.put('rowspan', 1)
				reportData << data
			}			
		}
		return reportData
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
			String fileName = new String(("INVOICE-REPORT-DATA"+ new SimpleDateFormat("yyyyMMdd").format(new Date())).getBytes(),"UTF-8")
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls")
			def titles = ["项目名称", "项目区间", "项目金额", "担当", "区分", "金额", "是否已开票", "开票时间", "验收时间", "备注","RowSpan"]
			def data = []
			def rows = request.getSession().getAttribute("export_program_invoice_data")
			rows.each{
				def rowData = [];
				rowData << it.getAt('name')
				rowData << it.getAt('properiod')
				rowData << it.getAt('amount')
				rowData << it.getAt('charger')
				rowData << it.getAt('stage')
				rowData << it.getAt('amountpart')
				rowData << it.getAt('makeoutStr')
				rowData << it.getAt('invoicedt')
				rowData << it.getAt('checkeddt')
				rowData << it.getAt('remark')
				rowData << it.getAt('rowspan')
				data << rowData
			}
			excelExportService.joinExport(titles, data, out)
			return "success"
		} catch(Exception e){
			e.printStackTrace()
			return "Excel导出失败"
		}
	}
}