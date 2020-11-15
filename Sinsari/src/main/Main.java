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
	// 필요한 메서드를 그때그때 오버라이드 하기 위해서 따로 추상클래스를 빼서 상속받은듯!
	// 여기서 모두 implement 받아두면 안 쓰는 메서드도 오버라이드 해야 함
	private JFrame frame;
	
//	private StartPanel startPanel; 			// 시작패널
//	private GamePanel gamePanel; 			// 게임패널
//	private GameOverPanel gameOverPanel; 	// 게임오버패널
//	private ClearPanel clearPanel; 			// 클리어패널
//	private RankingPanel rankingPanel; 		// 랭킹 패널
	
	private CardLayout cl; // 카드 레이아웃 - 패널 여러개를 돌려가며 보여줄수 있게 해줌
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
	}
	
	public Main() {
		init();
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
//		startPanel = new StartPanel(this); 
//		gamePanel = new GamePanel(this, frame, cl); // 새 게임 패널 생성
//		gameOverPanel = new GameOverPanel(this);
//		clearPanel = new ClearPanel(this);
//		rankingPanel = new RankingPanel(this);
		
		// 모든 패널의 레이아웃을 null로 변환
//		startPanel.setLayout(null);
//		gamePanel.setLayout(null);
//		gameOverPanel.setLayout(null);
//		clearPanel.setLayout(null);
//		rankingPanel.setLayout(null);
		
		// 프레임에 패널들을 추가한다.(카드 레이아웃을 위한 패널들)
//		frame.getContentPane().add(startPanel, "start");
//		frame.getContentPane().add(gamePanel, "game");
//		frame.getContentPane().add(gameOverPanel, "gameover");
//		frame.getContentPane().add(clearPanel, "clear");
//		frame.getContentPane().add(rankingPanel, "ranking");
		
		cl.show(frame.getContentPane(), "start"); // start패널을 카드레이아웃 최상단으로 변경
		//startPanel.requestFocus(); // 리스너를 start패널에 강제로 줌
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) { // mouseClicked로 변경가능
			
//		if (e.getComponent().getName().equals("StartButton")) { // StartButton이라는 이름을 가진 버튼을 눌렀다면
//			frame.getContentPane().remove(gamePanel); // 방금 했던 게임 패널을 프레임에서 삭제
//			gamePanel = new GamePanel(this, frame, cl); // 새 게임 패널 생성
//			gamePanel.setLayout(null);
//			gamePanel.gameStart();
//			frame.getContentPane().add(gamePanel, "game");
//			cl.show(frame.getContentPane(), "game"); // game패널을 카드레이아웃 최상단으로 변경
//			gamePanel.requestFocus(); // 리스너를 game패널에 강제로 줌
//			
//		} else if (e.getComponent().getName().equals("RankingButton")) { // RankingButton이라는 이름을 가진 버튼을 눌렀다면
//			cl.show(frame.getContentPane(), "ranking"); // ranking패널을 카드레이아웃 최상단으로 변경
//			rankingPanel.requestFocus(); // 리스너를 ranking패널에 강제로 줌
//			
//		} else if (e.getComponent().getName().equals("GameoverButton")) { // GameoverButton이라는 이름을 가진 버튼을 눌렀다면
//			cl.show(frame.getContentPane(), "gameover"); // gameover패널을 카드레이아웃 최상단으로 변경
//			gameOverPanel.requestFocus(); // 리스너를 gameOver패널에 강제로 줌
//			gamePanel.closeMusic(); // 음악 재생 중지
//			
//		} else if (e.getComponent().getName().equals("ClearButton")) { // ClearButton이라는 이름을 가진 버튼을 눌렀다면
//			cl.show(frame.getContentPane(), "clear"); // clear패널을 카드레이아웃 최상단으로 변경
//			clearPanel.requestFocus(); // 리스너를 clear패널에 강제로 줌	
//			gamePanel.closeMusic(); // 음악 재생 중지
//			
//		} else if (e.getComponent().getName().equals("ReplayButton")) { // ReplayButton이라는 이름을 가진 버튼을 눌렀다면
//			cl.show(frame.getContentPane(), "start"); // start패널을 카드레이아웃 최상단으로 변경
//			startPanel.requestFocus(); // 리스너를 start패널에 강제로 줌
//		} else if (e.getComponent().getName().equals("ExitButton")) { // ReplayButton이라는 이름을 가진 버튼을 눌렀다면
//			System.exit(0); 
//		}
	}
	
}