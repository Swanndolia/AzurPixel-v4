package net.azurpixel.launcher;

import static re.alwyn974.swinger.Swinger.getResource;

import java.awt.Graphics;
import java.awt.Image;

import javafx.embed.swing.JFXPanel;
import re.alwyn974.swinger.Swinger;
import re.alwyn974.swinger.event.SwingerEvent;
import re.alwyn974.swinger.event.SwingerEventListener;

@SuppressWarnings("serial")
public class StatsPanel extends JFXPanel implements SwingerEventListener {
	
	private Image background = getResource("navPanel.png");
	
	public StatsPanel()
    {
		Swinger.setResourcePath("/resources/");
		setBounds(260,284,545,300);
		setLayout(null);
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
