package panels;

import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel{

	public GamePanel(Object o) {
		JButton gameoverbt;
		JButton clearbt;
		
		gameoverbt = new JButton();
		gameoverbt.setName("GameoverButton");
		gameoverbt.setText("���ӿ���");
		gameoverbt.setBounds(1690, 10, 100, 50);
		gameoverbt.addMouseListener((MouseListener) o);
		add(gameoverbt);
		
		clearbt = new JButton();
		clearbt.setName("ClearButton");
		clearbt.setText("Ŭ����");
		clearbt.setBounds(1800, 10, 100, 50);
		clearbt.addMouseListener((MouseListener) o);
		add(clearbt);
	}

}
