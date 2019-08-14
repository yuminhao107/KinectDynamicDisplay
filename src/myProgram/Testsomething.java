package myProgram;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.texture.Image;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import igeo.IVec;

public class Testsomething {


	public static void main(String[] args) {
//		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
//		// TODO Auto-generated method stub
//		int[][] depthmap = getImageGRB("data/testData3.png");
//		IVec[][] vecs = short2IVec(depthmap);
//		PointCloudGeo geo = new PointCloudGeo(vecs);
//		geo.findFaces();
//		geo.combineFaces();
//		
//		Matrix a;
//		testMat();
		IVec v1=new IVec(1,0);
		IVec v2=new IVec(-1,0);
		System.out.println(v1.angle(v2));
		


	}

	public static IVec[][] short2IVec(int[][] pts) {
		final int depthmapXNum = 512;
		final int depthmapYNum = 424;
		final double camera_factor = 1;
		final double camera_cx = 255.5;
		final double camera_cy = 210.5;
		final double camera_fx = 518.0;
		final double camera_fy = 519.0;
		IVec[][] results = new IVec[depthmapYNum][depthmapXNum];
		for (int i = 0; i < depthmapYNum; i++)
			for (int j = 0; j < depthmapXNum; j++) {
				int value = pts[i][j];
				if (value > 0) {
					double z = value / camera_factor;
					double x = (i - camera_cx) * z / camera_fx;
					double y = (j - camera_cy) * z / camera_fy;
					results[i][j] = new IVec(x, y, z);
				}
			}
		return results;
	}

	public static int[][] getImageGRB(String filePath) {
		File file = new File(filePath);
		int[][] result = null;
		if (!file.exists()) {
			return result;
		}
		try {
			BufferedImage bufImg = ImageIO.read(file);
			int height = bufImg.getHeight();
			int width = bufImg.getWidth();
			result = new int[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					result[i][j] = bufImg.getRGB(j, i) & 0xFF;
					result[i][j] *= 3;

					// System.out.println(bufImg.getRGB(i, j) & 0xFFFFFF);

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static void testMat() {
		ArrayList<IVec> points = new ArrayList<IVec>();
        for (int i=0;i<100;i++){
        	IVec v=new IVec(Math.random()*40,Math.random()*40,0);
        	points.add(v);
        }
        Matrix mat=new Matrix(covMatrix(points));
        EigenvalueDecomposition eigen=new EigenvalueDecomposition(mat);
        double[] real=eigen.getRealEigenvalues();
        for (int i=0;i<real.length;i++){
        	System.out.println(real[i]);
        }
        double[][] res=mat.getArrayCopy();
        printDoubleArray(res);
	}
	
	private static IVec pointOnFace(double a,double b, double c, double d){
		double x=Math.random();
		double y=Math.random();
		double z=(a*x+b*y+d)/(-c);
		return new IVec(x,y,z);
	}
	
	  private static double[][] covMatrix(ArrayList<IVec> points){
		  double[] x=new double[points.size()];
		  double[] y=new double[points.size()];
		  double[] z=new double[points.size()];
		  for (int i=0;i<points.size();i++){
			  IVec p=points.get(i);
			  x[i]=p.x();
			  y[i]=p.y();
			  z[i]=p.z();
		  }
		  double averageX=average(x);
		  double averageY=average(y);
		  double averageZ=average(z);
		  double[][] results={
				  {cov(x,x,averageX,averageX),cov(x,y,averageX,averageY),cov(x,z,averageX,averageZ)},
				  {cov(y,x,averageY,averageX),cov(y,y,averageY,averageY),cov(y,z,averageY,averageZ)},
				  {cov(z,x,averageZ,averageX),cov(z,y,averageZ,averageY),cov(z,z,averageZ,averageZ)},
		  };
		  return results;

	  }
	  
	  private static double cov(double[] x,double[] y,double averageX,double averageY){
		  double sum=0;
		  for (int i=0;i<x.length;i++){
			  sum+=(x[i]-averageX)*(y[i]-averageY);
		  }
		  return sum/x.length;
	  }
	  
	  private static double average(double[] num){
		  double sum=0;
		  for (int i=0;i<num.length;i++){
			  sum+=num[i];
		  }
		  return sum/num.length;
	  }
	  
	  private static void printDoubleArray(double[][] num){
		  for (int i=0;i<num.length;i++){
			  for (int j=0;j<num[0].length;j++){
				  System.out.print(""+num[i][j]+" ");
			  }
			  System.out.println();
		  }
	  }
	  
	  private static void printDoubleArray(double[] num){
		  for (int i=0;i<num.length;i++){
			  System.out.print(""+num[i]+" ");
		  }
		  System.out.println();
	  }

}
