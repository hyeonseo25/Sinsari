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

import components.GunMonster;
import components.Monster;
import components.MonsterThread;
import components.Player;
import components.Shot;
import components.Field;
import components.Tacle;
import components.Item;
import main.Main;
import util.Util;

public class GamePanel extends JPanel{
	private boolean keyLeft = false;
	private boolean keyRight = false;
	private boolean keyEnter = false;
	private int cnt=5; // 총알에 딜레이 주기 위한 cnt
	
	//시간 뒷 배경
	private ImageIcon textBackImage1 = new ImageIcon("images/backImage.png");
	private Image textBackImg1 = textBackImage1.getImage();
	
	// 점수 뒷 배경
	private ImageIcon textBackImage2 = new ImageIcon("images/backImage2.png");
	private Image textBackImg2 = textBackImage2.getImage();
	
	private Clip backgroundMusic;
	
	//패널 배경
	private ImageIcon backImg = new ImageIcon("images/게임패널배경.png");
	private Image back = backImg.getImage();
	
	//체력 이미지
	private ImageIcon hpImg = new ImageIcon("images/HP.png");
	private Image hp = hpImg.getImage();
	
	// 발판 이미지 아이콘들
	private ImageIcon field1Ic = new ImageIcon("images/map/발판.png"); // 발판
	private ImageIcon field2Ic = new ImageIcon("images/map/공중발판.png"); // 공중발판

	// 장애물 이미지 아이콘들
	private ImageIcon tacle10Ic = new ImageIcon("images/map/무당벌레장애물.gif"); // 1칸 장애물
	
	// 피격시 붉은 화면
	private ImageIcon redBg = new ImageIcon("images/map/redBg.png"); 
	
	// 아이템 이미지 아이콘들
	private ImageIcon item1Ic = new ImageIcon("images/map/머스캣드링크.png");
	private ImageIcon item2Ic = new ImageIcon("images/map/찐만두.png");
	private ImageIcon item3Ic = new ImageIcon("images/map/포켓치킨.png");
	private ImageIcon itemHPIc = new ImageIcon("images/HP.png");
		
	private int[] monsterSpawnpoint = {2250,2500,3350,4000,4190,4520,5570,5990,6080,6560,8240,8880,9460,10520,10760,11130,11430}; //몬스터 스폰 위치
	private int nowMonster = 0; // 지금까지 스폰된 몬스터의 수
	
	// 화면 사이즈 받아오기 
	private Dimension view = Toolkit.getDefaultToolkit().getScreenSize();
	
	private int field = 800;
	
	private int backX = 0;

	private String endTime; //게임 클리어 시간
	
	// 리스트 생성
	private List<Item> itemList; // 아이템 리스트
	private List<Field> fieldList; // 발판 리스트
	private List<Tacle> tacleList; // 장애물 리스트
	
	// 이미지 파일로 된 맵을 가져온다.
	private int[] sizeArr; // 이미지의 넓이와 높이를 가져오는 1차원 배열
	private int[][] colorArr; // 이미지의 x y 좌표의 픽셀 색값을 저장하는 2차원배열
		
	private int end = back.getWidth(null)-(view.width-1660); // 도착지
	
	private Player player;
	private Monster monster;
	private util.Timer time;
	
	// 다른 클래스 변수들
	private JFrame frame;
	private CardLayout cl;
	private Main main;
	
	public String getTime() {
		if (Integer.valueOf(time.getSeconds()) < 0) {
			gameOver();
		}
		return time.getSeconds() + "초";
	}
	
	public String getScore1() {
		return Integer.toString(player.getScore());
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public int getField() {
		return field;
	}

	public void setField(int field) {
		this.field = field;
	}

	public String getEndTime() {
		return this.endTime;
	}
	public String getScore() {
		return Integer.toString(player.getScore()) + "점";
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
		time = new util.Timer();
		time.start();
		player = new Player(this);

		player.fall(); // field 위에 플레이어가 있으면 떨어지게
		player.deleteShot(); // 화면 밖으로 나간 총알을 없애는 메서드
		monster = new Monster(player);
		monster.createMonsters(); // 프레임 생성시 Monster 객체들을 배열에 추가
		setCpField();
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
		tacleList = new ArrayList<>(); // 장애물 리스트
		
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

				}else if (colorArr[i][j] == 2273612) { // 색값이 2273612일 경우 Hp 회복 물약 생성
					// 좌표에 40을 곱하고, 넓이와 높이는 70으로 한다.
					itemList.add(new Item(itemHPIc.getImage(), i * 40, j * 40, 70, 70));
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
		} // end of for i

		for (int i = 0; i < maxX; i += 2) { // 장애물은 4칸을 차지하는 공간이기 때문에 2,2사이즈로 반복문을 돌린다.
			for (int j = 0; j < maxY; j += 2) {
				if (colorArr[i][j] == 15539236) { // 색값이 15539236일 경우 (빨간색) 1칸
					// 좌표에 40을 곱하고, 넓이와 높이는 80으로 한다.
					tacleList.add(new Tacle(tacle10Ic.getImage(), i * 40 , j * 40, 80, 80));
				}
			}
		} // end of for i
		
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
	}
	
