package net.azurpixel.launcher;
import static net.azurpixel.launcher.Launcher.AP_SAVER;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;




@SuppressWarnings("serial")
public class OptionFrame extends JDialog 
{

	private static OptionFrame instance;

	private JLabel borderlessLabel = new JLabel("Fenêtré sans bordures");
	private static JCheckBox borderlessCheckBox = new JCheckBox();
	private JLabel autoConnectLabel = new JLabel("Auto-Connect Azurpixel.net");
	private static JCheckBox autoConnectCheckBox = new JCheckBox();
	private JLabel openAtStartLabel = new JLabel("<html>Ouvrir la page de vote<br>au démarrage du launcher</html>");
	private static JCheckBox openAtStartCheckBox = new JCheckBox();
	private JLabel integrityLabel = new JLabel("Intégritée des fiohiers");
	private JButton integrityButton = new JButton("Vérifier");
	private JLabel memoryLabel = new JLabel("RAM allouée");
	private JComboBox<GameMemory> memoryComboBox = new JComboBox<>(new GameMemory[]{GameMemory.XMX1G, GameMemory.XMX2G, GameMemory.XMX4G, GameMemory.XMX6G, GameMemory.XMX8G});
	private JLabel presetLabel = new JLabel("Graphismes");
	private JComboBox<GamePreset> presetComboBox = new JComboBox<>(new GamePreset[]{GamePreset.Mini, GamePreset.Low, GamePreset.Medium, GamePreset.High, GamePreset.Ultra, GamePreset.Custom});
	private JLabel versionLabel = new JLabel("Version");
	private JComboBox<GameVersion> versionComboBox = new JComboBox<>(new GameVersion[]{GameVersion.V1_8, GameVersion.V1_9, GameVersion.V1_10, GameVersion.V1_11, GameVersion.V1_12, GameVersion.V1_13, GameVersion.V1_14});

	private JButton saveButton = new JButton("Valider");
	private JLabel swannLabel = new JLabel("<html><u>Développeur Swanndolia</u></html>");
	private JLabel gfxLabel = new JLabel("<html><u>Graphiste Morgann Ej</u></html>");

	public static OptionFrame getInstance() {
		if (instance == null)
			instance = new OptionFrame();
		return instance;
	}

	private OptionFrame() {
		super(LauncherFrame.getInstance(), "AzurPixel v4 | Options", true);

		setSize(260, 300);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(LauncherFrame.getInstance());
		setLayout(null);

		memoryLabel.setBounds(20, 0, 200, 20);
		add(memoryLabel);
		memoryComboBox.setBounds(180, 0, 70, 20);
		add(memoryComboBox);
		
		presetLabel.setBounds(20, 30, 200, 20);
		add(presetLabel);
		presetComboBox.setBounds(180, 30, 70, 20);
		add(presetComboBox);
				
		versionLabel.setBounds(20, 60, 200, 20);
		add(versionLabel);
		versionComboBox.setBounds(180, 60, 70, 20);
		add(versionComboBox);
		
		integrityLabel.setBounds(20, 90, 200, 20);
		add(integrityLabel);
		integrityButton.setBounds(180, 90, 70, 20);
		integrityButton.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				new Thread("Launch procedure") {
					public void run() {
						try {
							AP_SAVER.set("verif", "true");
							AP_SAVER.set("error", "true");
							Launcher.update();
						} catch (Exception ex) {
							return;
							}
					}
					}.start();
				}
		});	
		add(integrityButton);
		
		if (AP_SAVER.get("borderless").equals("true"))
			borderlessCheckBox.setSelected(true);
		borderlessLabel.setBounds(20, 120, 200, 20);
		add(borderlessLabel);
		borderlessCheckBox.setBounds(205, 120, 20, 20);
		borderlessCheckBox.setEnabled(true);
		add(borderlessCheckBox);
		
		if (AP_SAVER.get("openAtStart").equals("true"))
			openAtStartCheckBox.setSelected(true);
		openAtStartLabel.setBounds(20, 150, 200, 30);
		add(openAtStartLabel);
		openAtStartCheckBox.setBounds(205, 155, 20, 20);
		openAtStartCheckBox.setEnabled(true);
		add(openAtStartCheckBox);
		
		if (AP_SAVER.get("autoConnect").equals("true"))
			autoConnectCheckBox.setSelected(true);
		autoConnectLabel.setBounds(20, 190, 200, 30);
		add(autoConnectLabel);
		autoConnectCheckBox.setBounds(205, 190, 20, 20);
		autoConnectCheckBox.setEnabled(true);
		add(autoConnectCheckBox);
		
		saveButton.setBounds(75, 220, 100, 30);
		saveButton.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				AP_SAVER.set("game-memory", ((GameMemory) memoryComboBox.getSelectedItem()).name());
				AP_SAVER.set("game-preset", ((GamePreset) presetComboBox.getSelectedItem()).name());
				AP_SAVER.set("game-version", ((GameVersion) versionComboBox.getSelectedItem()).name());
				if (!borderlessCheckBox.isSelected() || AP_SAVER.get("autoConnect") == null)
					AP_SAVER.set("borderless", "false");
				else
					AP_SAVER.set("borderless", "true");
				if (openAtStartCheckBox.isSelected()) 
					AP_SAVER.set("openAtStart", "true");
				else 
					AP_SAVER.set("openAtStart", "false");
				if (!autoConnectCheckBox.isSelected() || AP_SAVER.get("autoConnect") == null)
					AP_SAVER.set("autoConnect", "false");
				else
					AP_SAVER.set("autoConnect", "true");
				setVisible(false);
			}
		});
		add(saveButton);

		gfxLabel.setForeground(Color.BLUE);
		gfxLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gfxLabel.setBounds(5, 250, 200, 20);
		gfxLabel.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				WebPage.show("https://www.facebook.com/profile.php?id=100010350648184");
			}
		});
		add(gfxLabel);

		swannLabel.setForeground(Color.BLUE);
		swannLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		swannLabel.setBounds(130, 250, 200, 20);
		swannLabel.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				WebPage.show(Launcher.AP_URL);
			}
		});
		add(swannLabel);	
		}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			try {
				GameMemory gameMemory = GameMemory.valueOf(AP_SAVER.get("game-memory", "XMX2G"));
				memoryComboBox.setSelectedItem(gameMemory);
			} catch (IllegalArgumentException ex) {
				memoryComboBox.setSelectedIndex(1);
			}

			try {
				GamePreset gamePreset = GamePreset.valueOf(AP_SAVER.get("game-preset", "Mini"));
				presetComboBox.setSelectedItem(gamePreset);
			} catch (IllegalArgumentException ex) {
				presetComboBox.setSelectedIndex(1);
			}
			try {
				GameVersion gameVersion = GameVersion.valueOf(AP_SAVER.get("game-version", "V1_8"));
				versionComboBox.setSelectedItem(gameVersion);
			} catch (IllegalArgumentException ex) {
				versionComboBox.setSelectedIndex(1);
			}
		}
		super.setVisible(b);
	}

}