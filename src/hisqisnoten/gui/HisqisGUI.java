package hisqisnoten.gui;

import hisqisnoten.HQNContainer;
import hisqisnoten.HQNContainerComparator;
import hisqisnoten.HisqisGrabber;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class HisqisGUI extends JFrame {

	/**
	 * I don't know why'but Eclipse expect a version id.
	 */
	private static final long serialVersionUID = -8698476155618666560L;
	
	HisqisTableModel tableModel;
	JTable table;
	JScrollPane scrollPane;
	
	private String user;
	private String password;

	public HisqisGUI(String user, String password) {
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
        setTitle("Hisqis Noten Grabber");
        
        if (user == null || password == null) {
        	System.err.println("Username and Password missing!");
        	System.exit(1);
        } else {
        	this.user = user;
        	this.password = password;
        }
        
        HisqisGrabber grabber = new HisqisGrabber(this.user, this.password);
        ArrayList<HQNContainer> marks = grabber.process();
            
        // nach Semester sortieren
        Collections.sort(marks, new HQNContainerComparator());
		
		tableModel = new HisqisTableModel(marks);
		table = new JTable(tableModel);
		
		table.getColumnModel().getColumn(0).setMinWidth(350);
		
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(1).setMaxWidth(100);
		
		table.getColumnModel().getColumn(2).setMinWidth(50);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
		
		table.getColumnModel().getColumn(3).setMinWidth(120);
		table.getColumnModel().getColumn(3).setMaxWidth(120);
		
		scrollPane = new JScrollPane(table);
		
		add(scrollPane, BorderLayout.CENTER);
		
		//pack();
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
	}
}
