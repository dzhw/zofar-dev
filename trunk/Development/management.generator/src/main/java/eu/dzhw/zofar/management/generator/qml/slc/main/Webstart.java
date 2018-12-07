package eu.dzhw.zofar.management.generator.qml.slc.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import eu.dzhw.zofar.management.generator.qml.slc.QMLGenerator;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import net.miginfocom.swing.MigLayout;

public class Webstart implements ActionListener, ChangeListener, DocumentListener {

	private JFrame frame;

	private JTextField modulnameField;
	private JButton btnTemplates;
	private JSpinner loopSpinner;
	private JSpinner splitSpinner;

	private JButton btnGenerieren;
	private JButton btnExit;

	private File selectedDir;

	private File moduleDir;
	private Map<String, File> templates;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Webstart window = new Webstart();
					window.frame.setVisible(true);
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("QML Generator");
		frame.setBounds(100, 100, 334, 207);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[][30.00][164.00]", "[][][][][][][][][]"));

		JLabel lblModulname = new JLabel("Modulname:");
		frame.getContentPane().add(lblModulname, "cell 0 1,alignx right,aligny center");

		modulnameField = new JTextField();
		modulnameField.addActionListener(this);
		modulnameField.getDocument().addDocumentListener(this);
		frame.getContentPane().add(modulnameField, "cell 2 1,growx,aligny center");
		modulnameField.setColumns(10);

		JLabel lblTemplates = new JLabel("Templates:");
		frame.getContentPane().add(lblTemplates, "cell 0 2,alignx right,aligny center");

		btnTemplates = new JButton("...laden");
		btnTemplates.addActionListener(this);
		btnTemplates.setActionCommand("templates");
		
		frame.getContentPane().add(btnTemplates, "cell 2 2,alignx left,aligny center");

		JLabel lblLoops = new JLabel("Loops:");
		lblLoops.setHorizontalAlignment(SwingConstants.RIGHT);
		frame.getContentPane().add(lblLoops, "cell 0 3,alignx right,aligny center");

		loopSpinner = new JSpinner();
		loopSpinner.addChangeListener(this);
		loopSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		frame.getContentPane().add(loopSpinner, "cell 2 3,alignx left,aligny center");

		JLabel lblSplits = new JLabel("Splits:");
		frame.getContentPane().add(lblSplits, "cell 0 4,alignx right,aligny center");

		splitSpinner = new JSpinner();
		splitSpinner.addChangeListener(this);
		splitSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		frame.getContentPane().add(splitSpinner, "cell 2 4,alignx left,aligny center");

		btnGenerieren = new JButton("Generieren");
		btnGenerieren.setEnabled(false);
		btnGenerieren.addActionListener(this);
		btnGenerieren.setActionCommand("generate");
		frame.getContentPane().add(btnGenerieren, "cell 0 8,alignx right,aligny center");

