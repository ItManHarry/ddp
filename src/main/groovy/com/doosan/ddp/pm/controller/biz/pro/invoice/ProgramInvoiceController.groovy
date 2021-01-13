package com.doosan.ddp.pm.controller.biz.pro.invoice
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
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramInvoice
import com.doosan.ddp.pm.service.biz.pro.ProgramInvoiceService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/biz/pro/invoice")
class ProgramInvoiceController {
	
	@Autowired
	ProgramInvoiceService programInvoiceService
	
	/**
	 * 	保存项目发票
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
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
	@GetMapping("/list")
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
}