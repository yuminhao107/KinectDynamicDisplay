package myProgram;

import java.util.ArrayList;
import java.util.List;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import ProGAL.geom3d.Point;
import ProGAL.geom3d.PointList;
import ProGAL.geom3d.volumes.OBB;
import igeo.IVec;
import myAnimation.MyBox;

public class IFace {
	public ArrayList<IPoint> points;
	public IVec normal;
	public IVec center;
	IVec p1;
	IVec p2;
	IVec p3;
	public double A,B,C,D;
	public double eigen = Option.findFaceMaxEigen + 1.0;
	private ArrayList<IPoint> visitedPts, stack;

	public IFace() {
		points = new ArrayList<IPoint>();
	}

	public ArrayList<IPoint> boundPts() {
		ArrayList<IPoint> boundPts = new ArrayList<IPoint>();
		for (IPoint pt : points) {
			if (isBoundary(pt))
				boundPts.add(pt);
		}
		return boundPts;
	}
	
	public void printCenter(){
		System.out.println("Center is "+center.x+" "+center.y+" "+center.z);
	}

	private boolean isBoundary(IPoint pt) {
		if (pt.left().getFace() != this)
			return true;
		if (pt.right().getFace() != this)
			return true;
		if (pt.up().getFace() != this)
			return true;
		if (pt.down().getFace() != this)
			return true;
		return false;
	}

	public void linkPoints() {
		for (IPoint pt : points) {
			pt.changeFace(this);
			pt.mindist=Mymath.distPoint2Face(pt, this);
		}
	}

	public void tryExpand() {
		visitedPts = new ArrayList<IPoint>();
		stack = new ArrayList<IPoint>();
		for (IPoint pt : points) {
			pt.visited = true;
			visitedPts.add(pt);
		}
		for (IPoint pt : points) {
			tryAdd2Stack(pt.left());
			tryAdd2Stack(pt.right());
			tryAdd2Stack(pt.down());
			tryAdd2Stack(pt.up());
		}
		while (!stack.isEmpty()) {
			int id = stack.size() - 1;
			IPoint pt = stack.get(id);
			stack.remove(id);
			double dist = Mymath.distPoint2Face(pt, this);
			if (dist < pt.mindist) {
				points.add(pt);
				tryAdd2Stack(pt.left());
				tryAdd2Stack(pt.right());
				tryAdd2Stack(pt.up());
				tryAdd2Stack(pt.down());
			}

		}
		for (IPoint pt : visitedPts) {
			pt.visited = false;
		}
	}

	private void tryAdd2Stack(IPoint pt) {
		if (!pt.visited)
			if (pt.isUseful)
//				if (pt.getFace() == null) 
				{
				stack.add(pt);
				pt.visited = true;
				visitedPts.add(pt);
			}
	}

	private void tryExpandPoint(IPoint pt) {
		if (pt.visited)
			return;
		if (!pt.isUseful)
			return;
		if (pt.getFace() != null)
			return;
		pt.visited = true;
		visitedPts.add(pt);
		double dist = Mymath.distPoint2Face(pt, this);
		if (dist < Option.findFacePoint2FaceDistMax) {
			points.add(pt);
			tryExpandPoint(pt.left());
			tryExpandPoint(pt.right());
			tryExpandPoint(pt.up());
			tryExpandPoint(pt.down());
		}
	}

	public int pointsNum() {
		return points.size();
	}

	public void add(IPoint pt) {
		points.add(pt);
	}

	public void setPoints(ArrayList<IPoint> points) {
		this.points = points;
	}

	public void setNormal(IVec normal) {
		this.normal = normal;
	}

	public void calCenter() {
		if (points.size() == 0)
			return;
		int num = 0;
		IVec sum = new IVec(0, 0, 0);
		for (IPoint pt : points) {
			++num;
			sum.add(pt.pos());
		}
		center = sum.mul(1.0 / num);
	}

	public void calEquation() {
		A = normal.x();
		B = normal.y();
		C = normal.z();
		D = -(A * center.x() + B * center.y() + C * center.z());
	}

	public void fitFace() {
		ArrayList<IPoint> anaPts;
		if (points.size() <= Option.fitFacePointNumMax) {
			anaPts = points;
		} else {
			anaPts = new ArrayList<IPoint>();
			for (int i = 0; i < Option.fitFacePointNumMax; i++) {
				int id = (int) (Math.random() * points.size());
				anaPts.add(points.get(id));
			}
		}
		Matrix mat = new Matrix(covMatrix(this.points));
		EigenvalueDecomposition eigenDec = new EigenvalueDecomposition(mat);
		double[] real = eigenDec.getRealEigenvalues();
		double[][] res = eigenDec.getV().getArray();
		eigen = real[0];
		if (eigen < 0.000001)
			++Option.errorCount;
		normal = new IVec(res[0][0], res[1][0], res[2][0]).unit();
		if (normal.z() < 0)
			normal.mul(-1);
	}

