package eu.dzhw.zofar.management.generator.qml.variables.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import eu.dzhw.zofar.management.generator.qml.variables.VariableGenerator;
import eu.dzhw.zofar.management.utils.string.StringUtils;
import eu.dzhw.zofar.management.utils.system.SystemClient;

public class Webstart implements ActionListener {
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Webstart.class);
	private JFrame frame;
	private static final String NOQUESTIONNAIRELOADED = "kein Fragebogen geladen";
	private JLabel questionnaireLabel;
	private JButton questionnaireBtn;
	private JButton clipboardBtn;
	private JButton closeBtn;
	
	private JTextArea output;
	private final FileFilter filter = new FileNameExtensionFilter("XML Files", "xml");
	private final VariableGenerator variableGenerator;
	private File selectedFile;
	
	private Task task;
	private JScrollPane scrollPane;

	private class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground() {
			Webstart.this.clipboardBtn.setEnabled(false);
			Webstart.this.output.setText("");
			final JFileChooser fileChooser = new JFileChooser(selectedFile);
			fileChooser.setFileFilter(Webstart.this.filter);
			final int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				try {
					Webstart.this.questionnaireLabel.setText(StringUtils.getInstance().tail(selectedFile.getAbsolutePath(),25));
					Webstart.this.output.setText("..loading");

					final String result = Webstart.this.variableGenerator.generate(selectedFile);
					LOGGER.info("result {}",result);
					if(result != null){
						if(result.equals("")) Webstart.this.output.setText("no undeclared Variables found");
						else{
							Webstart.this.output.setText(result);
							Webstart.this.clipboardBtn.setEnabled(true);
						}
					}
					else{
						Webstart.this.output.setText("no result");
					}
				} catch (final Exception e1) {
					e1.printStackTrace();
				}
			} else {
				Webstart.this.questionnaireLabel.setText(NOQUESTIONNAIRELOADED);
			}
			return null;
		}
	}
	
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
		variableGenerator = VariableGenerator.getInstance();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		questionnaireBtn = new JButton("Fragebogen laden...");
		questionnaireBtn.addActionListener(this);
		questionnaireBtn.setActionCommand("load");
		frame.getContentPane().add(questionnaireBtn, "2, 4, fill, center");

		questionnaireLabel = new JLabel(NOQUESTIONNAIRELOADED);
		frame.getContentPane().add(questionnaireLabel, "4, 4, fill, center");

		clipboardBtn = new JButton("In die Zwischenablage");
		clipboardBtn.addActionListener(this);
		clipboardBtn.setActionCommand("clipboard");
		clipboardBtn.setEnabled(false);
		frame.getContentPane().add(clipboardBtn, "2, 8, left, top");

		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "4, 8, default, fill");
		
		output = new JTextArea();
		scrollPane.setViewportView(output);
		output.setEditable(false);
		
		closeBtn = new JButton("Schließen");
		closeBtn.addActionListener(this);
		closeBtn.setActionCommand("exit");
		frame.getContentPane().add(closeBtn, "4, 10, fill, center");
		
	}

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		if (cmd != null) {
			if (cmd.equals("load")) {
				task = new Task();
				task.execute();
			} else if (cmd.equals("clipboard")) {
				SystemClient.getInstance().toClipboard(output.getText());
			}
			else if (cmd.equals("exit")) {
				this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
}
