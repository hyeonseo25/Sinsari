package panels;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.io.File;

public class StartPanel extends JPanel{
	private JButton startbt;
	private JButton exitbt;
	
	private ImageIcon backImg = new ImageIcon("images/시작패널배경.png");
	private Image back = backImg.getImage();
	
	private Clip backgroundMusic;
	
	private Dimension view = Toolkit.getDefaultToolkit().getScreenSize();
	
	public StartPanel(Object o) {
		Image startButton = new ImageIcon("images/buttons/GameStartButton.png").getImage();
		Image exitButton = new ImageIcon("images/buttons/ExitButton.png").getImage();
		
		exitbt = new JButton(new ImageIcon("images/buttons/ExitButton.png"));
		exitbt.setName("ExitButton");
		exitbt.setBorderPainted(false);
		exitbt.setFocusPainted(false);
		exitbt.setContentAreaFilled(false);
		exitbt.setBounds(20, 20, exitButton.getWidth(null), exitButton.getHeight(null));		
		exitbt.addMouseListener((MouseListener) o);
		add(exitbt);
		
		startbt = new JButton(new ImageIcon("images/buttons/GameStartButton.png"));
		startbt.setName("StartButton");
		startbt.setBorderPainted(false);
		startbt.setFocusPainted(false);
		startbt.setContentAreaFilled(false);
		startbt.setBounds((view.width/2 - startButton.getWidth(null)/2), 750, startButton.getWidth(null), startButton.getHeight(null));		
		startbt.addMouseListener((MouseListener) o);
		add(startbt);
		
		playMusic();
	}
	

	public void playMusic() {
		 try {
			 File file = new File("music/startPanelMusic.wav");
			 AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			 backgroundMusic = AudioSystem.getClip();
			 backgroundMusic.open(stream);
			 backgroundMusic.start();
			 backgroundMusic.loop(-1);
	     } catch(Exception e) {
	    	 e.printStackTrace();
	     }	
	}
	
	public void closeMusic() {
		backgroundMusic.close();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(back, 0, 0, this);
	}
}