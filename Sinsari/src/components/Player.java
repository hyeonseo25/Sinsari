package components;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import util.Util;

public class Player {
	private JPanel mainPanel;
	private int x;
	private int y;
	private int distance = 200; // ĳ���Ͱ� �̵��� �� �Ÿ�
	private int hp = 1000; // ü��
	private int invincibility = 255; // �̹��� ����
	private int score = 0; // ����
	private Image image;
	private int cnt = 0; // ���� ĳ���� �̹��� �����ϴ� ���
	private boolean fall = false; // ���� ����
	private boolean jump = false; // ���� ����
	private int countJump = 0;
	private int field = 900;
	
	private ImageIcon backImg = new ImageIcon("images/�����гι��.png");
	private Image back = backImg.getImage();
	
	private Image images[] = {new ImageIcon("images/Player/Player1.png").getImage()
			,new ImageIcon("images/Player/Player1.png").getImage()
			,new ImageIcon("images/Player/Player1.png").getImage()
			,new ImageIcon("images/Player/Player2.png").getImage()
			,new ImageIcon("images/Player/Player2.png").getImage()
			,new ImageIcon("images/Player/Player2.png").getImage()
			,new ImageIcon("images/Player/Player3.png").getImage()
			,new ImageIcon("images/Player/Player3.png").getImage()
			,new ImageIcon("images/Player/Player3.png").getImage()
			,new ImageIcon("images/Player/Player4.png").getImage()
			,new ImageIcon("images/Player/Player4.png").getImage()
			,new ImageIcon("images/Player/Player4.png").getImage()};
	
	public boolean isFall() {
		return fall;
	}
	
	public void setFall(boolean fall) {
		this.fall = fall;
	}
	
	public int getField() {
		return field;
	}
	
	public void setField(int field) {
		this.field = field;
	}
	
	public boolean isJump() {
		return jump;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public int getCountJump() {
		return countJump;
	}
	
	public void setCountJump(int countJump) {
		this.countJump = countJump;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getInvincibility() {
		return invincibility;
	}
	
	public void setInvincibility(int invincibility) {
		this.invincibility = invincibility;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image imageIcon) {
		this.image = imageIcon;
	}
	
	//player ���������� �̵� (key)
	public void p_moveRight() {
		if(cnt == images.length) {
			cnt = 0;
		}
		setImage(images[cnt]);
		cnt++;
		if(distance < back.getWidth(null)-130) {
			x += 15;
			distance += 15;
		}		
	}
	
	// ȭ�� �߰����� ������ �̵�(key)
	public void p_moveRight(int num) {
		if(cnt == images.length) {
			cnt = 0;
		}
		setImage(images[cnt]);
		cnt++;
		distance += 10;
	}
	
	// ������ ���� �� 
	public void damaged(int damage) {
		if(invincibility == 255) { //������ 255�϶�
			Sound("music/hitSound.wav", false);
			Sound("music/ouchSound.wav", false);
			invincibility = 80;
			this.hp -= damage;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						//������ �ٲپ��ش�. 
						Thread.sleep(500);
						invincibility=254;
						Thread.sleep(500);
						invincibility=80;
						Thread.sleep(500);
						invincibility=254;
						Thread.sleep(500);
						invincibility=80;
						Thread.sleep(500);
						invincibility=255;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	// �ʵ�� �������� �޼��� 
	public void fall() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int foot = getY() + image.getHeight(null); // ĳ���� �� ��ġ �罺ĵ

					// �߹ٴ��� ���Ǻ��� ���� ������ �۵�
					if (foot < field // ���߿� ������
							&& !isJump() // ���� ���� �ƴϸ�
							&& !isFall()) { // �������� ���� �ƴ� ��
						
						setFall(true); // �������� ������ ��ȯ
						long t1 = Util.getTime(); // ����ð��� �����´�
						long t2;
						int set = 1; // ó�� ���Ϸ� (0~10) ���� �׽�Ʈ�غ���
						while (foot < field) { // ���� ���ǿ� ��� ������ �ݺ�
							t2 = Util.getTime() - t1; // ���� �ð����� t1�� ����
							int fallY = set + (int) ((t2) / 40); // ���Ϸ��� �ø���.
							foot = getY() + image.getHeight(null); // ĳ���� �� ��ġ �罺ĵ
							if (foot + fallY >= field) { // �߹ٴ�+���Ϸ� ��ġ�� ���Ǻ��� ���ٸ� ���Ϸ��� �����Ѵ�.
								fallY = field - foot;
							}
							setY(getY() + fallY); // Y��ǥ�� ���Ϸ��� ���Ѵ�
							if (isJump()) { // �������ٰ� ������ �ϸ� ��������
								break;
							}
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						setFall(false);
						if (!isJump()) { // ���� ���� ��� ���� ���� �ƴ� �� �������� ī��Ʈ�� 0���� ����
							setCountJump(0);
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	// ���� ����
	public void jump() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				setCountJump(getCountJump() + 1); // ���� Ƚ�� ����
				int nowJump = getCountJump(); // �̹������� �������� ������������ ����
				setJump(true); // ���������� ����
				long t1 = Util.getTime(); // ����ð��� �����´�
				long t2;
				int set = 12; // ���� ��� ����(0~20) ������ �ٲ㺸��
				int jumpY = 1; // 1�̻����θ� �����ϸ� �ȴ�.(while�� ���� ����)
				while (jumpY >= 0) { // ��� ���̰� 0�϶����� �ݺ�
					t2 = Util.getTime() - t1; // ���� �ð����� t1�� ����
					jumpY = set - (int) ((t2) / 40); // jumpY �� �����Ѵ�.
					setY(getY() - jumpY); // Y���� �����Ѵ�.
					if (nowJump != getCountJump()) { // ������ �ѹ� ���Ǹ� ù��° ������ �����.
						break;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (nowJump == getCountJump()) { // ������ ��¥ ������ ���� Ȯ��
					setJump(false); // �������¸� false�� ����
				}
			}
		}).start();
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
	
	public Player(JPanel main){
		this.mainPanel = main;
		setX(200);
		setY(600);
		setDistance(200);
		setScore(0);
		setInvincibility(255);
		setImage(new ImageIcon("images/Player/Player1.png").getImage());
	}
}