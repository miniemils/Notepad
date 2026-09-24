package gui;
import javax.swing.*;

public class FontMenu extends JDialog {
	
	private NotepadGUI source;
	
	public FontMenu(NotepadGUI source) {
		this.source = source;
		setTitle("Font Settings");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(425, 350);
		setLocationRelativeTo(source);
		setModal(true);
		
		addMenuComponents();
	}
	
	private void addMenuComponents() {
		JLabel fontLabel = new JLabel("Font: ");
	}
	
}
