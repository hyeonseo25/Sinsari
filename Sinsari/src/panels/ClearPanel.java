package panels;

import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClearPanel extends JPanel{
	
	public ClearPanel(Object o) {
		JLabel l1 = new JLabel("클리어");
		add(l1);
		JButton replaybt = new JButton();
		replaybt.setName("ReplayButton");
		replaybt.setText("다시하기");
		replaybt.setBounds(1690, 10, 100, 50);
		replaybt.addMouseListener((MouseListener) o);
		add(replaybt);
	}

}
