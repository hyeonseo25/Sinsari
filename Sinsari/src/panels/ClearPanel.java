package panels;

import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClearPanel extends JPanel{
	
	public ClearPanel(Object o) {
		JLabel l1 = new JLabel("Ŭ����");
		add(l1);
		JButton replaybt = new JButton();
		replaybt.setName("ReplayButton");
		replaybt.setText("�ٽ��ϱ�");
		replaybt.setBounds(1690, 10, 100, 50);
		replaybt.addMouseListener((MouseListener) o);
		add(replaybt);
	}

}
