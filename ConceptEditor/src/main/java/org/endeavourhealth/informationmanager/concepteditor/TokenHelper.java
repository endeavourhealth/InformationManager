package org.endeavourhealth.informationmanager.concepteditor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TokenHelper extends JList {
	private Document document;
	public TokenHelper(Document document){
		super(new DefaultListModel());
		this.document= document;
		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
					int index = locationToIndex(e.getPoint());

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
	}

	public DefaultListModel getContents() {
		return (DefaultListModel)getModel();
	}
}
