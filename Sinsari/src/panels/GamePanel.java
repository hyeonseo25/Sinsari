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
	
	// ���� �� ���
	private ImageIcon textBackImage2 = new ImageIcon("images/�������.png");
	private Image textBackImg2 = textBackImage2.getImage();
	
	private Clip backgroundMusic;
	
	//�г� ���
	private ImageIcon backImg = new ImageIcon("images/�����гι��.png");
	private Image back = backImg.getImage();
	
	//�г� ���
	private ImageIcon backImg2 = new ImageIcon("images/�����гι��2.jpg");
	private Image back2 = backImg2.getImage();
	
	// ���� �̹��� �����ܵ�
	private ImageIcon field1Ic = new ImageIcon("images/map/����.png"); // ����
	private ImageIcon field2Ic = new ImageIcon("images/map/���߹���.png"); // ���߹���
	
	// �ǰݽ� ���� ȭ��
	private ImageIcon redBg = new ImageIcon("images/map/redBg.png"); 
	
	// ������ �̹��� �����ܵ�
	private ImageIcon item1Ic = new ImageIcon("images/map/��.png");
	private ImageIcon item2Ic = new ImageIcon("images/map/�.png");
	private ImageIcon item3Ic = new ImageIcon("images/map/������.png");
	
	private Image item1Im = item1Ic.getImage();
	private Image item2Im = item2Ic.getImage();
	private Image item3Im = item3Ic.getImage();
	
	// ȭ�� ������ �޾ƿ��� 
	private Dimension view = Toolkit.getDefaultToolkit().getScreenSize();
	
	private int field = 800;
	
	private int backX = 0;
	
	// ����Ʈ ����
	private List<Item> itemList; // ������ ����Ʈ
	private List<Field> fieldList; // ���� ����Ʈ
	
	// �̹��� ���Ϸ� �� ���� �����´�.
	private int[] sizeArr; // �̹����� ���̿� ���̸� �������� 1���� �迭
	private int[][] colorArr; // �̹����� x y ��ǥ�� �ȼ� ������ �����ϴ� 2�����迭
		
	private int end = back.getWidth(null); // ������
	
	private Player player;
	
	// �ٸ� Ŭ���� ������
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
		player.fall(); // field ���� �÷��̾ ������ ��������
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
	
	// ���� ������ �׸��� �̹����� �޾Ƽ� ����
	private void initMap(int num) {
		itemList = new ArrayList<>(); // ������ ����Ʈ
		fieldList = new ArrayList<>(); // ���� ����Ʈ
		
		String tempMap = null;
		
		if (num == 1) {
			tempMap = "images/map/�ʹ�ġ.png";
		}

		// �� ���� �ҷ�����
		try {
			sizeArr = Util.getSize(tempMap); // �� ����� �迭�� ����
			colorArr = Util.getPic(tempMap); // �� �ȼ����� �迭�� ����
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int maxX = sizeArr[0]; // ���� ����
		int maxY = sizeArr[1]; // ���� ����

		for (int i = 0; i < maxX; i += 1) { // �������� 1ĭ�� �����ϱ� ������ 1,1������� �ݺ����� ������.
			for (int j = 0; j < maxY; j += 1) {
				if (colorArr[i][j] == 16756425) { // ������ 16756425�� ��� �ӽ�Ĺ�帵ũ ������ ����
					// ��ǥ�� 40�� ���ϰ�, ���̿� ���̴� 70���� �Ѵ�.
					itemList.add(new Item(item1Ic.getImage(), i * 40, j * 40, 70, 70));
					
				} else if (colorArr[i][j] == 11731002) { // ������ 11731002�� ��� �𸸵� ������ ����
					// ��ǥ�� 40�� ���ϰ�, ���̿� ���̴� 70���� �Ѵ�.
					itemList.add(new Item(item2Ic.getImage(), i * 40, j * 40, 70, 70));
					
				} else if (colorArr[i][j] == 10882462) { // ������ 10882462�� ��� ����ġŲ ������ ����
					// ��ǥ�� 40�� ���ϰ�, ���̿� ���̴� 70���� �Ѵ�.
					itemList.add(new Item(item3Ic.getImage(), i * 40, j * 40, 70, 70));

				}
			} // end of for j
		} //end of for i
		
		for (int i = 0; i < maxX; i += 2) { // ������ 4ĭ�� �����ϴ� �����̱� ������ 2,2������� �ݺ����� ������.
			for (int j = 0; j < maxY; j += 2) {
				if (colorArr[i][j] == 0) { // ������ 0 �ϰ�� (������)
					// ��ǥ�� 40�� ���ϰ�, ���̿� ���̴� 80,100���� �Ѵ�.
					fieldList.add(new Field(field1Ic.getImage(), i * 40 , j * 40, 80, 100));

				} else if (colorArr[i][j] == 12829635) { // ������ 12829635 �ϰ�� (ȸ��)
					// ��ǥ�� 40�� ���ϰ�, ���̿� ���̴� 80���� �Ѵ�.
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
		//���������޼ҵ�
		//�����������޾Ƶ鿩�ش���带�����Ų��.
		Clip clip;
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if (Loop) clip.loop(-1);
			//Loop ����true�� ������������ѹݺ���ŵ�ϴ�.
			//false�� �ѹ��������ŵ�ϴ�.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ������ �߰� �޼���
	private void initListener() {
		addKeyListener(new KeyAdapter() { // Ű ������ �߰�
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
						}else if(player.getX()>700) {  // �÷��̾ �߰��� ������
							player.p_moveRight(1); // �Ű������� �����ε��� �޼��带 ���� ��Ű�� ����. �� �� �ǹ� ����
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
	//���Ͱ� ������
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
	
	//�гο� �׸���
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
			
			// �ʵ� �׸��� 
			for (int i = 0; i < fieldList.size(); i++) {
				Field tempFoot = fieldList.get(i);
				// ����� �� ��Ƹ԰� �ϱ����� ��ġ
				if (tempFoot.getX() > -90 && tempFoot.getX() < view.getWidth()) { // x���� -90~810�� ��ü�鸸 �׸���.
					g.drawImage(tempFoot.getImage(), tempFoot.getX(), tempFoot.getY(), tempFoot.getWidth(), tempFoot.getHeight(), null);
				}
			}
			
			//������ �׸��� 
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
			
			Font font = new Font("����", Font.BOLD, 40);
			g.setFont(font);  //Ÿ�̸� �۾�ü
			g.drawString(Integer.toString(player.getItem1()), 1670, 80); // 1��������
			g.drawString(Integer.toString(player.getItem2()), 1670, 170); // 2��������
			g.drawString(Integer.toString(player.getItem3()), 1670, 260); // 3��������
			
			g.drawString("/10", 1720, 80); // 1��������
			g.drawString("/10", 1720, 170); // 2��������
			g.drawString("/10", 1720, 260); // 3��������
//			// �۾� �ߺ��̰� �ϱ� ���� �� �� ���
//			g.drawImage(textBackImg2, 1685, 11, this);
			
//			Font font = new Font("����", Font.BOLD, 40);
//			g.setFont(font);  //Ÿ�̸� �۾�ü
//			g.drawString(getScore(), 1750, 50); // ���� �׸���
		}
		
		// ���� ������Ʈ ��ġ 
		public void setObject() {
			int face = player.getX() + player.getImage().getWidth(null); // ĳ���� ���� ��ġ �罺ĵ
			int foot = player.getY() + player.getImage().getHeight(null); // ĳ���� �� ��ġ �罺ĵ
			
			for (int i = 0; i < itemList.size(); i++) {
				Item tempitem = itemList.get(i); // �ӽ� ������ ����Ʈ �ȿ� �ִ� ���� �������� �ҷ�����
				if ( // ĳ������ ���� �ȿ� �������� ������ �������� �Դ´�.
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
						tempitem.setImage(null); // �������� �̹����� ����Ʈ�� �ٲ۴�
					}
				} // end of first if 
			} //end of for  
			setCpField(); // �÷��̾� �ʵ� ����
	}
		
	//�г� ���� ������
	public void movebg() {
		backX -=1;
		
		// ������ ��ġ�� -10 �� ���ش�. (�������� �帣�� ȿ��)
		for (int i = 0; i < itemList.size(); i++) {
			Item tempitem = itemList.get(i); // �ӽ� ������ ����Ʈ �ȿ� �ִ� ���� �������� �ҷ�����
			if (tempitem.getX() < -90) { // �������� x ��ǥ�� -90 �̸��̸� �ش� �������� �����Ѵ�.(����ȭ)
				itemList.remove(tempitem);
			}else {
				tempitem.setX(tempitem.getX() - 10); // �� ���ǿ� �ش��� �ȵǸ� x��ǥ�� ������
			}
		}
		
		// ������ġ�� -10 �� ���ش�. (�������� �帣�� ȿ��)
		for (int i = 0; i < fieldList.size(); i++) {
			Field tempField = fieldList.get(i); // �ӽ� ������ ����Ʈ �ȿ� �ִ� ���� ������ �ҷ�����
			if (tempField.getX() < -90) { // ������ x��ǥ�� -90 �̸��̸� �ش� ������ �����Ѵ�.(����ȭ)
				fieldList.remove(tempField);
			} else {
				tempField.setX(tempField.getX() - 10); // �� ���ǿ� �ش��� �ȵǸ� x��ǥ�� ������
			}
		}
	}
	
	public void gameOver() {
		closeMusic();
		try {
			TimeUnit.SECONDS.sleep(1);
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
	
	// ������Ʈ �ʵ�  ���� (�÷��̾�, ����) 1�̸� �÷��̾�
	public void setCpField() {
		int face = player.getX() + player.getImage().getWidth(null); // ĳ���� ���� ��ġ �罺ĵ
		int foot = player.getY() + player.getImage().getHeight(null); // ĳ���� �� ��ġ �罺ĵ
		
		// ��Ű�� ���� ������ ����ϴ� �ڵ�
		int tempField; // ������ġ�� ��� ��ĵ�ϴ� ��������
		int tempNowField=2000; // ĳ���Ϳ� ������ ���̿� ���� ����Ǵ� ��������, ����� field�� �����Ѵ�

		for (int i = 0; i < fieldList.size(); i++) { // ������ ������ŭ �ݺ�
			int tempX = fieldList.get(i).getX(); // ������ x��
			if (tempX > player.getX() - 60 && tempX <= face) { // ������ ĳ�� ���� ���̶��
				tempField = fieldList.get(i).getY(); // ������ y���� tempField�� �����Ѵ�
				foot = player.getY() + player.getImage().getHeight(null); // ĳ���� �� ��ġ �罺ĵ
				// ������ġ�� tempNowField���� ����, �߹ٴ� ���� �Ʒ� �ִٸ�
				// ��, ĳ���� �� �Ʒ��� ���� ���� �ִ� �����̶�� tempNowField�� �����Ѵ�.
				if (tempField < tempNowField && tempField >= foot) {
					tempNowField = tempField;
				}
			}
		field = tempNowField; // ����� field�� ������Ʈ �Ѵ�.
		player.setField(this.field);
		}
	}// end of setCpField(int cp)
}