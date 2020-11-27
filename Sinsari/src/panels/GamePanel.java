package panels;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import components.Player;
import components.Field;
import components.Item;
import main.Main;
import util.Util;

public class GamePanel extends JPanel{
	
	// 점수 뒷 배경
	private ImageIcon textBackImage2 = new ImageIcon("images/점수배경.png");
	private Image textBackImg2 = textBackImage2.getImage();
	
	private Clip backgroundMusic;
	
	//패널 배경
	private ImageIcon backImg = new ImageIcon("images/게임패널배경.png");
	private Image back = backImg.getImage();
	
	//패널 배경
	private ImageIcon backImg2 = new ImageIcon("images/게임패널배경2.jpg");
	private Image back2 = backImg2.getImage();
	
	// 발판 이미지 아이콘들
	private ImageIcon field1Ic = new ImageIcon("images/map/발판.png"); // 발판
	private ImageIcon field2Ic = new ImageIcon("images/map/공중발판.png"); // 공중발판
	
	// 피격시 붉은 화면
	private ImageIcon redBg = new ImageIcon("images/map/redBg.png"); 
	
	// 아이템 이미지 아이콘들
	private ImageIcon item1Ic = new ImageIcon("images/map/떡.png");
	private ImageIcon item2Ic = new ImageIcon("images/map/어묵.png");
	private ImageIcon item3Ic = new ImageIcon("images/map/고추장.png");
	
	private Image item1Im = item1Ic.getImage();
	private Image item2Im = item2Ic.getImage();
	private Image item3Im = item3Ic.getImage();
	
	// 화면 사이즈 받아오기 
	private Dimension view = Toolkit.getDefaultToolkit().getScreenSize();
	
	private int field = 800;
	
	private int backX = 0;
	
	// 리스트 생성
	private List<Item> itemList; // 아이템 리스트
	private List<Field> fieldList; // 발판 리스트
	
	// 이미지 파일로 된 맵을 가져온다.
	private int[] sizeArr; // 이미지의 넓이와 높이를 가져오는 1차원 배열
	private int[][] colorArr; // 이미지의 x y 좌표의 픽셀 색값을 저장하는 2차원배열
		
	private int end = back.getWidth(null); // 도착지
	
	private Player player;
	
	// 다른 클래스 변수들
	private JFrame frame;
	private CardLayout cl;
	private Main main;
	
	public int getField() {
		return field;
	}

	public void setField(int field) {
		this.field = field;
	}

	public int getHp() {
		return player.getHp();
	}
	public void setBackX(int backX) {
		this.backX = backX;
	}
	
	public int getBackX() {
		return backX;
	}
	
	public GamePanel(Object o, JFrame frame, CardLayout cl) {
		this.frame = frame;
		this.cl = cl;
		this.main = (Main)o;
		playGame();
	}
	
	public void gameStart() {
		player = new Player(this);
		player.fall(); // field 위에 플레이어가 있으면 떨어지게
		player.setField(this.field);
		playerRun();
		repaintThread();
		playMusic();
	}
	
	private void playGame() {
		setFocusable(true);
		initListener();
		initMap(1);
	}
	
