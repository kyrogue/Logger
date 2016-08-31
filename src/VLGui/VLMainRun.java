package VLGui;

import javax.swing.JFrame;

public class VLMainRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			JFrame frame = new JFrame();
			frame.add(new VLMainFrame());
			frame.setVisible(true);
			frame.pack();
			
	}

}
