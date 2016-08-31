package VLGui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.*;

import net.miginfocom.swing.MigLayout;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import CLCore.CVDataEntry;
import CLStorage.CLExcelStorage;
import VLCore.CarEntry;
import VLCore.VDes;
import VLCore.VPlate;
import VLStorage.VLExcelStorage;

public class VLMainFrame extends JPanel{


	private JTable jTbl,jTbl2;
	private JPanel addPnl,recordsPnl;
	private Object[] columnNames = {"Veh No.","Description","Time In","Time Out"};
	private JButton btnAdd;
	private EventList<CarEntry> entries = new BasicEventList<CarEntry>();
	private JComboBox jcbPlate,jcbDes;
	private String plateTxtFile = System.getProperty("user.home")+File.separator+"VehicleLogs"+File.separator+"plate.txt";
	private String desTxtFile = System.getProperty("user.home")+File.separator+"VehicleLogs"+File.separator+"descriptions.txt";
    private VPlate[] plateArr;
    private VDes[] desArr;
    private JTextField txtFilter;
    private Date now = new Date();
    private java.util.Timer timerSave = new java.util.Timer();
    private FilterList<CarEntry> filteredVehicles;
	public VLMainFrame()
	{
		
		//Schdule video run at 23:59:50
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 30);
		Date time = calendar.getTime();
		//timerSave.schedule(new saveTask(this), time);

		//AutoCompleteSupport
 		plateArr = readPlateTxt();
 		desArr = readDesTxt();
		final EventList<VPlate> plates = GlazedLists.eventList(Arrays.asList(plateArr));
		final EventList<VDes> des = GlazedLists.eventList(Arrays.asList(desArr));
		jcbPlate = new JComboBox(plateArr);
		jcbDes = new JComboBox(desArr);
		//jtable settings
		jTbl = new JTable(new DefaultTableModel(columnNames,1));
		jTbl.setPreferredScrollableViewportSize(jTbl.getPreferredSize());
		jTbl.setCellSelectionEnabled(false);
		TableColumn plateColumn = jTbl.getColumnModel().getColumn(0);
		plateColumn.setCellEditor(new DefaultCellEditor(jcbPlate));
		TableColumn plateColumn2 = jTbl.getColumnModel().getColumn(1);
		plateColumn2.setCellEditor(new DefaultCellEditor(jcbDes));
		jTbl.getTableHeader().setReorderingAllowed(false);
		jTbl.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		jTbl.addMouseListener(new VLAddTableTimeMouseListener());
		jTbl.setValueAt("", 0, 0);
		jTbl.setValueAt("", 0, 1);
		jTbl.setValueAt("", 0, 2);
		jTbl.setValueAt("", 0, 3);

