package cn.nearf.ggz.utils;

import static org.jeecgframework.poi.util.PoiElUtil.FOREACH;
import static org.jeecgframework.poi.util.PoiElUtil.START_STR;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.poi.excel.export.template.ExcelExportOfTemplateUtil;

public class ExportExcelBaseUtil {
	public static String getKey(int index){  
        String colCode = "";  
        char key='A';  
        int loop = index / 26;  
        if(loop>0){  
            colCode += getKey(loop-1);  
        }  
        key = (char) (key+index%26);  
        colCode += key;  
        return colCode;  
    }
	
	public static HSSFCellStyle commonStyle(HSSFWorkbook wb,boolean isNumeric){
		HSSFCellStyle style = wb.createCellStyle();
		
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		
		style.setWrapText(true);//设置自动换行 
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		if(isNumeric){
			style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}else{
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		
		return style;
	}
	
	public static XSSFCellStyle commonStyle(XSSFWorkbook wb,boolean isNumeric){
		XSSFCellStyle style = wb.createCellStyle();
		
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		
		style.setWrapText(true);//设置自动换行 
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		if(isNumeric){
			style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}else{
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		
		return style;
	}
	
	private static HSSFCellStyle appendStyle(HSSFCellStyle style){
		
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		
		style.setWrapText(true);//设置自动换行 
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		return style;
	}
	
	private static XSSFCellStyle appendStyle(XSSFCellStyle style){
		
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		
		style.setWrapText(true);//设置自动换行 
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		return style;
	}
	
	
	public static void setStyle(HSSFWorkbook wb,HSSFCell nCell,HSSFCellStyle numStyle, HSSFCellStyle strStyle,HSSFCellStyle perStyle){
		if (nCell != null) {
			HSSFCellStyle st = nCell.getCellStyle();
			if(st != null && st.getUserStyleName() != null){
				System.out.println("st.getUserStyleName() = " + st.getUserStyleName());
				HSSFCellStyle style = wb.createCellStyle();
				style.cloneStyleFrom(nCell.getCellStyle());
				nCell.setCellStyle(appendStyle(style));
			}else{
				if ((nCell.getCellType() == Cell.CELL_TYPE_NUMERIC || nCell.getCellType() == Cell.CELL_TYPE_FORMULA)) {
					nCell.setCellStyle(numStyle);
				}else{
			        nCell.setCellType(Cell.CELL_TYPE_STRING);
			        String oldString = nCell.getStringCellValue();
			        if (oldString != null && oldString.indexOf(START_STR) != -1 && !oldString.contains(FOREACH)) {
			        	if(ExcelExportOfTemplateUtil.isNumber(oldString)){
			        		nCell.setCellStyle(numStyle);
			        	}else if(ExcelExportOfTemplateUtil.isPrecentage(oldString)){
			        		nCell.setCellStyle(perStyle);
			        	}else{
			        		nCell.setCellStyle(strStyle);
			        	}
			        }else{
			        	nCell.setCellStyle(strStyle);
			        }
				}
			}
		}
	}
	
	public static void setStyle(XSSFWorkbook wb,XSSFCell nCell,XSSFCellStyle numStyle, XSSFCellStyle strStyle,XSSFCellStyle perStyle){
		if (nCell != null) {
			XSSFCellStyle st = nCell.getCellStyle();
			if(st != null ){
//				System.out.println("st.getUserStyleName() = " + st.getUserStyleName());
				XSSFCellStyle style = wb.createCellStyle();
				style.cloneStyleFrom(nCell.getCellStyle());
				nCell.setCellStyle(appendStyle(style));
			}else{
				if ((nCell.getCellType() == Cell.CELL_TYPE_NUMERIC || nCell.getCellType() == Cell.CELL_TYPE_FORMULA)) {
					nCell.setCellStyle(numStyle);
				}else{
			        nCell.setCellType(Cell.CELL_TYPE_STRING);
			        String oldString = nCell.getStringCellValue();
			        if (oldString != null && oldString.indexOf(START_STR) != -1 && !oldString.contains(FOREACH)) {
			        	if(ExcelExportOfTemplateUtil.isNumber(oldString)){
			        		nCell.setCellStyle(numStyle);
			        	}else if(ExcelExportOfTemplateUtil.isPrecentage(oldString)){
			        		nCell.setCellStyle(perStyle);
			        	}else{
			        		nCell.setCellStyle(strStyle);
			        	}
			        }else{
			        	nCell.setCellStyle(strStyle);
			        }
				}
			}
		}
	}

}
