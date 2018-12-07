package eu.dzhw.zofar.management.generator.qml.tokens.components.ui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = -4967208485256641526L;
	
	private Class[] colClasses;

	public CustomTableModel(String[] columns,Class[] colClasses,Object[][] rows) {
		super(rows,(Object[])columns);
		this.colClasses = colClasses;
	}

    public Object getValueAt(int row, int col) {
        if((this.getDataVector().size() >= (row+1))&&(((Vector)this.getDataVector().get(row)).size() >= (col+1)))return ((Vector)this.getDataVector().get(row)).get(col);
        return null;
    }
//
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
//    	final Object obj = getValueAt(0, c);
//        if(obj != null)return obj.getClass();
//        return Object.class;
    	return colClasses[c];
    }

}
