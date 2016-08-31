package CLGui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import CLCore.CVDataEntry;
import CLCore.CVEntry;
import CLStorage.CLExcelRead;
import CLStorage.CLExcelStorage;
import CombinedGUI.CursorController;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import net.miginfocom.swing.MigLayout;




public class CMainFrame extends JPanel {
	
	final JPopupMenu popupMenu = new JPopupMenu();
	JMenuItem deleteItem = new JMenuItem("Delete Row");
	private JPanel leftPnl,rightPnl;
	private JTextField txtFilterData,txtFilterRecords;
	private JButton btnAdd,btnReload;
	private JTable tblCVFilter,tblCVRecords,tblAddCVRecords;
	private CLExcelRead excelRead;
	// create an EventList of MP3s
    final EventList<CVDataEntry> civilianList = new BasicEventList<CVDataEntry>();
    final EventList<CVEntry> civiliansRec = new BasicEventList<CVEntry>();
    private Date now = new Date();
    private FilterList<CVDataEntry> filteredCivilians;
    private FilterList<CVEntry> filteredCivilians2;
	public CMainFrame()
	{
		//settings
		setVisible(true);
		setLayout(new MigLayout());
		//dec
		txtFilterData = new JTextField();
		
		txtFilterRecords = new JTextField();
		leftPnl = new JPanel(new MigLayout());
		rightPnl = new JPanel(new MigLayout());
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new btnAddListener());
		btnReload = new JButton("Reload List");
		ActionListener cursorDoIt = CursorController.createListener(this, new btnReloadListener());
		btnReload.addActionListener(cursorDoIt);
		//populate jtable1 
		excelRead = new CLExcelRead();
		civilianList.addAll(excelRead.getcEntries());
		//txt filter for civilian datas
		TextFilterator cvFilterator = new TextFilterator(){
			@Override
			public void getFilterStrings(List baseList,Object element)
			{
				CVDataEntry entry = (CVDataEntry)element;
				baseList.add(entry.getName());
				baseList.add(entry.getCompany());
				baseList.add(entry.getNirc());
				baseList.add(entry.getPassport());
			}
		};
		TextComponentMatcherEditor matcherEditor = new TextComponentMatcherEditor(txtFilterData,cvFilterator);
		 filteredCivilians = new FilterList(civilianList,matcherEditor);
		
		//txt filter for civilian records
		TextFilterator cvFilterator2 = new TextFilterator(){
			@Override
			public void getFilterStrings(List baseList,Object element)
			{
				CVEntry entry = (CVEntry)element;
				baseList.add(entry.getName());
				baseList.add(entry.getNirc());
				baseList.add(entry.getPassport());
				baseList.add(entry.getTimeIn());
				baseList.add(entry.getTimeOut());
			}
		};
		TextComponentMatcherEditor matcherEditor2 = new TextComponentMatcherEditor(txtFilterRecords,cvFilterator2);
		 filteredCivilians2 = new FilterList(civiliansRec,matcherEditor2);
		 // build jtable of records from excel file
        String[] propertyNames = new String[] {"name","company", "nirc","passport"};
        String[] columnLabels = new String[] {"Name","Company", "NRIC","Passport No"};
        TableFormat tf = GlazedLists.tableFormat(CVDataEntry.class, propertyNames, columnLabels);
        tblCVFilter = new JTable(new DefaultEventTableModel(filteredCivilians, tf));
        tblCVFilter.getTableHeader().setReorderingAllowed(false);
        tblCVFilter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCVFilter.addMouseListener(new CLMouseAdapterLeftTbl());
        tblCVFilter.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCVFilter.getColumnModel().getColumn(0).setPreferredWidth(200);
        
