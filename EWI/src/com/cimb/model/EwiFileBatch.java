package com.cimb.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class EwiFileBatch {
	
	ResourceBundle resourceBundle = ResourceBundle.getBundle("ewi");
	final static Logger logger = Logger.getLogger(EwiFileBatch.class);
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("yyyyMMdd");
	private static final String EMPTY_STRING = "";
	
	public static void main(String[] args) {
		EwiFileBatch batch = new EwiFileBatch();
		batch.textFileWrtter();
	}

	private void textFileWrtter() {
		List<String> cifList = generateData();
		writeToFile(cifList);
		
	}

	@SuppressWarnings("resource")
	private List<String> generateData() {
	
	    	ResourceBundle resourceBundle = ResourceBundle.getBundle("ewi");	
	    	String filename = resourceBundle.getString("XLS_FILE_PATH");
	    	List<ParamModel> listParams = null;
	    	try {
	            FileInputStream excelFile = new FileInputStream(new File(filename));
	            Workbook workbook = new XSSFWorkbook(excelFile);
	            Sheet datatypeSheet = workbook.getSheetAt(0);
	            Iterator<Row> iterator = datatypeSheet.iterator();
	          // String tableName ="";
	           // StringBuilder sb = new StringBuilder();
	             listParams = new ArrayList<>();
	           // sb.append("CREATE TABLE "+tableName+" (");

	            while (iterator.hasNext()) {

	                Row currentRow = iterator.next();
	                Iterator<Cell> cellIterator = currentRow.iterator();
	                ParamModel param = new ParamModel();
	                while (cellIterator.hasNext()) {
	                	Cell nextCell = cellIterator.next();
	                	int columnIndex = nextCell.getColumnIndex();
	                	
	                	 switch (columnIndex) {
	                     case 0:
	                    	 if(nextCell != null){
	                    		param.setBarrowerName((String) getCellValue(nextCell));
	                    	 }
	                         break;
	                    case 1:
	                    	if(nextCell != null){
	                    		param.setBrnNo((String) getCellValue(nextCell));
	                    	}
	                         break;
	                     case 2:
	                    	 if(nextCell != null){
	                    		param.setCifNo((Integer) getCellValue(nextCell));
	                    	 }
	                         break;
	                     case 3:
	                    	 if(nextCell != null){
	                    	    param.setBankTrade((String) getCellValue(nextCell));
	                    	 }
	                         break;
	                     case 4:
	                    	 if(nextCell != null){
	                    	    param.setOriginUnit((String) getCellValue(nextCell));
	                    	 }
	                         break;
	                     case 5:
	                    	 if(nextCell != null){
	                    	    param.setRelManager((String) getCellValue(nextCell));
	                    	 }
	                         break; 
	                     case 6:
	                    	 if(nextCell != null){
	                    	    param.setEarlyAlert((String) getCellValue(nextCell));
	                    	 }
	                         break; 
	                     }
	                }
	                   
	                listParams.add(param);
	            }
	           // logger.info("Calling addDelimeter ...");
	            
	           // sb.append(");");
	            //writeSqlScript(listParams);
	          // System.out.println(sb.toString());
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    
		return addDelimeter(listParams);
	}
	
	private List<String> addDelimeter(List<ParamModel> listParams) {
		logger.info("Im in addDelimeter ...!");
		List<String> pipeList = new ArrayList<String>();
		for (ParamModel list : listParams) {
			if(list.getBarrowerName() !=null){
			StringBuilder bd = new StringBuilder();	
			bd.append(list.getBarrowerName()).append("|");
			bd.append(list.getBrnNo()).append("|");
			bd.append(list.getCifNo()).append("|");
			bd.append(list.getBankTrade()).append("|");
			bd.append(list.getOriginUnit()).append("|");
			bd.append(list.getEarlyAlert());
			logger.info("String -: "+bd.toString());
			pipeList.add(bd.toString());
			}
		}
	//logger.info("SIZE -: "+pipeList.size());
		return pipeList;
	}

private void writeToFile(List<String> cifList) {
		
		String path = getBatchOutputDir();
		if (path == null) {
			throw new RuntimeException("No location specified for batch output directory");
		}
		
		String outputFile = "";
		
		outputFile = path + "/" + resourceBundle.getString("FILE_ID") +sdf.format(new Date())+".txt";
		FileWriter fileWriter = null;
		try {
			logger.info("File Path :"+outputFile);
			fileWriter = new FileWriter(outputFile);
			//fileWriter.write(header);
			//fileWriter.write(System.getProperty("line.separator"));
				if(!cifList.isEmpty()){
				//	logger.info("writing text file :"+cifList.size());
				for (String row: cifList) {
					fileWriter.write(row);
					fileWriter.write(System.getProperty("line.separator"));
				}
				}
			//fileWriter.write(System.getProperty("line.separator"));				
			//fileWriter.write(trailer);
			
			
		}
		catch (IOException e) {
			logger.error("End of processing records.");
			throw new RuntimeException(e);
		}finally {
			if (fileWriter != null) {
				try {
					fileWriter.flush();
					fileWriter.close();
				}
				catch (IOException e) {throw new RuntimeException(e);}
			}
		}
	}

public String getBatchOutputDir() {
	String outputPath = resourceBundle.getString("GEN_FILE_DIR");
	
	if (outputPath != null) {
		File file = new File(outputPath);
		if (!file.exists()){
			file.mkdirs();
		}
	}
	else {
		outputPath = resourceBundle.getString("USER_DIR");
	}
	return outputPath;
}

private String denullify(String value) {
	return value == null ? EMPTY_STRING : value;
}
	
private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
     
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue();
     
        case Cell.CELL_TYPE_NUMERIC:
            return (int) cell.getNumericCellValue();
        }
     
        return null;
  }

}
