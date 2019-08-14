package myProgram;

import java.util.ArrayList;

import igeo.IVec;

public class PointGeo {
	// IPoint origin;
	// ArrayList<IFace> faces;
	// ArrayList<IPoint> pts=new ArrayList<IPoint>();
	//
	// public PointGeo(IVec[][] points) {
	// origin = new IPoint(points[0][0],0,0);
	// IPoint left = origin;
	// pts.add(origin);
	// for (int i = 1; i < points[0].length; i++) {
	// IPoint thisPoint = new IPoint(points[0][i],0,i);
	// pts.add(thisPoint);
	// thisPoint.setLeft(left);
	// left.setRight(thisPoint);
	// left=thisPoint;
	// }
	// IPoint up = origin;
	// IPoint begin = origin;
	// left = null;
	// for (int i = 1; i < points.length; i++) {
	//
	// for (int j = 0; j < points[0].length; j++) {
	// IPoint thisPoint = new IPoint(points[i][j],i,j);
	// pts.add(thisPoint);
	// thisPoint.setUp(up);
	// up.setDown(thisPoint);
	// thisPoint.setLeft(left);
	// if (left != null)
	// left.setRight(thisPoint);
	// left = thisPoint;
	// up = up.right();
	// }
	// begin = begin.down();
	// left = null;
	// up = begin;
	// }
	//
	// }
	//
	// public void findFaces(double delta, int minNum) {
	// faces = new ArrayList<IFace>();
	// for (IPoint pt:pts){
	// boolean flag = true;
	// if (pt.pos() == null)
	// flag = false;
	// if (pt.getFace() != null)
	// flag = false;
	// if (flag) {
	// findFace(pt, delta, minNum);
	// }
	//
	// }
	// System.out.println("find faces done "+faces.size()+" faces in total");
	//
	// }
	//
	// private void findFace(IPoint point, double delta, int minNum) {
	// ArrayList<IPoint> points = findNearPoints(point,delta);
	//
	//
	//
	// if (points.size() >= minNum) {
	// for (int i=0;i<3;i++){
	// IPoint
	// newCenter=points.get((int)Math.floor(points.size()*Math.random()));
	// ArrayList<IPoint> tem=findNearPoints(newCenter,delta);
	// if (tem.size()>points.size()) points=tem;
	// }
	// IFace face = new IFace();
	// for (IPoint pt : points) {
	// pt.setFace(face);
	// face.add(pt);
	// }
	// faces.add(face);
	// }
	// }
	//
	// private ArrayList<IPoint> findNearPoints(IPoint point, double delta){
	// ArrayList<IPoint> points = new ArrayList<IPoint>();
	// ArrayList<IPoint> stack = new ArrayList<IPoint>();
	// ArrayList<IPoint> visitPoints= new ArrayList<IPoint>();
	// stack.add(point);
	// point.visited=true;
	// visitPoints.add(point);
	// double thisZ = point.pos().z();
	//// this.noVisit();
	// while (!stack.isEmpty()) {
	// int end = stack.size() - 1;
	// IPoint pt = stack.get(end);
	// stack.remove(end);
	// boolean flag = true;
	// if (pt.pos() == null)
	// flag = false;
	// if (pt.getFace() != null)
	// flag = false;
	// if (flag) {
	// if (Math.abs(pt.pos().z() - thisZ) < delta) {
	// points.add(pt);
	// if (pt.right() != null)
	// if (pt.right().needVisit()){
	// stack.add(pt.right());
	// pt.right().visited=true;
	// visitPoints.add(pt.right());
	// }
	// if (pt.up() != null)
	// if (pt.up().needVisit()){
	// stack.add(pt.up());
	// pt.up().visited=true;
	// visitPoints.add(pt.up());
	// }
	// if (pt.left() != null)
	// if (pt.left().needVisit()){
	// stack.add(pt.left());
	// pt.left().visited=true;
	// visitPoints.add(pt.left());
	// }
	// if (pt.down() != null)
	// if (pt.down().needVisit()){
	// stack.add(pt.down());
	// pt.down().visited=true;
	// visitPoints.add(pt.down());
	// }
	// }
	// }
	//
	// }
	// for (IPoint pt:visitPoints) pt.visited=false;
	// return points;
	//
	// }
	//
	// private void findNearPoints2(IPoint pt, double delta,ArrayList<IPoint>
	// points ,double thisZ){
	// pt.visited=true;
	// if (Math.abs(pt.pos().z() - thisZ) < delta) {
	// points.add(pt);
	// if (pt.right() != null)
	// if (pt.right().needVisit()){
	// findNearPoints2(pt.right(),delta,points,thisZ);
	// }
	// if (pt.up() != null)
	// if (pt.up().needVisit()){
	// findNearPoints2(pt.up(),delta,points,thisZ);
	// }
	// if (pt.left() != null)
	// if (pt.left().needVisit()){
	// findNearPoints2(pt.left(),delta,points,thisZ);
	// }
	// if (pt.down() != null)
	// if (pt.down().needVisit()){
	// findNearPoints2(pt.down(),delta,points,thisZ);
	// }
	// }
	// pt.visited=false;
	// }
	//
	// public void calNormalization(){
	// for (IPoint pt:pts){
	//
	// }
	// }
	//
	// public void check(){
//		this.noVisit();
	// checkPoint(origin);
	// System.out.println("check done");
	//
	// }
	//
	// public void noVisit(){
	// for (IPoint pt:pts){
	// pt.visited=false;
	// }
	// }
	//
	// private void checkPoint(IPoint pt){
	// pt.visited=true;
	// pt.printlnXY();
	// if (pt.right()!=null)
	// if (pt.right().visited==false)
	// checkPoint(pt.right());
	// if (pt.down()!=null)
	// if (pt.down().visited==false)
	// checkPoint(pt.down());
	// }
	
	

}
