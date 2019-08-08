package net.azurpixel.launcher;

import static net.azurpixel.launcher.Launcher.tryToExit;
import static re.alwyn974.swinger.Swinger.getResource;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import fr.theshark34.supdate.BarAPI;
import re.alwyn974.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame implements WindowListener
{
	private static LauncherFrame instance;
	private LauncherPanel Launcherpanel;

	public LauncherFrame()
	{
		instance = this;
		setTitle(Launcher.AP_INFOS.getServerName());
		setIconImage(getResource("logo.jpg"));
		setSize(1000, 750);
		setUndecorated(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		WindowMover mover = new WindowMover(this);
		addMouseListener(mover);
		addMouseMotionListener(mover);

		addWindowListener(this);
		this.Launcherpanel = new LauncherPanel();
		add(Launcherpanel);
	}

	public static LauncherFrame getInstance()
	{
		return instance;
	}

	public LauncherPanel getLauncherPanel()
	{
		return this.Launcherpanel;	}	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		tryToExit();
		if (BarAPI.getNumberOfDownloadedFiles() != BarAPI.getNumberOfFileToDownload())
			Launcher.AP_SAVER.set("error", "true");
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}