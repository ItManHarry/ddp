package com.doosan.ddp.pm.controller.biz.tools

import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.comm.utils.fileUtil.FileService
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/comm/file")
class FileController {
    @Autowired
    FileService fileService;

    /**
     *
     * @param file
     * @param fileDirectory
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    def uploadFile(MultipartFile file, @RequestParam("fileDirectory") String fileDirectory){
        String filePath= fileService.uploadFileWithRandomName(file,fileDirectory);
        if(StringUtils.isNotEmpty(filePath)&&!filePath.equals("")){
            return ServerResultJson.success(filePath);
        }
        return ServerResultJson.error();
    }

    /**
     * 删除照片
     * @param filePaths 照片路径list   单个照片路径格式：bucket/子目录/文件名.扩展名   例如：programm/issuepic/c2140a2162464ac3989760ae15676f0d.jpg
     * @return
     */
    @RequestMapping("/remove")
    @ResponseBody
    def removePic(@RequestBody List<String> filePaths){
        fileService.deleteFiles(filePaths)
        return ServerResultJson.success()
    }

    /**
     * 获取照片
     */
    @RequestMapping(value="/get/**",method = RequestMethod.GET)
    @ResponseBody
    void getPic(HttpServletRequest request, HttpServletResponse response){
        String filePath=request.getRequestURI().split("/comm/file/get/")[1];
        InputStream inputStream= fileService.downloadFile(filePath);
        if(inputStream!=null){
            response.setContentType("image/jpeg");
            IOUtils.copy(inputStream,response.getOutputStream());
        }
    }
}
