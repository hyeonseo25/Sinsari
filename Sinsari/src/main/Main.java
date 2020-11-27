package main;

import java.awt.CardLayout;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import panels.ClearPanel;
import panels.GameOverPanel;
import panels.GamePanel;
import panels.ProloguePanel;
import panels.StartPanel;

public class Main extends ListenerAdapter{ 
	// �ʿ��� �޼��带 �׶��׶� �������̵� �ϱ� ���ؼ� ���� �߻�Ŭ������ ���� ��ӹ�����!
	// ���⼭ ��� implement �޾Ƶθ� �� ���� �޼��嵵 �������̵� �ؾ� ��
	private JFrame frame;
	
	private StartPanel startPanel; 			// �����г�
	private ProloguePanel prologuePanel; 	// ���ѷα� �г�
	private GamePanel gamePanel; 			// �����г�
	private GameOverPanel gameOverPanel; 	// ���ӿ����г�
	private ClearPanel clearPanel; 			// Ŭ�����г�
	
	private CardLayout cl; // ī�� ���̾ƿ� - �г� �������� �������� �����ټ� �ְ� ����
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
	}
	
	public Main() {
		init();
	}
	
	public ClearPanel getClearPanel() {
		return clearPanel;
	}
	
	public GameOverPanel getGameOverPanel() {
		return gameOverPanel;
	}
	
	public CardLayout getCl() {
		return cl;
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
		startPanel = new StartPanel(this); 
		prologuePanel = new ProloguePanel(this);
		gamePanel = new GamePanel(this, frame, cl); // �� ���� �г� ����
		gameOverPanel = new GameOverPanel(this);
		clearPanel = new ClearPanel(this);	
		
		// ��� �г��� ���̾ƿ��� null�� ��ȯ
		startPanel.setLayout(null);
		prologuePanel.setLayout(null);
		gamePanel.setLayout(null);
		gameOverPanel.setLayout(null);
		clearPanel.setLayout(null);
		
		// �����ӿ� �гε��� �߰��Ѵ�.(ī�� ���̾ƿ��� ���� �гε�)
		frame.getContentPane().add(startPanel, "start"); // start��� �̸����� �߰�
		frame.getContentPane().add(prologuePanel, "prologue");
		frame.getContentPane().add(gamePanel, "game");
		frame.getContentPane().add(gameOverPanel, "gameover");
		frame.getContentPane().add(clearPanel, "clear");
		
		cl.show(frame.getContentPane(), "start"); // start�г��� ī�巹�̾ƿ� �ֻ������ ����
		startPanel.requestFocus(); // �����ʸ� start�гο� ������ ��
	}
	 
	@Override
	public void mousePressed(MouseEvent e) { // mouseClicked�� ���氡��
			
		if (e.getComponent().getName().equals("StartButton")) { // StartButton�̶�� �̸��� ���� ��ư�� �����ٸ�
			frame.getContentPane().remove(prologuePanel); // ��� �ߴ� ���ѷα� �г��� �����ӿ��� ����
			prologuePanel = new ProloguePanel(this); // �� ���ѷα� �г� ����
			prologuePanel.setLayout(null);
			frame.getContentPane().add(prologuePanel, "prologue");
			cl.show(frame.getContentPane(), "prologue"); // prologue�г��� ī�巹�̾ƿ� �ֻ������ ����
			prologuePanel.requestFocus(); // �����ʸ� prologue�гο� ������ ��
			startPanel.closeMusic();
		} else if (e.getComponent().getName().equals("GameStartButton")) { // GameoverButton�̶�� �̸��� ���� ��ư�� �����ٸ�
			frame.getContentPane().remove(gamePanel); // ��� �ߴ� ���� �г��� �����ӿ��� ����
			gamePanel = new GamePanel(this, frame, cl); // �� ���� �г� ����
			gamePanel.setLayout(null);
			gamePanel.gameStart(); // ���� ���� �޼��� ����
			frame.getContentPane().add(gamePanel, "game");
			cl.show(frame.getContentPane(), "game"); // game�г��� ī�巹�̾ƿ� �ֻ������ ����
			gamePanel.requestFocus(); // �����ʸ� game�гο� ������ ��
		} else if (e.getComponent().getName().equals("GameOverReplayButton")) { // GameoverButton�̶�� �̸��� ���� ��ư�� �����ٸ�
			gameOverPanel.closeMusic();
			cl.show(frame.getContentPane(), "start"); // start�г��� ī�巹�̾ƿ� �ֻ������ ����
			startPanel.requestFocus(); // �����ʸ� start�гο� ������ ��
			startPanel.playMusic();
		} else if (e.getComponent().getName().equals("ClearReplayButton")) { // ClearButton�̶�� �̸��� ���� ��ư�� �����ٸ�
			clearPanel.closeMusic();
			cl.show(frame.getContentPane(), "start"); // start�г��� ī�巹�̾ƿ� �ֻ������ ����
			startPanel.requestFocus(); // �����ʸ� start�гο� ������ ��
			startPanel.playMusic();
		} else if (e.getComponent().getName().equals("ExitButton")) { // ExitButton�̶�� �̸��� ���� ��ư�� �����ٸ�
			System.exit(0); 	
		}
	}
	
}