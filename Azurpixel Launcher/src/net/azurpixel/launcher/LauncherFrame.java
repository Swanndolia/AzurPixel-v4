package net.azurpixel.launcher;

import static net.azurpixel.launcher.Launcher.tryToExit;
import static re.alwyn974.swinger.Swinger.getResource;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import re.alwyn974.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame implements WindowListener
{
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	private OptionPanel optionPanel;
	private WikiPanel wikiPanel;
	private StatsPanel statsPanel;
	
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
		optionPanel = new OptionPanel();
		optionPanel.setVisible(false);
		add(optionPanel);
		wikiPanel = new WikiPanel();
		wikiPanel.setVisible(false);
		add(wikiPanel);
		statsPanel = new StatsPanel();
		statsPanel.setVisible(false);
		add(statsPanel);
		launcherPanel = new LauncherPanel();
		add(launcherPanel);
		WindowMover mover = new WindowMover(this);
		addMouseListener(mover);
		addMouseMotionListener(mover);
		addWindowListener(this);
	}
	
	public static LauncherFrame getInstance()
	{
		return instance;
	}

	public LauncherPanel getLauncherPanel()
	{
		return launcherPanel;	}	
	
	public OptionPanel getOptionPanel()
	{
		return optionPanel;
	}	
	public WikiPanel getWikiPanel()
	{
		return wikiPanel;
	}	
	
	public StatsPanel getStatsPanel()
	{
		return statsPanel;
	}	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		tryToExit();
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