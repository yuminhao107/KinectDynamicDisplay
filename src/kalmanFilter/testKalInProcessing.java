package kalmanFilter;

import org.ejml.simple.SimpleMatrix;

import processing.core.PApplet;

public class testKalInProcessing extends PApplet{
	
	KalmanFilterSimple kal;
	SimpleMatrix R;
	double x=0;
	double y=0;
	double realX=0;
	double realY=0;

	double v=0;
	double round=200;
	double nowX,nowY;

	public void setup(){
		size(800,600);
		frameRate(20);
		ini();
	}
	
	public void draw(){
		change();
//		changeXY();
		background(255);
		noStroke();
		this.fill(255,0,0);
		this.ellipse((float)x+400, (float)y+300, 20, 20);
		this.fill(0, 255, 0);
		this.ellipse((float)nowX+400, (float)nowY+300, 20, 20);
		noFill();
		stroke(0, 0, 255);
		this.ellipse((float)realX+400, (float)realY+300, 20, 20);
	}
	
	public void ini(){
		double[][] fNum={{1,0,0.016,0},{0,1,0,0.016},{0,0,1,0},{0,0,0,1}};//转移
		SimpleMatrix fMat=new SimpleMatrix(fNum);
		double[][] hNum={{1,0,0.0,0},{0,1,0,0.0}};//观测
		SimpleMatrix hMat=new SimpleMatrix(hNum);
		double tol=0.0001;
		double[][] qNum={{tol,0,0,0},{0,tol,0,0},{0,0,tol,0},{0,0,0,tol}};//激励
		SimpleMatrix qMat=new SimpleMatrix(qNum);
		double[][] pNum={{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};//初始协方差
		SimpleMatrix pMat=new SimpleMatrix(pNum);
		double[][] xNum={{0},{0},{0},{0}};//初始状态
		SimpleMatrix xMat=new SimpleMatrix(xNum);
		double[][] rNum={{1,0},{0,1}};//设备协方差
		R=new SimpleMatrix(rNum);
		
		kal=new KalmanFilterSimple();
		kal.configure(fMat, qMat, hMat,R);
		kal.setState(xMat, pMat);
	}
	
	public void ini2(){
		double[][] fNum={{1,0},{0,1}};//转移
		SimpleMatrix fMat=new SimpleMatrix(fNum);
		double[][] hNum={{1,0},{0,1}};//观测
		SimpleMatrix hMat=new SimpleMatrix(hNum);
		double tol=0.00001;
		double[][] qNum={{tol,0},{0,tol}};//激励
		SimpleMatrix qMat=new SimpleMatrix(qNum);
		double[][] pNum={{0,0},{0,0}};//初始协方差
		SimpleMatrix pMat=new SimpleMatrix(pNum);
		double[][] xNum={{1},{1}};//初始状态
		SimpleMatrix xMat=new SimpleMatrix(xNum);
		double[][] rNum={{1,0},{0,1}};//设备协方差
		R=new SimpleMatrix(rNum);
		
		kal=new KalmanFilterSimple();
		kal.configure(fMat, qMat, hMat,R);
		kal.setState(xMat, pMat);
	}
	
	public void change(){
		realX+=v/60;
		realY+=v/60;
		x=realX+(Math.random()-0.5)*round;
		y=realY+(Math.random()-0.5)*round;
		
		double[][] zNum={{x},{y}};
		SimpleMatrix zMat=new SimpleMatrix(zNum);
		kal.predict();
		kal.update(zMat);
		
		SimpleMatrix res=kal.getState();
		nowX=res.get(0,0);
		nowY=res.get(1,0);
		System.out.println(Math.pow(realX-nowX, 2)+Math.pow(realY-nowY, 2));
	}
	
	public void changeXY(){
		if (frameCount==200){
			realX=100;
			realY=100;
		}
	}
	


}
