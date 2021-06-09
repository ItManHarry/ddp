package com.doosan.ddp.pm.utils.excel
import javax.servlet.ServletOutputStream

interface ExcelExportService {
	/**
	 * Excel导出
	 * @param titles
	 * @param data
	 * @param out
	 */
	void export(List<String> titles, List<List<String>> data, ServletOutputStream out)	
	/**
	 * Excel导出-合并单元格
	 * @param titles
	 * @param data
	 * @param out
	 */
	void joinExport(List<String> titles, List<List<String>> data, ServletOutputStream out)
}