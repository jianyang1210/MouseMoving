package com.yj.frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import javax.swing.ImageIcon;

import com.yj.mm.KeyHook;

public class Tempt extends Frame implements ActionListener {

	/**
	 * 添加hook
	 */
	private static final long serialVersionUID = 1L;
	// 左上角和右下角的坐标
	private int leftX = 350;
	private int leftY = 360;
	private int rightX = 1050;
	private int rightY = 341;

	// 是否移动
	public boolean flag = false;

	// 鼠标的坐标
	int X = 500;
	int Y = 500;
	int rs = 1;
	
	private long  pretime, time= 0;

	// 移动的速度
	private int SPEED = 1, BASESPEED = 1;

	private Button setButton;
	private Label leftLabel, rightLabel, xLabel, yLabel, speedLabel;
	private TextField leftXTextField, leftYTextField, rightXTextField,
			rightYTextField, speedTextField;
	
	private static KeyHook hook;

	public Tempt() {
		super("簡陋版本V1.0 F10開始 F9結束");
		this.setBounds(200, 300, 300, 250);

		// set icon
		this.setIconImage(new ImageIcon("logo.jpg").getImage());
		
//		set hook
//		hook = new KeyHook();

		// Label 和 TextField 设置
		leftLabel = new Label("L-U");
		leftLabel.setBounds(20, 80, 70, 30);
		this.add(leftLabel);

		rightLabel = new Label("R-B");
		rightLabel.setBounds(20, 130, 70, 30);
		this.add(rightLabel);

		xLabel = new Label("X");
		xLabel.setBounds(100, 30, 70, 30);
		this.add(xLabel);

		yLabel = new Label("Y");
		yLabel.setBounds(200, 30, 70, 30);
		this.add(yLabel);

		leftXTextField = new TextField(leftX + "");
		leftXTextField.setBounds(100, 80, 70, 30);
		this.add(leftXTextField);

		leftYTextField = new TextField(leftY + "");
		leftYTextField.setBounds(200, 80, 70, 30);
		this.add(leftYTextField);

		rightXTextField = new TextField(rightX + "");
		rightXTextField.setBounds(100, 130, 70, 30);
		this.add(rightXTextField);

		rightYTextField = new TextField(rightY + "");
		rightYTextField.setBounds(200, 130, 70, 30);
		this.add(rightYTextField);

		speedLabel = new Label("TIMES");
		speedLabel.setBounds(20, 180, 70, 30);
		this.add(speedLabel);

		speedTextField = new TextField(rs + "");
		speedTextField.setBounds(100, 180, 70, 30);
		this.add(speedTextField);

		// 设置按钮
		setButton = new Button("SET");
		setButton.addActionListener(this);
		setButton.setBounds(200, 180, 70, 40);
		this.setLayout(null);
		this.add(setButton);

		// 窗口退出事件
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.setResizable(false);
		// this.setBackground(Color.gray);
		this.setVisible(true);

		// 持续运动的线程
		new Thread(new moveThread()).start();
//		new Thread(new hookThread()).start();
	}

//	
//	private class hookThread implements Runnable{
//		public void run(){
//			while(true){
//				hook = new KeyHook();
//				if(hook.quit == 121 ){
//					flag = true;
//				}else if(hook.quit == 120){
//					flag = false;
//				}
//			}
//		}
//	}
//	
	
	// 运动线程
	private class moveThread implements Runnable {
		public void run() {		
			while (true) {
				moving();
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 模拟鼠标点击
	public void click() {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.mouseMove(924, 288);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(100);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	// 运动函数
	public void moving() {
		// Robot 对象控制鼠标
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		pretime = System.nanoTime()/100000000;
		while (flag) {
			if (X < leftX) {
				X = rightX;
			}
			if (Y < leftY) {
				Y = rightY;
			}
			X -= SPEED;
			Y -= SPEED;
			time = System.nanoTime()/100000000;
			if(time - pretime > 5){
				click();
				System.out.println(time - pretime);
				pretime = time;
				this.requestFocus();
			}
			robot.mouseMove(X, Y);
		}
	}

	// 按钮监听
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == setButton) {
			// 焦点转移
			this.requestFocus();

			// 获取坐标
			leftX = Integer.parseInt(leftXTextField.getText());
			leftY = Integer.parseInt(leftYTextField.getText());
			rightX = Integer.parseInt(rightXTextField.getText());
			rightY = Integer.parseInt(rightYTextField.getText());

			// 倍速
			rs = Integer.parseInt(speedTextField.getText());

			SPEED = BASESPEED * rs;
			
			
			System.out.println("The set button be pushed");
		}
	}

//	public static void main(String args[]) {
//		new Tempt();
//	}

}
