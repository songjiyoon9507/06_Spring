package com.home.board.main.controller;

import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelRead {

	public static void main(String[] args) {
		
		try {
			FileInputStream file = new FileInputStream(("testexcel.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			
			int rowindex = 0;
			int columnindex = 0;
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			int rows = sheet.getPhysicalNumberOfRows();
			for (rowindex = 0; rowindex < rows; rowindex++) {
				XSSFRow row = sheet.getRow(rowindex);
				
                if(row !=null){
                    //셀의 수
                    int cells=row.getPhysicalNumberOfCells();
                    for(columnindex=0; columnindex<=cells; columnindex++){
                        //셀값을 읽는다
                        XSSFCell cell=row.getCell(columnindex);
                        String value="";
                        //셀이 빈값일경우를 위한 널체크
                        if(cell==null){
                            continue;
                        }else{
                            //타입별로 내용 읽기
                            switch (cell.getCellType()) {
                            case STRING:
                                System.out.println(cell.getStringCellValue());
//                            case NUMERIC:
//                            	System.out.println(String.valueOf(cell.getNumericCellValue()));
//                            case BOOLEAN:
//                            	System.out.println(String.valueOf(cell.getBooleanCellValue()));
//                            case FORMULA:
//                            	System.out.println(cell.getCellFormula());
//                            case BLANK:
//                            	System.out.println("");
//                            case ERROR:
//                            	System.out.println(String.valueOf(cell.getErrorCellValue()));
//                            default:
//                                System.out.println("");
                            }
                        
                        }
                        System.out.println(rowindex+"번 행 : "+columnindex+"번 열 값은: "+value);
                    }
 
                }

			}
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
//	    public List<String> readExcel(MultipartFile file) throws IOException {
//
//	        List<String> timetableList = new ArrayList<>();
//
//	        InputStream inputStream = file.getInputStream();// 파일 읽기
//	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream); // 엑셀 파일 파싱
//
//	        XSSFSheet sheet = workbook.getSheetAt(0); // 엑셀 파일의 첫번째 (0) 시트지
//	        int rows = sheet.getPhysicalNumberOfRows(); // 행의 수
//
//	        for (int r = 1; r < rows-1; r++) { // 마지막에 건수 있음. 그 줄 제외
//	            XSSFRow row = sheet.getRow(r); // 0 ~ rows
//
//	            if (row != null) { // 행이 비어있지 않을 때
//	                int cells=row.getPhysicalNumberOfCells(); // 열의 수
//
//	                for (int c = 0; c < cells; c++) {
//	                    XSSFCell cell = row.getCell(c); // 0 ~ cell
//	                    String value = "";
//
//	                    // 12반쩨 열이 강의시간(강의실)
//	                    // r열 c행의 cell이 비어있을 때 혹은 시간표 열이 아닐때
//	                    if (cell == null || c != 12 || cell.getCellType().equals(CellType.BLANK)) { 
//	                        continue;
//	                    } else { // 타입별로 내용 읽기
//	                        switch (cell.getCellType()) {
//	                            case FORMULA:
//	                                value = cell.getCellFormula();
//	                                break;
//	                            case NUMERIC:
//	                                value = cell.getNumericCellValue() + "";
//	                                break;
//	                            case STRING:
//	                                value = cell.getStringCellValue() + "";
//	                                break;
//	                            case BLANK: // 빈칸에 false 값 들어있는 듯
//	                                value = cell.getBooleanCellValue() + "";
//	                                break;
//	                            case ERROR:
//	                                value = cell.getErrorCellValue() + "";
//	                                break;
//	                        }
//
//	                    }
//	                    timetableList.add(value);
//	                }
//	            }
//	        }
//
//	        return timetableList;
//	    }
		
	}
	
//	private static String getCellValue(Cell cell) {
//        if (cell == null) {
//            return "";
//        }
//        
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                return String.valueOf(cell.getNumericCellValue());
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            case BLANK:
//                return "";
//            case ERROR:
//                return String.valueOf(cell.getErrorCellValue());
//            default:
//                return "";
//        }
//    }
}
