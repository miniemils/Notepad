package application;

import javax.swing.*;
import gui.NotepadGUI;

public class Program {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					try {
						new NotepadGUI().setVisible(true);
						UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
		});

	}

}