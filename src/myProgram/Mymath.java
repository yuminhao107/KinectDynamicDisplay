package myProgram;

import com.jme3.math.Vector3f;

import igeo.IVec;

public class Mymath {
	public static IVec depthCamPos = new IVec(544,586,1497.4);
	public static IVec depthTarget=new IVec(547,614,0);
	public static IVec depthCamDir=new IVec( 0.007269565520894036,0.030822858251155785,-0.9994984266252582);
	public static double depthAng=-0.025;
	private static IVec temY=new IVec(Math.cos(depthAng),Math.sin(depthAng),0);
	public static IVec depthCamZ = depthCamDir.cp();
	public static IVec depthCamX = temY.cross(depthCamZ).unit();
	public static IVec depthCamY = depthCamZ.cross(depthCamX).unit();
	
	public static  double camera_fx = 366.6;//366.6 378.6
	public static  double camera_fy = 366.6;//366.6 373.6
	


	public static Vector3f IVecToVector3f(IVec pt){
		return new Vector3f((float)pt.x(),(float)pt.y(),(float)pt.z());
	}
	
	public static IVec Vector3fToIVec(Vector3f pt){
		return new IVec(pt.getX(),pt.getY(),pt.getZ());
	}
	
	public static double distPoint2Face(IPoint point,IFace face){
		double x=point.pos().x();
		double y=point.pos().y();
		double z=point.pos().z();
		double a=face.A;
		double b=face.B;
		double c=face.C;
		double d=face.D;
		return Math.abs(a*x+b*y+c*z+d)/Math.sqrt(a*a+b*b+c*c);
	}

	public static IVec depthCamera2World(IVec pos) {
		IVec p = depthCamPos.cp();
		p.add(depthCamX.cp().mul(pos.x()));
		p.add(depthCamY.cp().mul(pos.y()));
		p.add(depthCamZ.cp().mul(pos.z()));
		return p;
	}
	
	public static void printIVec(IVec center){
		System.out.println("vec is "+center.x+","+center.y+","+center.z);
	}

}
