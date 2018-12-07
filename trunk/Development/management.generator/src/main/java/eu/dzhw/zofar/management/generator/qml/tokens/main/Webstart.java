package eu.dzhw.zofar.management.generator.qml.tokens.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import eu.dzhw.zofar.management.generator.qml.tokens.MitarbeiterGenerator;
import eu.dzhw.zofar.management.generator.qml.tokens.PanelGenerator;
import eu.dzhw.zofar.management.generator.qml.tokens.ParticipantGenerator;
import eu.dzhw.zofar.management.generator.qml.tokens.components.ui.CustomTable;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.system.SystemClient;

public class Webstart implements ActionListener, ItemListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Webstart.class);

	private JFrame frmTokenGenerator;
	private Task task;
	private JTextField txtPrefix;
	private JTextField txtPostfix;
	private JTextField txtZeichen;
	private JTextField sample;
	private JTextArea outputCSV;
	private JButton clipboardCSVBtn;
	private JButton btnGenerieren;
	private JScrollPane scrollPaneCSV;
	private JScrollPane scrollPaneQML;
	private JTextArea outputQML;
	private JButton clipboardQMLBtn;
	private JLabel lblQml;
	private JLabel lblCSV;
	private JTextField txtUrl;
	private JLabel lblUrl;
	private JButton btnExit;
	private JPanel specialOptionPanel;
	private JLabel lblSpezialoptionen;
	private JComboBox<?> specialOptions;

	private JPanel panelOptions;

	private CardLayout cl;

	private JPanel employeeOptions;

	private JPanel noneOptions;
	private JLabel lblServer;
	private JTextField serverField;
	private JLabel lblUser;
	private JTextField userField;
	private JLabel lblPasswort;
	private JTextField passField;
	private JTextField portField;
	private JLabel lblPort;
	private JTextField dbField;
	private JLabel lblDatenbank;
	private JLabel lblKriterien;
	private JTextField criteriaField;
	private JScrollPane scrollPaneMapping;

	private JTextArea outputMapping;
	private JButton clipboardMappingBtn;
	private JLabel lblMapping;
	private JLabel lblSqlscript;
	private JButton btnSQL;

	public String sqlScript;

	private JFileChooser fc;

	private JTabbedPane tabbedPane;
	private JLabel lblRegeln;
	
	private JTable table;
	
	private List<String> preloadFunctions;
	private JPanel panel_1;
	private JButton btnAddRow;
	private JButton btnDeleteSelectedRows;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Webstart window = new Webstart();
					window.frmTokenGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Webstart() {
		initialize();
	}

	private class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			Webstart.this.btnGenerieren.setEnabled(false);
			Webstart.this.clipboardQMLBtn.setEnabled(false);
			Webstart.this.outputQML.setText("");
			Webstart.this.clipboardCSVBtn.setEnabled(false);
			Webstart.this.outputCSV.setText("");
			Webstart.this.clipboardMappingBtn.setEnabled(false);
			Webstart.this.outputMapping.setText("");
			Webstart.this.btnSQL.setEnabled(false);
			Webstart.this.sqlScript = null;

			Webstart.this.frmTokenGenerator.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

//			LOGGER.info("build up preloads...");
			Map<Integer, Map<String, Map<String, String>>> preloadRules = Webstart.this.plainPreloadRules();