	public void update() {
		fitFace();
		calCenter();
		calEquation();
	}

	public boolean isFace() {
		return eigen < Option.findFaceMaxEigen && eigen > 0.000001;
	}

	public ArrayList<IPoint> points() {
		return points;
	}
	
	public void anaBox(){
		List<Point> obbPts = new ArrayList<Point>();
		for (IPoint pt:points){
			IVec vec=pt.pos();
			obbPts.add(new Point(vec.x(),vec.y(),vec.z()));
			
		}
		PointList ptlist = new PointList(obbPts);
		OBB obb = OBB.createBoundingBox_Covariance(ptlist);
		Point[] pts=obb.getVertices();
	     p1=point2IVec(pts[0]);
	     p2=point2IVec(pts[2]);
	     p3=point2IVec(pts[4]);   
	    p1.z=center.z;
	    p2.z=center.z;
	    p3.z=center.z;
	    if (p2.dif(p1).cross(p3.dif(p1)).z()<0){
	    	IVec tem=p2;
	    	p2=p3;
	    	p3=tem;
	    }
	    
	}
	
	public MyBox anaUpFace2Box(){
		List<Point> obbPts = new ArrayList<Point>();
		for (IPoint pt:points){
			IVec vec=pt.pos();
			obbPts.add(new Point(vec.x(),vec.y(),0));
			
		}
		PointList ptlist = new PointList(obbPts);
		OBB obb = OBB.createBoundingBox_Covariance(ptlist);
		Point[] pts=obb.getVertices();
//	    center=point2IVec(obb.getCenter());
	    IVec p1=point2IVec(pts[0]);
	    IVec p2=point2IVec(pts[2]);
	    IVec p3=point2IVec(pts[4]);   
	    IVec v1=p2.dif(p1);
	    IVec v2=p3.dif(p1);
	    
	    float xLen=(float)v1.len();
	    float yLen=(float)v2.len();
	    float zLen=(float)center.z();
	    Vector3f pos=new Vector3f((float)center.x(),(float)center.y(),0);
	    if (xLen<yLen){
	    	float tem=xLen;
	    	xLen=yLen;
	    	yLen=tem;
	    	v1=v2;
	    }
	    if (v1.y()<0) v1.flip();
	    float ang=(float)v1.angle(IVec.xaxis);
	    if (Option.changeZ) zLen=changeZ(zLen,pos.x,pos.y);
	    MyBox box = new MyBox(pos,xLen,yLen,zLen,ang);
	    return box;
	    
	}
	
	private float changeZ(float z,float x,float y){
		float res=z/30+y/100-x/100;
		return z-res;
	}
	
	public Geometry generateBox(){
		 Vector3f point1=Mymath.IVecToVector3f(p2.dif(p1));
		 Vector3f point2=Mymath.IVecToVector3f(p3.dif(p1));
		 Vector3f pos=Mymath.IVecToVector3f(center);
		 float ang=(float) p2.dif(p1).angle(new IVec(1,0,0));
		 Box box=new Box(point1,point2);
		 Geometry boxGeo=new Geometry("box",box);
		 boxGeo.setLocalTranslation(pos);
//		 boxGeo.rotate(0, 0, ang);
		 return boxGeo;
	 }
	
	private IVec point2IVec(Point pt){
		return new IVec(pt.x(),pt.y(),pt.z());
	}
	
	private double[][] covMatrix(ArrayList<IPoint> points) {
		double[] x = new double[points.size()];
		double[] y = new double[points.size()];
		double[] z = new double[points.size()];
		for (int i = 0; i < points.size(); i++) {
			IPoint p = points.get(i);
			x[i] = p.pos().x();
			y[i] = p.pos().y();
			z[i] = p.pos().z();
		}
		double averageX = average(x);
		double averageY = average(y);
		double averageZ = average(z);
		double[][] results = {
				{ cov(x, x, averageX, averageX), cov(x, y, averageX, averageY), cov(x, z, averageX, averageZ) },
				{ cov(y, x, averageY, averageX), cov(y, y, averageY, averageY), cov(y, z, averageY, averageZ) },
				{ cov(z, x, averageZ, averageX), cov(z, y, averageZ, averageY), cov(z, z, averageZ, averageZ) }, };
		return results;

	}

	private double cov(double[] x, double[] y, double averageX, double averageY) {
		double sum = 0;
		for (int i = 0; i < x.length; i++) {
			sum += (x[i] - averageX) * (y[i] - averageY);
		}
		return sum / x.length;
	}

	private double average(double[] num) {
		double sum = 0;
		for (int i = 0; i < num.length; i++) {
			sum += num[i];
		}
		return sum / num.length;
	}
	
	public IVec center(){
		return center;
	}
	

}
