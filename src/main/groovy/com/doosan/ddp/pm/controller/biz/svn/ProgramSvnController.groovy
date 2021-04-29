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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
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
	@RequestMapping("/doclist")
	def doclist(HttpServletRequest request, Map map) {
		def userId = request.getSession().getAttribute("currentUserId")
		println "User uuid is : $userId"
		return WEB_URL + "/doclist"
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
	 * checkout svn
	 * @param path		//svn路径
	 * @param action	//action标识："upload":用以checkout svn后上传文件 ； "download"：用以checkout后下载文件
	 * @param request
	 * @return
	 */
	@ResponseBody
	@GetMapping("/checkout")
	def checkout(String path, String action, HttpServletRequest request) {
		String fileName = ""		//下载文件名称
		String folderName = ""		//SVN checkout目录
		String copyPath = ""		//SVN checkout路径
		SVNUtil svn = request.getSession().getAttribute("svninstance")
		String svnPath = svn.getSvnUrl()
		if(action == 'upload') {
			folderName = path.split("/")[-1]
			copyPath = svnPath + path
		}else {
			fileName = path.split("/")[-1]
			folderName = path.split("/")[-2]
			copyPath = svnPath + path.substring(0, path.lastIndexOf("/"))
		}
		File toDiskPath = new File(svnTempPath+"/"+folderName)
		//用于svn checkout的目录必须为空，所以每次要删除后重建
		if(toDiskPath.exists()) {
			deleteFile(toDiskPath)
		}
		toDiskPath.mkdirs()		
		SVNURL url = SVNURL.parseURIEncoded(copyPath)
		SvnOperates op = new SvnOperates()
		try{
			op.checkout(svn.getSVNClientManager(svn.getSVNRepository()), url, SVNRevision.HEAD, toDiskPath, true)
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_CHECKOUT)
		}
		String filePath = svnTempPath+"/"+folderName+"/"+fileName
		String workingCopyPath = svnTempPath+"/"+folderName
		request.getSession().setAttribute("downloadFileName", fileName)			//下载文件名称
		request.getSession().setAttribute("downloadFilePath", filePath)			//下载文件路径
		request.getSession().setAttribute("workingCopyPath", workingCopyPath)	//checkout工作空间路径
		return ServerResultJson.success()
	}
	/**
	 * 上传文件至临时工作区
	 * @param file
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/upload")
	def upload(MultipartFile file, HttpServletRequest request){
		//上传路径
		println ">>>>>>>>>>>>>>>>>>>>>>>>>>> do the upload action..."
		String workingCopyPath = request.getSession().getAttribute("workingCopyPath")
		println "File Name : " + file.getOriginalFilename()
		println "File type : " + file.getContentType()
		File dest = new File(workingCopyPath+"/"+file.getOriginalFilename())
		//若存在,执行删除后再上传
		if(dest.exists())
			dest.delete()
		file.transferTo(dest)
		//文件上传成功后执行SVN更新
		SVNUtil svn = request.getSession().getAttribute("svninstance")
		SvnOperates op = new SvnOperates()
		try{
			op.update(svn.getSVNClientManager(svn.getSVNRepository()), new File(workingCopyPath), SVNRevision.HEAD, true)
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_UPDATE)
		}
		println "SVN更新成功，执行版本控制添加>>>>>>>>>>>>>>>>>>>>>>"
		try{
			op.addVersionToSvn(svn.getSVNClientManager(svn.getSVNRepository()), new File(workingCopyPath), true)
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_VERSION)
		}
		println "SVN版本控制添加成功，执行SVN提交>>>>>>>>>>>>>>>>>>>>>>"
		//SVN提交
		try{
			op.commit(svn.getSVNClientManager(svn.getSVNRepository()), new File(workingCopyPath), false, "add new file ")
		}catch(Exception e) {
			return ServerResultJson.error(ServerResults.ERROR_SVN_COMMIT)
		}
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
		File file = new File(filePath)
		fileName = URLEncoder.encode(fileName,"UTF-8")
		if (file.exists()) {
			try {
				//以流的形式下载文件。
				InputStream input = new BufferedInputStream(new FileInputStream(file.getPath()))
				byte[] buffer = new byte[input.available()]
				input.read(buffer)
				input.close()
				//清空response
				response.reset()
				generate(fileName, request, response)
				OutputStream output = new BufferedOutputStream(response.getOutputStream())
				output.write(buffer)
				output.flush()
				output.close()
			} catch (IOException e) {
				e.printStackTrace()
				println "文件下载异常" + e
			}
		}
	}
	/**
	 * 处理文件名/内容乱码和浏览器兼容问题
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	void generate(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8")
		response.setContentType("application/octet-stream")
		response.setHeader("success", "true")
		String userAgent = request.getHeader("User-Agent")
		String formFileName = ""
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			// 针对IE或者以IE为内核的浏览器：
			formFileName = URLEncoder.encode(fileName, "UTF-8")
		} else {
			// 非IE浏览器的处理：
			formFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1")
		}
		//如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
		response.setHeader("Content-Disposition", "attachment;filename=" + formFileName)
	}
	/**
	 * 删除文件/文件夹
	 * @param file
	 */
	void deleteFile(File file) {
		if(file.exists()) {
			if(file.isFile()) {
				file.delete()
			}else{
				def files = file.listFiles()
				files.each { 
					deleteFile(it)
				}
				file.delete()
			}
		}else {
			println "要删除的文件不存在!"
		}
	}
}