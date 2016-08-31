package CLCore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

public class CLRecordCellListener implements ListSelectionListener {

private int currentCol;
private int currentRow;
private JTable jTbl;
private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
private Date now = new Date();
private EventList<CVEntry> clEntries = new BasicEventList<CVEntry>();
	public CLRecordCellListener()
	{
		
	}

	public CLRecordCellListener(JTable tbl,EventList<CVEntry> vec)
	{
		jTbl = tbl;
		clEntries = vec;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		
		now.setTime(System.currentTimeMillis());
		currentRow = jTbl.getSelectedRow();
		currentCol = jTbl.getSelectedColumn();
		
		try{
			if(currentCol == 5)
			{
		
				clEntries.get(currentRow).setTimeIn(dateFormatter.format(now));
				System.out.println("Row 1:"+currentRow);
				
			}
			if(currentCol == 6 )
			{
				clEntries.get(currentRow).setTimeOut(dateFormatter.format(now));
				System.out.println("Row 2:"+currentRow);
			}
		}catch(ArrayIndexOutOfBoundsException exp)
		{
			if(currentCol == 5)
			{
		
				clEntries.get(currentRow+1).setTimeIn(dateFormatter.format(now));
				System.out.println("eRow 1:"+currentRow);
			}
			if(currentCol == 6 )
			{
				clEntries.get(currentRow+1).setTimeOut(dateFormatter.format(now));
				System.out.println("eRow 2:"+currentRow);
			}
		}
		

	
	}
	
	

}
