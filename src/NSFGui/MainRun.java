package NSFGui;

import javax.swing.JFrame;

import NSFCore.nsfConstantStrings;

public class MainRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame testFrameNSF = new JFrame();
		testFrameNSF.setSize(500, 500);
		testFrameNSF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrameNSF.setLocationRelativeTo(null);
		NSFPanel nsfJPanel = new NSFPanel();
		testFrameNSF.add(nsfJPanel);
		testFrameNSF.setVisible(true);
		
		
	}

}
