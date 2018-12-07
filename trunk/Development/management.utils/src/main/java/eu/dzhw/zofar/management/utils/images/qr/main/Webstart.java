package eu.dzhw.zofar.management.utils.images.qr.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import eu.dzhw.zofar.management.utils.images.ImageConverter;
import eu.dzhw.zofar.management.utils.images.qr.QRGenerator;

public class Webstart implements ActionListener{

	private JFrame frame;
	private JTextField textField;
	private JPanel imagePanel;
	private JLabel lblImage;
	private JButton btnSpeichern;
	private JFileChooser fc;

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
		frame.setBounds(100, 100, 450, 405);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
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
				RowSpec.decode("fill:default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblText = new JLabel("Text");
		frame.getContentPane().add(lblText, "2, 2, right, default");
		
		textField = new JTextField();
		frame.getContentPane().add(textField, "4, 2, fill, default");
		textField.setColumns(10);
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(this);
		btnGenerate.setActionCommand("generate");
		frame.getContentPane().add(btnGenerate, "4, 4");
		
		imagePanel = new JPanel();
		frame.getContentPane().add(imagePanel, "4, 8, fill, fill");
		
		lblImage = new JLabel("Press 'Generate' for QR Code");
		lblImage.setIcon(null);
		imagePanel.add(lblImage);
		
		btnSpeichern = new JButton("Save Image as PNG");
		btnSpeichern.setEnabled(false);
		btnSpeichern.addActionListener(this);
		btnSpeichern.setActionCommand("save");
		frame.getContentPane().add(btnSpeichern, "4, 10");
	}

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		if(cmd != null){
			final ImageConverter converter = ImageConverter.getInstance();
			if(cmd.equals("generate")){
				String text = textField.getText();
				boolean error = false;
				if(text != null){
					text = text.trim();
					if(!text.equals("")){
						final QRGenerator qr = QRGenerator.getInstance();
						lblImage.setText(null);
						lblImage.setIcon(new ImageIcon(converter.scale(qr.generate(text),250,250)));
					}
					else{
						error = true;
					}
				}
				else{
					error = true;
				}
				if(error){
					lblImage.setIcon(null);
					lblImage.setText("No QR Code generated");
				}
				btnSpeichern.setEnabled(lblImage.getIcon() != null);
			}
			else if(cmd.equals("save")){
				if (this.fc == null) {
					this.fc = new JFileChooser();
					final File preset = new File(new File(
							System.getProperty("user.home")), "QRCode.png");
					this.fc.setSelectedFile(preset);
				}
				final int returnVal = this.fc.showSaveDialog(this.frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File file = this.fc.getSelectedFile();
					BufferedImage image = converter.fromImage(((ImageIcon)lblImage.getIcon()).getImage());
					try {
						ImageIO.write(image, "png", file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
