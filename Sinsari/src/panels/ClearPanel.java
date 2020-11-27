package panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ClearPanel extends JPanel{
	private JButton replaybt;
	
	private ImageIcon backImg = new ImageIcon("images/클리어패널배경.png");
	private Image back = backImg.getImage();
	
	private Clip backgroundMusic;
	private Dimension view = Toolkit.getDefaultToolkit().getScreenSize();

	public ClearPanel(Object o) {
		Image replaybtn = new ImageIcon("images/buttons/RestartButton.png").getImage();
		
		replaybt = new JButton(new ImageIcon("images/buttons/RestartButton.png"));
		replaybt.setName("ClearReplayButton");
		replaybt.setBorderPainted(false);
		replaybt.setFocusPainted(false);
		replaybt.setContentAreaFilled(false);
		replaybt.setBounds(1200, 700, replaybtn.getWidth(null), replaybtn.getHeight(null));	
		replaybt.addMouseListener((MouseListener) o);
		add(replaybt);
	}

	public void playMusic() {
		 try {
			 File file = new File("music/gameOverMusic.wav");
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
	
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(back, 0, 0, this);
	}
}