package es.ucm.fdi.view;

import java.io.IOException;
import java.io.OutputStream;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.nio.file.Files;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomTextComponent extends JPanel {

	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 8279020056163205261L;
	private JFileChooser fileChooser;
	private JTextArea textArea;
	private JPopupMenu popupMenu;
	private TextOutputStream asOutputStream;
	
	public CustomTextComponent(boolean isEditable){
		fileChooser = new JFileChooser();
		setLayout(new GridLayout());
		textArea = new JTextArea();
		asOutputStream = new TextOutputStream();
		textArea.setEditable(isEditable);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane area = new JScrollPane(textArea);
		setPreferredSize(new Dimension(400,300));
		setLayout(new BorderLayout());
		add(area, BorderLayout.CENTER);
		
		popupMenu = new JPopupMenu();
		textArea.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(MouseEvent e) {
				if (e.isPopupTrigger() && popupMenu.isEnabled()) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
	}
	
	public void setPopupMenu(JPopupMenu pm){
		popupMenu = pm;
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
	
	public OutputStream getStreamToText(){
		return asOutputStream;
	}
	
	private class TextOutputStream extends OutputStream{
		private JTextArea area;
		
		public TextOutputStream(){
			area = textArea; 
		}
		
		@Override
		public void write(int b) throws IOException{
			textArea.append(String.valueOf((char)b));
			textArea.setCaretPosition(area.getDocument().getLength());
		}
	}
}
