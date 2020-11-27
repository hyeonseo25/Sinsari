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
	// 필요한 메서드를 그때그때 오버라이드 하기 위해서 따로 추상클래스를 빼서 상속받은듯!
	// 여기서 모두 implement 받아두면 안 쓰는 메서드도 오버라이드 해야 함
	private JFrame frame;
	
	private StartPanel startPanel; 			// 시작패널
	private ProloguePanel prologuePanel; 	// 프롤로그 패널
	private GamePanel gamePanel; 			// 게임패널
	private GameOverPanel gameOverPanel; 	// 게임오버패널
	private ClearPanel clearPanel; 			// 클리어패널
	
	private CardLayout cl; // 카드 레이아웃 - 패널 여러개를 돌려가며 보여줄수 있게 해줌
	
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
		frame.setTitle("달토끼의 떡볶이"); // 프로그램 이름 지정
		//frame.setUndecorated(true);
		frame.setVisible(true); // 창 보이게하기
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체화면
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 엑스버튼을 누르면 종료
		
		cl = new CardLayout();
		frame.getContentPane().setLayout(cl); // 프레임을 카드레이아웃으로 변경
		
		// 패널에 main에 있는 리스너를 넣어줌
		startPanel = new StartPanel(this); 
		prologuePanel = new ProloguePanel(this);
		gamePanel = new GamePanel(this, frame, cl); // 새 게임 패널 생성
		gameOverPanel = new GameOverPanel(this);
		clearPanel = new ClearPanel(this);	
		
		// 모든 패널의 레이아웃을 null로 변환
		startPanel.setLayout(null);
		prologuePanel.setLayout(null);
		gamePanel.setLayout(null);
		gameOverPanel.setLayout(null);
		clearPanel.setLayout(null);
		
		// 프레임에 패널들을 추가한다.(카드 레이아웃을 위한 패널들)
		frame.getContentPane().add(startPanel, "start"); // start라는 이름으로 추가
		frame.getContentPane().add(prologuePanel, "prologue");
		frame.getContentPane().add(gamePanel, "game");
		frame.getContentPane().add(gameOverPanel, "gameover");
		frame.getContentPane().add(clearPanel, "clear");
		
		cl.show(frame.getContentPane(), "start"); // start패널을 카드레이아웃 최상단으로 변경
		startPanel.requestFocus(); // 리스너를 start패널에 강제로 줌
	}
	 
	@Override
	public void mousePressed(MouseEvent e) { // mouseClicked로 변경가능
			
		if (e.getComponent().getName().equals("StartButton")) { // StartButton이라는 이름을 가진 버튼을 눌렀다면
			frame.getContentPane().remove(prologuePanel); // 방금 했던 프롤로그 패널을 프레임에서 삭제
			prologuePanel = new ProloguePanel(this); // 새 프롤로그 패널 생성
			prologuePanel.setLayout(null);
			frame.getContentPane().add(prologuePanel, "prologue");
			cl.show(frame.getContentPane(), "prologue"); // prologue패널을 카드레이아웃 최상단으로 변경
			prologuePanel.requestFocus(); // 리스너를 prologue패널에 강제로 줌
			startPanel.closeMusic();
		} else if (e.getComponent().getName().equals("GameStartButton")) { // GameoverButton이라는 이름을 가진 버튼을 눌렀다면
			frame.getContentPane().remove(gamePanel); // 방금 했던 게임 패널을 프레임에서 삭제
			gamePanel = new GamePanel(this, frame, cl); // 새 게임 패널 생성
			gamePanel.setLayout(null);
			gamePanel.gameStart(); // 게임 시작 메서드 실행
			frame.getContentPane().add(gamePanel, "game");
			cl.show(frame.getContentPane(), "game"); // game패널을 카드레이아웃 최상단으로 변경
			gamePanel.requestFocus(); // 리스너를 game패널에 강제로 줌
		} else if (e.getComponent().getName().equals("GameOverReplayButton")) { // GameoverButton이라는 이름을 가진 버튼을 눌렀다면
			gameOverPanel.closeMusic();
			cl.show(frame.getContentPane(), "start"); // start패널을 카드레이아웃 최상단으로 변경
			startPanel.requestFocus(); // 리스너를 start패널에 강제로 줌
			startPanel.playMusic();
		} else if (e.getComponent().getName().equals("ClearReplayButton")) { // ClearButton이라는 이름을 가진 버튼을 눌렀다면
			clearPanel.closeMusic();
			cl.show(frame.getContentPane(), "start"); // start패널을 카드레이아웃 최상단으로 변경
			startPanel.requestFocus(); // 리스너를 start패널에 강제로 줌
			startPanel.playMusic();
		} else if (e.getComponent().getName().equals("ExitButton")) { // ExitButton이라는 이름을 가진 버튼을 눌렀다면
			System.exit(0); 	
		}
	}
	
}