        // build a a jtable of records for book in/out
        String[] propertyNames2 = new String[] {"name",  "nirc","passport","passNo","vehPassNo","timeIn","timeOut"};
        String[] columnLabels2 = new String[] {"Name","NRIC","Passport No","Pass No","Veh Pass","Time In","Time Out"};
        boolean[] editable = new boolean[] {true,true,true,true,true,true,true};
        TableFormat tf2 = GlazedLists.tableFormat(CVEntry.class, propertyNames2, columnLabels2,editable);
        tblCVRecords = new JTable(new DefaultEventTableModel(filteredCivilians2, tf2));
        tblCVRecords.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tblCVRecords.addMouseListener(new CLMouseAdapterRightTbl());
        tblCVRecords.getTableHeader().setReorderingAllowed(false);
        tblCVRecords.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCVRecords.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblCVRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //jtable for adding of cv
        Object[] columnNames = {"Name","NRIC","Passport No","Pass No","Veh Pass","Time In","Time Out"};
        tblAddCVRecords = new JTable(new DefaultTableModel(columnNames,1));
        tblAddCVRecords.setPreferredScrollableViewportSize(tblAddCVRecords.getPreferredSize());
        tblAddCVRecords.setCellSelectionEnabled(false);
        tblAddCVRecords.getTableHeader().setReorderingAllowed(false);
        tblAddCVRecords.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tblAddCVRecords.addMouseListener(new CLMouseAdapterAddTbl());
        tblAddCVRecords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for(int i = 0; i <tblAddCVRecords.getColumnCount();i++)
        {
        	tblAddCVRecords.setValueAt("", 0, i);
        }
              
        //components 
        JScrollPane scrollPane = new JScrollPane(tblCVFilter);
        JScrollPane scrollPane2 = new JScrollPane(tblAddCVRecords);
        JScrollPane scrollPane3 = new JScrollPane(tblCVRecords);
		leftPnl.add(txtFilterData,"wrap,grow");
        leftPnl.add(scrollPane,"wrap");
        leftPnl.add(btnReload,"center");
        rightPnl.add(txtFilterRecords,"wrap,grow");
        rightPnl.add(scrollPane2,"grow");
        rightPnl.add(btnAdd,"wrap");
		rightPnl.add(scrollPane3,"grow");

		add(leftPnl,"west");
		add(rightPnl,"east");
	
