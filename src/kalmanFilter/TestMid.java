package kalmanFilter;

import org.ejml.simple.SimpleMatrix;

import ProGAL.geom3d.Point;
import ProGAL.geom3d.PointList;
import ProGAL.geom3d.volumes.OBB;
import processing.core.PApplet;

public class TestMid extends PApplet{
	MidFliter mid;
	
	KalmanFilterSimple kal;
	SimpleMatrix R;
	double x=0;
	double y=0;
	double realX=0;
	double realY=0;

	double v=10;
	double round=40;
	double nowX,nowY;
	
	float r=4;
	OBB obb;
	Point[] pts;
	public void setup(){
		size(800,600);
		mid=new MidFliter(100);
		PointList ptlist =new PointList();
		for (int i=0;i<10;i++){
			double x=Math.random()*100;
			double y=Math.random()*100;
			double z=0;
		ptlist.add(new Point(x,y,z));
		
		}

		obb = OBB.createBoundingBox_Covariance(ptlist);
		pts=obb.getVertices();
		noFill();
		for (int i=0;i<pts.length;i++){
			this.ellipse((float)pts[i].x()+400, (float)pts[i].y()+300, r, r);
			r+=4;
		}
		System.out.println(pts.length);
		
	}
	
	public void draw(){
//		nowX=Math.random()*100-50;
//		mid.updata((float)nowX);
//		x=mid.value();
//		background(255);
//		this.stroke(255, 0, 0);
//		this.ellipse((float)x+400, (float)300, 4, 4);
//		this.stroke(0, 255, 0);
//		this.ellipse((float)nowX+400, (float)300, 4, 4);
//		this.stroke(0, 0, 255);
//		this.ellipse((float)realX+400, (float)300, 4, 4);
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
	
	
	


}

