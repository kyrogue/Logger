package NSFStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import NSFCore.nsfConstantStrings;
import NSFCore.nsfRecord;


public class NSFExcelStore {

	private Calendar now = Calendar.getInstance();
	private String months[] ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	private String yearNow = Integer.toString(now.get(Calendar.YEAR));
	private String mthNow = months[now.get(Calendar.MONTH)];
	private String dayNow = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
	private String yearlyFolder = nsfConstantStrings.NSF_FOLDER+yearNow+File.separator;
	private String monthlyFolder = nsfConstantStrings.NSF_FOLDER+yearNow+File.separator+mthNow+File.separator;
	private String dailyExcelFile = monthlyFolder+dayNow+".xls";

	public NSFExcelStore(boolean f)
	{
		//for executing empty constructor
	}
	public NSFExcelStore()
	{
		File yearFolder = new File(yearlyFolder);
		//Checking if year folder exists
		if(yearFolder.exists())
		{
			createMonthlyFolders();
			File dailyExFile = new File(dailyExcelFile);
			
			if(dailyExFile.exists())
			{
					headerCreationHelper(dailyExFile);
			}else
			{
					headerCreationHelper();
			}

		}else
		{
			createMonthlyFolders();
			//dirs dont exist, first run of program
			if(yearFolder.mkdirs())
			{
				headerCreationHelper();
			}
			
		}

	} 
	
	private void createMonthlyFolders()
	{
		for(String m : months )
		{
			File monthlyFolder = new File(yearlyFolder+m);
			if(!monthlyFolder.exists())
				monthlyFolder.mkdir();
		}
	}
	
	private Font createCustomFont(Workbook book)
	{

	    // Create a new font and alter it.
		Workbook wb = book;
	    Font font = wb.createFont();
	    font.setFontHeightInPoints((short)24);
	    font.setFontName("Courier New");
	    font.setUnderline(Font.U_SINGLE);
	    
		return font;

	}
	