		//Install autocompplete
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					AutoCompleteSupport.install(jcbPlate, plates);
					AutoCompleteSupport.install(jcbDes, des );
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		//filtering for table 2
		txtFilter = new JTextField(15);
				TextFilterator vehFilterator = new TextFilterator(){
					public void getFilterStrings(List baseList,Object element)
					{
						CarEntry entry = (CarEntry)element;
						baseList.add(entry.getPlate());
						baseList.add(entry.getDescription());
						baseList.add(entry.getTimeIn());
						baseList.add(entry.getTimeOut());
					}
				};
				TextComponentMatcherEditor matcherEditor = new TextComponentMatcherEditor(txtFilter,vehFilterator);
				 filteredVehicles = new FilterList(entries,matcherEditor);
		//jtbl 2
		String[] propertyNames = new String[]{"plate","description","timeIn","timeOut"};
		String[] columnNames =new String[] {"Veh No.","Description","Time In","Time Out"};
		boolean[] editable = new boolean[] {true,true,true,true};
		TableFormat<CarEntry> tf = GlazedLists.tableFormat(CarEntry.class,propertyNames,columnNames,editable);
		EventTableModel tableModel = new EventTableModel(filteredVehicles, tf);
		EventSelectionModel selectionModel = new EventSelectionModel(filteredVehicles);
		jTbl2 = new JTable(tableModel);
		jTbl2.setSelectionModel(selectionModel);
		jTbl2.setCellSelectionEnabled(false);
		jTbl2.getTableHeader().setReorderingAllowed(false);
		jTbl2.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		jTbl2.addMouseListener(new VLRecordsTableMouseListener());
		//Dec
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new btnAddListener());
		JScrollPane tableContainer = new JScrollPane(jTbl);
		JScrollPane tableContainer2 = new JScrollPane(jTbl2);

		addPnl = new JPanel(new MigLayout());
		recordsPnl = new JPanel();
		Date now = new Date();
		
		//Adding Components
		addPnl.add(tableContainer);
		addPnl.add(btnAdd,"wrap");
		addPnl.add(txtFilter,"span,grow,");
		recordsPnl.add(tableContainer2,BorderLayout.PAGE_START);
		
		add(addPnl,"west");
		add(recordsPnl,"east");

	}
	public void loadOldRecordsToTbl(){
		VLExcelStorage load = new VLExcelStorage(false);
		entries.clear();
		entries.addAll(load.loadRecordsFromOld());
		
	}
    private VPlate[] readPlateTxt()
    {
    	ArrayList<VPlate> plateList = new ArrayList<VPlate>();
		//creating file for plate numbers.
		File plateTxt = new File(plateTxtFile);
		if(!plateTxt.exists())
		{
			try {
				plateTxt.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			  // Open the file 
				FileInputStream fstream = new FileInputStream(plateTxtFile);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null) 
			  {
					  plateList.add(new VPlate(strLine));
				
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
		return plateList.toArray(new VPlate[plateList.size()]);
    	
    }
    
    private VDes[] readDesTxt()
    {
    	ArrayList<VDes> desList = new ArrayList<VDes>();
		//creating file for plate numbers.
		File desTxt = new File(desTxtFile);
		if(!desTxt.exists())
		{
			try {
				desTxt.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			  // Open the file 
				FileInputStream fstream = new FileInputStream(desTxtFile);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null) 
			  {
					  desList.add(new VDes(strLine));
					 
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
		return desList.toArray(new VDes[desList.size()]);
    	
    }
	
	public void saveRecordsOnTbl()
	{
		VLExcelStorage store = new VLExcelStorage();
		Iterator<CarEntry> itr = entries.iterator();
		while(itr.hasNext())
		{
			//System.out.println("Adding entry to excel");
			CarEntry entry = itr.next();
			store.addCarEntry(entry.getPlate(), entry.getDescription(), entry.getTimeIn(), entry.getTimeOut());
		}
	}
	 class VLRecordsTableMouseListener extends MouseAdapter {

		 	private Date now = new Date();
		 	private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
			public VLRecordsTableMouseListener()
			{
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
			    if (e.getClickCount() == 1) {
			  	      JTable target = (JTable)e.getSource();
			  	      int row = target.getSelectedRow();
			  	      int column = target.getSelectedColumn();
			  	      int choice;
			  	      // do some action if appropriate column
			  	      now.setTime(System.currentTimeMillis());
			  	      if(target.getValueAt(row, column)== null || target.getValueAt(row, column).toString().equals(""))
			  	      {

				  	      if(column == 2)
				  	      {
				  	    	
				  	    	  filteredVehicles.get(row).setTimeIn(dateFormatter.format(now));
				  	      }
				  	      if(column == 3)
				  	      {
				  	    	
				  	    	  filteredVehicles.get(row).setTimeOut(dateFormatter.format(now));
				  	      }
				  	    	
			  	      }else if(!target.getValueAt(row, column).toString().isEmpty() && (column == 2 || column ==3)){
				  	    	choice = JOptionPane.showConfirmDialog(getParent(), "Overwrite existing time entry?","",JOptionPane.YES_NO_OPTION);
				  	    	if(choice == 0)
				  	    	{
						  	      if(column == 2)
						  	      {
						  	    	
						  	    	
						  	    	  filteredVehicles.get(row).setTimeIn(dateFormatter.format(now));
						  	      }
						  	      if(column == 3)
						  	      {
						  	    	  filteredVehicles.get(row).setTimeOut(dateFormatter.format(now));
						  	      }
				  	    	}
			  	      }
					  
					  
					  
			  	     
			  	    }
			}


			
		}
	 class VLAddTableTimeMouseListener extends MouseAdapter {

		 	private Date now = new Date();
		 	private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
			public VLAddTableTimeMouseListener()
			{
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
			    if (e.getClickCount() == 1) {
			  	      JTable target = (JTable)e.getSource();
			  	    
			  	      int column = target.getSelectedColumn();
			  	      // do some action if appropriate column
			  	      now.setTime(System.currentTimeMillis());
			  	      if(column == 2)
			  	      {
			  	    	jTbl.setValueAt(dateFormatter.format(now), 0, 2);
			  	      }
			  	      if(column == 3)
			  	      {
			  	    	jTbl.setValueAt(dateFormatter.format(now), 0, 3);
			  	      }
			  	     
			  	    }
			}


			
		}
	class btnAddListener implements ActionListener{ 
		 String vNo="";
		 String vDes="";
		 String timeIn="";
		 String timeOut="";

		@Override
		public void actionPerformed(ActionEvent e) {

			try
			{
	
					vNo = jTbl.getValueAt(0,0).toString();
			
					vDes = jTbl.getValueAt(0,1).toString();
		
					timeIn = jTbl.getValueAt(0, 2).toString();
			
					timeOut =jTbl.getValueAt(0,3).toString();
				
				if(!(vNo.equals("") && vDes.equals("") && timeIn.equals("") && timeOut.equals("")))
				{
					if(entries.add(new CarEntry(vNo,vDes,timeIn,timeOut)))
					{
						System.out.println("Entry added");
						//Clearing fields
						for(int i = 0; i < jTbl.getRowCount(); i++)
						{
							for(int j = 0; j < jTbl.getColumnCount(); j++)
							{
								jTbl.setValueAt("", i, j);
							}
						}
						
					}//
				}

			}catch(NullPointerException exp)
			{
				System.out.println("Ignoring null exception");

				if(!(vNo.equals("") && vDes.equals("") && timeIn.equals("") && timeOut.equals("")))
				{
					if(entries.add(new CarEntry(vNo,vDes,timeIn,timeOut)))
					{
						System.out.println("Entry added");
						//Clearing fields
						for(int i = 0; i < jTbl.getRowCount(); i++)
						{
							for(int j = 0; j < jTbl.getColumnCount(); j++)
							{
								jTbl.setValueAt("", i, j);
							}
						}
						
					}//
				}
	
			}
			
			
		}
		
	}

}
