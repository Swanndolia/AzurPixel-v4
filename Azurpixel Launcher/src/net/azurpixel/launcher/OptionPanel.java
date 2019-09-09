package net.azurpixel.launcher;

import static net.azurpixel.launcher.Launcher.AP_SAVER;
import static re.alwyn974.swinger.Swinger.getResource;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;


import javafx.embed.swing.JFXPanel;
import re.alwyn974.swinger.Swinger;
import re.alwyn974.swinger.event.SwingerEvent;
import re.alwyn974.swinger.event.SwingerEventListener;
import re.alwyn974.swinger.textured.STexturedButton;
import re.alwyn974.swinger.textured.STexturedCheckBox;

@SuppressWarnings("serial")
public class OptionPanel extends JFXPanel implements SwingerEventListener {
	
	private Image background = getResource("options.png");
	
	private JComboBox<GameMemory> memoryComboBox = new JComboBox<>(new GameMemory[]{GameMemory.XMX1G, GameMemory.XMX2G, GameMemory.XMX4G, GameMemory.XMX6G, GameMemory.XMX8G});
	private JComboBox<GamePreset> presetComboBox = new JComboBox<>(new GamePreset[]{GamePreset.Mini, GamePreset.Low, GamePreset.Medium, GamePreset.High, GamePreset.Ultra, GamePreset.Custom});
	private JComboBox<GameVersion> versionComboBox = new JComboBox<>(new GameVersion[]{GameVersion.V1_8, GameVersion.V1_9, GameVersion.V1_10, GameVersion.V1_11, GameVersion.V1_12, GameVersion.V1_13, GameVersion.V1_14});
	
	private STexturedButton saveButton = new STexturedButton(getResource("save.png"), getResource("saveHover.png"));
	private STexturedButton integrityButton = new STexturedButton(getResource("integrity.png"), getResource("integrityHover.png"));
	private static STexturedCheckBox borderlessCheckBox = new STexturedCheckBox(getResource("box.png"), getResource("boxChecked.png"));
	private static STexturedCheckBox autoConnectCheckBox = new STexturedCheckBox(getResource("box.png"), getResource("boxChecked.png"));
	private static STexturedCheckBox openAtStartCheckBox = new STexturedCheckBox(getResource("box.png"), getResource("boxChecked.png"));
	
	
	private JTextField ipTextField = new JTextField(AP_SAVER.get("ip"));
	
	private JLabel swannLabel = new JLabel("<html><u>Développeur Swanndolia</u></html>");
	private JLabel gfxLabel = new JLabel("<html><u>Graphiste Morgann Ej</u></html>");
	
	public void save() {
	    Launcher.presence.details = "Admire le launcher";
		Launcher.updatePresence();
		AP_SAVER.set("game-memory", ((GameMemory) memoryComboBox.getSelectedItem()).name());
		AP_SAVER.set("game-preset", ((GamePreset) presetComboBox.getSelectedItem()).name());
		AP_SAVER.set("game-version", ((GameVersion) versionComboBox.getSelectedItem()).name());
		if (!borderlessCheckBox.isSelected())
			AP_SAVER.set("borderless", "false");
		else
			AP_SAVER.set("borderless", "true");
		if (openAtStartCheckBox.isSelected()) 
			AP_SAVER.set("openAtStart", "true");
		else 
			AP_SAVER.set("openAtStart", "false");
		if (!autoConnectCheckBox.isSelected())
			AP_SAVER.set("autoConnect", "false");
		else
			AP_SAVER.set("autoConnect", "true");
		setVisible(false);
		AP_SAVER.set("ip", ipTextField.getText());
	}


	
	public OptionPanel()
    {	
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER)
						save();
					}
		});
		
		Swinger.setResourcePath("/resources/");
		setBounds(200,280,600,300);
		setLayout(null);
		memoryComboBox.setBounds(190, 215, 70, 20);
		add(memoryComboBox);
		
		presetComboBox.setBounds(190, 148, 70, 20);
		add(presetComboBox);
				
		versionComboBox.setBounds(190, 80, 70, 20);
		add(versionComboBox);
		
		integrityButton.setBounds(370, 35, 90, 44);
		integrityButton.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				new Thread("Launch procedure") {
					public void run() {
						try {
							Launcher.updateAssets();
						} catch (Exception ex) {
							Launcher.reportException(ex);
							}
						try {
							Launcher.update();
						} catch (Exception ex) {
							Launcher.reportException(ex);
						}
					}
					}.start();
				}
		});	
		add(integrityButton);
		
		if (AP_SAVER.get("borderless").equals("true"))
			borderlessCheckBox.setSelected(true);
		borderlessCheckBox.setBounds(525, 75, 30, 30);
		borderlessCheckBox.setEnabled(true);
		add(borderlessCheckBox);
		
		if (AP_SAVER.get("openAtStart").equals("true"))
			openAtStartCheckBox.setSelected(true);
		openAtStartCheckBox.setBounds(525, 135, 30, 30);
		openAtStartCheckBox.setEnabled(true);
		add(openAtStartCheckBox);
		
		if (AP_SAVER.get("autoConnect").equals("true"))
			autoConnectCheckBox.setSelected(true);
		autoConnectCheckBox.setBounds(525, 205, 30, 30);
		autoConnectCheckBox.setEnabled(true);
		add(autoConnectCheckBox);
		
		ipTextField.setForeground(new Color(100, 0, 0));
		ipTextField.setCaretColor(new Color(100, 0, 0));
		ipTextField.setFont(ipTextField.getFont().deriveFont(22f));
		ipTextField.setOpaque(false);
		ipTextField.setBorder(null);
		ipTextField.setBounds(287, 220, 212, 30);
		ipTextField.setHorizontalAlignment(JTextField.CENTER);
		add(ipTextField);
		ipTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER)
						save();
					}
		});

		saveButton.setBounds(225, 240, 150, 60);
		saveButton.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				save();
			}
		});
		add(saveButton);

		gfxLabel.setForeground(Color.DARK_GRAY);
		gfxLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gfxLabel.setBounds(470, 250, 200, 20);
		gfxLabel.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("https://www.facebook.com/profile.php?id=100010350648184")));
				} catch (IOException | URISyntaxException e1) {
				}
			}
		});
		add(gfxLabel);

		swannLabel.setForeground(Color.DARK_GRAY);
		swannLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		swannLabel.setBounds(470, 270, 200, 20);
		swannLabel.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL));
				} catch (IOException | URISyntaxException e1) {
				}
			}
		});
		add(swannLabel);	
		
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

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);
	}
	
	@Override
	public void onEvent(SwingerEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