	// 리스너 추가 메서드
	private void initListener() {
		addKeyListener(new KeyAdapter() { // 키 리스너 추가
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_A: keyLeft = true; break;
				case KeyEvent.VK_D: keyRight = true; break;
				case KeyEvent.VK_ENTER: keyEnter = true; break;
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
				case KeyEvent.VK_A: keyLeft = false; player.stop();break;
				case KeyEvent.VK_D: keyRight = false;  player.stop();break;
				case KeyEvent.VK_ENTER: keyEnter = false; break;
				}
			}
		});
	}
	
	public void keyCheck() {
		if(keyLeft==true) {				
			player.p_moveLeft();
			
		}else if(keyRight==true) {
			if(player.getDistance()+1100==monsterSpawnpoint[monster.getMonsterCnt()]&&nowMonster==monster.getMonsterCnt()) {
				monsterSpawn();
			}
			if(player.getDistance()>end) {
				clear();
			}else if(player.getDistance()>back.getWidth(null)-(view.width-700)) {
				player.p_moveRight();
			}else if(player.getX()>700) {  // 플레이어가 중간을 넘으면
				player.p_moveRight(1); // 매개변수는 오버로딩된 메서드를 실행 시키기 위함. 그 외 의미 없음
				movebg();
			}else {
				player.p_moveRight();
			}
		}
		
		if(keyEnter==true) {
			if(cnt==5) {
				Sound("music/shotSound.wav", false); // 총쏘는 소리
				player.p_hit();
				cnt = 0;
			}
		}
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
						setObject();
						if(cnt<5) {
							cnt++; // 총알에 딜레이
						}
						
						if(player.getY() - player.getImage().getHeight(null)>1100) {
							player.setHp(0);
						}
						
						if(player.getHp()<=0) {
							gameOver();	
							break;
						}
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
			g.drawImage(back, backX, 0, this);
			ArrayList<Shot> list = player.getShots();
			
			// 몬스터 그리기 
			for (int i = 0; i < monster.getMonsterList().size(); i++) {
				if(monster.getMonsterList().get(i) instanceof GunMonster){
					ArrayList<Shot> GunMonster_shotlist = ((GunMonster) monster.getMonsterList().get(i)).getShotList();
					for(int j=0; j<GunMonster_shotlist.size();j++) {
						g.drawImage(GunMonster_shotlist.get(j).getImage(), GunMonster_shotlist.get(j).getX(), GunMonster_shotlist.get(j).getY(), this);
					}
				}
				g.drawImage(monster.getMonsterList().get(i).getImage(), monster.getMonsterList().get(i).getX(), monster.getMonsterList().get(i).getY(), this);
			}
			
			// 총알 그리기
			for(int i=0; i<list.size();i++) {
				g.drawImage(list.get(i).getImage(), list.get(i).getX(), list.get(i).getY(), this);
			}
			
			Graphics2D g2 = (Graphics2D)g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) player.getInvincibility()/255));
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 255 / 255));
			
			// 체력 그리기 
			for(int i=0; i<player.getHp()/200; i++) {
				g.drawImage(hp, 10+i*70, 10, this);
			}
			
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
			
			// 장애물 그리기 
			for (int i = 0; i < tacleList.size(); i++) {
				Tacle tempTacle = tacleList.get(i);
				if (tempTacle.getX() > -90 && tempTacle.getX() < view.getWidth()) {
					g.drawImage(tempTacle.getImage(), tempTacle.getX(), tempTacle.getY(), tempTacle.getWidth(), tempTacle.getHeight(), null);
				}
			}
			
			// 글씨 잘보이게 하기 위한 흰 뒷 배경
			g.drawImage(textBackImg1, (view.width/2 - textBackImg1.getWidth(null)/2), 11, this);
			g.drawImage(textBackImg2, 1685, 11, this);
			
			Font font = new Font("돋움", Font.BOLD, 40);
			g.setFont(font);  //타이머 글씨체
			g.drawString(getTime(), (view.width/2 - textBackImg1.getWidth(null)/2) + 22, 50); // 타이머 그리기
			g.drawString(getScore(), 1750, 50); // 점수 그리기
		}
		
		// 게임 오브젝트 배치 
		public void setObject() {
			int face = player.getX() + player.getImage().getWidth(null); // 캐릭터 정면 위치 재스캔
			int foot = player.getY() + player.getImage().getHeight(null); // 캐릭터 발 위치 재스캔
			
			for (int i = 0; i < tacleList.size(); i++) {
				Tacle tempTacle = tacleList.get(i); // 임시 변수에 리스트 안에 있는 개별 장애물을 불러오자
				if ( // 무적상태가 아니고 캐릭터의 범위 안에 장애물이 있으면 부딛힌다
						player.getInvincibility() == 255
							&& tempTacle.getX() + tempTacle.getWidth() / 2 >= player.getX()
							&& tempTacle.getX() + tempTacle.getWidth() / 2 <= face
							&& tempTacle.getY() + tempTacle.getHeight() / 2 >= player.getY()
							&& tempTacle.getY() + tempTacle.getHeight() / 2 <= foot) {
						player.damaged(200);
	
					} else if ( // 공중장애물
						player.getInvincibility() == 255
							&& tempTacle.getX() + tempTacle.getWidth() / 2 >= player.getX()
							&& tempTacle.getX() + tempTacle.getWidth() / 2 <= face
							&& tempTacle.getY() <= player.getY()
							&& tempTacle.getY() + tempTacle.getHeight() * 95 / 100 > player.getY()) {
						player.damaged(200);
					}
			}
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
							player.setScore(player.getScore()+30); // 총점수에 젤리 점수를 더한다
						}else if(tempitem.getImage()==item2Ic.getImage()) {
							Sound("music/eatItemSound.wav", false);
							player.setScore(player.getScore()+100); // 총점수에 젤리 점수를 더한다
						}else if (tempitem.getImage() == itemHPIc.getImage()) {
							Sound("music/eatHpSound.wav", false);
							if ((player.getHp() + 200) > 1000) {
								player.setScore(player.getScore()+100);
							} else {
								player.setHp(player.getHp() + 200);
							}
						}
						tempitem.setImage(null); // 아이템의 이미지를 이펙트로 바꾼다
					}
				} // end of first if 
			} //end of for  
			setCpField(1); // 플레이어 필드 설정
			setCpField(2); // 몬스터 필드 설정
	}
		
	//패널 전용 스레드
	public void movebg() {
		backX -=10;
		for (int i = 0; i < monster.getMonsterList().size(); i++) {
			monster.getMonsterList().get(i).m_move(15);	
		}
		
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
		// 장애물위치를 - 4 씩 해준다.
		for (int i = 0; i < tacleList.size(); i++) {
			Tacle tempTacle = tacleList.get(i); // 임시 변수에 리스트 안에 있는 개별 장애물을 불러오자
			if (tempTacle.getX() < -90) {
				tacleList.remove(tempTacle); // 장애물의 x 좌표가 -90 미만이면 해당 젤리를 제거한다.(최적화)
			} else {
				tempTacle.setX(tempTacle.getX() - 10); // 위 조건에 해당이 안되면 x좌표를 줄이자
			}
		}
	}
	
	public void gameOver() {
		closeMusic();
		time.interrupt();
		Sound("music/dieMusic.wav", false);
		for (int i = 0; i < monster.getMonsterList().size(); i++) {
			monster.getMonsterList().get(i).setPlayer(null);
		}
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
		time.interrupt();
		Sound("music/clearMusic.wav", false);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		main.getClearPanel().setName("이름을 입력해주세요");
		player.setScore(player.getScore()+Integer.valueOf(time.getSeconds())*10+player.getHp()/200*100);
		main.getClearPanel().setScore(player.getScore());
		cl.show(frame.getContentPane(), "clear");
		frame.requestFocus();
		setEndTime(getTime()); //게임 클리어 시간
	}
	
	// 컴포넌트 필드 초기 설정 (플레이어, 몬스터)
	public void setCpField() {
		player.setField(this.field);
		for (int i = 0; i < monster.getMonsterList().size(); i++) {
			monster.getMonsterList().get(i).setField(this.field);
		}
	}
	
	// 컴포넌트 필드  설정 (플레이어, 몬스터) 1이면 플레이어, 2면 몬스터
	public void setCpField(int cp) {
		if(cp==1) {
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
			}
			field = tempNowField; // 결과를 field에 업데이트 한다.
			player.setField(this.field);
		}else if(cp==2) {
			for (int j = 0; j < monster.getMonsterList().size(); j++) {
				MonsterThread m = monster.getMonsterList().get(j);
				int face = m.getX() + m.getImage().getWidth(null); // 몬스터 정면 위치 재스캔
				int foot = m.getY() + m.getImage().getHeight(null); // 몬스터 발 위치 재스캔
				// 몬스터가 밟을 발판을 계산하는 코드
				
				int tempField; // 발판위치를 계속 스캔하는 지역변수
				int tempNowField=2000; // 몬스터와 발판의 높이에 따라 저장되는 지역변수, 결과를 field에 저장한다
				
				for (int i = 0; i < fieldList.size(); i++) { // 발판의 개수만큼 반복
					int tempX = fieldList.get(i).getX(); // 발판의 x값
					if (tempX > m.getX() - 60 && tempX <= face) { // 발판이 몬스터 범위 안이라면
						tempField = fieldList.get(i).getY(); // 발판의 y값을 tempField에 저장한다
						foot = m.getY() + m.getImage().getHeight(null); // 몬스터 발 위치 재스캔
						// 발판위치가 tempNowField보다 높고, 발바닥 보다 아래 있다면
						// 즉, 캐릭터 발 아래에 제일 높이 있는 발판이라면 tempNowField에 저장한다.
						if (tempField < tempNowField && tempField >= foot) {
							tempNowField = tempField;
						}
					}
				}
				m.setField(tempNowField);
			}
		}
	}// end of setCpField(int cp)
		
	public void monsterSpawn() {
		monster.addMonster();
		nowMonster++;
	}
}