	// 맵의 구조를 그림판 이미지를 받아서 세팅
	private void initMap(int num) {
		itemList = new ArrayList<>(); // 아이템 리스트
		fieldList = new ArrayList<>(); // 발판 리스트
		
		String tempMap = null;
		
		if (num == 1) {
			tempMap = "images/map/맵배치.png";
		}

		// 맵 정보 불러오기
		try {
			sizeArr = Util.getSize(tempMap); // 맵 사이즈를 배열에 저장
			colorArr = Util.getPic(tempMap); // 맵 픽셀값을 배열에 저장
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int maxX = sizeArr[0]; // 맵의 넓이
		int maxY = sizeArr[1]; // 맵의 높이

		for (int i = 0; i < maxX; i += 1) { // 아이템은 1칸을 차지하기 때문에 1,1사이즈로 반복문을 돌린다.
			for (int j = 0; j < maxY; j += 1) {
				if (colorArr[i][j] == 16756425) { // 색값이 16756425일 경우 머스캣드링크 아이템 생성
					// 좌표에 40을 곱하고, 넓이와 높이는 70으로 한다.
					itemList.add(new Item(item1Ic.getImage(), i * 40, j * 40, 70, 70));
					
				} else if (colorArr[i][j] == 11731002) { // 색값이 11731002일 경우 찐만두 아이템 생성
					// 좌표에 40을 곱하고, 넓이와 높이는 70으로 한다.
					itemList.add(new Item(item2Ic.getImage(), i * 40, j * 40, 70, 70));
					
				} else if (colorArr[i][j] == 10882462) { // 색값이 10882462일 경우 포켓치킨 아이템 생성
					// 좌표에 40을 곱하고, 넓이와 높이는 70으로 한다.
					itemList.add(new Item(item3Ic.getImage(), i * 40, j * 40, 70, 70));

				}
			} // end of for j
		} //end of for i
		
		for (int i = 0; i < maxX; i += 2) { // 발판은 4칸을 차지하는 공간이기 때문에 2,2사이즈로 반복문을 돌린다.
			for (int j = 0; j < maxY; j += 2) {
				if (colorArr[i][j] == 0) { // 색값이 0 일경우 (검은색)
					// 좌표에 40을 곱하고, 넓이와 높이는 80,100으로 한다.
					fieldList.add(new Field(field1Ic.getImage(), i * 40 , j * 40, 80, 100));

				} else if (colorArr[i][j] == 12829635) { // 색값이 12829635 일경우 (회색)
					// 좌표에 40을 곱하고, 넓이와 높이는 80으로 한다.
					fieldList.add(new Field(field2Ic.getImage(), i*40 , j * 40, 80, 80));
				}
			}
		} 
		
	}
	
	private void playMusic() {
		 try {
			 File file = new File("music/backgroundMusic.wav");
			 AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			 backgroundMusic = AudioSystem.getClip();
			 backgroundMusic.open(stream);
			 backgroundMusic.start();
			 backgroundMusic.loop(-1);
	     } catch(Exception e) {
	    	 e.printStackTrace();
	     }	
	}
	public void closeMusic() {
		backgroundMusic.close();
	}
	public void Sound(String file, boolean Loop){
		//사운드재생용메소드
		//사운드파일을받아들여해당사운드를재생시킨다.
		Clip clip;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if (Loop) clip.loop(-1);
			//Loop 값이true면 사운드재생을무한반복시킵니다.
			//false면 한번만재생시킵니다.
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * 📢BGM
			✔️Track - PerituneMaterial - Holiday4
			✔️Soundcloud - https://soundcloud.com/sei_peridot
			✔️나눔뮤직 - https://tv.naver.com/v/11875470
		 */ 
	}
	
	// 리스너 추가 메서드
	private void initListener() {
		addKeyListener(new KeyAdapter() { // 키 리스너 추가
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_SPACE: 
					if(player.getCountJump() < 2) {
						player.jump();
						Sound("music/jumpSound.wav", false);
					}
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				//case KeyEvent.VK_ENTER: keyEnter = false; break;
				}
			}
		});
	}
	
	public void keyCheck() {	
		
	}
	
	public void playerRun() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						if(player.getDistance()>end) {
							if(player.getItem1()>=10&&player.getItem2()>=10&&player.getItem3()>=10) {
								clear();
							}else{
								gameOver();
							}
							break;
						}
						if(player.getHp()<=0) {
							break;
						}
						if(player.getDistance()>back.getWidth(null)-(view.width-700)) {
							player.p_moveRight();
						}else if(player.getX()>700) {  // 플레이어가 중간을 넘으면
							player.p_moveRight(1); // 매개변수는 오버로딩된 메서드를 실행 시키기 위함. 그 외 의미 없음
							movebg();
						}else {
							player.p_moveRight();
						}
						Thread.sleep(20);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	//몬스터가 움직임
	public void repaintThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					repaint();
					
					try {
						keyCheck();
						if(player.getY() - player.getImage().getHeight(null)>1100) {
							player.damaged();
							player.setHp(0);
						}
						if(player.getHp()<=0) {
							gameOver();	
							break;
						}
						setObject();	
						Thread.sleep(40);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	//패널에 그리기
		public void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			g.drawImage(back2, backX, 0, this);
			
			Graphics2D g2 = (Graphics2D)g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) player.getInvincibility()/255));
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 255 / 255));
			
			if (player.getInvincibility() == 80) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 125 / 255));
				g.drawImage(redBg.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 255 / 255));
			}
			
			// 필드 그리기 
			for (int i = 0; i < fieldList.size(); i++) {
				Field tempFoot = fieldList.get(i);
				// 사양을 덜 잡아먹게 하기위한 조치
				if (tempFoot.getX() > -90 && tempFoot.getX() < view.getWidth()) { // x값이 -90~810인 객체들만 그린다.
					g.drawImage(tempFoot.getImage(), tempFoot.getX(), tempFoot.getY(), tempFoot.getWidth(), tempFoot.getHeight(), null);
				}
			}
			
			//아이템 그리기 
			for (int i = 0; i < itemList.size(); i++) {
				Item tempitem = itemList.get(i);
				if (tempitem.getX() > -90 && tempitem.getX() < view.getWidth()) {
					g.drawImage(tempitem.getImage(), tempitem.getX(), tempitem.getY(), tempitem.getWidth(), tempitem.getHeight(), null);
				}
			}
			g.drawImage(textBackImg2, 1650, 11, 250, 300,  this);
			
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
			g.drawImage(item1Im, 1800, 30, 80, 80, this);
			g.drawImage(item2Im, 1800, 120, 80, 80, this);
			g.drawImage(item3Im, 1800, 210, 80, 80, this);
			
			Font font = new Font("돋움", Font.BOLD, 40);
			g.setFont(font);  //타이머 글씨체
			g.drawString(Integer.toString(player.getItem1()), 1670, 80); // 1번아이템
			g.drawString(Integer.toString(player.getItem2()), 1670, 170); // 2번아이템
			g.drawString(Integer.toString(player.getItem3()), 1670, 260); // 3번아이템
			
			g.drawString("/10", 1720, 80); // 1번아이템
			g.drawString("/10", 1720, 170); // 2번아이템
			g.drawString("/10", 1720, 260); // 3번아이템
