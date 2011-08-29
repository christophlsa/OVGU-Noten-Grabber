/* Copyright (C) 2010-2011 Christoph Giesel
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package hisqisnoten.gui;

import hisqisnoten.HQNContainer;
import hisqisnoten.HQNContainerComparator;
import hisqisnoten.HisqisGUIGrabber;
import hisqisnoten.HisqisGrabberResults;
import hisqisnoten.gui.dialog.HisqisLoginDataDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HisqisGUI extends JFrame implements PropertyChangeListener {

	/**
	 * I don't know why but Eclipse expects a version id.
	 */
	private static final long serialVersionUID = -8698476155618666560L;
	
	HisqisTableModel tableModel;
	JTable table;
	JScrollPane scrollPane;
	JProgressBar progressBar;
	JLabel averageMarkLabel;
	JLabel totalCPLabel;
	
	private String user;
	private String password;
	
	HisqisGUIGrabber grabber;

	public HisqisGUI(String user, String password) {
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
        setTitle("Hisqis Noten Grabber");
        
        setLayout(new BorderLayout());
		
		tableModel = new HisqisTableModel(null);
		table = new JTable(tableModel);
		
		table.getColumnModel().getColumn(0).setMinWidth(350);
		
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMaxWidth(100);
		
		table.getColumnModel().getColumn(2).setMinWidth(50);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
		
		table.getColumnModel().getColumn(3).setMinWidth(50);
		table.getColumnModel().getColumn(3).setMaxWidth(50);
		
		table.getColumnModel().getColumn(4).setMinWidth(120);
		table.getColumnModel().getColumn(4).setMaxWidth(120);
		
		scrollPane = new JScrollPane(table);
		
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new FlowLayout());
		
		JLabel averageMarkTitleLabel = new JLabel("Note: ");
		bottomPanel.add(averageMarkTitleLabel);
		averageMarkLabel = new JLabel("   ");
		bottomPanel.add(averageMarkLabel);
		
		bottomPanel.add(new JLabel("   "));
		
		JLabel totalCPTitleLabel = new JLabel("CP: ");
		bottomPanel.add(totalCPTitleLabel);
		totalCPLabel = new JLabel("   ");
		bottomPanel.add(totalCPLabel);
		
		bottomPanel.add(new JLabel("   "));
		
		progressBar = new JProgressBar(0, 6);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		bottomPanel.add(progressBar);
		
		add(bottomPanel, BorderLayout.SOUTH);
		
        setLocationRelativeTo(null);
        setVisible(true);
        
        if (user == null || password == null) {
        	String[] loginData = HisqisLoginDataDialog.getLoginDataViaDialog(this);
        	
        	this.user = loginData[0];
        	this.password = loginData[1];
        } else {
        	this.user = user;
        	this.password = password;
        }
        
        grabber = new HisqisGUIGrabber(this.user, this.password);
        grabber.addPropertyChangeListener(this);

        grabber.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource().equals(grabber)) {
			if (event.getPropertyName().equals("progress")) {
				progressBar.setValue((Integer) event.getNewValue());
			}
			
			if(grabber.isDone()) {
				try {
					HisqisGrabberResults results = grabber.get();
					
					ArrayList<HQNContainer> marks = results.getMarks();
					
					// nach Semester sortieren
			        Collections.sort(marks, new HQNContainerComparator());

			        tableModel.setMarks(marks);
			        
			        averageMarkLabel.setText(results.getAverageGrade());
			        totalCPLabel.setText(results.getTotalCreditPoints());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
