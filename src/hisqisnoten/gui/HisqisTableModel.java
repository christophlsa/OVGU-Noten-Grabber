package hisqisnoten.gui;

import hisqisnoten.HQNContainer;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class HisqisTableModel extends AbstractTableModel {

	/**
	 * I don't know why'but Eclipse expect a version id.
	 */
	private static final long serialVersionUID = -9203527508628027317L;
	
	private ArrayList<HQNContainer> marks;

	public HisqisTableModel(ArrayList<HQNContainer> marks) {
		super();
		
		this.marks = marks;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		if (marks != null) {
			return marks.size();
		}
		
		return 0;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex) {
			case 0:
				return "Fach";
			case 1:
				return "Semester";
			case 2:
				return "Note";
			case 3:
				return "CP";
			case 4:
				return "Bestanden";
			default:
				return "";
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= getRowCount()
				|| columnIndex >= getColumnCount()
				|| marks.get(rowIndex) == null) {
			return "";
		}
		
		HQNContainer mark = marks.get(rowIndex);
		
		switch(columnIndex) {
			case 0:
				return mark.getFach();
			case 1:
				return mark.getSemester();
			case 2:
				return mark.getNote();
			case 3:
				return mark.getCreditpoints();
			case 4:
				return mark.getBestanden();
			default:
				return "";
		}
	}
	
	public void setMarks(ArrayList<HQNContainer> marks) {
		this.marks = marks;
		
		fireTableDataChanged();
	}
}
