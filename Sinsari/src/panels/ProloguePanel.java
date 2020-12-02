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
	
	private Image scene[] = {new ImageIcon("images/prologue/프롤로그1.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그2.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그3.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그4.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그5.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그6.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그7.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그8.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그9.PNG").getImage()
			,new ImageIcon("images/prologue/프롤로그10.PNG").getImage()};
	
	private Clip backgroundMusic;
	private Dimension view = Toolkit.getDefaultToolkit().getScreenSize();

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
			 File file = new File("music/prologueMusic.wav");
			 AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			 backgroundMusic = AudioSystem.getClip();
			 backgroundMusic.open(stream);
			 backgroundMusic.start();
			 backgroundMusic.loop(-1);
	     } catch(Exception e) {
	    	 e.printStackTrace();
	     }	
		  /* Track : HEMIO - 종이접기
			Music by Hemio
			Follow Artist : https://www.youtube.com/user/FVGozak
			Music Promoted by DayDreamSound https://youtu.be/NsvVEh8K0SM
			𝐌𝐮𝐬𝐢𝐜 𝐏𝐥𝐚𝐲𝐥𝐢𝐬𝐭 : https://youtu.be/cKF2tz31j2U
		  */
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
