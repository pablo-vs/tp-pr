package es.ucm.fdi.view.customcomponents;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.awt.Dimension;
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
	private JFileChooser fileChooser = new JFileChooser();
	private JTextArea textArea = new JTextArea();
	private JPopupMenu popupMenu = new JPopupMenu();
	private TextOutputStream asOutputStream;
	
	public CustomTextComponent(boolean isEditable){
		setLayout(new GridLayout());
		
		asOutputStream = new TextOutputStream();
		textArea.setEditable(isEditable);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane area = new JScrollPane(textArea);
		setPreferredSize(new Dimension(400,300));
		setLayout(new BorderLayout());
		add(area, BorderLayout.CENTER);
		
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
	
	public void append(String s){
		textArea.append(s);
	}
	
	public void setPopupMenu(JPopupMenu pm){
		popupMenu = pm;
	}
	
	public boolean load() throws IOException{
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			textArea.setText(new String(
					Files.readAllBytes(file.toPath()), "UTF-8"));	
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean save() throws IOException{
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Files.write(file.toPath(), textArea.getText().getBytes("UTF-8"));
			
			return true;
		} else {
			return false;
		}
	}
	
	public String getText() {
		return textArea.getText();
	}

	public void setText(String text) {
		textArea.setText(text);
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
