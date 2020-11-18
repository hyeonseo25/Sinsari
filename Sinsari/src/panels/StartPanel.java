package panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StartPanel extends JPanel{

	private JButton startbt;
	private JButton exitbt;
	
	//private ImageIcon backImg = new ImageIcon("images/시작패널배경.png");
	//private Image back = backImg.getImage();
	
//	private Clip backgroundMusic;
	
	Dimension view = Toolkit.getDefaultToolkit().getScreenSize();
	
	public StartPanel(Object o) {
		Image startButton = new ImageIcon("images/buttons/StartButton.png").getImage();
		Image exitButton = new ImageIcon("images/buttons/ExitButton.png").getImage();
		
		exitbt = new JButton(new ImageIcon(exitButton));
		exitbt.setName("ExitButton");
		exitbt.setBorderPainted(false);
		exitbt.setFocusPainted(false);
		exitbt.setContentAreaFilled(false);
		exitbt.setBounds(20, 20, exitButton.getWidth(null), exitButton.getHeight(null));		
		exitbt.addMouseListener((MouseListener) o);
		add(exitbt);
		
		startbt = new JButton(new ImageIcon(startButton));
		startbt.setName("StartButton");
		startbt.setBorderPainted(false);
		startbt.setFocusPainted(false);
		startbt.setContentAreaFilled(false);
		startbt.setBounds((view.width/2 - startButton.getWidth(null)/2), 800, startButton.getWidth(null), startButton.getHeight(null));		
		startbt.addMouseListener((MouseListener) o);
		add(startbt);

	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.drawImage(back, 0, 0, this);
	}

}
