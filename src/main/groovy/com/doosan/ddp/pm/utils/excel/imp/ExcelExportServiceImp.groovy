package com.doosan.ddp.pm.utils.excel.imp
import javax.servlet.ServletOutputStream
import org.apache.log4j.Logger
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.util.CellRangeAddress
import org.springframework.stereotype.Service
import com.doosan.ddp.pm.utils.excel.ExcelExportService
@Service
class ExcelExportServiceImp implements ExcelExportService {

	void export(List<String> titles, List<List<String>> data, ServletOutputStream out) {
		try{
			// 第一步，创建一个workbook，对应一个Excel文件
			HSSFWorkbook workbook = new HSSFWorkbook()
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = workbook.createSheet("sheet1")
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = sheet.createRow(0)
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = workbook.createCellStyle()
			//居中样式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER)
			HSSFCell cell = null
			println("表头列数 : " + titles.size())
			for (int i = 0; i < titles.size(); i++) {
				cell = row.createCell(i)			//列索引从0开始
				cell.setCellValue(titles.get(i))	//列名
				cell.setCellStyle(style)			//列居中显示
			}
			println("数据行数 : " + data.size())
			// 第五步，写入实体数据
			if(data != null && !data.isEmpty()){
				for (int i = 0; i < data.size(); i++) {
					row = sheet.createRow(i+1)
					List<String> rd = data.get(i)
					for(int c = 0; c < rd.size(); c++){
						cell = row.createCell(c)			//列索引从0开始
						cell.setCellValue(rd.get(c))		//列名
						cell.setCellStyle(style)			//列居中显示
					}
				}
			}
			// 第七步，将文件输出到客户端浏览器
			try {
				workbook.write(out)
				out.flush()
				out.close()
			} catch (Exception e) {
				e.printStackTrace()
			}
			println("Excel成功导出!")
		}catch(Exception e){
			e.printStackTrace();
			println("Excel导出失败!")
		}
	}
	
	void joinExport(List<String> titles, List<List<String>> data, ServletOutputStream out) {
		try{
			// 第一步，创建一个workbook，对应一个Excel文件
			HSSFWorkbook workbook = new HSSFWorkbook()
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = workbook.createSheet("sheet1")
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = sheet.createRow(0)
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = workbook.createCellStyle()
			//居中样式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER)
			HSSFCell cell = null
			println("表头列数 : " + titles.size())
			for (int i = 0; i < titles.size(); i++) {
				cell = row.createCell(i)			//列索引从0开始
				cell.setCellValue(titles.get(i))	//列名
				cell.setCellStyle(style)			//列居中显示
			}
			println("数据行数 : " + data.size())
			// 第五步，写入实体数据
			if(data != null && !data.isEmpty()){
				for (int i = 0; i < data.size(); i++) {
					row = sheet.createRow(i+1)
					List<String> rd = data.get(i)
					for(int c = 0; c < rd.size(); c++){
						cell = row.createCell(c)			//列索引从0开始
						cell.setCellValue(rd.get(c))		//列名
						cell.setCellStyle(style)			//列居中显示
					}
				}
			}
			//合并单元格
			CellRangeAddress region = null
			int rowspan = 0			
			for (int index=1; index<=sheet.getLastRowNum(); index++){
				row = sheet.getRow(index)
				cell = row.getCell(row.getLastCellNum()-1)
				println "Value is >>>>>>>>>>>>>>>>" + cell.getNumericCellValue().intValue()
				rowspan = cell.getNumericCellValue().intValue()
				if(cell.getNumericCellValue().intValue() > 1) {
					//合并单元格对应的四个参数 : 起始行，结束行，起始列，结束列
					region = new CellRangeAddress(index, index+rowspan-1, 0, 0)
					sheet.addMergedRegion(region)
					region = new CellRangeAddress(index, index+rowspan-1, 1, 1)
					sheet.addMergedRegion(region)
					region = new CellRangeAddress(index, index+rowspan-1, 2, 2)
					sheet.addMergedRegion(region)
					region = new CellRangeAddress(index, index+rowspan-1, 3, 3)
					sheet.addMergedRegion(region)
				}					
			}
			// 第七步，将文件输出到客户端浏览器
			try {
				workbook.write(out)
				out.flush()
				out.close()
			} catch (Exception e) {
				e.printStackTrace()
			}
			println("Excel成功导出!")
		}catch(Exception e){
			e.printStackTrace();
			println("Excel导出失败!")
		}
	}
}