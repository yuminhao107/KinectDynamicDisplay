package kalmanFilter;

import org.ejml.simple.SimpleMatrix;

public class testKalman {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double R=0.01;
		double[][] fNum={{1,0},{0,1}};//转移
		SimpleMatrix fMat=new SimpleMatrix(fNum);
		SimpleMatrix HMat=fMat.copy();//观测
		double[][] qNum={{0.1,0},{0,0.1}};//激励
		SimpleMatrix qMat=new SimpleMatrix(qNum);
		double[][] pNum={{0,1},{1,0}};//初始协方差
		SimpleMatrix pMat=new SimpleMatrix(pNum);
		double[][] xNum={{1},{1}};//初始状态
		SimpleMatrix xMat=new SimpleMatrix(xNum);
		double[][] zNum={{1.1},{1.1}};
		SimpleMatrix zMat=new SimpleMatrix(zNum);
		
		
		
		KalmanFilterSimple kal=new KalmanFilterSimple();
		kal.configure(fMat, qMat, HMat,zMat);
		kal.setState(xMat, pMat);
		
//		kal.update(zMat, R);
//		printMat(kal.getState());
	

	}
	
	public static void printMat(SimpleMatrix mat){
    	for (int i=0;i<mat.numRows();i++){
    		for (int j=0;j<mat.numCols();j++){
    			System.out.print(mat.get(i, j)+" ");
    		}
    		System.out.println();
    	}
    }

}
