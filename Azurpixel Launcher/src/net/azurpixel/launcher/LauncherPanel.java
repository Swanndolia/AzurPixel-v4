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
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import javafx.embed.swing.JFXPanel;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.swinger.event.SwingerEvent;
import re.alwyn974.swinger.event.SwingerEventListener;
import re.alwyn974.swinger.textured.STexturedButton;
import re.alwyn974.swinger.textured.STexturedCheckBox;
import re.alwyn974.swinger.textured.STexturedProgressBar;


@SuppressWarnings("serial")
public class LauncherPanel extends JFXPanel implements SwingerEventListener 
{
	public static boolean willRefresh;
	
	private Image background = getResource("background.jpg");
	private Image navbar = getResource("navBar.png");
	private Image news = null;
	private Image skin = null;	

	private JTextField usernameField = new JTextField("Mail (premium) / Pseudo");
	private JPasswordField passwordField = new JPasswordField("Mot de passe (premium)");
	private STexturedButton voteButton = new STexturedButton(getResource("vote.jpg"), getResource("voteHover.jpg"));
	private STexturedButton disconnectButton = new STexturedButton(getResource("disconnect.png"), getResource("disconnectHover.png"));
	
	private JLabel keepLogin = new JLabel("Rester connecté", SwingConstants.CENTER);
	
	private STexturedButton playButton = new STexturedButton(getResource("play.jpg"), getResource("playHover.jpg"));
	private STexturedButton quitButton = new STexturedButton(getResource("close.png"), getResource("closeHover.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"), getResource("hideHover.png"));
	private STexturedButton settingsButton = new STexturedButton(getResource("settings.png"), getResource("settingsHover.png"));

	private STexturedButton leaderBoardButton = new STexturedButton(getResource("leaderBoard.png"), getResource("leaderBoardHover.png"));
	private STexturedButton achievementsButton = new STexturedButton(getResource("achievements.png"), getResource("achievementsHover.png"));
	private STexturedButton newsButton = new STexturedButton(getResource("news.png"), getResource("newsHover.png"));
	private STexturedButton statsButton = new STexturedButton(getResource("stats.png"), getResource("statsHover.png"));
	private STexturedButton wikiButton = new STexturedButton(getResource("wiki.png"), getResource("wikiHover.png"));

	private STexturedButton facebookButton = new STexturedButton(getResource("facebook.png"), getResource("facebookHover.png"));
	private STexturedButton githubButton = new STexturedButton(getResource("github.png"), getResource("githubHover.png"));
	private STexturedButton discordButton = new STexturedButton(getResource("discord.png"), getResource("discordHover.png"));
	
	private STexturedProgressBar progressBar = new STexturedProgressBar(getResource("progressEmpty.jpg"), getResource("progressFull.jpg"));
	
	private STexturedCheckBox keeploginCheckBox = new STexturedCheckBox(getResource("box.png"), getResource("boxChecked.png"));

	
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
		setOpaque(false);
		newsLoader();
		if (AP_SAVER.get("premium").equals("true"))
			skinLoader(AP_SAVER.get("username"));
		else
			skinLoader("");
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

		disconnectButton.setBounds(570, 230);
		disconnectButton.addEventListener(this);
		disconnectButton.setVisible(willRefresh);
		add(disconnectButton);

		playButton.setBounds(130, 135);
		playButton.addEventListener(this);
		add(playButton);

		quitButton.setBounds(950, 20);
		quitButton.addEventListener(this);
		add(quitButton);

		hideButton.setBounds(920, 20);
		hideButton.addEventListener(this);
		add(hideButton);

		settingsButton.setBounds(890, 20);
		settingsButton.addEventListener(this);
		add(settingsButton);

		progressBar.setBounds(0, 694, 1000, 56);
		progressBar.setForeground(new Color(0, 147, 255));
		progressBar.setFont(usernameField.getFont().deriveFont(26f));
		progressBar.setString("Connecte toi pour rejoindre AzurPixel !");
	    progressBar.setStringPainted(true);
		add(progressBar);
		
