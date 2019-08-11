package net.azurpixel.launcher;

import static net.azurpixel.launcher.Launcher.AP_IMG;
import static net.azurpixel.launcher.Launcher.AP_SAVER;
import static net.azurpixel.launcher.Launcher.auth;
import static net.azurpixel.launcher.Launcher.interruptUpdateThread;
import static net.azurpixel.launcher.Launcher.launch;
import static net.azurpixel.launcher.Launcher.refresh;
import static net.azurpixel.launcher.Launcher.reportException;
import static net.azurpixel.launcher.Launcher.saveInfos;
import static net.azurpixel.launcher.Launcher.setAuthInfos;
import static net.azurpixel.launcher.Launcher.tryToExit;
import static re.alwyn974.swinger.Swinger.getResource;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.swinger.event.SwingerEvent;
import re.alwyn974.swinger.event.SwingerEventListener;
import re.alwyn974.swinger.textured.STexturedButton;
import re.alwyn974.swinger.textured.STexturedCheckBox;
import re.alwyn974.swinger.textured.STexturedProgressBar;


@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener 
{
	
	private Image background = getResource("background.jpg");
	private BufferedImage news = null;
	private boolean willRefresh;

	private JTextField usernameField = new JTextField("Mail (premium) / Pseudo");
	private JPasswordField passwordField = new JPasswordField("Mot de passe (premium)");
	private STexturedButton voteButton = new STexturedButton(getResource("vote.jpg"), getResource("votehover.jpg"));
	private STexturedButton disconnectButton = new STexturedButton(getResource("disconnect.png"));
	private JLabel authLabel = new JLabel("", SwingConstants.CENTER);

	private STexturedButton playButton = new STexturedButton(getResource("play.jpg"), getResource("playhover.jpg"));

	private STexturedButton quitButton = new STexturedButton(getResource("close.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"));
	private STexturedButton settingsButton = new STexturedButton(getResource("settings.png"));

	private STexturedButton newsButton = new STexturedButton(getResource("news.jpg"), getResource("newshover.jpg"));
	private STexturedProgressBar progressBar = new STexturedProgressBar(getResource("progressEmpty.jpg"), getResource("progressFull.jpg"));
	
	STexturedCheckBox keeploginCheckBox = new STexturedCheckBox(getResource("keep-login.png"), getResource("boxChecked.png"));

	
	@SuppressWarnings("deprecation")
	public void Play() {
		if(passwordField.getText().equals("Mot de passe (premium)")) 
			 passwordField.setText("");
		if(usernameField.getText().equals("Mail (premium) / Pseudo")) 
			 usernameField.setText("");
		setFieldsEnabled(false);
		new Thread("Launch procedure") {
		@Override
		public void run() {
			try {
				if (willRefresh)
					refresh();
				else{
					auth(usernameField.getText(), passwordField.getText());
				}
			} catch (AuthenticationException ex) {
				setInfoText(ex.getErrorModel().getErrorMessage());
				willRefresh = false;
				setFieldsEnabled(true);
				return;
				}
			saveInfos(keeploginCheckBox.isSelected());
			willRefresh = true;
			try {
				Launcher.update();
			} catch (Exception ex) {
				ex.printStackTrace();
				interruptUpdateThread();
				setInfoText("La mise à jour a échoué. (" + ex + ")");
				reportException(ex);
				AP_SAVER.set("error", "true");
				return;
			}
			try {
				launch();
			} catch (LaunchException | InterruptedException ex) {
				ex.printStackTrace();
				setInfoText("Impossible de lancer le jeu. (" + ex + ")");
				reportException(ex);
			}
			disconnectButton.setVisible(false);
		}
		}.start();
	}
	
	public LauncherPanel()
	{ 		
		setLayout(null);
		setSize(1000, 750);
		willRefresh = !AP_SAVER.get("access-token", "").equals("");

		usernameField.setForeground(new Color(0, 80, 150));
		usernameField.setCaretColor(new Color(0, 80, 150));
		usernameField.setFont(usernameField.getFont().deriveFont(26f));
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(102, 84, 340, 30);
		usernameField.setVisible(!willRefresh);
		add(usernameField);
	    usernameField.addMouseListener(new CustomMouseListener() {
			public void mousePressed(MouseEvent e) {
				if((usernameField.getText().equals("Mail (premium) / Pseudo")) && (e.getSource() == (usernameField))) {
					usernameField.setText("");
					usernameField.setForeground(new Color(0, 147, 255));
					usernameField.setCaretColor(new Color(0, 147, 255));
				}
			}});
	    
		usernameField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
                if(usernameField.getText().equals("Mail (premium) / Pseudo")) {
                	usernameField.setText("");
            		usernameField.setForeground(new Color(0, 147, 255));
            		usernameField.setCaretColor(new Color(0, 147, 255));
                }
		        if (e.getKeyCode() == KeyEvent.VK_ENTER)
						Play();
					}
		});
				
		passwordField.setForeground(new Color(0, 80, 150));
		passwordField.setCaretColor(new Color(0, 80, 150));
		passwordField.setFont(passwordField.getFont().deriveFont(26f));
    	passwordField.setEchoChar((char)0);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(550, 84, 340, 30);
		passwordField.setVisible(!willRefresh);
		add(passwordField);
	    passwordField.addMouseListener(new CustomMouseListener() {
			@SuppressWarnings("deprecation")
			public void mousePressed(MouseEvent e) {               
				if((passwordField.getText().equals("Mot de passe (premium)")) && (e.getSource() == (passwordField))) {
                	passwordField.setText("");
        			passwordField.setEchoChar('•');
                	passwordField.setForeground(new Color(0, 147, 255));
                	passwordField.setCaretColor(new Color(0, 147, 255));
                }
			}
			});
		passwordField.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			
			public void keyPressed(KeyEvent e) {
                if(passwordField.getText().equals("Mot de passe (premium)")) {
            			passwordField.setText("");
            			passwordField.setEchoChar('•');
            			passwordField.setForeground(new Color(0, 147, 255));
            			passwordField.setCaretColor(new Color(0, 147, 255));
            	}
               	if (e.getKeyCode() == KeyEvent.VK_ENTER)
						Play();
					}
		});
 		
	    voteButton.setBounds(620, 135);
		voteButton.addEventListener(this);
		add(voteButton);

		disconnectButton.setBounds(198, 536);
		disconnectButton.addEventListener(this);
		disconnectButton.setVisible(willRefresh);
		add(disconnectButton);

		playButton.setBounds(130, 135);
		playButton.addEventListener(this);
		add(playButton);

		quitButton.setBounds(950, 15);
		quitButton.addEventListener(this);
		add(quitButton);

		hideButton.setBounds(920, 15);
		hideButton.addEventListener(this);
		add(hideButton);

		settingsButton.setBounds(890, 15);
		settingsButton.addEventListener(this);
		add(settingsButton);

		newsButton.setBounds(653, 533, 150, 50);
		newsButton.addEventListener(this);
		add(newsButton);

		progressBar.setBounds(0, 694, 1000, 56);
		progressBar.setForeground(new Color(0, 147, 255));
		progressBar.setFont(usernameField.getFont().deriveFont(26f));
		progressBar.setString("Connecte toi pour rejoindre AzurPixel !");

	    progressBar.setStringPainted(true);
		add(progressBar);
		
		keeploginCheckBox.setBounds(430, 150, 150, 25);
		keeploginCheckBox.setEnabled(true);
		keeploginCheckBox.setSelected(willRefresh);
		add(keeploginCheckBox);
		keeploginCheckBox.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent e) {
		           if (e.getKeyCode() == KeyEvent.VK_ENTER)
						Play();
					}
		});

		if (willRefresh)
			setInfoText("Bienvenue " + AP_SAVER.get("username"));	
			if (AP_SAVER.get("premium").equals("true")) 
				setInfoText("Bienvenue " + AP_SAVER.get("username") + " | Premium");	

		new Thread("News Loader"){
			@Override
			public void run() {
				try {
					URLConnection connection = new URL(AP_IMG.concat("/news.png")).openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
					news = ImageIO.read(connection.getInputStream());
					repaint();
				} catch (IOException ex) {
					System.err.println("Impossible de charger l'image de news (" + ex + ")");
				}

			}
		}.start();
	}	
	
	public STexturedProgressBar getProgressBar()
	{
		return progressBar;
	}
	
	public void setInfoText(String infoText)
	{
		progressBar.setString(infoText);
	}

	@Override
	public void onEvent(SwingerEvent event)
	{		
		if (event.getType() == SwingerEvent.BUTTON_CLICKED_EVENT) {
			if (event.getSource() == quitButton)
				tryToExit();
			else if (event.getSource() == hideButton)
				LauncherFrame.getInstance().setState(Frame.ICONIFIED);
			else if (event.getSource() == settingsButton)
				OptionFrame.getInstance().setVisible(true);
			else if (event.getSource() == voteButton)
				WebPage.show(Launcher.AP_URL.concat("/vote"));
			else if (event.getSource() == newsButton)
				WebPage.show(Launcher.AP_URL);
			else if (event.getSource() == disconnectButton)
			{
				try {
					setAuthInfos("", "", "");
				} catch (AuthenticationException e) {
				}
				setInfoText("Connecte toi pour rejoindre AzurPixel !");
				saveInfos(false);
				setFieldsEnabled(true);
				disconnectButton.setVisible(false);
				willRefresh = false;
			}
			else if (event.getSource() == playButton) {
				Play();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);
		if (news != null)
			g.drawImage(news, 200, 280, 600, 300, this);
	}

	void setFieldsEnabled(boolean enabled)
	{
		usernameField.setVisible(enabled);
		passwordField.setVisible(enabled);
		playButton.setEnabled(enabled);
		authLabel.setVisible(!enabled);
	}
}