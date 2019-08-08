package net.azurpixel.bootstrap;



import java.io.File;

import fr.theshark34.supdate.SUpdate;
import re.alwyn974.openlauncherlib.LaunchException;
import re.alwyn974.openlauncherlib.external.ClasspathConstructor;
import re.alwyn974.openlauncherlib.external.ExternalLaunchProfile;
import re.alwyn974.openlauncherlib.external.ExternalLauncher;
import re.alwyn974.openlauncherlib.minecraft.util.GameDirGenerator;
import re.alwyn974.openlauncherlib.util.CrashReporter;
import re.alwyn974.openlauncherlib.util.SplashScreen;
import re.alwyn974.openlauncherlib.util.explorer.ExploredDirectory;
import re.alwyn974.openlauncherlib.util.explorer.Explorer;
import re.alwyn974.swinger.Swinger;
import re.alwyn974.swinger.animation.Animator;



public class AzurpixelBootstrap {



	private static SplashScreen splash;

	public static final File AP_B_DIR = new File(GameDirGenerator.createGameDir("AzurPixel v4"), "Launcher");

	private static CrashReporter crashReporter = new CrashReporter("Bootstrap-Crash", new File(AP_B_DIR, "crashreport/"));



	public static void main(String[]args) throws InterruptedException {

		Swinger.setResourcePath("/resources/");

		displaySplash();

		try {

			doUpdate();

		} catch (Exception e) {

			crashReporter.catchError(e, "Impossible de mettre à jour le Launcher !");
		}



		try {

			launchLauncher();

		} catch (LaunchException e) {

			crashReporter.catchError(e, "Impossible de lancer le Launcher !");

		}



	}



	public static void displaySplash() throws InterruptedException {

		splash = new SplashScreen("AzurPixel v4", Swinger.getResource("splash.jpg"));

		splash.setIconImage(Swinger.getResource("logo.jpg"));

		splash.setTransparent();

		splash.setLayout(null);
		 Thread.sleep(800L);
		Animator.fadeInFrame(splash, Animator.FAST);

		splash.setVisible(true);

	}



	public static void doUpdate() throws Exception {



		SUpdate su = new SUpdate("https://azurpixel.net/launcher/bootstrap/", AP_B_DIR);
		su.getServerRequester().setRewriteEnabled(true);
	
		su.start();
		
	}



	private static void launchLauncher() throws LaunchException {

		ClasspathConstructor constructor = new ClasspathConstructor();

		ExploredDirectory gameDir = Explorer.dir(AP_B_DIR);

		constructor.add(gameDir.sub("libs").allRecursive().files().match("^(.*\\.((jar)$))*$"));

		constructor.add(gameDir.get("Launcher.jar"));



		ExternalLaunchProfile profile = new ExternalLaunchProfile("net.azurpixel.launcher.Launcher", constructor.make());

		ExternalLauncher launcher = new ExternalLauncher(profile);
		Process p = launcher.launch();

		splash.setVisible(false);
		try {

			p.waitFor();

		} catch (InterruptedException localInterruptedException) {

		}

		System.exit(0);

	}

	
}