		btnExit = new JButton("Exit");
		btnExit.addActionListener(this);
		btnExit.setActionCommand("exit");
		frame.getContentPane().add(btnExit, "cell 2 8,alignx left,aligny center");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCmd = "UNKOWN";
		if (e.getActionCommand() != null)
			actionCmd = e.getActionCommand();
		if (actionCmd.equals("exit")) {
			frame.dispose();
			System.exit(0);
		} else if (actionCmd.equals("templates")) {

			final JFileChooser chooser = new JFileChooser();

			File gotoDir = DirectoryClient.getInstance().getHome();
			if (selectedDir != null)
				gotoDir = selectedDir;
			if (moduleDir != null)
				gotoDir = moduleDir;

			// moduleDir = null;
			// templates = null;

			chooser.setCurrentDirectory(gotoDir);
			chooser.setDialogTitle("Module Ordner");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			
			chooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory())
						return true;
					return false;
				}

				public String getDescription() {
					return "Project Directory";
				}
			});
			
			//
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				moduleDir = null;
				templates = null;
				File currentDir = chooser.getSelectedFile();
				if (!currentDir.isDirectory())
					currentDir = currentDir.getParentFile();
				System.out.println("current Dir : " + currentDir.getAbsolutePath());
				selectedDir = currentDir;
				final DirectoryClient dirClient = DirectoryClient.getInstance();
				final List<File> loadedTemplates = dirClient.recursiveSearchFiles(currentDir,
						"Template_(Split|Module|Loop)((Pre|Post)fix)*_cleaned.xml");
				if ((loadedTemplates == null) || (loadedTemplates.isEmpty())) {
					System.out.println("no templates found");
				} else {
					final Map<String, File> templateMap = new HashMap<String, File>();
					boolean loadFlag1 = true;

					for (File template : loadedTemplates) {
						System.out.println("Template : " + template.getAbsolutePath());
						final String fileName = template.getName();
						if (!templateMap.containsKey(fileName))
							templateMap.put(fileName, template);
						else {
							loadFlag1 = false;
							System.out.println("Multiple Files of " + fileName + " found : "
									+ templateMap.get(fileName).getAbsolutePath() + " and "
									+ template.getAbsolutePath());
							break;
						}
					}

					if (loadFlag1) {
						boolean loadFlag2 = true;
						if (!templateMap.containsKey("Template_ModulePrefix_cleaned.xml")) {
							System.out.println("keine Template_ModulePrefix_cleaned.xml gefunden");
							loadFlag2 = false;
						}

						if (!templateMap.containsKey("Template_ModulePostfix_cleaned.xml")) {
							System.out.println("keine Template_ModulePostfix_cleaned.xml gefunden");
							loadFlag2 = false;
						}

						if (!templateMap.containsKey("Template_LoopPrefix_cleaned.xml")) {
							System.out.println("keine Template_LoopPrefix_cleaned.xml gefunden");
							loadFlag2 = false;
						}
						if (!templateMap.containsKey("Template_LoopPostfix_cleaned.xml")) {
							System.out.println("keine Template_LoopPostfix_cleaned.xml gefunden");
							loadFlag2 = false;
						}

						if (!templateMap.containsKey("Template_SplitPostfix_cleaned.xml")) {
							System.out.println("keine Template_SplitPostfix_cleaned.xml gefunden");
							loadFlag2 = false;
						}
						if (!templateMap.containsKey("Template_SplitPrefix_cleaned.xml")) {
							System.out.println("keine Template_SplitPrefix_cleaned.xml gefunden");
							loadFlag2 = false;
						}
						if (!templateMap.containsKey("Template_Split_cleaned.xml")) {
							System.out.println("keine Template_Split_cleaned.xml gefunden");
							loadFlag2 = false;
						}

						if (loadFlag2) {
							templates = templateMap;
							moduleDir = currentDir;
							btnTemplates.setText(moduleDir.getName()+" geladen");
						}
					}
				}
			} else {
				System.out.println("No Selection ");
			}
		} else if (actionCmd.equals("generate")) {
			try {
				final Map<String, String> globalReplacements = new HashMap<String, String>();
				globalReplacements.put("modul", modulnameField.getText().trim());

				globalReplacements.put("loop00", "0");
				globalReplacements.put("split00", "0");

				globalReplacements.put("loop0", "a");
				globalReplacements.put("loop1", "b");
				globalReplacements.put("loop2", "c");
				globalReplacements.put("loop3", "d");
				globalReplacements.put("loop4", "e");
				globalReplacements.put("loop5", "f");
				globalReplacements.put("loop6", "g");
				globalReplacements.put("loop7", "h");
				globalReplacements.put("loop8", "i");
				globalReplacements.put("loop9", "j");
				globalReplacements.put("loop10", "k");
				globalReplacements.put("loop11", "l");
				globalReplacements.put("loop12", "m");
				globalReplacements.put("loop13", "n");
				globalReplacements.put("loop14", "o");
				globalReplacements.put("loop15", "p");
				globalReplacements.put("loop16", "q");
				globalReplacements.put("loop17", "r");
				globalReplacements.put("loop18", "s");
				globalReplacements.put("loop19", "t");
				globalReplacements.put("loop20", "u");
				globalReplacements.put("loop21", "v");
				globalReplacements.put("loop22", "w");
				globalReplacements.put("loop23", "x");
				globalReplacements.put("loop24", "y");
				globalReplacements.put("loop25", "z");

				globalReplacements.put("split0", "a");
				globalReplacements.put("split1", "b");
				globalReplacements.put("split2", "c");
				globalReplacements.put("split3", "d");
				globalReplacements.put("split4", "e");
				globalReplacements.put("split5", "f");
				globalReplacements.put("split6", "g");
				globalReplacements.put("split7", "h");
				globalReplacements.put("split8", "i");
				globalReplacements.put("split9", "j");
				globalReplacements.put("split10", "k");
				globalReplacements.put("split11", "l");
				globalReplacements.put("split12", "m");
				globalReplacements.put("split13", "n");
				globalReplacements.put("split14", "o");
				globalReplacements.put("split15", "p");
				globalReplacements.put("split16", "q");
				globalReplacements.put("split17", "r");
				globalReplacements.put("split18", "s");
				globalReplacements.put("split19", "t");
				globalReplacements.put("split20", "u");
				globalReplacements.put("split21", "v");
				globalReplacements.put("split22", "w");
				globalReplacements.put("split23", "x");
				globalReplacements.put("split24", "y");
				globalReplacements.put("split25", "z");

				final String generated = QMLGenerator.getInstance().generate(templates,
						Integer.parseInt(splitSpinner.getValue() + ""), Integer.parseInt(loopSpinner.getValue() + ""),
						globalReplacements);

				final JFileChooser chooser = new JFileChooser();
				
				File gotoDir = DirectoryClient.getInstance().getHome();
				if (selectedDir != null)
					gotoDir = selectedDir;
				if (moduleDir != null)
					gotoDir = moduleDir;

				chooser.setCurrentDirectory(gotoDir);
				
				chooser.setSelectedFile(new File("generated__" + modulnameField.getText().trim() + ".xml"));
				chooser.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						return f.getName().toLowerCase().endsWith(".xml");
					}

					public String getDescription() {
						return "QML-File (*.xml)";
					}
				});

				int returnVal = chooser.showSaveDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if(!file.exists())file = FileClient.getInstance().createOrGetFile(FileClient.getInstance().getNameWithoutSuffix(file), "."+FileClient.getInstance().getSuffix(file), file.getParentFile());
					FileClient.getInstance().writeToFile(file, generated, false);
					showInfo("Erfolgreich","QML in "+file.getAbsolutePath()+" gespeichert");
				}

			} catch (Exception e1) {
				showError(e1);
			}
		}

		btnGenerieren.setEnabled(ready());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		btnGenerieren.setEnabled(ready());
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		btnGenerieren.setEnabled(ready());
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		btnGenerieren.setEnabled(ready());
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		btnGenerieren.setEnabled(ready());
	}

	private boolean ready() {
		boolean ok = true;

		if ((templates == null) || (templates.isEmpty())) {
			System.out.println("No Templates loaded");
			ok = false;
		}
		if ((moduleDir == null) || (!moduleDir.exists()) || (!moduleDir.isDirectory()) || (!moduleDir.canRead())) {
			System.out.println("Module Directory does not exist or is not readable");
			ok = false;
		}

		final String modulName = modulnameField.getText();
		if (modulName.trim().equals("")) {
			System.out.println("No Modulname set");
			ok = false;
		}
		if (Integer.parseInt(loopSpinner.getValue() + "") <= 0) {
			System.out.println("Loop count is 0");
			ok = false;
		}
		if (Integer.parseInt(splitSpinner.getValue() + "") <= 0) {
			System.out.println("Split count is 0");
			ok = false;
		}
		return ok;
	}

	private void showError(final Throwable e) {
			final String msg = e.getMessage();
			JOptionPane.showMessageDialog(frame, msg, "Fehler", JOptionPane.ERROR_MESSAGE, null);
	}
	
	private void showInfo(final String title,final String message) {
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE, null);
}

}
