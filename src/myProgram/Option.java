package myProgram;

import org.ejml.simple.SimpleMatrix;

import com.jme3.math.Vector3f;

public class Option {
	
	//ȫ�ֿ���
	//ֻ��ⶥ��
	public static boolean findFaceOnlyUpFace=true;
	
	public static boolean enbleMediaFilter=true;
	
	public static boolean enblekalmanFilter=true;
	
	public static boolean changeZ=true;
	//���n֡δ��� ���������е�����
	public static final int animationThrowStep=3;
	public static final int animationCreatStep=4;
	
	public static final Vector3f camPos = new Vector3f(1098.13f, -725.83f, 1205.21f);
	public static final float viewAng = 26.94f;
	public static final Vector3f screenCenter =new Vector3f(568, 786, 0);
	
//   public static final double delta4FindBox=9;//mm
   
//   public static final int minNum4FindBox=1000;
   
   public static final int fps=59;
   
   
   //pixels of depthcamera
   public static final int depthNumX=512;
   public static final int depthNumY=424;
   
	// var of detecting faces
   
	public static final int findFaceOriginSize = 10;// 10*10=100 points
	public static final int findFaceOriginMin = 50;
	public static final double findFaceMaxEigen = 1;
	public static final double findFacePoint2FaceDistMax = 6;// mm
	public static final int findFaceFinalMin = 800;

	// var of combine faces

	public static final double combineFaceAngleMax = 10.0 / 180 * Math.PI;
	public static final double combineFaceDistMax = 6;
	public static final double combineFaceZDistMax = 30;

	// var of fit faces
	public static final int fitFacePointNumMax = 300;

	// error count
	public static int errorCount = 0;
	
	//var of match box
	//λ�ò���ϵ�� 
	public static final double matchBoxDistFact=1;
	//��״����ϵ�� Խ��Խ������λ�ò���
	public static final double matchBoxShapeDistFact=1;
	//�߶Ȳ���ϵ��
	public static final double matchBoxZDistFact=5;
	//�ǶȲ���ϵ��
	public static final double matchBoxAngelFact=100;
	//����ֵ��
	public static final double matchBoxAllMaxPoint=35;
	public static final double matchBoxShapeMaxPoint=30;
	
	
	//config for KalmanFilter
	public static double tps=1.0/20;
	public static double smallNum=0.00002;
	public static double[][] fNum={//ת�ƾ��� ״̬Ϊ(x,y,ang,xLen,yLen,zLen,xV,yV,angV)
			{1,0,0,0,0,0,tps,0},
			{0,1,0,0,0,0,0,tps},
			{0,0,1,0,0,0,0,0},
			{0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0},
			{0,0,0,0,0,1,0,0},
			{0,0,0,0,0,0,1,0},
			{0,0,0,0,0,0,0,1},
			};
	public static SimpleMatrix fMat=new SimpleMatrix(fNum);
	public static double[][] hNum={//�۲����
			{1,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0},
			{0,0,1,0,0,0,0,0},
			{0,0,0,1,0,0,0,0},
			{0,0,0,0,1,0,0,0},
			{0,0,0,0,0,1,0,0},
	};
	public static SimpleMatrix hMat=new SimpleMatrix(hNum);
	public static double[][] qNum={//��������
			{smallNum,0,0,0,0,0,0,0},
			{0,smallNum,0,0,0,0,0,0},
			{0,0,smallNum,0,0,0,0,0},
			{0,0,0,smallNum,0,0,0,0},
			{0,0,0,0,smallNum,0,0,0},
			{0,0,0,0,0,smallNum,0,0},
			{0,0,0,0,0,0,smallNum,0},
			{0,0,0,0,0,0,0,smallNum},
	};
	public static SimpleMatrix qMat=new SimpleMatrix(qNum);
	public static double[][] pNum={//��ʼЭ����
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
	};
	public static SimpleMatrix pMat=new SimpleMatrix(pNum);
	public static double[][] rNum={//�豸���
			{1,0,0,0,0,0},
			{0,1,0,0,0,0},
			{0,0,3,0,0,0},
			{0,0,0,1,0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1},
	};
	public static SimpleMatrix rMat=new SimpleMatrix(rNum);
}
