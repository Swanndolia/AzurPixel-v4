package net.azurpixel.launcher;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openauth.model.response.RefreshResponse;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.openlauncherlib.external.BeforeLaunchingEvent;
import re.alwyn974.openlauncherlib.external.ExternalLaunchProfile;
import re.alwyn974.openlauncherlib.external.ExternalLauncher;
import re.alwyn974.openlauncherlib.minecraft.AuthInfos;
import re.alwyn974.openlauncherlib.minecraft.GameFolder;
import re.alwyn974.openlauncherlib.minecraft.GameInfos;
import re.alwyn974.openlauncherlib.minecraft.GameTweak;
import re.alwyn974.openlauncherlib.minecraft.GameType;
import re.alwyn974.openlauncherlib.minecraft.GameVersion;
import re.alwyn974.openlauncherlib.minecraft.MinecraftLauncher;
import re.alwyn974.openlauncherlib.minecraft.util.ConnectToServer;
import re.alwyn974.openlauncherlib.util.CrashReporter;
import re.alwyn974.openlauncherlib.util.Saver;
import re.alwyn974.swinger.Swinger;
import re.alwyn974.swinger.textured.STexturedProgressBar;

public class Launcher {
	
	public static final String AP_IP = "azurpixel.net";
	public static final String AP_URL = "https://azurpixel.net";
	public static final String AP_IMG  = AP_URL.concat("/app/webroot/img/uploads/");
	public static GameVersion AP_VERSION = new GameVersion("AzurPixel_V1_8", GameType.V1_8_HIGHER);
	public static GameInfos AP_INFOS = new GameInfos("AzurPixel v4", AP_VERSION, new GameTweak[] {GameTweak.OPTIFINE});
	public static final File AP_DIR = AP_INFOS.getGameDir();
	public static final SUpdate AP_UPDATER = new SUpdate(AP_URL.concat("/launcher/update"), AP_DIR);
	public static final SUpdate AP_ASSETSNLIBS = new SUpdate(AP_URL.concat("/launcher/assetsNlibs"), AP_DIR);
	public static final Saver AP_SAVER = new Saver(new File(AP_DIR, "AzurPixel.properties"));
	public static final CrashReporter AP_CRASH = new CrashReporter(AP_INFOS.getServerName(), new File(AP_DIR, "crashs"));
	public static final File AP_LOGS = new File(AP_DIR, "/logs/logs.txt");
	public static final Authenticator AP_AUTH = new Authenticator("https://authserver.mojang.com/", AuthPoints.NORMAL_AUTH_POINTS);
    
	private static DiscordRichPresence presence = new DiscordRichPresence();
    private static DiscordRPC lib = DiscordRPC.INSTANCE;
	
	private static AuthInfos authInfos;
	private static Thread updateThread;
	private static Thread updateAssetsThread;

	public static void main(String[] args) throws Exception
	{
		if(!AP_DIR.exists())
			AP_DIR.mkdir();
	    if (AP_SAVER.get("autoConnect") == null)
			AP_SAVER.set("autoConnect", "true");
	    if (AP_SAVER.get("borderless") == null)
			AP_SAVER.set("borderless", "false");
		if (AP_SAVER.get("premium") == null)
			AP_SAVER.set("premium", "");
		if (AP_SAVER.get("openAtStart") == null) 
			AP_SAVER.set("openAtStart", "false");
		if (AP_SAVER.get("openAtStart").equals("true")) 
			Desktop.getDesktop().browse(new URI(Launcher.AP_URL.concat("/vote")));
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/resources/");
		AP_UPDATER.addApplication(new FileDeleter());
		new LauncherFrame().setVisible(true);
		if (AP_SAVER.get("autoConnect").equals("true"))
			new ConnectToServer(AP_IP, "25565");
		else
			;
	    String applicationId = "617320590570815498";
	    String steamId = "";
	    DiscordEventHandlers handlers = new DiscordEventHandlers();
	    lib.Discord_Initialize(applicationId, handlers, true, steamId);
	    presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
	    presence.details = "Admire le menu";
	    presence.state = "Empire / SkyAcid";
	    presence.largeImageKey = "icone";
	    presence.largeImageText = "azurpixel.net";
	    updatePresence();
	}
	
