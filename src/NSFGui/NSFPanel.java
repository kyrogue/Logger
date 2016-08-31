package NSFGui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ListIterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import NSFCore.nsfConstantStrings;
import NSFCore.nsfCore;
import NSFCore.nsfRecord;
import NSFStorage.NSFExcelStore;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventTableModel;

public class NSFPanel extends JPanel {

	private JLabel lblInfo;
	private String scannedString;
	private JTextField scannedText;
	private JTable nsfTbl;
    //JTable properties
    String[] propertyNames = new String[] {"nric",  "name","contact","location","remarks","timeOut","timeIn"};
    String[] columnLabels = new String[] {"Nric","Name","Contact","Location","Remarks","Time Out","Time In"};
    boolean[] editable = new boolean[] {true,true,true,true,true,true,true};
    EventList<nsfRecord> nsfEventList = new BasicEventList<nsfRecord>();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
    private JComboBox jcbLoc,jcbRemarks;
    private String[] locArr,remarksArr;
 
	public NSFPanel()
	{
		scannedText = new JTextField(10);
		lblInfo = new JLabel("Waiting Input");
		setLayout(new BorderLayout());
		scannedText.addKeyListener(new KeyAdapter() {
			 @Override
		    public void keyReleased(KeyEvent e) { 
		        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	
		        	nsfCore core = new nsfCore(getNsfEventList());
		        	if(core.existsInTable(getScannedString()))//rec already exists in table means he has already booked out, so booking IN now.
		        	{
		        		if(getNsfEventList().size() >= 1)
		        		{		        			
		        			for(ListIterator<nsfRecord> i = getNsfEventList().listIterator(); i.hasNext();)
		        			{      				
		        				nsfRecord rec = i.next();       				
		        				if(rec.getNric().equalsIgnoreCase(getScannedString()))
		        				{
		        					
		        					if(rec.getTimeIn().equalsIgnoreCase("NIL"))
		        					{
				        					Date now = new Date();
				        					rec.setTimeIn(dateFormatter.format(now));
				        					i.set(rec);
				        					lblInfo.setText(rec.getName().toUpperCase()+" BOOK IN");
		        					}

			        			}

		        			}
		        		}
		        		clearScannedStringTextBox();
		        		
		        	}else//rec does not exists in table, first time booking OUT.
		        	{ 
		        		
		        		if(core.existsInDB(getScannedString(), "NRIC"))
		        		{
		        			Date now = new Date();
			        		nsfRecord rec = core.retrieveRecordFromDB(getScannedString());
			        		rec.setTimeOut(dateFormatter.format(now));
			        		insertIntoNSFTable(rec);
			        		lblInfo.setText(rec.getName().toUpperCase()+" ADDED TO TABLE,BOOK OUT");
		        		}else
		        		{
		        			lblInfo.setText("NO RECORD FOUND IN DATABASE");
		        		}
		        		clearScannedStringTextBox();
		        		
		        	}
		           
		        } else {
		            // some character has been read, append it to your "barcode cache"
		           // setBarCode(frame.getBarCode() + e.getKeyChar());
		        }
		        
		        
		    }

		});
		//auto complete
		locArr = readLocationsTxtFile();
		remarksArr = readRemarksTxtFile();
		final EventList<String> loc = GlazedLists.eventList(Arrays.asList(locArr));
		final EventList<String> remarks = GlazedLists.eventList(Arrays.asList(remarksArr));
		jcbLoc = new JComboBox(locArr);
		jcbRemarks = new JComboBox(remarksArr);
		//create table
		TableFormat<nsfRecord> tf = GlazedLists.tableFormat(nsfRecord.class,propertyNames,columnLabels,editable);
		nsfTbl = new JTable(new EventTableModel<nsfRecord>(nsfEventList,tf));
		nsfTbl.setFillsViewportHeight(true);
		nsfTbl.getTableHeader().setReorderingAllowed(false);
		TableColumn plateColumn = nsfTbl.getColumnModel().getColumn(3);
		plateColumn.setCellEditor(new DefaultCellEditor(jcbLoc));
		TableColumn plateColumn2 = nsfTbl.getColumnModel().getColumn(4);
		plateColumn2.setCellEditor(new DefaultCellEditor(jcbRemarks));
		//Install autocompplete
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					AutoCompleteSupport.install(jcbLoc, loc);
					AutoCompleteSupport.install(jcbRemarks, remarks );
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		add(scannedText,BorderLayout.NORTH);
		add(lblInfo,BorderLayout.CENTER);
		add(new JScrollPane(nsfTbl),BorderLayout.SOUTH);
		scannedText.requestFocusInWindow();
	}
	
	public void saveRecordsToExcel()
	{
		NSFExcelStore store = new NSFExcelStore();
		for(ListIterator<nsfRecord> i = getNsfEventList().listIterator(); i.hasNext();)
		{
			nsfRecord rec = i.next();
			store.addExcelRow(rec.getNric(), rec.getName(), rec.getContact(), rec.getLocation(), rec.getRemarks(),
					rec.getTimeOut(), rec.getTimeIn());
		}
		
	}
	
	public void loadRecordsToTable()
	{
		NSFExcelStore store = new NSFExcelStore(false);
		getNsfEventList().clear();
		getNsfEventList().addAll(store.loadRecordsFromOld());
	}
	public void insertIntoNSFTable(nsfRecord nsf)
	{
		getNsfEventList().add(nsf);
	}
	
	public void clearScannedStringTextBox()
	{
		if(getScannedTextField().getText().length() > 0)
		{
			getScannedTextField().setText("");
		}
	}
	
	public String getScannedString() {
		scannedString = getScannedTextField().getText().toUpperCase();
		if(scannedString.length() > 9)
		{
			
			scannedString = scannedString.substring(0, 9).toUpperCase();
		}
		//System.out.println("scannedString(): "+scannedString);
		return scannedString;
	}

	public void setScannedString(String scannedString) {
		this.scannedString = scannedString;
	}

	public JTextField getScannedTextField() {
		return scannedText;
	}

	public void setScannedText(JTextField scannedText) {
		this.scannedText = scannedText;
	}
	
    public EventList<nsfRecord> getNsfEventList() {
		return nsfEventList;
	}
    private String[] readRemarksTxtFile()
    {
    	ArrayList<String> remarksArrList = new ArrayList<String>();
		//creating file for plate numbers.
       	File folder = new File(nsfConstantStrings.NSF_FOLDER);
    	File remTxtFile = new File(nsfConstantStrings.REMARKS_TXT_FILE);
    		
    		if(!folder.exists())
    		{
    			folder.mkdir();
    		}
    		if(!remTxtFile.exists())
    		{

    			try {
    				remTxtFile.createNewFile();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
		try{
			  // Open the file 
				FileInputStream fstream = new FileInputStream(nsfConstantStrings.REMARKS_TXT_FILE);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null) 
			  {
					  remarksArrList.add(new String(strLine));
					  //System.out.println(strLine);
			  }
			  
			  //Close the input stream
			  br.close();
			  in.close();
			  fstream.close();
			  }
		catch (FileNotFoundException e){
			  System.err.println("Error: " + e.getMessage());
			  } catch (IOException e) {
		      System.err.println("Error: " + e.getMessage());
		}
		return remarksArrList.toArray(new String[remarksArrList.size()]);
    	
    }
    private String[] readLocationsTxtFile()
    {
    	ArrayList<String> locArrList = new ArrayList<String>();
		//creating file for plate numbers.
    	File folder = new File(nsfConstantStrings.NSF_FOLDER);
		File locTxtFile = new File(nsfConstantStrings.LOCATIONS_TXT_FILE);
		
		if(!folder.exists())
		{
			folder.mkdir();
		}
		if(!locTxtFile.exists())
		{

			try {
				locTxtFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			  // Open the file 
				FileInputStream fstream = new FileInputStream(nsfConstantStrings.LOCATIONS_TXT_FILE);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null) 
			  {
					  locArrList.add(new String(strLine));
					 /// System.out.println(strLine);
			  }
			  
			  //Close the input stream
			  br.close();
			  in.close();
			  fstream.close();
			  }
		catch (FileNotFoundException e){
			  System.err.println("Error: " + e.getMessage());
			  } catch (IOException e) {
		      System.err.println("Error: " + e.getMessage());
		}
		return locArrList.toArray(new String[locArrList.size()]);
    	
    }
}
