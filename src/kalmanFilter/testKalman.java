package kalmanFilter;

import org.ejml.simple.SimpleMatrix;

public class testKalman {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double R=0.01;
		double[][] fNum={{1,0},{0,1}};//ת��
		SimpleMatrix fMat=new SimpleMatrix(fNum);
		SimpleMatrix HMat=fMat.copy();//�۲�
		double[][] qNum={{0.1,0},{0,0.1}};//����
		SimpleMatrix qMat=new SimpleMatrix(qNum);
		double[][] pNum={{0,1},{1,0}};//��ʼЭ����
		SimpleMatrix pMat=new SimpleMatrix(pNum);
		double[][] xNum={{1},{1}};//��ʼ״̬
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
