package panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ProloguePanel extends JPanel{
	private JButton nextbt;
	private JButton startbt;
	
	private int page = 0;
	
	private Image scene[] = {new ImageIcon("images/prologue/���ѷα�1.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�2.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�3.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�4.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�5.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�6.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�7.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�8.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�9.PNG").getImage()
			,new ImageIcon("images/prologue/���ѷα�10.PNG").getImage()};
	
	private Clip backgroundMusic;
	Dimension view = Toolkit.getDefaultToolkit().getScreenSize();

	public ProloguePanel(Object o) {
		page = 0;
		Image nextButton = new ImageIcon("images/buttons/ArrowButton.png").getImage();
		Image startButton = new ImageIcon("images/buttons/GameStartButton.png").getImage();
		
		nextbt = new JButton(new ImageIcon(nextButton));
		nextbt.setName("NextButton");
		nextbt.setBorderPainted(false);
		nextbt.setFocusPainted(false);
		nextbt.setContentAreaFilled(false);
		nextbt.setBounds(1500, 750, nextButton.getWidth(null), nextButton.getHeight(null));
		
		startbt = new JButton(new ImageIcon(startButton));
		startbt.setName("GameStartButton");
		startbt.setBorderPainted(false);
		startbt.setFocusPainted(false);
		startbt.setContentAreaFilled(false);
		startbt.setBounds((view.width/2 - startButton.getWidth(null)/2), 720, startButton.getWidth(null), startButton.getHeight(null));
		startbt.addMouseListener((MouseListener) o);
		
		nextbt.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				page++;
				if(page>=scene.length-1) {
					add(startbt);
					remove(nextbt);
				}
				repaint();
			}
		});
		add(nextbt);
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
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(scene[page], 0, 0, this);
	}
}
