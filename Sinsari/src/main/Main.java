package main;

import java.awt.CardLayout;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import panels.ClearPanel;
//import panels.GameOverPanel;
//import panels.GamePanel;
//import panels.RankingPanel;
//import panels.StartPanel;

public class Main extends ListenerAdapter{ 
	// �ʿ��� �޼��带 �׶��׶� �������̵� �ϱ� ���ؼ� ���� �߻�Ŭ������ ���� ��ӹ�����!
	// ���⼭ ��� implement �޾Ƶθ� �� ���� �޼��嵵 �������̵� �ؾ� ��
	private JFrame frame;
	
//	private StartPanel startPanel; 			// �����г�
//	private GamePanel gamePanel; 			// �����г�
//	private GameOverPanel gameOverPanel; 	// ���ӿ����г�
//	private ClearPanel clearPanel; 			// Ŭ�����г�
//	private RankingPanel rankingPanel; 		// ��ŷ �г�
	
	private CardLayout cl; // ī�� ���̾ƿ� - �г� �������� �������� �����ټ� �ְ� ����
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
	}
	
	public Main() {
		init();
	}
	
	private void init() {
		frame = new JFrame();
		frame.setTitle("���䳢�� ������"); // ���α׷� �̸� ����
		//frame.setUndecorated(true);
		frame.setVisible(true); // â ���̰��ϱ�
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // ��üȭ��
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ư�� ������ ����
		
		cl = new CardLayout();
		frame.getContentPane().setLayout(cl); // �������� ī�巹�̾ƿ����� ����
		
		// �гο� main�� �ִ� �����ʸ� �־���
//		startPanel = new StartPanel(this); 
//		gamePanel = new GamePanel(this, frame, cl); // �� ���� �г� ����
//		gameOverPanel = new GameOverPanel(this);
//		clearPanel = new ClearPanel(this);
//		rankingPanel = new RankingPanel(this);
		
		// ��� �г��� ���̾ƿ��� null�� ��ȯ
//		startPanel.setLayout(null);
//		gamePanel.setLayout(null);
//		gameOverPanel.setLayout(null);
//		clearPanel.setLayout(null);
//		rankingPanel.setLayout(null);
		
		// �����ӿ� �гε��� �߰��Ѵ�.(ī�� ���̾ƿ��� ���� �гε�)
//		frame.getContentPane().add(startPanel, "start");
//		frame.getContentPane().add(gamePanel, "game");
//		frame.getContentPane().add(gameOverPanel, "gameover");
//		frame.getContentPane().add(clearPanel, "clear");
//		frame.getContentPane().add(rankingPanel, "ranking");
		
		cl.show(frame.getContentPane(), "start"); // start�г��� ī�巹�̾ƿ� �ֻ������ ����
		//startPanel.requestFocus(); // �����ʸ� start�гο� ������ ��
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) { // mouseClicked�� ���氡��
			
//		if (e.getComponent().getName().equals("StartButton")) { // StartButton�̶�� �̸��� ���� ��ư�� �����ٸ�
//			frame.getContentPane().remove(gamePanel); // ��� �ߴ� ���� �г��� �����ӿ��� ����
//			gamePanel = new GamePanel(this, frame, cl); // �� ���� �г� ����
//			gamePanel.setLayout(null);
//			gamePanel.gameStart();
//			frame.getContentPane().add(gamePanel, "game");
//			cl.show(frame.getContentPane(), "game"); // game�г��� ī�巹�̾ƿ� �ֻ������ ����
//			gamePanel.requestFocus(); // �����ʸ� game�гο� ������ ��
//			
//		} else if (e.getComponent().getName().equals("RankingButton")) { // RankingButton�̶�� �̸��� ���� ��ư�� �����ٸ�
//			cl.show(frame.getContentPane(), "ranking"); // ranking�г��� ī�巹�̾ƿ� �ֻ������ ����
//			rankingPanel.requestFocus(); // �����ʸ� ranking�гο� ������ ��
//			
//		} else if (e.getComponent().getName().equals("GameoverButton")) { // GameoverButton�̶�� �̸��� ���� ��ư�� �����ٸ�
//			cl.show(frame.getContentPane(), "gameover"); // gameover�г��� ī�巹�̾ƿ� �ֻ������ ����
//			gameOverPanel.requestFocus(); // �����ʸ� gameOver�гο� ������ ��
//			gamePanel.closeMusic(); // ���� ��� ����
//			
//		} else if (e.getComponent().getName().equals("ClearButton")) { // ClearButton�̶�� �̸��� ���� ��ư�� �����ٸ�
//			cl.show(frame.getContentPane(), "clear"); // clear�г��� ī�巹�̾ƿ� �ֻ������ ����
//			clearPanel.requestFocus(); // �����ʸ� clear�гο� ������ ��	
//			gamePanel.closeMusic(); // ���� ��� ����
//			
//		} else if (e.getComponent().getName().equals("ReplayButton")) { // ReplayButton�̶�� �̸��� ���� ��ư�� �����ٸ�
//			cl.show(frame.getContentPane(), "start"); // start�г��� ī�巹�̾ƿ� �ֻ������ ����
//			startPanel.requestFocus(); // �����ʸ� start�гο� ������ ��
//		} else if (e.getComponent().getName().equals("ExitButton")) { // ReplayButton�̶�� �̸��� ���� ��ư�� �����ٸ�
//			System.exit(0); 
//		}
	}
	
}