		//right click
	       deleteItem.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                JOptionPane.showMessageDialog(null, "Right-click performed on table and choose DELETE");
	            }
	        });
	       popupMenu.add(deleteItem);

	}
	public void loadOldRecordsToTbl(){
		CLExcelStorage load = new CLExcelStorage(false);
		civiliansRec.clear();
		civiliansRec.addAll(load.loadRecordsFromOld());
		
	}

	public void saveCVRecordsOnTbl()
	{
		CLExcelStorage store = new CLExcelStorage();
		Iterator<CVEntry> itr = civiliansRec.iterator();
		while(itr.hasNext())
		{
			//System.out.println("Adding entry to excel");
			CVEntry entry = itr.next();
			store.addCVEntry(entry.getName(), entry.getNirc(),entry.getPassport(), entry.getPassNo(), entry.getVehPassNo(), entry.getTimeIn(), entry.getTimeOut());
		}
	}
	 class CLMouseAdapterAddTbl extends MouseAdapter {

	
		 	private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
			public CLMouseAdapterAddTbl()
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
			  	      // do some action if appropriate column
			  	    now.setTime(System.currentTimeMillis());
			  	      if(column == 5)
			  	      {
			  	    	
			  	    	target.setValueAt(dateFormatter.format(now), 0, 5);
			  	      }
			  	      if(column == 6)
			  	      {
			  	    	
			  	    	target.setValueAt(dateFormatter.format(now), 0, 6);
			  	      }


			  	     
			  	    }
			}

	 }
	 class CLMouseAdapterRightTbl extends MouseAdapter {

		 	private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
			public CLMouseAdapterRightTbl()
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

					  	      if(column == 5)
					  	      {
					  	    	
					  	    	filteredCivilians2.get(row).setTimeIn(dateFormatter.format(now));
					  	      }
					  	      if(column == 6)
					  	      {
					  	    	
					  	    	filteredCivilians2.get(row).setTimeOut(dateFormatter.format(now));
					  	      }
					  	    	
				  	      }else if(!target.getValueAt(row, column).toString().isEmpty() && (column == 5 || column ==6)){
					  	    	choice = JOptionPane.showConfirmDialog(getParent(), "Overwrite existing time entry?","",JOptionPane.YES_NO_OPTION);
					  	    	if(choice == 0)
					  	    	{
							  	      if(column == 5)
							  	      {
							  	    
							  	    	filteredCivilians2.get(row).setTimeIn(dateFormatter.format(now));
							  	      }
							  	      if(column == 6)
							  	      {
							  	    	
							  	    	filteredCivilians2.get(row).setTimeOut(dateFormatter.format(now));
							  	      }
					  	    	}
				  	      }

			  	    }
			}


			
		}
	 class CLMouseAdapterLeftTbl extends MouseAdapter {

			public CLMouseAdapterLeftTbl()
			{
				
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				try{
				    if (e.getClickCount() == 2) {

				  	      JTable target = (JTable)e.getSource();
				  	      int row = target.getSelectedRow();
				  	      int[] rows = target.getSelectedRows();
				  	      // do some action if appropriate column
				  	      for(int i : rows)
				  	      {
					  	      String valueName = target.getValueAt(i, 0).toString();
					  	      String valueNRIC = target.getValueAt(i, 2).toString();
					  	      String valuePass = target.getValueAt(i, 3).toString();
					  	      civiliansRec.add(new CVEntry(valueName,valueNRIC,valuePass));
				  	      }

				  	    }
				}catch(NullPointerException exp)
				{
					exp.printStackTrace();
				}

			}
			
			
		}
	 class btnAddListener implements ActionListener{ 


		 String name;
		 String nirc;
		 String passportNo;
		 String passNo;
		 String vehPass;
		 String timeInCV;
		 String timeOutCV;
			@Override
			public void actionPerformed(ActionEvent e) {

				try
				{
		
					name = tblAddCVRecords.getValueAt(0,0).toString();
					nirc = tblAddCVRecords.getValueAt(0,1).toString();
					passportNo = tblAddCVRecords.getValueAt(0, 2).toString();
					passNo = tblAddCVRecords.getValueAt(0,3).toString();
					vehPass = tblAddCVRecords.getValueAt(0,4).toString();
					timeInCV = tblAddCVRecords.getValueAt(0,5).toString();
					timeOutCV = tblAddCVRecords.getValueAt(0,6).toString();
						
					
					if(!(name.equals("") && nirc.equals("") && passportNo.equals("") && passNo.equals("") && vehPass.equals("") && timeInCV.equals("") && timeOutCV.equals("")))
					{
						if(civiliansRec.add(new CVEntry(name,nirc,passportNo,passNo,vehPass,timeInCV,timeOutCV)))
						{
							System.out.println("Entry added");
							//Clearing fields
					        for(int i = 0; i <tblAddCVRecords.getColumnCount();i++)
					        {
					        	tblAddCVRecords.setValueAt("", 0, i);
					        }
							
						}//
					}

				}catch(NullPointerException exp)
				{
					System.out.println("Ignoring null exception");

					
					if(!(name.equals("") && nirc.equals("") && passportNo.equals("") && passNo.equals("") && vehPass.equals("") && timeInCV.equals("") && timeOutCV.equals("")))
					{
						if(civiliansRec.add(new CVEntry(name,nirc,passportNo,passNo,vehPass,timeInCV,timeOutCV)))
						{
							System.out.println("Entry added");
							//Clearing fields
					        for(int i = 0; i <tblAddCVRecords.getColumnCount();i++)
					        {
					        	tblAddCVRecords.setValueAt("", 0, i);
					        }
							
						}//
					}
		
				}
				
				
			}
			
		}
	 class btnReloadListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			civilianList.clear();
			civilianList.addAll(new CLExcelRead().getcEntries());

		}
		 
	 }
	public JTextField getTxtFilterData() {
		return txtFilterData;
	}
}
