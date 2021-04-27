package com.doosan.ddp.pm.controller.biz.svn
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.wc.SVNRevision
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.comm.results.ServerResults
import com.doosan.ddp.pm.controller.biz.pro.issue.ProgramIssueController
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import com.doosan.ddp.pm.utils.svn.SVNUtil
import com.doosan.ddp.pm.utils.svn.SvnOperates
import com.doosan.ddp.pm.utils.svn.bean.SvnEntry
/**
 * 项目SVN管理
 */
@Controller
@RequestMapping("/pm/biz/pro/svn")
class ProgramSvnController {
	
	final String WEB_URL = "biz/pro/svn"
	@Autowired
	ProgramMainService programMainService
	@Autowired
	SystemUserService systemUserService
	@Autowired
	ProgramIssueController programIssueController
	@Value('${svn.temp}')
	String svnTempPath
	
	/**
	 *	跳转项目SVN清单 - 树结构
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/list"
	}
	/**
	 *	跳转项目SVN清单 - ElementUI树表格
	 * 	@return
	 */
	@RequestMapping("/table")
	def table(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/tablelist"
	}
		
	@ResponseBody
	@GetMapping("/prolist")
	def getProList(HttpServletRequest request) {
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//获取所有参与的项目
		List<ProgramMain> pros = programIssueController.getUserPros(userId)		
		return ServerResultJson.success(pros)
	}
	/**
	 * 获取svn文档
	 * @param path	--svn相对路径
	 * @param id	--树节点id
	 * @param proId	--项目id		
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/docs")
	def getSvnDocuments(String path,String id, String proId, HttpServletRequest request) {
		SVNUtil svn = null
		String currentPropramId = request.getSession().getAttribute("programid")==null ? "" : request.getSession().getAttribute("programid")
		if(currentPropramId && currentPropramId.equals(proId)){
			println "获取session中的svn实例>>>>>>>>>>>>>"
			svn = request.getSession().getAttribute("svninstance")
		}else{
			println "重新构筑svn实例>>>>>>>>>>>>>"
			//session获取用户账号
			def userId = request.getSession().getAttribute("currentUser")
			SystemUser user = systemUserService.getUserByCode(userId)
			if(user.getSvncode() == null) {
				request.getSession().removeAttribute("programid")
				return ServerResultJson.error(ServerResults.ERROR_SVN_CODE)
			}
			if(user.getSvnpwd() == null) {
				request.getSession().removeAttribute("programid")
				return ServerResultJson.error(ServerResults.ERROR_SVN_PWD)
			}
			ProgramMain program = programMainService.getProgramById(proId)
			if(program.getSvnadd() == null) {
				request.getSession().removeAttribute("programid")
				return ServerResultJson.error(ServerResults.ERROR_SVN_ADD)
			}
			svn = new SVNUtil(user.getSvncode(), user.getSvnpwd(), program.getSvnadd())
			//将SVN库放入session中,便于后续其他的SVN操作
			request.getSession().setAttribute("svninstance", svn)
			request.getSession().setAttribute("programid", proId)
		}		
		SVNRepository repository = null
		try {
			repository = svn.getSVNRepository()
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_AUTH) 
		}		
		SvnOperates op = new SvnOperates()
		//获取svn节点
		/*SvnEntry root = null
		if(id == '0') {
			root = new SvnEntry(
				name:'Root',
				path:'/',
				leaf:false,
				parent:true,
				icon:"/static/images/ztree/folder_close.png",
				iconOpen:"/static/images/ztree/folder_open.png",
				iconClose:"/static/images/ztree/folder_close.png",
				open:true,
				id:id,
				pId:null)
			def nodes = op.listEntries(repository, path, id)
			root.setChildren(nodes)
			return ServerResultJson.success(root)
		}else {
			def nodes = op.listEntries(repository, path, id)
			return ServerResultJson.success(nodes)
		}*/	
		//获取svn子目录
		def nodes = [], data = []
		//println "The path is : $path"
		try {
			nodes = op.listEntries(repository, path, id)
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_AUTH)
		}
		if(nodes) {
			def nodeMap = [:]
			//按照文档名称排序
			def names = []
			nodes.each {
				names << it.getName()
				nodeMap.put(it.getName(), it)
			}			
			names.sort().each {
				println 'Name :' + it
				data << nodeMap.get(it)
			}
		}else {
			if(path == "/") {
				SvnEntry node = new SvnEntry(
					name:"/",
					path:"/",
					leaf:false,
					parent:true,
					hasChildren:true,
					icon:"/static/images/ztree/folder_close.png",
					iconOpen:"/static/images/ztree/folder_open.png",
					iconClose:"/static/images/ztree/folder_close.png",
					open:true,
					id:UUID.randomUUID().toString().replaceAll("-",""),
					pId:null)
				data << node
			}			
		}		
		return ServerResultJson.success(data)
	}
	/**
	 * 	创建svn目录
	 * @param path		//当前路径
	 * @param folder	//文件夹名称
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/mkdir")
	def makeDir(String path, String folder, HttpServletRequest request) {
		SVNUtil svn = request.getSession().getAttribute("svninstance")
		String svnPath = svn.getSvnUrl()
		String folderPath = svnPath + path + "/" + folder
		SVNURL url = SVNURL.parseURIEncoded(folderPath)
		SvnOperates op = new SvnOperates()
		try{
			op.makeDirectory(svn.getSVNClientManager(svn.getSVNRepository()), url, 'new folder , add by program system!')
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_FOLDER)
		}		
		return ServerResultJson.success()
	}
	/**
	 * 删除目录/文件
	 * @param path
	 * @param folder
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/delete")
	def delete(String path, HttpServletRequest request) {
		SVNUtil svn = request.getSession().getAttribute("svninstance")
		String svnPath = svn.getSvnUrl()
		String deletePath = svnPath + path
		SVNURL url = SVNURL.parseURIEncoded(deletePath)
		SvnOperates op = new SvnOperates()
		try{
			op.delete(svn.getSVNClientManager(svn.getSVNRepository()), url, 'delete folder/file , deleted by program system!')
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_DELETE)
		}
		return ServerResultJson.success()
	}
	/**
	 *  下载
	 * @param path
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/checkout")
	def checkout(String path, HttpServletRequest request) {
		String fileName = path.split("/")[-1]
		String folderName = path.split("/")[-2]
		File toDiskPath = new File(svnTempPath+"/"+folderName)
		//用于svn checkout的目录必须为空，所以每次要删除后重建
		if(toDiskPath.exists())
			toDiskPath.delete()		
		toDiskPath.mkdirs()
		SVNUtil svn = request.getSession().getAttribute("svninstance")
		String svnPath = svn.getSvnUrl()
		String copyPath = svnPath + path.substring(0, path.lastIndexOf("/"))
		SVNURL url = SVNURL.parseURIEncoded(copyPath)
		SvnOperates op = new SvnOperates()
		try{
			op.checkout(svn.getSVNClientManager(svn.getSVNRepository()), url, SVNRevision.HEAD, toDiskPath, true)
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_CHECKOUT)
		}
		String downloadFilePath = svnTempPath+"/"+folderName+"/"+fileName
		request.getSession().setAttribute("downloadFileName", fileName)			//下载文件名称
		request.getSession().setAttribute("downloadFilePath", downloadFilePath)	//下载文件路径
		return ServerResultJson.success()
	}
	/**
	 * 文件下载
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
	def download(HttpServletRequest request, HttpServletResponse response) {
		String fileName = request.getSession().getAttribute("downloadFileName")	
		String filePath = request.getSession().getAttribute("downloadFilePath")
		try {
			InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)))
			fileName = URLEncoder.encode(fileName,"UTF-8")
			//设置文件下载头
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName)
			//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())
			byte[] buffer = new byte[1024]
			int len = bis.read(buffer)
			while(len != -1){
				out.write(len)
				out.flush()
				len = bis.read(buffer)
			}
			out.close()
		} catch (Exception e) {
			println "文件下载异常" + e
		}
	}
}