		keepLogin.setBounds(350, 240, 200, 20);
		keepLogin.setForeground(new Color(0, 60, 120, 180));
		keepLogin.setFont(usernameField.getFont().deriveFont(19f));
		add(keepLogin);
		
	    achievementsButton.setBounds(475, 70);
	    achievementsButton.addEventListener(this);
		add(achievementsButton);
		
	    githubButton.setBounds(10, 10);
	    githubButton.addEventListener(this);
		add(githubButton);
		
		keeploginCheckBox.setBounds(530, 237, 30, 30);
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

		navBar();
		
		if (willRefresh) {
			setInfoText("Bienvenue " + AP_SAVER.get("username"));	
			if (AP_SAVER.get("premium").equals("true")) {
				setInfoText("Bienvenue " + AP_SAVER.get("username") + " | Premium");
			}
		}
	}

	public void navBar()
	{
		wikiButton.setBounds(208, 282);
		wikiButton.addEventListener(this);
		add(wikiButton);

		newsButton.setBounds(208, 332);
		newsButton.addEventListener(this);
		add(newsButton);
		
		statsButton.setBounds(208, 382);
		statsButton.addEventListener(this);
		add(statsButton);
		
		facebookButton.setBounds(208, 432);
		facebookButton.addEventListener(this);
		add(facebookButton);
		
		discordButton.setBounds(208, 482);
		discordButton.addEventListener(this);
		add(discordButton);		
		
	    leaderBoardButton.setBounds(208, 532);
	    leaderBoardButton.addEventListener(this);
		add(leaderBoardButton);
	}
	
	public void newsLoader()
	{
		new Thread("News Loader"){
			@Override
			public void run() {
				try {
					URLConnection connection = new URL(AP_IMG.concat("/news.png")).openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
					news = ImageIO.read(connection.getInputStream());
					repaint();
				} catch (IOException ex) {
					reportException(ex);
				}
			}
		}.start();
	}
	
	public void skinLoader(String username)
	{
		new Thread("Skin Loader"){
			@Override
			public void run() {
				try {
					URLConnection connection = new URL("https://mc-heads.net/head/" + username + "/100").openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
					skin = ImageIO.read(connection.getInputStream());
					repaint();
				} catch (IOException ex) {
					reportException(ex);
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
			else if (event.getSource() == facebookButton)
				try {
					Desktop.getDesktop().browse(new URI("https://www.facebook.com/azurpixel"));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == githubButton)
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/Swanndolia/AzurPixel-v4"));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == discordButton)
				try {
					Desktop.getDesktop().browse(new URI("https://discord.gg/BRESeuM"));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == wikiButton)
				LauncherFrame.getInstance().getWikiPanel().setVisible(true);
			else if (event.getSource() == statsButton)
				LauncherFrame.getInstance().getStatsPanel().setVisible(true);
			else if (event.getSource() == leaderBoardButton)
				try {
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("/p/classement")));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == newsButton) {
				LauncherFrame.getInstance().getWikiPanel().setVisible(false);
				LauncherFrame.getInstance().getStatsPanel().setVisible(false);
			}
			else if (event.getSource() == achievementsButton)
				try {
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("/p/succes")));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == hideButton)
				LauncherFrame.getInstance().setState(Frame.ICONIFIED);
			else if (event.getSource() == settingsButton)
				LauncherFrame.getInstance().getOptionPanel().setVisible(true);
			else if (event.getSource() == voteButton)
				try {
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("/vote")));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == disconnectButton)
			{
				try {
					setAuthInfos("", "", "");
				} catch (AuthenticationException e) {
				}
				skinLoader("");
				AP_SAVER.set("premium", "false");
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
		if (skin != null)
			g.drawImage(skin, 450, 130, this);
		g.drawImage(navbar, 204, 284, this);
	}

	void setFieldsEnabled(boolean enabled)
	{
		usernameField.setVisible(enabled);
		passwordField.setVisible(enabled);
		playButton.setEnabled(enabled);
	}
}