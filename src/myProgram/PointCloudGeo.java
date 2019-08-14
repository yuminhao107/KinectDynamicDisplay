package myProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.bulletphysics.collision.narrowphase.GjkEpaSolver.Face;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import igeo.IVec;

public class PointCloudGeo {
	public IPoint[][] pts = new IPoint[Option.depthNumY][Option.depthNumX];
	ArrayList<IFace> faces;

	public PointCloudGeo(IVec[][] points) {
		for (int i = 0; i < Option.depthNumY; i++)
			for (int j = 0; j < Option.depthNumX; j++) {
				pts[i][j] = new IPoint(points[i][j], j, i, this);
			}
		for (int i = 0; i < Option.depthNumY; i++)
			for (int j = 0; j < Option.depthNumX; j++) {
				pts[i][j].link();
			}

		IPoint nullPt = new IPoint();
		nullPt.isUseful = false;
		for (int i = 0; i < Option.depthNumY; i++)
			for (int j = 0; j < Option.depthNumX; j++) {
				IPoint pt = pts[i][j];
				if (pt.left() == null)
					pt.setLeft(nullPt);
				if (pt.right() == null)
					pt.setRight(nullPt);
				if (pt.up() == null)
					pt.setUp(nullPt);
				if (pt.down() == null)
					pt.setDown(nullPt);
			}
	}

	public void findFaces() {
		faces = new ArrayList<IFace>();
		ArrayList<IFace> smallFaces = new ArrayList<IFace>();
		int size = Option.findFaceOriginSize;
		int xNum = Option.depthNumX / size;
		int yNum = Option.depthNumY / size;
		for (int i = 0; i < yNum; i++)
			for (int j = 0; j < xNum; j++) {
				ArrayList<IPoint> originPts = new ArrayList<IPoint>();
				for (int m = i * size; m < i * size + size; m++)
					for (int n = j * size; n < j * size + size; n++) {
						if (pts[m][n].isUseful)
							originPts.add(pts[m][n]);
					}
				if (originPts.size() < Option.findFaceOriginMin)
					continue;

				IFace face = new IFace();
				face.setPoints(originPts);
				face.fitFace();
				if (!face.isFace())
					continue;
				smallFaces.add(face);
			}
		Collections.sort(smallFaces, new SortByEigen());
		for (IFace face : smallFaces) {
			ArrayList<IPoint> points = new ArrayList<IPoint>();
			for (IPoint pt : face.points) {
				if (pt.getFace() == null)
					points.add(pt);
			}
			face.setPoints(points);
			if (face.pointsNum() < Option.findFaceOriginMin)
				continue;
			face.update();
			if (!face.isFace())
				continue;
			if (Option.findFaceOnlyUpFace) {
				double ang = face.normal.angle(IVec.zaxis);
				if (ang > 5.0 / 180 * Math.PI)
					continue;
				if (face.center.z() < 50)
					continue;
			}
			// System.out.println("try expanding face eigen = " + face.eigen);

			face.tryExpand();
			if (face.pointsNum() > Option.findFaceFinalMin) {
				faces.add(face);
				face.linkPoints();
				face.update();
			}
		}
		// printsomething
		System.out.println("faces in total " + faces.size());
	}

	public void combineFaces() {
		ArrayList<IFace> result = new ArrayList<IFace>();
		int size = faces.size();
		boolean[] isCombined = new boolean[size];
		for (int i = 0; i < size; i++) {
			if (isCombined[i])
				continue;
			IFace face1 = faces.get(i);
			for (int j = i + 1; j < size; j++) {
				if (isCombined[j])
					continue;
				IFace face2 = faces.get(j);
				if (isMatch(face1, face2)) {
					combine(face1, face2);
					isCombined[j] = true;
				}
			}
			result.add(face1);
		}
		faces = result;
		// printsomething
		System.out.println("faces after combine " + faces.size());
	}

	private boolean isMatch(IFace face1, IFace face2) {
		double ang = face1.normal.angle(face2.normal);
		if (ang > Option.combineFaceAngleMax)
			return false;
		IVec vec = face1.center.dif(face2.center);
		double zDist = Math.max(Math.abs(vec.dot(face1.normal)), Math.abs(vec.dot(face2.normal)));
		if (zDist > Option.combineFaceZDistMax)
			return false;
		if (!isNearby(face1, face2))
			return false;
		return true;
	}

	private double distFace2Face(IFace face1, IFace face2) {
		// to be continue
		return 0;
	}

	private boolean isNearby(IFace face1, IFace face2) {
		for (IPoint p1 : face1.boundPts())
			for (IPoint p2 : face2.boundPts()) {
				if (p1.left() == p2)
					return true;
				if (p1.right() == p2)
					return true;
				if (p1.down() == p2)
					return true;
				if (p1.up() == p2)
					return true;
			}
		return false;
	}

	private void combine(IFace face1, IFace face2) {
		for (IPoint pt : face2.points()) {
			pt.setFace(face1);
			face1.add(pt);

		}
		face1.update();
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

	public ArrayList<IPoint> points() {
		ArrayList<IPoint> result = new ArrayList<IPoint>();
		for (int i = 0; i < pts.length; i++)
			for (int j = 0; j < pts[0].length; j++) {
				result.add(pts[i][j]);
			}
		return result;
	}
	
	public IVec getDepthCamDir(){
		int yc=212;
		int xc=256;
		int r=50;
		IFace face=new IFace();
		for (int i=yc-r;i<yc+r;i++){
			for (int j=xc-r;j<xc+r;j++){
				if (pts[i][j].pos()!=null){
					face.add(pts[i][j]);
				}
				
			}
		}
		face.fitFace();
		face.calCenter();
		System.out.println("Avrage height is "+face.center.z);
		return face.normal;
	}

}

class SortByEigen implements Comparator<IFace> {
	public int compare(IFace face1, IFace face2) {
		if (face1.eigen > face2.eigen)
			return 1;

		return -1;
	}
}