package net.azurpixel.launcher;

import static re.alwyn974.swinger.Swinger.getResource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import re.alwyn974.swinger.util.WindowMover;

public class WebPage {
  public static void show(String url){
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	          public void run() {
	            ApplicationFrame mainFrame = new ApplicationFrame(url);
	            mainFrame.setVisible(true);
	          }
    });

  }

}

@SuppressWarnings("serial")
class ApplicationFrame extends JFrame{

	  JFXPanel javafxPanel;
	  WebView webComponent;
	  JPanel mainPanel;

	  JTextField urlField;
	  JButton goButton;

  public ApplicationFrame(String url){

    javafxPanel = new JFXPanel();

	Dimension tailleMoniteur = Toolkit.getDefaultToolkit().getScreenSize();
	int width = tailleMoniteur.width;
	int height = tailleMoniteur.height;
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(javafxPanel, BorderLayout.CENTER);
    JPanel urlPanel = new JPanel(new FlowLayout());
	setTitle(Launcher.AP_INFOS.getServerName());
	setIconImage(getResource("logo.jpg"));
	setDefaultCloseOperation(HIDE_ON_CLOSE);
	setLocation(width *1/16,height *1/16);
	WindowMover mover = new WindowMover(this);
	addMouseListener(mover);
	addMouseMotionListener(mover);
    /**
     * Handling the loading of new URL, when the user
     * enters the URL and clicks on Go button.
     */


    mainPanel.add(urlPanel, BorderLayout.NORTH);

    this.add(mainPanel);
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setSize(width *7/8, height *7/8);
    Platform.runLater(new Runnable() {
        @Override
        public void run() {

          BorderPane borderPane = new BorderPane();
          webComponent = new WebView();

          webComponent.getEngine().load(url);

          borderPane.setCenter(webComponent);
          Scene scene = new Scene(borderPane);
          javafxPanel.setScene(scene);

        }
      });
  }}
