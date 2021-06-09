package com.doosan.ddp.pm.comm.utils.excelUtil

import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.lang.reflect.Field
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExcelUtils {
    private static int sheetSize = 100000;
    ExcelUtils(){

    }

    public static <T> void DTOListToExcel(String filePath, String fileName, List<T> data, Map<String, String> fields, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        workbook.setCompressTempFiles(true);
        fileName = fileName + ".xlsx";
        ServletOutputStream rout = response.getOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(rout);
        try {
            if (data == null || data.size() == 0) {
                workbook.close();
                throw new Exception("提供的数据为空");
            }
            int pages = data.size() / sheetSize;
            if (data.size() % sheetSize > 0) {
                ++pages;
            }
            String[] egtitles = new String[fields.size()];
            String[] cntitles = new String[fields.size()];
            Iterator<String> it = fields.keySet().iterator();
            String type;
            for(int count = 0; it.hasNext(); ++count) {
                String egtitle = (String)it.next();
                type = (String)fields.get(egtitle);
                egtitles[count] = egtitle;
                cntitles[count] = type;
            }
            int startIndex;
            for(int i = 0; i < pages; ++i) {
                int rownum = 0;
                startIndex = i * sheetSize;
                int endIndex = (i + 1) * sheetSize - 1 > data.size() ? data.size() : (i + 1) * sheetSize - 1;
                SXSSFSheet sheet = workbook.createSheet("Sheet" + (i + 1));
                SXSSFRow row = sheet.createRow(rownum);
                int j;
                for(j = 0; j < cntitles.length; ++j) {
                    SXSSFCell cell = row.createCell(j);
                    cell.setCellValue(cntitles[j]);
                }
                rownum = rownum + 1;
                for(j = startIndex; j < endIndex; ++j) {
                    row = sheet.createRow(rownum);
                    T item = data.get(j);

                    for(int h = 0; h < cntitles.length; ++h) {
                        Field fd = item.getClass().getDeclaredField(egtitles[h]);
                        fd.setAccessible(true);
                        Object o = fd.get(item);
                        String value = o == null ? "" : String.valueOf(o);
                        SXSSFCell cell = row.createCell(h);
                        cell.setCellValue(value);
                    }
                    ++rownum;
                }
            }
            workbook.write(out);
            workbook.dispose();
            workbook.close();
            out.flush();
            out.close();

            type = request.getServletContext().getMimeType(fileName);
            response.setHeader("Content-Type", type + ";charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));

            rout.flush();
            rout.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != workbook) {
                workbook.dispose();
                workbook.close();
            }
            if (null != out) out.close();
            if (null != rout) rout.close();
        }
    }

    public static void MAPListToExcel(String filePath, String fileName, List<Map<String, Object>> data, Map<String, String> fields, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        workbook.setCompressTempFiles(true);
        fileName = fileName + ".xlsx";
        ServletOutputStream rout = response.getOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(rout);
        try {
            if (data == null || data.size() == 0) {
                workbook.close();
                throw new Exception("提供的数据为空");
            }
            int pages = data.size() / sheetSize;
            if (data.size() % sheetSize > 0) {
                ++pages;
            }
            String[] egtitles = new String[fields.size()];
            String[] cntitles = new String[fields.size()];
            Iterator<String> it = fields.keySet().iterator();
            String type;
            for(int count = 0; it.hasNext(); ++count) {
                String egtitle = (String)it.next();
                type = (String)fields.get(egtitle);
                egtitles[count] = egtitle;
                cntitles[count] = type;
            }
            int startIndex;
            for(int i = 0; i < pages; ++i) {
                int rownum = 0;
                startIndex = i * sheetSize;
                int endIndex = (i + 1) * sheetSize - 1 > data.size() ? data.size() : (i + 1) * sheetSize - 1;
                SXSSFSheet sheet = workbook.createSheet("Sheet" + (i + 1));
                SXSSFRow row = sheet.createRow(rownum);
                int j;
                for(j = 0; j < cntitles.length; ++j) {
                    SXSSFCell cell = row.createCell(j);
                    cell.setCellValue(cntitles[j]);
                }
                rownum = rownum + 1;
                for(j = startIndex; j < endIndex; ++j) {
                    row = sheet.createRow(rownum);
                    Map<String, Object> currentMap = (Map)data.get(j);
                    for(int h = 0; h < cntitles.length; ++h) {
                        Object s = currentMap.get(egtitles[h]);
                        String value = s == null ? "" : String.valueOf(s);
                        SXSSFCell cell = row.createCell(h);
                        cell.setCellValue(value);
                    }
                    ++rownum;
                }
            }
            workbook.write(out);
            workbook.dispose();
            workbook.close();
            out.flush();
            out.close();

            type = request.getServletContext().getMimeType(fileName);
            response.setHeader("Content-Type", type + ";charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));

            rout.flush();
            rout.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != workbook) {
                workbook.dispose();
                workbook.close();
            }
            if (null != out) out.close();
            if (null != rout) rout.close();
        }
    }

    public static void MAPListToZipExcel(String filePath, String fileName, List<Map<String, Object>> data, Map<String, String> fields, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletOutputStream rout = null;
        ByteArrayOutputStream bzout = null;
        ByteArrayInputStream inputStream= null;
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        workbook.setCompressTempFiles(true);
        File excelFile = new File(filePath + fileName + ".xlsx");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(excelFile));
        try {
            if (data == null || data.size() == 0) {
                workbook.close();
                throw new Exception("提供的数据为空");
            }
            int pages = data.size() / sheetSize;
            if (data.size() % sheetSize > 0) {
                ++pages;
            }
            String[] egtitles = new String[fields.size()];
            String[] cntitles = new String[fields.size()];
            Iterator<String> it = fields.keySet().iterator();
            String type;
            for(int count = 0; it.hasNext(); ++count) {
                String egtitle = (String)it.next();
                type = (String)fields.get(egtitle);
                egtitles[count] = egtitle;
                cntitles[count] = type;
            }
            int startIndex;
            for(int i = 0; i < pages; ++i) {
                int rownum = 0;
                startIndex = i * sheetSize;
                int endIndex = (i + 1) * sheetSize - 1 > data.size() ? data.size() : (i + 1) * sheetSize - 1;
                SXSSFSheet sheet = workbook.createSheet("Sheet" + (i + 1));
                SXSSFRow row = sheet.createRow(rownum);
                int j;
                for(j = 0; j < cntitles.length; ++j) {
                    SXSSFCell cell = row.createCell(j);
                    cell.setCellValue(cntitles[j]);
                }
                rownum = rownum + 1;
                for(j = startIndex; j < endIndex; ++j) {
                    row = sheet.createRow(rownum);
                    Map<String, Object> currentMap = (Map)data.get(j);
                    for(int h = 0; h < cntitles.length; ++h) {
                        Object s = currentMap.get(egtitles[h]);
                        String value = s == null ? "" : String.valueOf(s);
                        SXSSFCell cell = row.createCell(h);
                        cell.setCellValue(value);
                    }
                    ++rownum;
                }
            }
            workbook.write(out);
            workbook.dispose();
            workbook.close();
            out.flush();
            out.close();
            bzout = transToZip(excelFile);
            byte[] byteArray = bzout.toByteArray();
            fileName = fileName + ".zip";
            type = request.getServletContext().getMimeType(fileName);
            response.setHeader("Content-Type", type + ";charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            rout = response.getOutputStream();
            inputStream = new ByteArrayInputStream(byteArray);
            int len = false;
            byte[] b = new byte[1024];
            while((startIndex = inputStream.read(b)) != -1) {
                rout.write(b, 0, startIndex);
            }
            inputStream.close();
            rout.flush();
            rout.close();
            bzout.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != workbook) {
                workbook.dispose();
                workbook.close();
            }
            if (null != out) out.close();
            if (null != inputStream) inputStream.close();
            if (null != rout) rout.close();
            if (null != bzout) bzout.close();
        }
    }

    public static ByteArrayOutputStream transToZip(File excelFile) throws Exception {
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(bout));

        try {
            if (!excelFile.exists()) {
                throw new Exception("要进行压缩的Excel文件不存在");
            }
            byte[] buffer = new byte[1024];
            int len;
            if (excelFile.isFile()) {
                ZipEntry entry = new ZipEntry(excelFile.getName());
                zout.putNextEntry(entry);
                bufferedInputStream = new BufferedInputStream(new FileInputStream(excelFile));
                while((len = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                    zout.write(buffer, 0, len);
                }
                zout.closeEntry();
                bufferedInputStream.close();
            }
            if (excelFile.isDirectory()) {
                File[] files = excelFile.listFiles();
                for(int i = 0; i < files.length; i++) {
                    File file = files[i];
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    while((len = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                        zout.write(buffer, 0, len);
                    }
                    zout.closeEntry();
                    bufferedInputStream.close();
                }
            }
            zout.flush();
            bout.flush();
            zout.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedInputStream) bufferedInputStream.close();
            if (null != zout) zout.close();
            if (null != excelFile) excelFile.delete();
        }
        return bout;
    }

}
