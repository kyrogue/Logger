package CombinedGUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import CLGui.CMainFrame;
import VLGui.VLMainFrame;

public class GFrame extends JFrame {
	private JTabbedPane tabPane;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu subMenuLoad;
	private JMenu subMenuSave;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemSave2;
	private JMenuItem menuItemLoad;
	private JMenuItem menuItemLoad2;
	private CMainFrame civilianPanel;
	private VLMainFrame vehiclePanel;
	private JMenuListener menuListener = new JMenuListener();
	private Timer timerSave = new Timer();
	
	public GFrame(){
			Calendar calendar = Calendar.getInstance();
		    calendar.set(11, 23);
		    calendar.set(12, 59);
		    calendar.set(13, 30);
		    Date time = calendar.getTime();
		    
		    this.timerSave.schedule(new saveTask(this), time);
		    
		    this.civilianPanel = new CMainFrame();
		    this.vehiclePanel = new VLMainFrame();
		    
		    this.menuBar = new JMenuBar();
		    
		    this.menuFile = new JMenu("File");
		    this.subMenuLoad = new JMenu("Load");
		    this.subMenuSave = new JMenu("Save");
		    this.menuFile.add(this.subMenuSave);
		    this.menuFile.add(this.subMenuLoad);
		    
		    this.menuItemSave = new JMenuItem("Save VehicleLog");
		    this.menuItemSave2 = new JMenuItem("Save CivilianLog");
		    this.subMenuSave.add(this.menuItemSave);
		    this.subMenuSave.add(this.menuItemSave2);
		    
		    this.menuItemLoad = new JMenuItem("Load VehicleLog");
		    this.menuItemLoad2 = new JMenuItem("Load CivilianLog");
		    this.subMenuLoad.add(this.menuItemLoad);
		    this.subMenuLoad.add(this.menuItemLoad2);
		    this.menuBar.add(this.menuFile);
		    ActionListener cursorDoIt = CursorController.createListener(this, this.menuListener);
		    this.menuItemSave.addActionListener(cursorDoIt);
		    this.menuItemSave2.addActionListener(cursorDoIt);
		    this.menuItemLoad.addActionListener(cursorDoIt);
		    this.menuItemLoad2.addActionListener(cursorDoIt);
		    this.tabPane = new JTabbedPane();
		    
		    this.tabPane.add("Civilians", this.civilianPanel);
		    this.tabPane.add("Vehicle", this.vehiclePanel);
		    
		    this.tabPane.setMnemonicAt(0, 67);
		    this.tabPane.setMnemonicAt(1, 86);
		    
		    add(this.tabPane);
		    setDefaultCloseOperation(3);
		    setTitle("Combined Log");
		    setVisible(true);
		    setJMenuBar(this.menuBar);
		    
		    pack();
	}
	
	
	class saveTask
    extends TimerTask
  {
    JFrame frame;
    
    public saveTask(GFrame mainFrame)
    {
      this.frame = mainFrame;
    }
    
    public void run()
    {
      System.out.println("Timer task run");
      GFrame.this.vehiclePanel.saveRecordsOnTbl();
      
      GFrame.this.civilianPanel.saveCVRecordsOnTbl();
      
      WindowEvent closingEvent = new WindowEvent(this.frame, 201);
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
      GFrame.this.timerSave.cancel();
    }
  }
	
	class JMenuListener
    implements ActionListener
  {
    JMenuListener() {}
    
    public void actionPerformed(ActionEvent e)
    {
      JMenuItem source = (JMenuItem)e.getSource();
      String txtSource = source.getText();
      String str1;
      
      switch(txtSource){
      		case "Save CivilianLog":
      			GFrame.this.civilianPanel.saveCVRecordsOnTbl();
      			break;
      		
      		case "Save VehicleLog":
      			GFrame.this.vehiclePanel.saveRecordsOnTbl();
      			break;
      			
      		case "Load VehicleLog":
      			GFrame.this.vehiclePanel.loadOldRecordsToTbl();
      			break;
      		
      		case "Load CivilianLog":
      			GFrame.this.civilianPanel.loadOldRecordsToTbl();
      			break;
      			
      		default:
      			break;     		 	    	  
      }
      
//      switch ((str1 = txtSource).hashCode())
//      {
//      case -2125985122: 
//        if (str1.equals("Save CivilianLog")) {
//          break;
//        }
//        break;
//      case -1097631758: 
//        if (str1.equals("Load VehicleLog")) {}
//        break;
//      case -896124793: 
//        if (str1.equals("Load CivilianLog")) {}
//      case 1910736635: 
//        if ((goto 165) && (str1.equals("Save VehicleLog")))
//        {
//          GFrame.this.vehiclePanel.saveRecordsOnTbl();
//          return;
//          
//          GFrame.this.civilianPanel.saveCVRecordsOnTbl();
//          return;
//          
//          GFrame.this.vehiclePanel.loadOldRecordsToTbl();
//          return;
//          
//          GFrame.this.civilianPanel.loadOldRecordsToTbl();
//        }
//        break;
//      }
    }
  }
}
