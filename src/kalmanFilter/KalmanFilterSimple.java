package kalmanFilter;

import org.ejml.simple.SimpleMatrix;

public class KalmanFilterSimple{

    // kinematics description
    private SimpleMatrix F,Q,H,R;

    // sytem state estimate
    private SimpleMatrix x,P;

    public void configure(SimpleMatrix F, SimpleMatrix Q, SimpleMatrix H,SimpleMatrix R) {
        this.F = F;
        this.Q = Q;
        this.H = H;
        this.R=R;
    }

    public void setState(SimpleMatrix x, SimpleMatrix P) {
        this.x = x;
        this.P = P;
    }

    public void predict() {
        // x = F x
        x = F.mult(x);

        // P = F P F' + Q
        P = F.mult(P).mult(F.transpose()).plus(Q);
    }

    public void update(SimpleMatrix _z) {
        // a fast way to make the matrices usable by SimpleMatrix
        SimpleMatrix z = _z;

        // y = z - H x
        SimpleMatrix y = z.minus(H.mult(x));

        // S = H P H' + R
        SimpleMatrix S = H.mult(P).mult(H.transpose()).plus(R);
        
//        printMat(P);
        

        // K = PH'S^(-1)
        SimpleMatrix K = P.mult(H.transpose().mult(S.invert()));

        // x = x + Ky
        x = x.plus(K.mult(y));

        // P = (I-kH)P = P - KHP
        P = P.minus(K.mult(H).mult(P));
    }

    public SimpleMatrix getState() {
        return x;
    }

    public SimpleMatrix getCovariance() {
        return P;
    }
    
    public void printMat(SimpleMatrix mat){
    	for (int i=0;i<mat.numRows();i++){
    		for (int j=0;j<mat.numCols();j++){
    			System.out.print(mat.get(i, j)+" ");
    		}
    		System.out.println();
    	}
    	System.out.println();
    }
}
