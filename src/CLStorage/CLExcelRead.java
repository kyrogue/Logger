package CLStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import CLCore.CVDataEntry;

public class CLExcelRead {

	private File contractorsFile;
	private ArrayList<CVDataEntry> cEntries = new ArrayList<CVDataEntry>();
	public CLExcelRead()
	{
		contractorsFile = new File(CLStrings.CL_FOLDER+CLStrings.CL_FILE);
		if(contractorsFile.exists())
		{
			//read file
			 InputStream inp;
			try {
				inp = new FileInputStream(contractorsFile);

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
				        	CVDataEntry entry = new CVDataEntry();
				        	//itr thru cell in row
					    	for(Cell cell :r)
					    	{
					    		switch(cell.getColumnIndex())
			                        {
			                        case 1:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setName(cell.getRichStringCellValue().getString());
				                            break;
			                            case Cell.CELL_TYPE_NUMERIC:
			                              	entry.setName(Double.toString(cell.getNumericCellValue()));
			                                break;
			                              default:
			                                
			                        }
			                           		
			                        	
			                        case 2:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setNirc(cell.getRichStringCellValue().getString());
			                                break;
			                            case Cell.CELL_TYPE_NUMERIC:
			                              	entry.setNirc(Double.toString(cell.getNumericCellValue()));
			                                break;
			                              default:
			                                
			                        }
			                           	break;
			                           	
			                        case 3:
			                            switch (cell.getCellType()) {
			                            case Cell.CELL_TYPE_STRING:
			                            	entry.setPassport(cell.getRichStringCellValue().getString());
			                                break;
			                            case Cell.CELL_TYPE_NUMERIC:
			                               	entry.setPassport(Double.toString(cell.getNumericCellValue()));
			                                break;
			                              default:
			                               
			                        }
			                        	break;
			                        	
			                        case 4:
			                        	entry.setNationality(cell.getRichStringCellValue().getString());
			                        	break;
			                        	
			                        case 5:
			                          	entry.setCompany(cell.getRichStringCellValue().getString());
			                        	break;
			                 
			                        }

					    	}
					    	cEntries.add(entry);
		                }
				        
				    }
				    //System.out.println("Finished reading");
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
			   
		}else
		{
			System.out.println("contractors.xlsx dont exists");
		}
		

	}
	
	//public static void main(String[] args)
	//{
	//	new CLExcelRead();
	//}

	public ArrayList<CVDataEntry> getcEntries() {
		return cEntries;
	}
	
	
}
