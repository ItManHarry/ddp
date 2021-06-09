package com.doosan.ddp.pm.controller.biz.report

import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.comm.utils.ConditionTypeEnum
import com.doosan.ddp.pm.comm.utils.excelUtil.ExcelUtils
import com.doosan.ddp.pm.controller.biz.pro.issue.ProgramIssueController
import com.doosan.ddp.pm.controller.sys.dict.EnumerationController
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.service.biz.issue.ProgramIssueService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat

@Controller
@RequestMapping("/pm/biz/report")
class IssueReportController {
    final String WEB_URL = "biz/report"

    @Autowired
    ProgramIssueService programIssueService
    @Autowired
    EnumerationController enumerationController
    @Autowired
    SystemUserService systemUserService;
    @Autowired
    ProgramIssueController programIssueController;

    /**
     *	跳转项目issue清单
     * 	@return
     */
    @RequestMapping("/issuereport")
    def list(HttpServletRequest request, Map map) {
        def userId = request.getSession().getAttribute("currentUserId")
        println "User uuid is : $userId"
        return WEB_URL + "/issuereport"
    }

    @ResponseBody
    @GetMapping("/queryByPage")
    def queryByPage(Integer page, Integer limit, String programid, String issuetype,String issuegrade,String state,String finishdate_2, HttpServletRequest request){
        return query(true, page, limit, programid, issuetype, issuegrade, state, finishdate_2, request)
    }

    @ResponseBody
    @GetMapping("/excelDownload")
    def excelDownload(String programid, String issuetype, String issuegrade, String state, String finishdate_2, HttpServletRequest request, HttpServletResponse response){
        def data = query(false, 1, 100000, programid, issuetype, issuegrade, state, finishdate_2, request)
        def dataList = data.getData()
        def filePath = request.getServletContext().getRealPath("/")
        def fileName = "issue报表" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
        def fields= [:]
        fields.put("program", "项目所属");
        fields.put("type", "Issue类型");
        fields.put("grade", "紧急度");
        fields.put("handlerNm", "处理人员");
        fields.put("issueremark", "issue内容");
        fields.put("stateStr", "Issue状态");

        ExcelUtils.DTOListToExcel(filePath,fileName,dataList,fields,request,response)
    }

    def query(boolean isPageBreak, Integer page, Integer limit, String programid, String issuetype,String issuegrade,String state,String finishdate_2, HttpServletRequest request){
        if(page==null){
            page=1
        }
        if(limit==null){
            limit=10
        }
        //session获取用户账号
        def userId = request.getSession().getAttribute("currentUser")
        //获取所有项目
        List<ProgramMain> pros = programIssueController.getUserPros(userId)
        def proMap = [:]
        def proList = []
        pros.each {
            proMap.put(it.getTid(), it.getName())
            proList.add(it.getTid())
        }
        //获取用户清单
        def ul = systemUserService.getAll();
        def userMap = [:]
        ul.each {
            userMap.put(it.getAt("code"), it.getAt("name"))
        }
        ProgramIssue programIssue=new ProgramIssue();
        //数据过滤，只查询用户加入的项目
        if(CollectionUtils.isNotEmpty(proList)||proList.size()>0){
            programIssue.setProgramid_2(StringUtils.join(proList.toArray(),","));
            programIssue.getMapCondition().put("programid_2",ConditionTypeEnum.IN);
        }
        //前台查询条件过滤
        if(StringUtils.isNotEmpty(programid)){
            programIssue.setProgramid(programid);
            programIssue.getMapCondition().put("programid", ConditionTypeEnum.EQ);
        }
        if(StringUtils.isNotEmpty(issuetype)){
            programIssue.setIssuetype(Integer.valueOf(issuetype));
            programIssue.getMapCondition().put("issuetype", ConditionTypeEnum.EQ);
        }
        if(StringUtils.isNotEmpty(issuegrade)){
            programIssue.setIssuegrade(Integer.valueOf(issuegrade));
            programIssue.getMapCondition().put("issuegrade", ConditionTypeEnum.EQ);
        }
        if(StringUtils.isNotEmpty(state)){
            programIssue.setState(Integer.valueOf(state));
            programIssue.getMapCondition().put("state", ConditionTypeEnum.EQ);
        }
        if(StringUtils.isNotEmpty(finishdate_2)){
            programIssue.setFinishdate(finishdate_2.split(",")[0]);
            programIssue.getMapCondition().put("finishdate", ConditionTypeEnum.GE);
            programIssue.setFinishdate_2(finishdate_2.split(",")[1]);
            programIssue.getMapCondition().put("finishdate_2", ConditionTypeEnum.LE);
        }
        def data,dataList,count
        if(isPageBreak) {
            data = programIssueService.searchByMultiParamsAndPage(programIssue,page,limit)
            dataList=data.getContent()
            count=data.getTotalElements().intValue()
        }else{
            data = programIssueService.searchByMultiParams(programIssue)
            dataList=data
            count=data.size()
        }
        def typeMap = enumerationController.getOptions('D007')
        def gradeMap = enumerationController.getOptions('D008')
        def stateMap = enumerationController.getOptions('D009')
        dataList.each {
            it.setProgram(proMap.get(it.getProgramid()))
            it.setType(typeMap.getAt(it.getIssuetype()+""))
            it.setGrade(gradeMap.getAt(it.getIssuegrade()+""))
            it.setStateStr(stateMap.getAt(it.getState()+""))
            it.setHandlerNm(userMap.get(it.getHandler()))
        }
        return ServerResultJson.success(dataList, count)
    }
}
