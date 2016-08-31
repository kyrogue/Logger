package NSFCore;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;


import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

public class nsfCore {

	private EventList<nsfRecord> nsfList = new BasicEventList<nsfRecord>();
	public static String NSF_DB_TABLE_NAME = "NSFTable";
	public static final String NSF_DB_ACCESS_FILE =  System.getProperty("user.home")+File.separator+"NSFDatabase.mdb";
	public static final String HOME_FOLDER =  System.getProperty("user.home")+File.separator;
	
	public nsfCore(EventList<nsfRecord> nsfList)
	{
		this.nsfList = nsfList;
	}
	
	public EventList<nsfRecord> getNsfList() {
		return nsfList;
	}

	public void setNsfList(EventList<nsfRecord> nsfList) {
		this.nsfList = nsfList;
	}

	public nsfCore() {
		// TODO Auto-generated constructor stub
	}

	public nsfRecord retrieveFromTable(nsfRecord r)
	{
		nsfRecord rec = null;
		if(getNsfList().contains(r))
		{
			int i = getNsfList().indexOf(r);
			rec = getNsfList().get(i);
			System.out.println(rec.toString());
		}
		return rec;
	}
	public boolean existsInTable(String nric)
	{
		boolean flag = false;
		ListIterator<nsfRecord> itr = getNsfList().listIterator();
			while(itr.hasNext())
			{
				nsfRecord rec = itr.next();
				if(rec.getNric().equalsIgnoreCase(nric))
				{
					if(rec.getTimeIn().equalsIgnoreCase("NIL"))
					{
						flag = true;
					}
				}

			}
	
			return flag;

		
	}
	
	public boolean existsInDB(String nric,String columnName)
	{
		boolean flag = false;
		Database db;
		Table table;
		
		try {
			System.out.println(NSF_DB_ACCESS_FILE);
			db = DatabaseBuilder.open(new File(NSF_DB_ACCESS_FILE));
			table = db.getTable(NSF_DB_TABLE_NAME);
			  for(Row row : table) {
				    
				    Column column = table.getColumn(columnName);
				    Object value = row.get(columnName);
				    if(nric.equalsIgnoreCase((String) value))
				    {
				    	//System.out.println("nsfCore Record exists in DB! "+value);
				    	flag = true;
				    }
				    
				  }
			//DB close
			db.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public nsfRecord retrieveRecordFromDB(String nric)
	{
		nsfRecord retrievedRecord = new nsfRecord();
		Database db;
		Table table;
		
		try {
			db = DatabaseBuilder.open(new File(NSF_DB_ACCESS_FILE));
			table = db.getTable(NSF_DB_TABLE_NAME);
			Row row = CursorBuilder.findRow(table, Collections.singletonMap("NRIC", nric));
			for(Column column : table.getColumns()) {				
				    String columnName = column.getName();
				    Object value = row.get(columnName);
				    if(columnName.equalsIgnoreCase("NRIC"))
				    {
				    	retrievedRecord.setNric((String) value);
				    }else if(columnName.equalsIgnoreCase("NSF_Name"))
				    {
				    	retrievedRecord.setName((String) value);
				    }else if(columnName.equalsIgnoreCase("CONTACT"))
				    {
				    	retrievedRecord.setContact((String) value);
				    }else if(columnName.equalsIgnoreCase("LOCATION"))
				    {
				    	retrievedRecord.setLocation((String) value);
				    }else if(columnName.equalsIgnoreCase("REMARKS"))
				    {
				    	retrievedRecord.setRemarks((String) value);
				    }else if(columnName.equalsIgnoreCase("TIMEOUT"))
				    {
				    	retrievedRecord.setTimeOut((String) value);
				    }else if(columnName.equalsIgnoreCase("TIMEIN"))
				    {
				    	retrievedRecord.setTimeIn((String) value);
				    }
				  }
				db.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retrievedRecord;
	}
	

}
