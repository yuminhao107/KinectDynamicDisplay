package myProgram;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ProGAL.geom3d.Line;
import ProGAL.geom3d.Point;
import ProGAL.geom3d.PointList;
import ProGAL.geom3d.viewer.J3DScene;
import ProGAL.geom3d.volumes.OBB;
import ProGAL.geom3d.volumes.Sphere;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import igeo.IVec;

public class Display2Dpt {


	
	// public static void main(String[] args) {
	// Kinect kinect=new Kinect();
	// kinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON);
	// // Generate points
	// List<Point> points = new ArrayList<Point>();
	// PointGeo geo=null;
	// while (geo==null){
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
//			}
	// geo=kinect.createGeo();
//		}
	//
	////
	//// for (int x = 0; x < 10; x++) {
	//// for (int z = 0; z < 10; z++) {
	//// points.add(new Point(x, (Math.random()*3), z));
	//// }
	//// }
	//// for (int z = 0; z < 10; z++) {
	//// for (int y = 0; y < 10; y++) {
	//// points.add(new Point(0, y, z));
	//// }
	//// }
	//// for (int x = 0; x < 10; x++) {
	//// for (int y = 0; y < 10; y++) {
	//// points.add(new Point(x, y, 0));
	//// }
	//// }
	//
	// J3DScene scene = J3DScene.createJ3DSceneInFrame();
	// scene.setAxisEnabled(true);
	//
	//// for (Point p : points) {
	//// // A filled circle with radius 0.01 and border 0
	//// scene.addShape(new Sphere(p, 0.3), Color.BLACK, 0);
	//// }
	//
	// for (IPoint pt:geo.pts){
	// IVec vec=pt.pos();
	// if (vec==null) continue;
	// Point p=new Point(vec.x(),vec.y(),vec.z());
	// scene.addShape(new Sphere(p, 1.5), Color.BLACK, 0);
//		}
//
	//// PointList ptlist = new PointList(points);
	////
	//// OBB obb = OBB.createBoundingBox_Covariance(ptlist);
	////
	//// scene.addShape(obb);
//
	// scene.centerCamera();
	//// Point[] pts=obb.getVertices();
	//// System.out.println(pts.length);
	//// for (int i=0;i<pts.length;i++){
	////// Line line=new Line(pts[i],pts[i+1]);
	////// scene.addShape(line);
	//// Point pt=pts[i];
	//// System.out.println(""+pt.x()+" "+pt.y()+" "+pt.z());
	//// }
	//
	// }

}
