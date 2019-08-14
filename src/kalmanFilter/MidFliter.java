package kalmanFilter;

public class MidFliter {
	int length=17;
	int resNum=10;
	float[] nums;
	int flag=0;
	
	
	public MidFliter(float num){
		nums=new float[length];
		for (int i=0;i<length;i++) nums[i]=num;
	}
	
	public void updata(float num){
		nums[flag]=num;
		++flag;
		if(flag==length) flag=0;
	}
	
	public float value(){
		float[] res=nums.clone();
		for (int i=0;i<length;i++)
			for (int j=0;j<length-i-1;j++){
				if (res[j]>res[j+1]){
					float tem=res[j];
					res[j]=res[j+1];
					res[j+1]=tem;
				}
			}
		float result=0;
		int start=(length-resNum)/2;
		for (int i=start;i<start+resNum;i++){
			result+=res[i];
		}
		result/=resNum;
		
		return result;
	}

}