//			// 글씨 잘보이게 하기 위한 흰 뒷 배경
//			g.drawImage(textBackImg2, 1685, 11, this);
			
//			Font font = new Font("돋움", Font.BOLD, 40);
//			g.setFont(font);  //타이머 글씨체
//			g.drawString(getScore(), 1750, 50); // 점수 그리기
		}
		
		// 게임 오브젝트 배치 
		public void setObject() {
			int face = player.getX() + player.getImage().getWidth(null); // 캐릭터 정면 위치 재스캔
			int foot = player.getY() + player.getImage().getHeight(null); // 캐릭터 발 위치 재스캔
			
			for (int i = 0; i < itemList.size(); i++) {
				Item tempitem = itemList.get(i); // 임시 변수에 리스트 안에 있는 개별 아이템을 불러오자
				if ( // 캐릭터의 범위 안에 아이템이 있으면 아이템을 먹는다.
					tempitem.getX() + tempitem.getWidth() * 20 / 100 >= player.getX()
							&& tempitem.getX() + tempitem.getWidth() * 80 / 100 <= face
							&& tempitem.getY() + tempitem.getWidth() * 20 / 100 >= player.getY()
							&& tempitem.getY() + tempitem.getWidth() * 80 / 100 <= foot
							) {
					if(tempitem.getImage()!=null) {
						if(tempitem.getImage()==item1Ic.getImage()) {
							Sound("music/eatItemSound.wav", false);
							player.setItem1(player.getItem1()+1);
						}else if(tempitem.getImage()==item2Ic.getImage()) {
							Sound("music/eatItemSound.wav", false);
							player.setItem2(player.getItem2()+1);
						}else if(tempitem.getImage()==item3Ic.getImage()) {
							Sound("music/eatItemSound.wav", false);
							player.setItem3(player.getItem3()+1);
						}
						tempitem.setImage(null); // 아이템의 이미지를 이펙트로 바꾼다
					}
				} // end of first if 
			} //end of for  
			setCpField(); // 플레이어 필드 설정
	}
		
	//패널 전용 스레드
	public void movebg() {
		backX -=1;
		
		// 아이템 위치를 -10 씩 해준다. (왼쪽으로 흐르는 효과)
		for (int i = 0; i < itemList.size(); i++) {
			Item tempitem = itemList.get(i); // 임시 변수에 리스트 안에 있는 개별 아이템을 불러오자
			if (tempitem.getX() < -90) { // 아이템의 x 좌표가 -90 미만이면 해당 아이템을 제거한다.(최적화)
				itemList.remove(tempitem);
			}else {
				tempitem.setX(tempitem.getX() - 10); // 위 조건에 해당이 안되면 x좌표를 줄이자
			}
		}
		
		// 발판위치를 -10 씩 해준다. (왼쪽으로 흐르는 효과)
		for (int i = 0; i < fieldList.size(); i++) {
			Field tempField = fieldList.get(i); // 임시 변수에 리스트 안에 있는 개별 발판을 불러오자
			if (tempField.getX() < -90) { // 발판의 x좌표가 -90 미만이면 해당 발판을 제거한다.(최적화)
				fieldList.remove(tempField);
			} else {
				tempField.setX(tempField.getX() - 10); // 위 조건에 해당이 안되면 x좌표를 줄이자
			}
		}
	}
	
	public void gameOver() {
		closeMusic();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cl.show(frame.getContentPane(), "gameover");
		main.getGameOverPanel().playMusic();
		frame.requestFocus();
	}
	
	public void clear() {
		closeMusic();
		player.setDistance(200);
		Sound("music/clearMusic.wav", false);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		main.getClearPanel().playMusic();
		cl.show(frame.getContentPane(), "clear");
		frame.requestFocus();
	}
	
	// 컴포넌트 필드  설정 (플레이어, 몬스터) 1이면 플레이어
	public void setCpField() {
		int face = player.getX() + player.getImage().getWidth(null); // 캐릭터 정면 위치 재스캔
		int foot = player.getY() + player.getImage().getHeight(null); // 캐릭터 발 위치 재스캔
		
		// 쿠키가 밟을 발판을 계산하는 코드
		int tempField; // 발판위치를 계속 스캔하는 지역변수
		int tempNowField=2000; // 캐릭터와 발판의 높이에 따라 저장되는 지역변수, 결과를 field에 저장한다

		for (int i = 0; i < fieldList.size(); i++) { // 발판의 개수만큼 반복
			int tempX = fieldList.get(i).getX(); // 발판의 x값
			if (tempX > player.getX() - 60 && tempX <= face) { // 발판이 캐릭 범위 안이라면
				tempField = fieldList.get(i).getY(); // 발판의 y값을 tempField에 저장한다
				foot = player.getY() + player.getImage().getHeight(null); // 캐릭터 발 위치 재스캔
				// 발판위치가 tempNowField보다 높고, 발바닥 보다 아래 있다면
				// 즉, 캐릭터 발 아래에 제일 높이 있는 발판이라면 tempNowField에 저장한다.
				if (tempField < tempNowField && tempField >= foot) {
					tempNowField = tempField;
				}
			}
		field = tempNowField; // 결과를 field에 업데이트 한다.
		player.setField(this.field);
		}
	}// end of setCpField(int cp)
}