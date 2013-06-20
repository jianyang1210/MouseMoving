package com.yj.frame;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int leftX = 350;
	private int leftY = 360;
	private int rightX = 1050;
	private int rightY = 341;

	public boolean flag = false;

	int X = 500;
	int Y = 500;

	private int width = 60, height = 20;

	private long pretime, time = 0;

	private int SPEED = 1, BASESPEED = 1, times = 1;

	private JButton setJButton;
	private JLabel leftJLabel, rightJLabel, xJLabel, yJLabel, speedJLabel,
			bgJLabel;
	private JTextField lxField, lyField, rxField, ryField, spField;
	private ArrayList<Point> points;
	private ImageIcon bgIcon;
	Image icon;;
	
	public Client() {
		super("简陋版本2.0 F10开始 F9结束");
		setBounds(200, 300, 350, 300);
		setIconImage(new ImageIcon("logo1.jpg").getImage());
		setLayout(null);
		setResizable(false);
		
		/*
		bgIcon = new ImageIcon("bg.jpg");
		bgJLabel = new JLabel(bgIcon);
		bgJLabel.setBounds(0, 0, 350, 300);
		add(bgJLabel);
		*/

		xJLabel = new JLabel("X");
		xJLabel.setBounds(190, 20, width, height);
		add(xJLabel);

		yJLabel = new JLabel("Y");
		yJLabel.setBounds(270, 20, width, height);
		add(yJLabel);

		leftJLabel = new JLabel("L-U");
		leftJLabel.setBounds(150, 80, width, height);
		add(leftJLabel);

		lxField = new JTextField(leftX + "");
		lxField.setBounds(190, 80, width, height);
		add(lxField);

		lyField = new JTextField(leftY + "");
		lyField.setBounds(270, 80, width, height);
		add(lyField);

		rightJLabel = new JLabel("	R-B");
		rightJLabel.setBounds(150, 140, width, height);
		add(rightJLabel);

		rxField = new JTextField(rightX + "");
		rxField.setBounds(190, 140, width, height);
		add(rxField);

		ryField = new JTextField(rightY + "");
		ryField.setBounds(270, 140, width, height);
		add(ryField);

		speedJLabel = new JLabel("Times");
		speedJLabel.setBounds(150, 200, width, height);
		add(speedJLabel);

		spField = new JTextField("1");
		spField.setBounds(190, 200, width, height);
		add(spField);

		setJButton = new JButton("SET");
		setJButton.setBounds(270, 200, width, height);
		add(setJButton);
		
		bgIcon = new ImageIcon("bg.jpg");
		bgJLabel = new JLabel(bgIcon);
		bgJLabel.setBounds(0, 0, 350, 300);
		add(bgJLabel);

		points = new ArrayList<Point>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Thread(new moveThread()).start();
		this.setVisible(true);
	}
	
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == setJButton) {

			try {
				leftX = Integer.parseInt(lxField.getText());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			try {
				leftY = Integer.parseInt(lyField.getText());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			try {
				rightX = Integer.parseInt(rxField.getText());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			try {
				rightY = Integer.parseInt(ryField.getText());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			try {
				times = Integer.parseInt(spField.getText());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			
			SPEED = BASESPEED * times;
		}
	}
	
	public void listAdd(){
		java.awt.Point mousepoint = MouseInfo.getPointerInfo().getLocation();
		
		int rx = mousepoint.x;
		int ry = mousepoint.y;
		
		Point r = new Point(rx, ry);
		
		points.add(r);
	}
	
	public void click() {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		for(Point r : points){
			robot.mouseMove(r.x,r.y);
			System.out.println(r.x + " " + r.y);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.delay(100);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}
	
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

//	public static void main(String args[]) {
//		new Client();
//	}
}
