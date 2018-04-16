package es.ucm.fdi.view;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.nio.file.Files;

import javax.swing.*;
import java.awt.*;

public class CustomTextComponent extends JPanel {

	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 8279020056163205261L;
	private JFileChooser fileChooser;
	private JTextArea textArea;
	
	public CustomTextComponent(boolean isEditable){
		textArea = new JTextArea();
		textArea.setEditable(isEditable);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane area = new JScrollPane(textArea);
		setPreferredSize(new Dimension(400,300));
		setLayout(new BorderLayout());
		add(area, BorderLayout.CENTER);
		fileChooser = new JFileChooser();
	}
	
	public void load() throws IOException{
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			textArea.setText(new String(
					Files.readAllBytes(file.toPath()), "UTF-8"));	
		}
	}
	
	public void save() throws IOException{
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Files.write(file.toPath(), textArea.getText().getBytes("UTF-8"));
		}
	}
	
	public String getText() {
		return textArea.getText();
	}

	public void clear(){
		textArea.setText("");
	}
}
