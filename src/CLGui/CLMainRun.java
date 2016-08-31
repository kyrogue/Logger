package CLGui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CLMainRun {

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.add(new CMainFrame());
				frame.setVisible(true);
				frame.pack();
			}
		});
		
	}
	
}
