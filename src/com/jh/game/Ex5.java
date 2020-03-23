package com.jh.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class Ex5 extends JFrame {
	
	Ex5() {
		this.setTitle("사격 게임");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GamePanel p = new GamePanel();
		this.add(p);

		this.setLocationRelativeTo(null);
		this.setSize(300, 300);
		this.setResizable(false);
		this.setVisible(true);
		p.startGame();
		//git
	}
}

class GamePanel extends JPanel {
	TargetThread targetThread;
	JLabel base = new JLabel();
	JLabel bullet = new JLabel();
	JLabel[] bullets = new JLabel[15];

	JLabel target;

	
	GamePanel() {
		for(int i = 0; i < bullets.length ;i++) {
			bullets[i] = new JLabel();
			bullets[i].setSize(10, 10);
			bullets[i].setOpaque(true);
			bullets[i].setBackground(Color.red);
			this.add(bullets[i]);
		}
		
		this.setLayout(null);
		base.setSize(40, 40);
		base.setOpaque(true);
		base.setBackground(Color.BLACK);

		ImageIcon img = new ImageIcon("image\\chicken.png");
		target = new JLabel(img);
		target.setSize(img.getIconWidth(), img.getIconHeight());
		
		this.add(base);
		this.add(target);
	}

	public void startGame() {
		base.setLocation(this.getWidth() / 2 - 20, this.getHeight() - 40);
		target.setLocation(this.getWidth() / 2 - 20, this.getHeight() - 50);

		for(int i = 0; i < bullets.length ; i++) {
			bullets[i].setLocation(this.getWidth() / 2 - 5, this.getHeight() - 50);
		}

		
		targetThread = new TargetThread(target);
		targetThread.start();

		base.requestFocus();//
		base.addKeyListener(new KeyListener() {
			int i = 0;
			//BulletThread[] bulletThread = null;
			BulletThread[] bulletThread = new BulletThread[15];
			@Override
			public void keyPressed(KeyEvent ke) {
				for(int j = 0 ; j < bulletThread.length; j++) {
					bulletThread[j] = new BulletThread(bullets[j], target, targetThread);
				}
/*				if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
					base.setLocation(base.getLocation().x + 3, base.getLocation().y);
				}
				else if(ke.getKeyCode() == KeyEvent.VK_LEFT){
					base.setLocation(base.getLocation().x - 3, base.getLocation().y);
				}
				else if(ke.getKeyCode() == KeyEvent.VK_UP){
					base.setLocation(base.getLocation().x, base.getLocation().y - 3);
				}
				else if(ke.getKeyCode() == KeyEvent.VK_DOWN){
					base.setLocation(base.getLocation().x, base.getLocation().y + 3);
				}*/

			}

			@Override
			public void keyReleased(KeyEvent ke) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent ke) {
				// TODO Auto-generated method stub
				if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
					// 스레드가 죽어있는 상태인지 확인
						//bulletThread[i] = new BulletThread(bullets[i], target, targetThread);
					bulletThread[i%15].start();
					i++;
				}
			}
		});
	}
}

class PlayerThread extends Thread{
	
	JLabel base;
	
	PlayerThread(JLabel base){
		this.base = base;
	}
	
	public void run() {
		
	}
	
}

class TargetThread extends Thread {
	JLabel target;

	TargetThread(JLabel target) {
		this.target = target;
		target.setLocation(-20, 0);
	}

	public void run() {
		int moveNum = 0;
		while (true) {
			int x = target.getX() + moveNum;
			int y = target.getY();

			boolean turn = false;
			if (x > 280)
				moveNum = -8;
			else if (x <= -20)
				moveNum = 8;

			target.setLocation(x, y);

			try {
				sleep(20); // corutine 에서 딜레이 타임의 역할과 동일, 프레임마다 가하는 딜레이 타임
			} catch (Exception e) {
				target.setLocation(0, 0);
				try {
					sleep(500);
				} catch (Exception e2) {
				}
			}
		}
	}
}

class BulletThread extends Thread {
	JLabel bullet, target;
	Thread targetThread;

	public BulletThread(JLabel bullet, JLabel target, Thread targetThread) { //스레드 클래스의 생성자
		this.bullet = bullet;
		this.target = target;
		this.targetThread = targetThread;
	}

	public void run() {
		while (true) {
			if (hit()) {
				targetThread.interrupt();//총알과 타깃이 충돌했을 때 타깃의 스레드를 멈춘다
				bullet.setLocation(bullet.getParent().getWidth() / 2 - 5, bullet.getParent().getHeight() - 50); //총알 원위치
				return;
			} else {
				int x = (int) (bullet.getX());
				int y = bullet.getY() - 5;

				if (y < 0) {
					bullet.setLocation(bullet.getParent().getWidth() / 2 - 5, bullet.getParent().getHeight() - 50);//화면밖으로 총알 이탈시 총알 원위치
					return;
				} else
					bullet.setLocation(x, y);
			}

			try {
				sleep(20);
			} catch (Exception e) {
			}
		}
	}

	private boolean hit() {
		int x = bullet.getX();
		int y = bullet.getY();
		int w = bullet.getWidth();
		int h = bullet.getHeight();

		if (targetContains(x, y) || targetContains(x + w - 1, y) || targetContains(x + w - 1, y + h - 1)
				|| targetContains(x, y + h - 1))
			return true;
		else
			return false;
	}

	private boolean targetContains(int x, int y) {
		if (((target.getX() <= x) && (x < target.getX() + target.getWidth()))
				&& ((target.getY() <= y) && (y < target.getY() + target.getHeight()))) {
			return true;
		} else {
			return false;
		}
	}
}
