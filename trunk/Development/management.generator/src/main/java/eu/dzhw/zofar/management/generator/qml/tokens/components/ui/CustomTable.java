package eu.dzhw.zofar.management.generator.qml.tokens.components.ui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomTable extends JTable {

	private static final long serialVersionUID = -7138013539032357460L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomTable.class);

	public CustomTable(String[] columns,Class[] colClasses,Object[][] rows) {
		this(new CustomTableModel(columns,colClasses,rows));
	}	
	
	private CustomTable(TableModel dm) {
		super(dm);
		final int count = dm.getColumnCount();
		for(int a=0;a<count;a++){
			final Class clazz = dm.getColumnClass(a);
			if((List.class).isAssignableFrom(clazz)){
				this.getColumnModel().getColumn(a).setCellEditor(new ComboCellEditor());
			}
		}
	}
}
