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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ProloguePanel extends JPanel{
	private JButton nextbt;
	private JButton startbt;
	
	private int page = 0;
	
	private Image scene[] = {new ImageIcon("images/prologue/프롤로그1.png").getImage()
			,new ImageIcon("images/prologue/프롤로그2.png").getImage()
			,new ImageIcon("images/prologue/프롤로그3.png").getImage()};
	
	Dimension view = Toolkit.getDefaultToolkit().getScreenSize();

	public ProloguePanel(Object o) {
		page = 0;
		Image nextButton = new ImageIcon("images/buttons/ArrowButton.png").getImage();
		Image startButton = new ImageIcon("images/buttons/StartButton.png").getImage();
		
		nextbt = new JButton(new ImageIcon(nextButton));
		nextbt.setName("NextButton");
		nextbt.setBorderPainted(false);
		nextbt.setFocusPainted(false);
		nextbt.setContentAreaFilled(false);
		nextbt.setBounds((view.width/2 - nextButton.getWidth(null)/2), 800, nextButton.getWidth(null), nextButton.getHeight(null));
		nextbt.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				page++;
				if(page>=scene.length-1) {
					startbt = new JButton(new ImageIcon(startButton));
					startbt.setName("GameStartButton");
					startbt.setBorderPainted(false);
					startbt.setFocusPainted(false);
					startbt.setContentAreaFilled(false);
					startbt.setBounds((view.width/2 - startButton.getWidth(null)/2), 800, startButton.getWidth(null), startButton.getHeight(null));
					startbt.addMouseListener((MouseListener) o);
					add(startbt);
					remove(nextbt);
				}
				repaint();
			}
		});
		add(nextbt);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(scene[page], 0, 0, this);
	}
}
