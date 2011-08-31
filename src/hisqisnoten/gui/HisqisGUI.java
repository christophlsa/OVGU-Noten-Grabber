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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HisqisGUI extends JFrame implements PropertyChangeListener, ActionListener {

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
	JButton syncButton;

	private String username;
	private String password;

	HisqisGUIGrabber grabber;

	public HisqisGUI(String username, String password) {
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

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

		JLabel averageMarkTitleLabel = new JLabel("Note: ");
		bottomPanel.add(averageMarkTitleLabel);
		averageMarkLabel = new JLabel("0", JLabel.RIGHT);
		bottomPanel.add(averageMarkLabel);

		JLabel totalCPTitleLabel = new JLabel("CP: ");
		bottomPanel.add(totalCPTitleLabel);
		totalCPLabel = new JLabel("0", JLabel.RIGHT);
		bottomPanel.add(totalCPLabel);

		progressBar = new JProgressBar(0, 6);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		bottomPanel.add(progressBar);

		syncButton = new JButton("Update");
		syncButton.addActionListener(this);

		bottomPanel.add(syncButton);

		add(bottomPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		setVisible(true);

		this.username = username;
		this.password = password;

		if (GregorianCalendar.getInstance(TimeZone.getTimeZone("CET")).get(GregorianCalendar.HOUR_OF_DAY) == 2) {
			int result = JOptionPane.showConfirmDialog(this, "Es ist zwischen 2 und 3 Uhr. Laut Webseite sollten derzeit Wartungsarbeiten laufen.\n\nTrotztdem weitermachen?", "eventuelle Wartungsarbeiten", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (result == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}

		process(false);
	}

	public void process(boolean forceLoginDialog) {
		syncButton.setEnabled(false);

		if (username == null || password == null || forceLoginDialog) {
			Object[] loginData = HisqisLoginDataDialog.getLoginDataViaDialog(this, username, password);

			if (!(Boolean) loginData[0] || loginData.length != 3) {
				syncButton.setEnabled(true);
				return;
			}

			username = (String) loginData[1];
			password = (String) loginData[2];
		}

		grabber = new HisqisGUIGrabber(this.username, this.password);
		grabber.addPropertyChangeListener(this);

		grabber.setUser(username);
		grabber.setPassword(password);

		grabber.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource().equals(grabber)) {
			if (event.getPropertyName().equals("progress")) {
				progressBar.setValue((Integer) event.getNewValue());
			}

			if(grabber.isDone()) {
				if (grabber.isCancelled()) {
					grabber.removePropertyChangeListener(this);

					JOptionPane.showMessageDialog(this, "Username or Password are wrong.", "Login failed", JOptionPane.ERROR_MESSAGE);
					process(true);
				} else {
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

				progressBar.setValue(0);
				syncButton.setEnabled(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(syncButton)) {
			process(false);
		}
	}
}
