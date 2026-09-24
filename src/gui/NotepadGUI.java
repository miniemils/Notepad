package gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JCheckBoxMenuItem;

public class NotepadGUI extends JFrame{
	
	private JTextArea textArea;
	private UndoManager undoManager;
	private File currentFile;
	private JFileChooser fileChooser;

	public NotepadGUI() {
		super("Notepad");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		undoManager = new UndoManager();
		fileChooser = new JFileChooser();
		
		addGuiComponents();
	}
	
	private void addGuiComponents() {
		addToolBar();
		
		textArea = new JTextArea();
		textArea.getDocument().addUndoableEditListener(
			new UndoableEditListener() {
				@Override
				public void undoableEditHappened(UndoableEditEvent e) {
					undoManager.addEdit(e.getEdit());
				}
			}
		);
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void addToolBar() {
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		toolBar.setFloatable(false);
		
		JMenuBar menuBar = new JMenuBar();	
		menuBar.add(addFileMenu());
		menuBar.add(addEditMenu());
		menuBar.add(addFormatMenu());
		menuBar.add(addViewMenu());
		toolBar.add(menuBar);
	}
	
	private JMenu addFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setTitle("Notepad");
					textArea.setText("");
					currentFile = null;
				}
			}
		);
		fileMenu.add(newMenuItem);
		
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = fileChooser.showOpenDialog(NotepadGUI.this);
					if(result != JFileChooser.APPROVE_OPTION)
						return;
					try {
						newMenuItem.doClick();
							
						File selectedFile = fileChooser.getSelectedFile();
						setTitle(selectedFile.getName());
						currentFile = selectedFile;
							
						FileReader fileReader = new FileReader(selectedFile);
						BufferedReader bufferedReader = new BufferedReader(fileReader);
							
						String readText;
						StringBuilder fileText = new StringBuilder();
						while((readText = bufferedReader.readLine()) != null) {
							fileText.append(readText + "/n");
						}
							
						bufferedReader.close();
						textArea.setText(fileText.toString());
					}
					catch(Exception e1) {
					}
				}
			}
		);
		fileMenu.add(openMenuItem);
		
		JMenuItem saveAsMenuItem = new JMenuItem("Save as");
		saveAsMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = fileChooser.showSaveDialog(NotepadGUI.this);
					if(result != JFileChooser.APPROVE_OPTION)
						return;
					try {
						File selectedFile = fileChooser.getSelectedFile();
						
						String fileName = selectedFile.getName();
						if(!fileName.substring(fileName.length() - 4).equalsIgnoreCase(".txt")) {
							selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
						}
						
						selectedFile.createNewFile();
						
						FileWriter fileWriter = new FileWriter(selectedFile);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
						bufferedWriter.write(textArea.getText());
						bufferedWriter.close();
						fileWriter.close();
						
						setTitle(fileName);

	                    currentFile = selectedFile;

	                    JOptionPane.showMessageDialog(NotepadGUI.this, "Saved File!");
					}
					catch(Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		);
		fileMenu.add(saveAsMenuItem);
		
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					NotepadGUI.this.dispose();
				}
			}
		);
		fileMenu.add(exitMenuItem);
		
		return fileMenu;
	}
	
	private JMenu addEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(undoManager.canUndo()) {
						undoManager.undo();
					}
				}
			}
		);
		editMenu.add(undoMenuItem);	
		
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(undoManager.canRedo()) {
						undoManager.redo();
					}
				}
			}
		);
		editMenu.add(redoMenuItem);
		
		return editMenu;
	}
	
	private JMenu addFormatMenu() {
		JMenu formatMenu = new JMenu("Format");
		
		JCheckBoxMenuItem wordWrapMenuItem = new JCheckBoxMenuItem("Word Wrap");
		wordWrapMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean isChecked = wordWrapMenuItem.getState();
					if(isChecked) {
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);
					}
					else {
						textArea.setLineWrap(false);
						textArea.setWrapStyleWord(false);
					}
				}
			}
		);
		formatMenu.add(wordWrapMenuItem);
		
		JMenu alignTextMenu = new JMenu("Align Text");
		
		JMenuItem alignTextLeftMenuItem = new JMenuItem("Left");
		alignTextLeftMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				}
			});
		alignTextMenu.add(alignTextLeftMenuItem);
		
		JMenuItem alignTextRightMenuItem = new JMenuItem("Right");
		alignTextRightMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				}
			});
		alignTextMenu.add(alignTextRightMenuItem);
		
		formatMenu.add(alignTextMenu);
		
		JMenuItem fontMenuItem = new JMenuItem("Font...");
		fontMenuItem.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new FontMenu(NotepadGUI.this).setVisible(true);
				}
			});
		formatMenu.add(fontMenuItem);
		
		return formatMenu;
	}
	
	private JMenu addViewMenu() {
		JMenu viewMenu = new JMenu("View");
		return viewMenu;
	}
}