	public static void updatePresence() {
	    lib.Discord_UpdatePresence(presence);
		Thread t = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				lib.Discord_RunCallbacks();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					lib.Discord_Shutdown();
					break;
				}
			}
		}, "RPC-Callback-Handler");
		t.start();
	}
	
	public static void copyFile(File from, File to) throws IOException {
	    Files.copy(from.toPath(), to.toPath());
	} 
	
	public static void replaceFile(File from, File to) throws IOException {
	    Files.copy( from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
	} 
	
	public static void auth(String user, String pass) throws AuthenticationException {
		if (user.length() == 0) {
			LauncherFrame.getInstance().getLauncherPanel().setInfoText("Veuillez entrer un nom d'utilisateur ou un email valide");
			LauncherFrame.getInstance().getLauncherPanel().setFieldsEnabled(true);
		}
		else {
			if(pass.length() == 0) {
				AP_SAVER.set("premium", "false");
				authInfos = new AuthInfos(user, "sry", "nope");
			}
			else {
				AP_SAVER.set("premium", "true");
				AuthResponse rep = AP_AUTH.authenticate(AuthAgent.MINECRAFT, user, pass, AP_SAVER.get("client-token", null));
				AP_SAVER.set("client-token", rep.getClientToken());
				authInfos = new AuthInfos(rep.getSelectedProfile().getName(), rep.getAccessToken(), rep.getSelectedProfile().getId());
			}
		}
	}
	
	public static void refresh() throws AuthenticationException {
		if (AP_SAVER.get("premium").equals("true")) {
			RefreshResponse rep = AP_AUTH.refresh(AP_SAVER.get("access-token"), AP_SAVER.get("client-token"));
			authInfos = new AuthInfos(rep.getSelectedProfile().getName(), rep.getAccessToken(), rep.getSelectedProfile().getId());	
		}
		else
			authInfos = new AuthInfos(AP_SAVER.get("username"), "sry", "nope");	
	}
	
	public static void setAuthInfos(String user, String accessToken, String uuid) throws AuthenticationException {
			authInfos = new AuthInfos(user,	accessToken, uuid);
	}

	public static void saveInfos(boolean keepLogin) {
		AP_SAVER.set("username", authInfos.getUsername());
		String accessToken = "";
		if (keepLogin)
			accessToken = authInfos.getAccessToken();
		AP_SAVER.set("access-token", accessToken);
	}
		
	public static void update() throws Exception
	{
		updateThread = new Thread()
		{
			private int val;
			private int max;

			public void run()
			{
				STexturedProgressBar progressBar = LauncherFrame.getInstance().getLauncherPanel().getProgressBar();
				while (!isInterrupted())
				{
					if (BarAPI.getNumberOfFileToDownload() == 0)
					{
						LauncherFrame.getInstance().getLauncherPanel().setInfoText("Vérification des versions en cours, veuillez patienter");
						continue;
					}
					this.val = (int) (BarAPI.getNumberOfTotalDownloadedBytes()/1000);
					this.max = (int) (BarAPI.getNumberOfTotalBytesToDownload()/1000);

					progressBar.setValue(this.val);
					progressBar.setMaximum(this.max);

					LauncherFrame.getInstance().getLauncherPanel().setInfoText("Téléchargement des versions " + BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " + Swinger.percentage(this.val, this.max) + "%");
				}
			}
		};
		
			if (AP_SAVER.get("game-version") == null) 
				AP_SAVER.set("game-version", "V1_8");
			if (AP_SAVER.get("game-preset") == null)
				AP_SAVER.set("game-preset", "low");
			if (AP_SAVER.get("game-memory") == null)
				AP_SAVER.set("game-memory", "XMX1G");
			AP_SAVER.set("libs", "libs/" + AP_SAVER.get("game-version"));
			AP_SAVER.set("bin", "bin/azurpixel_" + AP_SAVER.get("game-version") + ".jar");
			AP_VERSION = new GameVersion("AzurPixel_" + AP_SAVER.get("game-version"), GameType.V1_8_HIGHER);
			AP_INFOS = new GameInfos("AzurPixel v4", AP_VERSION, new GameTweak[] {GameTweak.OPTIFINE});
		
			if (AP_SAVER.get("game-version") == "V1_13") 
				AP_SAVER.set("natives", "bin/natives/1.13");
			else if (AP_SAVER.get("game-version") == "V1_14")
				AP_SAVER.set("natives", "bin/natives/1.14");
			else
				AP_SAVER.set("natives", "bin/natives/1.8-1.12");
			if (AP_SAVER.get("borderless") == null)
				AP_SAVER.set("borderless", "false");
		
		updateThread.start();
		AP_UPDATER.start();
		updateThread.interrupt();
		presetSet();
		LauncherFrame.getInstance().getLauncherPanel().setInfoText("Vérification des versions terminée");
	}
	
	
	
	public static void updateAssets() throws Exception
	{
		updateAssetsThread = new Thread()
		{
			private int val;
			private int max;

			public void run()
			{
				STexturedProgressBar progressBar = LauncherFrame.getInstance().getLauncherPanel().getProgressBar();
				while (!isInterrupted())
				{
					if (BarAPI.getNumberOfFileToDownload() == 0)
					{
						LauncherFrame.getInstance().getLauncherPanel().setInfoText("Vérification des assets en cours, veuillez patienter");
						continue;
					}
					this.val = (int) (BarAPI.getNumberOfTotalDownloadedBytes()/1000);
					this.max = (int) (BarAPI.getNumberOfTotalBytesToDownload()/1000);

					progressBar.setValue(this.val);
					progressBar.setMaximum(this.max);

					LauncherFrame.getInstance().getLauncherPanel().setInfoText("Téléchargement des asssets " + BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " + Swinger.percentage(this.val, this.max) + "%");
				}
			}
		};
		
		updateAssetsThread.start();
		AP_ASSETSNLIBS.start();
		updateAssetsThread.interrupt();
		LauncherFrame.getInstance().getLauncherPanel().setInfoText("Vérification des assets terminée");

	}

	public static void presetSet() throws IOException {
			
		if (new File(AP_DIR, "/options.txt").isFile() && new File(AP_DIR, "/presets/custom/options.txt").isFile()) {
				replaceFile(new File(AP_DIR, "/options.txt"), new File(AP_DIR, "/presets/custom/options.txt"));
				replaceFile(new File(AP_DIR, "/optionsof.txt"), new File(AP_DIR, "/presets/custom/optionsof.txt"));
			}
		replaceFile(new File(AP_DIR, "/presets/" + AP_SAVER.get("game-preset") + "/optionsof.txt"), new File(AP_DIR, "/optionsof.txt"));
		
		if (new File(AP_DIR, "/servers.dat").isFile())
			;
		else 
			copyFile(new File(AP_DIR, "/presets/servers.dat"), new File(AP_DIR, "/servers.dat"));
		if (new File(AP_DIR, "/options.txt").isFile())
			;
		else 
			copyFile(new File(AP_DIR, "/presets/options.txt"), new File(AP_DIR, "/options.txt"));

		Dimension tailleMoniteur = Toolkit.getDefaultToolkit().getScreenSize();
		String width = Integer.toString(tailleMoniteur.width *7/8);
		String height = Integer.toString(tailleMoniteur.height *7/8);
		
		if (AP_SAVER.get("borderless").equals("true")) {
			width = Integer.toString(tailleMoniteur.width);
			height = Integer.toString(tailleMoniteur.height);
		}
		File dirFrom = new File(AP_DIR, "/options.txt");
		File tmp = new File(AP_DIR, "/tmp.txt");
		
		BufferedReader fichier = new BufferedReader(new FileReader(dirFrom)); 
		BufferedWriter fichierW = new BufferedWriter(new FileWriter(tmp)); 
		String str; 
		str = fichier.readLine( ); 

		while (str != null){ 
			str=str.replaceAll(":","="); 
			fichierW.write(str);
			fichierW.write('\n'); 
			fichierW.flush(); 
			str = fichier.readLine(); 
		}
		fichierW.close(); 
		fichier.close(); 
		tmp.renameTo(dirFrom); 
		
		Saver AP_OPTIONS = new Saver(tmp);
		Saver AP_OPTIONSPRESET = new Saver(new File(AP_DIR, "/presets/" + AP_SAVER.get("game-preset") + "/options.txt"));
		AP_OPTIONS.set("overrideWidth", width);
		AP_OPTIONS.set("overrideHeight", height);
		AP_OPTIONS.set("renderDistance", AP_OPTIONSPRESET.get("renderDistance"));
		AP_OPTIONS.set("particles", AP_OPTIONSPRESET.get("particles"));
		AP_OPTIONS.set("fboEnable", AP_OPTIONSPRESET.get("fboEnable"));
		AP_OPTIONS.set("ao", AP_OPTIONSPRESET.get("ao"));
		AP_OPTIONS.set("lastServer", AP_OPTIONSPRESET.get("lastServer"));
		AP_OPTIONS.set("useVbo", AP_OPTIONSPRESET.get("useVbo"));
		AP_OPTIONS.set("mipmapLevels", AP_OPTIONSPRESET.get("mipmapLevels"));
		AP_OPTIONS.set("allowBlockAlternatives", AP_OPTIONSPRESET.get("allowBlockAlternatives"));
		AP_OPTIONS.set("entityShadows", AP_OPTIONSPRESET.get("entityShadows"));

		str = "";
		fichier = new BufferedReader(new FileReader(tmp)); 
		fichierW = new BufferedWriter(new FileWriter(dirFrom));  
		str = fichier.readLine( ); 
		while (str != null){ 
			str=str.replaceAll("=",":"); 
			fichierW.write(str); 
			fichierW.write('\n');
			fichierW.flush(); 
			str = fichier.readLine(); 
		}
		fichierW.close();  
		fichier.close(); 
		tmp.renameTo(dirFrom); 
		tmp.delete();
	}
	
	public static void interruptUpdateThread()
	{
		updateThread.interrupt();
	}

	public static void launch() throws LaunchException, InterruptedException {
		
	    if (AP_SAVER.get("username").equals("Swanndolia"))
	    	presence.smallImageKey = AP_SAVER.get("username").toLowerCase();
	    else	
	    	presence.smallImageKey = "default";
	    presence.smallImageText = AP_SAVER.get("username");
	    presence.details = "A rejoint AzurPixel";
	    presence.state = "Empire / SkyAcid";
	    presence.largeImageKey = "icone";
	    presence.largeImageText = "azurpixel.net";
		presence.partySize = 1;
		presence.partyMax  = 4;
		updatePresence();
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(AP_INFOS, new GameFolder("assets", AP_SAVER.get("libs"), AP_SAVER.get("natives"), AP_SAVER.get("bin")), authInfos);
		
		GameMemory gameMemory = GameMemory.XMX1G;
		try {
			gameMemory = GameMemory.valueOf(AP_SAVER.get("game-memory", "XMX1G"));
		} catch (IllegalArgumentException ex) {
			reportException(ex);
		}
		profile.getVmArgs().addAll(0, gameMemory.getVmArgs());
		if (AP_SAVER.get("borderless").equals("true"))
			profile.getVmArgs().add("-Dorg.lwjgl.opengl.Window.undecorated=true");
		ExternalLauncher launcher = new ExternalLauncher(profile, new BeforeLaunchingEvent() {
			@Override
			public void onLaunching(ProcessBuilder processBuilder) {
				String javaPath = AP_SAVER.get("java-path", "");
				if (javaPath != null && !javaPath.equals(""))
					processBuilder.command().set(0, javaPath);
			}
		});

		LauncherFrame.getInstance().setVisible(false);
		int exitCode = launcher.launch().waitFor();
		System.out.println("\nMinecraft finished with exit code " + exitCode);
		System.exit(0);
	}

	public static String reportException(Exception e)
	{
		try {
			File file = AP_CRASH.writeError(e);
			return file.getCanonicalPath();
		} catch (IOException ex) {
			ex.printStackTrace();
			return "les logs du launcher.";
		}
	}

	public static void tryToExit() {
		if (BarAPI.getNumberOfDownloadedFiles() != BarAPI.getNumberOfFileToDownload() &&
				JOptionPane.showConfirmDialog(LauncherFrame.getInstance(),
						"Voulez-vous vraiment interrompre le téléchargement en cours ?", "Téléchargement en cours",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
				) != JOptionPane.YES_OPTION)
			return;
		System.exit(0);
	}

	public static AuthInfos getAuthInfos() {
		return authInfos;
	}
}