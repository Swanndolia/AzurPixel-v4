package net.azurpixel.launcher;

import static net.azurpixel.launcher.Launcher.AP_IMG;
import static net.azurpixel.launcher.Launcher.AP_SAVER;
import static net.azurpixel.launcher.Launcher.auth;
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
import net.azurpixel.launcher.MinecraftPing.MinecraftPingReply;
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
	
	private JLabel keepLoginLabel = new JLabel("Rester connecté", SwingConstants.CENTER);
	public JLabel pingLabel = new JLabel("", SwingConstants.CENTER);
	public JLabel infoLabel = new JLabel("", SwingConstants.CENTER);
	
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
	public void play() {
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
						if (!Launcher.inUpdate)
							launch();
					} catch (LaunchException | InterruptedException ex) {
						ex.printStackTrace();
						setFieldsEnabled(true);
						setInfoText("Impossible de lancer le jeu. (" + ex + ")");
						reportException(ex);
					}
					if (!Launcher.inUpdate)
						Launcher.presence.details = "A rejoint azurpixel";
					disconnectButton.setVisible(false);
				}
			}.start();
		}

	public LauncherPanel()
	{ 		
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER)
						play();
					}
		});
		
		setOpaque(false);
		newsLoader();
		if (AP_SAVER.get("premium").equals("true"))
			skinLoader(AP_SAVER.get("username"));
		else
			skinLoader("");
		setLayout(null);
		setSize(1000, 750);
		willRefresh = !AP_SAVER.get("access-token", "").equals("");
		
		usernameField.setHorizontalAlignment(JTextField.CENTER);
		usernameField.setForeground(new Color(0, 147, 255));
		usernameField.setCaretColor(new Color(0, 147, 255));
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
					usernameField.setForeground(new Color(0, 80, 150));
					usernameField.setCaretColor(new Color(0, 80, 150));
				}
			}});
	    
		usernameField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
                if(usernameField.getText().equals("Mail (premium) / Pseudo")) {
                	usernameField.setText("");
            		usernameField.setForeground(new Color(0, 80, 150));
            		usernameField.setCaretColor(new Color(0, 80, 150));
                }
		        if (e.getKeyCode() == KeyEvent.VK_ENTER)
						play();
					}
		});
		
		passwordField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setForeground(new Color(0, 147, 255));
		passwordField.setCaretColor(new Color(0, 147, 255));
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
                	passwordField.setForeground(new Color(0, 80, 150));
                	passwordField.setCaretColor(new Color(0, 80, 150));
                }
			}
			});
		passwordField.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			
			public void keyPressed(KeyEvent e) {
                if(passwordField.getText().equals("Mot de passe (premium)")) {
            			passwordField.setText("");
            			passwordField.setEchoChar('•');
            			passwordField.setForeground(new Color(0, 80, 150));
            			passwordField.setCaretColor(new Color(0, 80, 150));
            	}
               	if (e.getKeyCode() == KeyEvent.VK_ENTER)
						play();
					}
		});
 		
	    voteButton.setBounds(620, 135);
		voteButton.addEventListener(this);
		add(voteButton);

		disconnectButton.setBounds(570, 230);
		disconnectButton.addEventListener(this);
		disconnectButton.setVisible(willRefresh);
		add(disconnectButton);

		getPlayButton().setBounds(130, 135);
		getPlayButton().addEventListener(this);
		add(getPlayButton());

		quitButton.setBounds(950, 20);
		quitButton.addEventListener(this);
		add(quitButton);

		hideButton.setBounds(920, 20);
		hideButton.addEventListener(this);
		add(hideButton);

		settingsButton.setBounds(890, 20);
		settingsButton.addEventListener(this);
		add(settingsButton);

		progressBar.setBounds(0, 694, 998, 56);
		progressBar.setForeground(new Color(120, 0, 0));
		progressBar.setFont(usernameField.getFont().deriveFont(26f));
		progressBar.setString("Connecte toi pour rejoindre AzurPixel !");
	    progressBar.setStringPainted(true);
		add(progressBar);
		
		keepLoginLabel.setBounds(350, 240, 200, 20);
		keepLoginLabel.setForeground(new Color(0, 60, 120, 180));
		keepLoginLabel.setFont(usernameField.getFont().deriveFont(20f));
		add(keepLoginLabel);
		
		pingLabel.setBounds(-10, 10, 100, 30);
		pingLabel.setForeground(new Color(0, 80, 0));
		pingLabel.setFont(usernameField.getFont().deriveFont(20f));
		add(pingLabel);
		
		infoLabel.setBounds(75, 10, 250, 30);
		infoLabel.setForeground(new Color(0, 80, 0));
		infoLabel.setFont(usernameField.getFont().deriveFont(20f));
		add(infoLabel);
		
	    achievementsButton.setBounds(475, 70);
	    achievementsButton.addEventListener(this);
		add(achievementsButton);
		
	    githubButton.setBounds(935, 640);
	    githubButton.addEventListener(this);
		add(githubButton);
		
		keeploginCheckBox.setBounds(530, 237, 30, 30);
		keeploginCheckBox.setEnabled(true);
		keeploginCheckBox.setSelected(willRefresh);
		add(keeploginCheckBox);

		navBar();
		pingShow();
		
		if (willRefresh) {
			setInfoText("Bienvenue " + AP_SAVER.get("username"));	
			if (AP_SAVER.get("premium").equals("true")) {
				setInfoText("Bienvenue " + AP_SAVER.get("username") + " | Premium");
			}
		}
	}	
	
	public void pingShow()
	{
		new Thread("Ping Updater"){
			@Override
			public void run() {
				while (true) {
					MinecraftPingReply data;
					try {
						data = new MinecraftPing().getPing(AP_SAVER.get("ip"), 25565);
						pingLabel.setText(data.getLatency() + "ms");
					    infoLabel.setText(data.getPlayers().getOnline() + "/" + data.getPlayers().getMax() + " joueurs en ligne");
					    if (data.getLatency() < 40)
							pingLabel.setForeground(new Color(0, 80, 0));
					    if (data.getLatency() > 40)
							pingLabel.setForeground(new Color(20, 80, 0));
					    if (data.getLatency() > 60)
							pingLabel.setForeground(new Color(40, 80, 0));
					    if (data.getLatency() > 80)
							pingLabel.setForeground(new Color(60, 80, 0));
					    if (data.getLatency() > 100)
							pingLabel.setForeground(new Color(80, 80, 0));
					    if (data.getLatency() > 125)
							pingLabel.setForeground(new Color(80, 60, 0));
					    if (data.getLatency() > 150)
							pingLabel.setForeground(new Color(80, 40, 0));
					    if (data.getLatency() > 175)
							pingLabel.setForeground(new Color(80, 20, 0));
					    if (data.getLatency() > 200)
							pingLabel.setForeground(new Color(80, 0, 0));
						Thread.sleep(1000);
					} catch (IOException e1) {} catch (InterruptedException e) {}
				}
			}
		}.start();
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

	public void setInfoText(String infoText)
	{
		progressBar.setString(infoText);
	}

	public String getInfoText()
	{
		return progressBar.getString();
	}
	
	public STexturedProgressBar getProgressBar()
	{
		return progressBar;
	}
	
	@Override
	public void onEvent(SwingerEvent event)
	{		
		if (event.getType() == SwingerEvent.BUTTON_CLICKED_EVENT) {
			if (event.getSource() == quitButton)
				tryToExit();
			else if (event.getSource() == facebookButton)
				try {
				    Launcher.presence.details = "Regarde les actus fb";
					Desktop.getDesktop().browse(new URI("https://www.facebook.com/azurpixel"));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == githubButton)
				try {
				    Launcher.presence.details = "Vérifie sur le code";
					Desktop.getDesktop().browse(new URI("https://github.com/Swanndolia/AzurPixel-v4"));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == discordButton)
				try {
				    Launcher.presence.details = "Rejoint le serveur discord";
					Desktop.getDesktop().browse(new URI("https://discord.gg/BRESeuM"));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == wikiButton) {
			    Launcher.presence.details = "Se renseigne sur le wiki";
				LauncherFrame.getInstance().getWikiPanel().setVisible(true);
			}
			else if (event.getSource() == statsButton) {
			    Launcher.presence.details = "Statistiques ? Fantastique !";
				LauncherFrame.getInstance().getStatsPanel().setVisible(true);
			}
			else if (event.getSource() == leaderBoardButton)
				try {
				    Launcher.presence.details = "Admire le classement";
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("/p/classement")));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == newsButton) {
			    Launcher.presence.details = "Matte les nouveautés";
				LauncherFrame.getInstance().getWikiPanel().setVisible(false);
				LauncherFrame.getInstance().getStatsPanel().setVisible(false);
			}
			else if (event.getSource() == achievementsButton)
				try {
				    Launcher.presence.details = "Apprécie ses achievements";
					Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("/p/succes")));
				} catch (IOException | URISyntaxException e1) {}
			else if (event.getSource() == hideButton) {
			    Launcher.presence.details = "Occupé à autre chose";
				LauncherFrame.getInstance().setState(Frame.ICONIFIED);
			}
			else if (event.getSource() == settingsButton) {
			    Launcher.presence.details = "Bidouille les options";
				LauncherFrame.getInstance().getOptionPanel().setVisible(true);
			}
			else if (event.getSource() == voteButton)
				try {
				    Launcher.presence.details = "Vote pour azurpixel";
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
			else if (event.getSource() == getPlayButton())
				play();
		Launcher.updatePresence();
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
		getPlayButton().setEnabled(enabled);
	}

	public STexturedButton getPlayButton() {
		return playButton;
	}
}