//			if(preloadRules != null){ 
//				for(Map.Entry<Integer, Map<String, Map<String, String>>> item:preloadRules.entrySet()){
//					final Integer tokenNr = item.getKey();
//					final Map<String, Map<String, String>> preloadMap = item.getValue();
//					LOGGER.info("Preload ("+tokenNr+") : "+preloadMap.toString());					
//				}
//				
//			}
//			LOGGER.info("...done");
			if (Webstart.this.specialOptions.getSelectedItem().equals("Mitarbeiter")) {
				final MitarbeiterGenerator generator = MitarbeiterGenerator.getInstance();
				try {
					Map<String, String> result = generator.generate(Webstart.this.txtUrl.getText(),preloadRules);
					if (result != null) {
						if (result.containsKey("csv")) {
							Webstart.this.outputCSV.setText(result.get("csv"));
							Webstart.this.clipboardCSVBtn.setEnabled(true);
						}
						if (result.containsKey("qml")) {
							Webstart.this.outputQML.setText(result.get("qml"));
							Webstart.this.clipboardQMLBtn.setEnabled(true);
						}
						if (result.containsKey("sql")) {
							Webstart.this.sqlScript = result.get("sql");

							Webstart.this.btnSQL.setEnabled(Webstart.this.sqlScript != null);
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Webstart.this.frmTokenGenerator, "Fehler :\n" + e.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			} else if (Webstart.this.specialOptions.getSelectedItem().equals("Panel")) {
				final PanelGenerator generator = PanelGenerator.getInstance();

				try {
					Map<String, String> result = generator.generate(Webstart.this.serverField.getText(), Webstart.this.portField.getText(), Webstart.this.dbField.getText(), Webstart.this.userField.getText(), Webstart.this.passField.getText(),
							Webstart.this.criteriaField.getText(), Webstart.this.txtPrefix.getText(), Webstart.this.txtPostfix.getText(), Webstart.this.txtUrl.getText(), Webstart.this.txtZeichen.getText(), 5, 8,preloadRules);
					if (result != null) {
						if (result.containsKey("csv")) {
							Webstart.this.outputCSV.setText(result.get("csv"));
							Webstart.this.clipboardCSVBtn.setEnabled(true);
						}
						if (result.containsKey("qml")) {
							Webstart.this.outputQML.setText(result.get("qml"));
							Webstart.this.clipboardQMLBtn.setEnabled(true);
						}
						if (result.containsKey("mapping")) {
							Webstart.this.outputMapping.setText(result.get("mapping"));
							Webstart.this.clipboardMappingBtn.setEnabled(true);
						}
						if (result.containsKey("sql")) {
							Webstart.this.sqlScript = result.get("sql");

							Webstart.this.btnSQL.setEnabled(Webstart.this.sqlScript != null);
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Webstart.this.frmTokenGenerator, "Fehler :\n" + e.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				final ParticipantGenerator generator = ParticipantGenerator.getInstance();
				try {
					Map<String, String> result = generator.generate(Webstart.this.txtPrefix.getText(), Webstart.this.txtPostfix.getText(), Webstart.this.txtUrl.getText(), Webstart.this.txtZeichen.getText(), 5, 8, Integer.parseInt(Webstart.this.sample.getText()),preloadRules);
					if (result != null) {
						if (result.containsKey("csv")) {
							Webstart.this.outputCSV.setText(result.get("csv"));
							Webstart.this.clipboardCSVBtn.setEnabled(true);
						}
						if (result.containsKey("qml")) {
							Webstart.this.outputQML.setText(result.get("qml"));
							Webstart.this.clipboardQMLBtn.setEnabled(true);
						}
						if (result.containsKey("sql")) {
							Webstart.this.sqlScript = result.get("sql");

							Webstart.this.btnSQL.setEnabled(Webstart.this.sqlScript != null);
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(Webstart.this.frmTokenGenerator, "Fehler :\n" + e.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			}
			Webstart.this.btnGenerieren.setEnabled(true);
			Webstart.this.frmTokenGenerator.setCursor(Cursor.getDefaultCursor());
			return null;
		}
	}

	private void toggle() {
		final boolean employee = specialOptions.getSelectedItem().equals("Mitarbeiter");
		final boolean panel = specialOptions.getSelectedItem().equals("Panel");
		sample.setEnabled(!employee);
		txtPrefix.setEnabled(!employee);
		txtPostfix.setEnabled(!employee);
		txtZeichen.setEnabled(!employee);
		txtUrl.setEnabled(!employee);

		lblMapping.setVisible(panel);
		scrollPaneMapping.setVisible(panel);
		clipboardMappingBtn.setVisible(panel);

		this.clipboardQMLBtn.setEnabled(false);
		this.outputQML.setText("");
		this.clipboardCSVBtn.setEnabled(false);
		this.outputCSV.setText("");
		this.clipboardMappingBtn.setEnabled(false);
		this.outputMapping.setText("");
		this.btnSQL.setEnabled(false);

		cl.show(specialOptionPanel, specialOptions.getSelectedItem() + "");
	}
	
	protected Map<Integer,Map<String,Map<String,String>>> plainPreloadRules(){
		final int rowCount = this.table.getRowCount();
		final int columnCount = this.table.getColumnCount();
		LOGGER.info(rowCount+" preload rules found");
		
		Map<Integer,Map<String,Map<String,String>>> back = new HashMap<Integer,Map<String,Map<String,String>>>();
		for(int a=0;a<rowCount;a++){
			final HashMap<Integer,Object> ruleMap = new LinkedHashMap<Integer,Object>();
			boolean failed = false;
			for(int b=0;b<columnCount;b++){
				final Object value = this.table.getModel().getValueAt(a, b);
				final Class clazz = value.getClass();
				final String name = this.table.getModel().getColumnName(b);
				if((List.class).isAssignableFrom(clazz)){
					LOGGER.warn("ignore rule. cause : no function selected");
					failed = true;
				}
//				LOGGER.info("Cell ("+name+" ) ["+b+"] = "+value+" ("+value.getClass()+")");
				ruleMap.put(b, value);
			}
			if(failed){
				LOGGER.info("skip rule : "+ruleMap);
				continue;
			}
			
			LOGGER.info("build rule : "+ruleMap);
			
			final Integer fromIndex = new Integer(ruleMap.get(0)+"");
			final Integer toIndex = new Integer(ruleMap.get(1)+"");
			
			final String variable = ruleMap.get(2)+"";
			final String function = ruleMap.get(3)+"";
			final String value = ruleMap.get(4)+"";
			
			for(Integer b=fromIndex;b<=toIndex;b++){
				Map<String,Map<String,String>> preloadMap = null;
				if(back.containsKey(b))preloadMap = back.get(b);
				if(preloadMap == null)preloadMap = new LinkedHashMap<String,Map<String,String>>();
				final Map<String,String> functionPair = new HashMap<String,String>();

				
				functionPair.put("function", function);
				functionPair.put("value", value);
				LOGGER.info(functionPair+" ("+variable+") => "+preloadMap);
				preloadMap.put(variable, functionPair);

				back.put(b, preloadMap);
			}

			
//			final Integer fromIndex = (Integer) this.table.getModel().getValueAt(a, 0);
//			final Integer toIndex = (Integer) this.table.getModel().getValueAt(a, 1);
//			final String variable = (String) this.table.getModel().getValueAt(a, 2);
//			final String function = (String) this.table.getModel().getValueAt(a, 3);
//			final String value = (String) this.table.getModel().getValueAt(a, 4);
//			
//			LOGGER.info("build rule :  [from="+fromIndex+",to="+toIndex+",variable="+variable+",function="+function+",value="+value+"]");
//			
//			for(Integer b=fromIndex;b<=toIndex;b++){
//				Map<String,Map<String,String>> preloadMap = null;
//				if(back.containsKey(b))preloadMap = back.get(b);
//				if(preloadMap == null)preloadMap = new HashMap<String,Map<String,String>>();
//				final Map<String,String> functionPair = new HashMap<String,String>();
//				functionPair.put(function, value);
//				LOGGER.info(functionPair+" ("+variable+") => "+preloadMap);
//				preloadMap.put(variable, functionPair);
//
//				back.put(b, preloadMap);
//			}
		}
		return back;
	}
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		preloadFunctions = new ArrayList<String>();
		preloadFunctions.add("Fester Wert");
		preloadFunctions.add("Zufälliger Wert aus");

		frmTokenGenerator = new JFrame();
		frmTokenGenerator.setTitle("Token Generator");
		frmTokenGenerator.setBounds(100, 100, 635, 706);
		frmTokenGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane();
		frmTokenGenerator.getContentPane().add(this.tabbedPane);

		JComponent panel1 = new JPanel();
		this.tabbedPane.addTab("Generator", panel1);
		panel1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("max(100dlu;pref):grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblStichprobe = new JLabel("Stichprobe");
		panel1.add(lblStichprobe, "6, 2, right, center");

		sample = new JTextField();
		sample.setText("100");
		panel1.add(sample, "8, 2, fill, default");

		JLabel lblPrefix = new JLabel("Prefix");
		panel1.add(lblPrefix, "6, 4, right, center");

		txtPrefix = new JTextField();
		txtPrefix.setText("prefix");
		panel1.add(txtPrefix, "8, 4, fill, default");
		txtPrefix.setColumns(10);

		JLabel lblPostfix = new JLabel("Postfix");
		panel1.add(lblPostfix, "6, 6, right, center");

		txtPostfix = new JTextField();
		txtPostfix.setText("postfix");
		panel1.add(txtPostfix, "8, 6, fill, default");
		txtPostfix.setColumns(10);

		JLabel lblGltigeZeichen = new JLabel("Gültige Zeichen");
		panel1.add(lblGltigeZeichen, "6, 8, right, center");

		txtZeichen = new JTextField();
		txtZeichen.setText("abcdefghijklmnopqrstuvwxyzABDEFGHJMNQRT1234567890");
		txtZeichen.setCaretPosition(0);
		panel1.add(txtZeichen, "8, 8, fill, default");
		txtZeichen.setColumns(10);

		lblUrl = new JLabel("URL");
		panel1.add(lblUrl, "6, 10, right, center");

		txtUrl = new JTextField();
		txtUrl.setText("http://survey01.dzhw.eu/<befragungsname>/special/login.html?zofar_token=");
		txtUrl.setCaretPosition(0);
		panel1.add(txtUrl, "8, 10, fill, center");
		txtUrl.setColumns(10);

		lblSpezialoptionen = new JLabel("Spezial-Optionen");
		panel1.add(lblSpezialoptionen, "6, 12, right, center");

		specialOptions = new JComboBox();
		specialOptions.setModel(new DefaultComboBoxModel(new String[] { "keine", "Mitarbeiter", "Panel" }));
		specialOptions.setSelectedIndex(0);
		specialOptions.addActionListener(this);
		specialOptions.addItemListener(this);
		specialOptions.setActionCommand("toogle");
		panel1.add(specialOptions, "8, 12, fill, center");

		lblMapping = new JLabel("Mapping");
		lblMapping.setVisible(false);
		panel1.add(lblMapping, "6, 14, center, center");

		scrollPaneMapping = new JScrollPane();
		outputMapping = new JTextArea();
		outputMapping.setColumns(30);
		outputMapping.setRows(10);
		scrollPaneMapping.setViewportView(outputMapping);
		outputMapping.setEditable(false);
		scrollPaneMapping.setVisible(false);
		panel1.add(scrollPaneMapping, "6, 16");

		clipboardMappingBtn = new JButton("Zwischenablage");
		clipboardMappingBtn.addActionListener(this);
		clipboardMappingBtn.setActionCommand("clipboard_mapping");
		clipboardMappingBtn.setEnabled(false);
		clipboardMappingBtn.setVisible(false);
		panel1.add(clipboardMappingBtn, "6, 18");

		cl = new CardLayout();
		specialOptionPanel = new JPanel(cl);
		noneOptions = new JPanel();
		specialOptionPanel.add(noneOptions, "keine");
		noneOptions.setLayout(new FormLayout(new ColumnSpec[] {}, new RowSpec[] {}));
		panelOptions = new JPanel();
		panelOptions.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("19px"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		specialOptionPanel.add(panelOptions, "Panel");

		lblServer = new JLabel("Server");
		panelOptions.add(lblServer, "2, 2, right, center");

		serverField = new JTextField();
		serverField.setText("localhost");
		panelOptions.add(serverField, "4, 2, fill, center");
		serverField.setColumns(10);

		lblPort = new JLabel("Port");
		panelOptions.add(lblPort, "2, 4, right, center");

		portField = new JTextField();
		portField.setText("5432");
		panelOptions.add(portField, "4, 4, fill, default");
		portField.setColumns(10);

		lblUser = new JLabel("User");
		panelOptions.add(lblUser, "2, 6, right, center");

		userField = new JTextField();
		userField.setText("postgres");
		panelOptions.add(userField, "4, 6, fill, center");
		userField.setColumns(10);

		lblPasswort = new JLabel("Passwort");
		panelOptions.add(lblPasswort, "2, 8, right, center");

		passField = new JTextField();
		passField.setText("postgres");
		panelOptions.add(passField, "4, 8, fill, center");
		passField.setColumns(10);

		lblDatenbank = new JLabel("Datenbank");
		panelOptions.add(lblDatenbank, "2, 10, right, center");

		dbField = new JTextField();
		dbField.setText("panel");
		panelOptions.add(dbField, "4, 10, fill, center");
		dbField.setColumns(10);

		lblKriterien = new JLabel("Kriterien");
		panelOptions.add(lblKriterien, "2, 12, right, center");

		criteriaField = new JTextField();
		criteriaField.setText("answeroption.quid = '1094' AND answeroption.answeroptionuid='84'");
		panelOptions.add(criteriaField, "4, 12, fill, center");
		criteriaField.setColumns(10);
		employeeOptions = new JPanel();
		specialOptionPanel.add(employeeOptions, "Mitarbeiter");
		employeeOptions.setLayout(new FormLayout(new ColumnSpec[] {}, new RowSpec[] {}));

		panel1.add(specialOptionPanel, "8, 16, default, fill");

		btnGenerieren = new JButton("Generieren");
		btnGenerieren.addActionListener(this);
		btnGenerieren.setActionCommand("generate");
		panel1.add(btnGenerieren, "8, 18, fill, center");

		lblSqlscript = new JLabel("SQL-Script");
		panel1.add(lblSqlscript, "6, 20, right, center");

		btnSQL = new JButton("Speichern...");
		btnSQL.setEnabled(false);
		btnSQL.addActionListener(this);
		btnSQL.setActionCommand("sql");
		panel1.add(btnSQL, "8, 20, left, center");

		lblQml = new JLabel("QML");
		panel1.add(lblQml, "6, 22, center, center");

		lblCSV = new JLabel("CSV");
		panel1.add(lblCSV, "8, 22, center, center");

		clipboardQMLBtn = new JButton("Zwischenablage");
		clipboardQMLBtn.setEnabled(false);
		clipboardQMLBtn.addActionListener(this);

		scrollPaneQML = new JScrollPane();
		panel1.add(scrollPaneQML, "6, 24");

		outputQML = new JTextArea();
		outputQML.setColumns(30);
		outputQML.setRows(10);
		scrollPaneQML.setViewportView(outputQML);
		outputQML.setEditable(false);
		clipboardQMLBtn.setActionCommand("clipboard_qml");
		panel1.add(clipboardQMLBtn, "6, 26");

		scrollPaneCSV = new JScrollPane();
		panel1.add(scrollPaneCSV, "8, 24");

		outputCSV = new JTextArea();
		outputCSV.setColumns(30);
		outputCSV.setRows(10);
		scrollPaneCSV.setViewportView(outputCSV);
		outputCSV.setEditable(false);

		clipboardCSVBtn = new JButton("Zwischenablage");
		clipboardCSVBtn.addActionListener(this);
		clipboardCSVBtn.setActionCommand("clipboard_csv");
		clipboardCSVBtn.setEnabled(false);
		panel1.add(clipboardCSVBtn, "8, 26");

		btnExit = new JButton("Schließen");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("exit");
		panel1.add(btnExit, "8, 28");
		
		JComponent panel2 = new JPanel();
		this.tabbedPane.addTab("Preloads", panel2);
		panel2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblRegeln = new JLabel("Regeln");
		panel2.add(lblRegeln, "5, 2, center, center");
		
		
		final String[] columns = new String[6]; 
		columns[0] = "Von Index";
		columns[1] = "Bis Index";
		columns[2] = "Variable";
		columns[3] = "Funktion";
		columns[4] = "Wert";
		columns[5] = "Auswahl";
		
		final Class[] colClasses = new Class[6];
		colClasses[0] = Integer.class;
		colClasses[1] = Integer.class;
		colClasses[2] = String.class;
		colClasses[3] = List.class;
		colClasses[4] = String.class;
		colClasses[5] = Boolean.class;
		
		final Object[][] rows = new Object[0][6];
		
		table = new CustomTable(columns,colClasses,rows);
		table.setCellSelectionEnabled(true);
		JScrollPane tableScrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		panel2.add(tableScrollPane, "5, 4, fill, fill");
		
		panel_1 = new JPanel();
		panel2.add(panel_1, "5, 6, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));
		
		btnAddRow = new JButton("Add Row");
		btnAddRow.addActionListener(this);
		btnAddRow.setActionCommand("ADDROW");
		
		panel_1.add(btnAddRow, BorderLayout.WEST);
		
		btnDeleteSelectedRows = new JButton("Delete selected Rows");
		btnDeleteSelectedRows.addActionListener(this);
		btnDeleteSelectedRows.setActionCommand("DELROWS");
		panel_1.add(btnDeleteSelectedRows, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		if (cmd != null) {
			if (cmd.equals("toggle")) {
				this.toggle();
			} else if (cmd.equals("generate")) {
				task = new Task();
				task.execute();
			} else if (cmd.equals("clipboard_qml")) {
				SystemClient.getInstance().toClipboard(outputQML.getText());
			} else if (cmd.equals("clipboard_csv")) {
				SystemClient.getInstance().toClipboard(outputCSV.getText());
			} else if (cmd.equals("clipboard_mapping")) {
				SystemClient.getInstance().toClipboard(outputMapping.getText());
			} else if (cmd.equals("sql")) {
				if (this.fc == null) {
					this.fc = new JFileChooser();
					final File preset = new File(new File(System.getProperty("user.home")), "preloads.sql");
					this.fc.setSelectedFile(preset);
				}
				final int returnVal = this.fc.showSaveDialog(this.frmTokenGenerator);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File file = this.fc.getSelectedFile();
					try {
						FileClient.getInstance().writeToFile(file.getAbsolutePath(), this.sqlScript, false);
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
				}
			} else if (cmd.equals("exit")) {
				this.frmTokenGenerator.dispatchEvent(new WindowEvent(this.frmTokenGenerator, WindowEvent.WINDOW_CLOSING));
			}
		}
		if((e.getSource() != null)&&(e.getSource().equals(this.btnAddRow))){
			final Object[] rowData = new Object[] { 1,Integer.parseInt(this.sample.getText()) , "variable",preloadFunctions, "data", new Boolean(false) };
			((DefaultTableModel)table.getModel()).addRow(rowData);
		}
		if((e.getSource() != null)&&(e.getSource().equals(this.btnDeleteSelectedRows))){
			DefaultTableModel model = ((DefaultTableModel)table.getModel());
			final Vector<Vector<Object>> modelData = model.getDataVector();
			Iterator<Vector<Object>> it = modelData.iterator(); 
			final List<Vector<Object>> toRemove = new ArrayList<Vector<Object>>();

			while(it.hasNext()){
				final Vector<Object> row = it.next();
				if((row != null)&&(row.size()>=6)&&(((Boolean)row.get(5)))){
					toRemove.add(row);
				}
			}
			it = toRemove.iterator();
			while(it.hasNext()){
				final Vector<Object> row = it.next();
				final int index = modelData.indexOf(row);
				if((row != null)&&(row.size()>=6)&&(((Boolean)row.get(5)))){
					model.removeRow(index);
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		final Object source = e.getSource();
		if (source != null) {
			if (source.equals(this.specialOptions))
				this.toggle();
		}
	}
}