	//creates the  headers needed using a exisiting file
	private void headerCreationHelper(File file)
	{
		File fileToWriteInto = file;
		Workbook wb = null;
		Sheet sheet;
		System.out.println(dailyExcelFile+" exists,writing into it");
		//get daily sheet,create if dont exists
		try
		{
			wb = WorkbookFactory.create(fileToWriteInto);
			try
			{
				
				sheet = wb.getSheet(dayNow);
				wb.removeSheetAt(wb.getSheetIndex(dayNow));
				sheet = wb.createSheet(dayNow);
				wb.setActiveSheet(wb.getSheetIndex(dayNow));
				System.out.println("gotten sheet, using it ");

			}catch(IllegalArgumentException e)
			{
				sheet = wb.createSheet(dayNow);
				wb.setActiveSheet(wb.getSheetIndex(dayNow));
				System.out.println("unable to get sheet, creating  it ");
			}

			//create 4 rows of header
			Row row = sheet.createRow((short)0);
			createCell(wb, row,"Name", (short) 0, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb, row,"NRIC", (short) 1, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb, row,"Passport", (short) 2, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb, row,"Pass No", (short) 3, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb, row,"Veh Pass", (short) 4, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb, row,"Time In", (short) 5, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb, row,"Time Out", (short) 6, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
		}catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				FileOutputStream fileOut;
				try {
					fileOut = new FileOutputStream(monthlyFolder+dayNow+".xls");
				    wb.write(fileOut);
				    fileOut.close();
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Please Close the file before saving", "Error", JOptionPane.ERROR_MESSAGE);
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//
	
	}
	//creates the headers needed and creates a new file
	private void headerCreationHelper()
	{

		try
		{
		//dirs dont exist, first run of program
			System.out.println("First run of program! Creating nessassry stuffs");
			//Creating a new .xls spreadsheet
			System.out.println(dailyExcelFile+" dont exists,creating one");
			Workbook wb3;
			Sheet sheet3;
			wb3 = new HSSFWorkbook();

		
			//get daily sheet,create if dont exists
			try
			{
				sheet3 = wb3.createSheet(dayNow);
				wb3.setActiveSheet(wb3.getSheetIndex(dayNow));
			}catch(IllegalArgumentException e)
			{
				System.out.println("Sheet name exisits using it ");
				sheet3 = wb3.getSheet(dayNow);
				wb3.removeSheetAt(wb3.getSheetIndex(dayNow));
				sheet3 = wb3.createSheet(dayNow);
				wb3.setActiveSheet(wb3.getSheetIndex(dayNow));
			}
			//create 4 rows of header
			Row row = sheet3.createRow((short)0);
			createCell(wb3, row,"Name", (short) 0, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb3, row,"NRIC", (short) 1, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb3, row,"Passport", (short) 2, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb3, row,"Pass No", (short) 3, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb3, row,"Veh Pass", (short) 4, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb3, row,"Time In", (short) 5, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);
			createCell(wb3, row,"Time Out", (short) 6, CellStyle.ALIGN_FILL, CellStyle.VERTICAL_CENTER);

			sheet3.autoSizeColumn(0);
			sheet3.autoSizeColumn(1);
			sheet3.autoSizeColumn(2);
			sheet3.autoSizeColumn(3);
			sheet3.autoSizeColumn(4);
			sheet3.autoSizeColumn(5);
			sheet3.autoSizeColumn(6);

			FileOutputStream fileOut;
		
				fileOut = new FileOutputStream(monthlyFolder+dayNow+".xls");
				wb3.write(fileOut);
				fileOut.close();
		
		}catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Please Close the file before saving", "Error", JOptionPane.ERROR_MESSAGE);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//
	}

	public void addExcelRow(String nric,String name,String contact,String location,String remarks,String timeOut,String timeIn)
	{
		String[] rowEntry = {nric,name,contact,location,remarks,timeOut,timeIn};
		
	    //InputStream inp = new FileInputStream("workbook.xlsx");
		
	    Workbook wb;
		try {
			wb = WorkbookFactory.create(new File(dailyExcelFile));
		    CreationHelper createHelper = wb.getCreationHelper();
		    Sheet sheet = wb.getSheet(Integer.toString(now.get(Calendar.DAY_OF_MONTH)));
		    
		    
		    Row row = sheet.createRow(sheet.getLastRowNum()+1);
		    
		    for(int i = 0 ; i<rowEntry.length;i++)
		    {
		    	row.createCell(i).setCellValue(createHelper.createRichTextString(rowEntry[i]));
		    }
		    
		    
		    // Write the output to a file
		    FileOutputStream fileOut;
			fileOut = new FileOutputStream(dailyExcelFile);
		    wb.write(fileOut);
		    fileOut.close();
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}



	}
    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param wb     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.
     */
    private void createCell(Workbook wb, Row row,String cellStringValue, short column, short halign, short valign) {
      
    	Cell cell = row.createCell(column);
        cell.setCellValue(cellStringValue);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setFont(createCustomFont(wb));
        cell.setCellStyle(cellStyle);
    }
	
	
    public ArrayList<nsfRecord> loadRecordsFromOld()
    {
    	File dailyExFile = new File(dailyExcelFile);
    	ArrayList<nsfRecord> cEntries = new ArrayList<nsfRecord>();
    	if(dailyExFile.exists())
    	{
			//read file
			 InputStream inp;
			try {
				inp = new FileInputStream(dailyExFile);

			    Workbook wb = WorkbookFactory.create(inp);
			    Sheet sheet = wb.getSheetAt(0);
			    //getting cell contents
			    Iterator<Row> itr = sheet.rowIterator();
			    try{
				    while(itr.hasNext())
				    {
				    	//itr thru row
				    	Row r = itr.next();
				    	
				        if( r.getRowNum() != 0)
		                {
				        	nsfRecord entry = new nsfRecord();
				        	//itr thru cell in row
					    	for(Cell cell :r)
					    	{
					    		switch(cell.getColumnIndex())
			                        {
			                        case 0:
		                            switch (cell.getCellType()) {
		                            case Cell.CELL_TYPE_STRING:
		                            	entry.setNric(cell.getRichStringCellValue().getString());
			                            break; 
		                            }
			                        case 1:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setName(cell.getRichStringCellValue().getString());
				                            break;            
			                        }
			                        case 2:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setLocation(cell.getRichStringCellValue().getString());
			                                break;
			                        }

			                        case 3:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setContact(cell.getRichStringCellValue().getString());
			                                break;
			                            case Cell.CELL_TYPE_NUMERIC:
			                               	entry.setContact(Double.toString(cell.getNumericCellValue()));
			                                break; 
			                        }
			                        case 4:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setRemarks(cell.getRichStringCellValue().getString());
			                                break;
			                        }   	
			                        case 5:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setTimeOut(cell.getRichStringCellValue().getString());
			                                break;
			                            case Cell.CELL_TYPE_NUMERIC:
			                               	entry.setTimeOut(Double.toString(cell.getNumericCellValue()));
			                                break;

			                               
			                        }
			                        case 6:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setTimeIn(cell.getRichStringCellValue().getString());
			                                break;
			                            case Cell.CELL_TYPE_NUMERIC:
			                               	entry.setTimeIn(Double.toString(cell.getNumericCellValue()));
			                                break;

			                               
			                        }
			                          
			                        	break;
			                 
			                        }

					    	}
					    	cEntries.add(entry);
		                }
				        
				    }
				   // System.out.println("Finished reading");
				   // System.out.println(cEntries.size());
			    }catch(NoSuchElementException exp)
			    {
			    	//System.out.println("Finished reading");
			    	//System.out.println(cEntries.size());
			    }
			    //

			    inp.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else{
    		JOptionPane.showMessageDialog(null, "No File to load from.");
    	}
	    return cEntries;
    }
    
	public static void main(String[] args)
	{
		new NSFExcelStore();
		
	}

}
