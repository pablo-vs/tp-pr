package es.ucm.fdi.view.customcomponents;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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
	private TextOutputStream asOutputStream = new TextOutputStream();

	public CustomTextComponent(boolean isEditable) {
		setLayout(new GridLayout());

		textArea.setEditable(isEditable);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane area = new JScrollPane(textArea);
		setPreferredSize(new Dimension(400, 300));
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

	/**
	 * Appends text to the TextArea.
	 *
	 * @param s
	 *            The text to append.
	 */
	public void append(String s) {
		textArea.append(s);
	}

	public void setPopupMenu(JPopupMenu pm) {
		popupMenu = pm;
	}

	/**
	 * Launches a <code>JFileChooser</code> to load a file's contents to the
	 * TextArea.
	 */
	public boolean load() throws IOException {
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			textArea.setText(new String(Files.readAllBytes(file.toPath()),
					"UTF-8"));
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Launches a <code>JFileChooser</code> to save the TextArea's contents to
	 * the desired file.
	 */
	public boolean save() throws IOException {
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Files.write(file.toPath(), textArea.getText().getBytes("UTF-8"));

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return The text currently in the area.
	 */
	public String getText() {
		return textArea.getText();
	}

	/**
	 * Substitutes the current text for the given.
	 *
	 * @param text
	 *            The new text.
	 */
	public void setText(String text) {
		textArea.setText(text);
	}

	/**
	 * Removes all the text from the TextArea.
	 */
	public void clear() {
		textArea.setText("");
	}

	/**
	 * Returns the text in the TextArea as an <code>OutputStream</code>
	 *
	 * @return An <code>OutputStream</code> containing the text.
	 */
	public OutputStream getStreamToText() {
		return asOutputStream;
	}

	/**
	 * <code>OutputStream</code> with the textArea as an output channel.
	 */
	private class TextOutputStream extends OutputStream {
		private JTextArea area = textArea;

		/**
		 * Write to the textArea.
		 */
		@Override
		public void write(int b) throws IOException {
			textArea.append(String.valueOf((char) b));
			textArea.setCaretPosition(area.getDocument().getLength());
		}
	}
}
