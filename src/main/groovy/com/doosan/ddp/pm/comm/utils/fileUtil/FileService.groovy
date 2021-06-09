package com.doosan.ddp.pm.comm.utils.fileUtil

import org.springframework.web.multipart.MultipartFile

interface FileService {
    /**
     * 上传文件，以随机文件名保存
     * @param file web页传过来的图片，MultipartFile格式
     * @param secondDirectory 保存目录，像是 issue, 或者 issue/pic/202103
     * @return 文件保存路径
     * @throws IOException
     */
    public String uploadFileWithRandomName(MultipartFile file, String secondDirectory) throws IOException;

    public String uploadFileWithOriginalName(MultipartFile file);

    public InputStream downloadFile(String fullPath);

    public void deleteFiles(List<String> filePaths);

}