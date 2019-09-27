package com.example.pintu23;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class MainActivity extends Activity {
	/**�ж���Ϸ�Ƿ�ʼ*/
	private boolean isGameStart=false;
	/**���ö�ά���鴴�����ɸ���ϷС����*/
	private ImageView[][] iv_game_arr = new ImageView[3][5];
	/**��Ϸ������*/
	private GridLayout gl_main_game;
	/**��ǰ�շ����ʵ���ı���*/
	private ImageView iv_null_ImageView;
	/**��ǰ����*/
	private GestureDetector mDetector;
	
	@Override//���Ƽ���
	public boolean onTouchEvent(MotionEvent event) {
		return mDetector.onTouchEvent(event);
	}
	
	@Override//��ͼ�ϰ벿���ƶ�
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetector =new  GestureDetector(this, new OnGestureListener() {
			public boolean onSingleTapUp(MotionEvent arg0) {
				return false;
			}
			
			public void onShowPress(MotionEvent arg0) {
				
			}
			
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				return false;
			}
			
			public void onLongPress(MotionEvent arg0) {
				
			}
			
			public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,float arg3) {
				int type = getDirByGes(arg0.getX(), arg0.getY(), arg1.getX(), arg1.getY());
			  //Toast.makeText(MainActivity.this, ""+type, Toast.LENGTH_SHORT).show();
				changeByDir(type);
				return false;
			}
			
			public boolean onDown(MotionEvent arg0) {
				return false;
			}
		});
        setContentView(R.layout.activity_main);
        /**��ʼ����Ϸ�����ɸ�С����*/
        //��ȡһ�Ŵ�ͼ
        Bitmap bigBm = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic5)).getBitmap();
        int tuWandH = bigBm.getWidth()/5;//ÿ��СͼƬ�Ŀ�͸�
        int ivWandH = getWindowManager().getDefaultDisplay().getWidth()/5;//С����Ŀ��Ӧ����������Ļ�Ŀ�/5
        for (int i = 0; i < iv_game_arr.length; i++) {
			for (int j = 0; j < iv_game_arr[0].length; j++) {
				Bitmap bm= Bitmap.createBitmap(bigBm, j*tuWandH, i*tuWandH,tuWandH,tuWandH);//�г��к����г����ɸ�ͼƬ
				
				iv_game_arr[i][j] = new ImageView(this);
				iv_game_arr[i][j].setImageBitmap(bm);//����ÿһ����ϷС�����ͼ��
				//С����Ŀ��Ӧ����������Ļ�Ŀ�/5
				iv_game_arr[i][j].setLayoutParams(new RelativeLayout.LayoutParams(ivWandH, ivWandH));
				//���÷���֮��ļ��
				iv_game_arr[i][j].setPadding(1, 1, 1, 1);
				
				iv_game_arr[i][j].setTag(new GameData(i,j,bm));//���Զ��������
				//����
				iv_game_arr[i][j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						boolean flag=isHasByNullImageView((ImageView)v);
//						Toast.makeText(MainActivity.this, "λ�ù�ϵ�Ƿ����:"+flag, Toast.LENGTH_SHORT).show();
						if(flag){
							changDataByImageView((ImageView) v); 
						}
					}
				});
				
				
			}
		}
        
        /*��ʼ����Ϸ�����棬��������ɸ�С����*/
        gl_main_game = (GridLayout)findViewById(R.id.gl_main_game);
        for (int i = 0; i < iv_game_arr.length; i++) {
        	for (int j = 0; j < iv_game_arr[0].length; j++) {
        		gl_main_game.addView(iv_game_arr[i][j]);
			}
		}
        /**
    	 * �������һ������Ϊ�յ�
    	 */
    	setNullImageView(iv_game_arr[2][4]);
    	/**��ʼ��  �������˳�򷽿�*/
    	randommove();
    	isGameStart=true;//��ʼ״̬
    }
    /**
     * �������Ƶķ��򣬻�ȡ�շ�����Ӧ������λ�ã�������ڷ��飬��ô�������ݽ���
     * @param typr 1:�� 2:�� 3:�� 4:��
     */
    public void changeByDir(int type){
    	changeByDir(type,true);
    }
    /**
     * �������Ƶķ��򣬻�ȡ�շ�����Ӧ������λ�ã�������ڷ��飬��ô�������ݽ���
     * @param type 
     * 				1:�� 2:�� 3:�� 4:��
     * @param isAnim 
     * 				true:�ж��� false:�޶���
     */
    
    public void changeByDir(int type,boolean isAnim){
    	//��ȡ��ǰ�շ����λ��
    	GameData mNullGameData=(GameData) iv_null_ImageView.getTag();
    	//���ݷ���������Ӧ�����ڵ�λ�õ����꣬
    	int new_x = mNullGameData.x;
    	int new_y = mNullGameData.y;
    	if(type==1){//Ҫ�ƶ��ķ����ڵ�ǰ �շ��������
    		new_x++;
    	}else if(type==2){
    		new_x--;
    	}else if(type==3){
    		new_y++;
    	}else if(type==4){
    		new_y--;
    	}
    	//�ж���������꣬�Ƿ����
    	if(new_x>=0&&new_x<iv_game_arr.length&&new_y>=0&&new_y<iv_game_arr[0].length){
    		//���ڵĻ�����ʼ�ƶ�
    		if(isAnim){
    			changDataByImageView(iv_game_arr[new_x][new_y]);
    		}else{
    		//�����ڣ�ֱ�ӽ���
    			changDataByImageView(iv_game_arr[new_x][new_y],isAnim);
    		}
    	}else{
    		//ʲôҲ����
    	}
    }
    /**
     * �ж���Ϸ�����ķ���
     */
    public void isGameOver(){
    	boolean isGameOver=true;
    	//Ҫ����ÿ����Ϸ С����
    	for (int i = 0; i < iv_game_arr.length; i++) {
			for (int j = 0; j < iv_game_arr[0].length; j++) {
				//Ϊ�յķ������ݲ��ж�����
				if(iv_game_arr[i][j]==iv_null_ImageView){
					continue;
				}
				GameData mGameData=(GameData) iv_game_arr[i][j].getTag();
				if(!mGameData.isTrue()){
					isGameOver=false;
					break;
				}
			}
		}
    	//����һ�����ر���������Ϸ�Ƿ����������ʱ����ʾ
    	if(isGameOver){
    		Toast.makeText(this, "��Ϸ����", Toast.LENGTH_SHORT).show();
    	}
    }
    /**
     * �����жϣ������󻬣��������һ�
     * @param start_x ���Ƶ���ʼ��x
     * @param start_y ���Ƶ���ʼ��y
     * @param end_x ���Ƶ���ֹ��x
     * @param end_y ���Ƶ���ֹ��y
     * @return 1:�� 2:�� 3:�� 4:��
     */
    public int getDirByGes(float start_x,float start_y,float end_x,float end_y){
    	boolean isLeftOrRight=(Math.abs(start_x-end_x)>Math.abs(start_y-end_y))?true:false;//�Ƿ�������
    	
    	if(isLeftOrRight){//����
    		boolean isLeft=(start_x-end_x)>0?true:false;//
    		if(isLeft){
    			return 3;
    		}else{
    			return 4;
    		}
    	}else{//����
    		boolean isUp=(start_y-end_y)>0?true:false;//
    		if(isUp){
    			return 1;
    		}else{
    			return 2;
    		}
    	}
    }
    //�������˳�� ������ѭ���Ĵ���
    public void randommove(){
    	//���ҵĴ���
    	for(int i=0;i<100;i++){
    		//�������ƿ�ʼ�������޶���
    		int type=(int)(Math.random()*4)+1;//1��4
    		changeByDir(type, false);
    		
    	}
    	
    }
    /**
     * ���ö�������֮�󣬽����������������
     * @param mImageView
     */
    public void changDataByImageView(final ImageView mImageView){
    	changDataByImageView(mImageView,true);
    	
    }
    /**
     * ���ö�������֮�󣬽����������������
     * @param mImageView
     * 				����ķ���
     * @param isAnim true:�ж���  false:�޶���
     */
    public void changDataByImageView(final ImageView mImageView,boolean isAnim){
    	
    	if(!isAnim){
    		//�޶���Ч��
    		GameData mGameData=(GameData)mImageView.getTag();
			iv_null_ImageView.setImageBitmap(mGameData.bm);
			GameData mNullGameData=(GameData)iv_null_ImageView.getTag();
			mNullGameData.bm=mGameData.bm;
			mNullGameData.p_x=mGameData.p_x;
			mNullGameData.p_y=mGameData.p_y;
			setNullImageView(mImageView);//���õ�ǰ������ǿշ���
			if(isGameStart){
	    		isGameOver();//�ɹ�ʱ���ᵯһ��toast
	    	}
			
    		return;
    	}
    	//����һ�����������ú÷����ƶ��ľ���
    	TranslateAnimation translateAnimation=null;
    	if(mImageView.getX()>iv_null_ImageView.getX()){//��ǰ����ķ����ڿշ�����±�
    		//�����ƶ�
    		translateAnimation = new TranslateAnimation(0.1f, -mImageView.getWidth(),0.1f,0.1f);
    	}else if(mImageView.getX()<iv_null_ImageView.getX()){//��ǰ����ķ����ڿշ�����±�
    		//�����ƶ�
    		translateAnimation = new TranslateAnimation(0.1f, mImageView.getWidth(),0.1f,0.1f);
    	}else if(mImageView.getY()>iv_null_ImageView.getY()){//��ǰ����ķ����ڿշ�����±�
    		//�����ƶ�
    		translateAnimation = new TranslateAnimation(0.1f, 0.1f,0.1f,-mImageView.getWidth());
    	}else if(mImageView.getY()<iv_null_ImageView.getY()){//��ǰ����ķ����ڿշ�����±�
    		//�����ƶ�
    		translateAnimation = new TranslateAnimation(0.1f, 0.1f, 0.1f,mImageView.getWidth());
    	}
    	//���ö�����ʱ��
    	translateAnimation.setDuration(30);
    	//���ö�������֮���Ƿ�ͣ��
    	translateAnimation.setFillAfter(true);
    	//���ö���֮��Ҫ���������ݽ�����   ���ö����ļ���
    	translateAnimation.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation arg0) {
				
			}
			
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			public void onAnimationEnd(Animation arg0) {
				mImageView.clearAnimation();
				GameData mGameData=(GameData)mImageView.getTag();
				iv_null_ImageView.setImageBitmap(mGameData.bm);
				GameData mNullGameData=(GameData)iv_null_ImageView.getTag();
				mNullGameData.bm=mGameData.bm;
				mNullGameData.p_x=mGameData.p_x;
				mNullGameData.p_y=mGameData.p_y;
				setNullImageView(mImageView);//���õ�ǰ������ǿշ���
				if(isGameStart){
		    		isGameOver();//�ɹ�ʱ���ᵯһ��toast
		    	}
			}
		});
    	//ִ�ж���
    	mImageView.startAnimation(translateAnimation);
    	
    }
    /**
     * ����ĳһ������Ϊ�շ���
     * @param mImageView��ǰҪ����Ϊ�յķ��� ��ʵ��
     */
    
    public void setNullImageView(ImageView mImageView){
    	mImageView.setImageBitmap(null);
    	iv_null_ImageView=mImageView;
    }
    /**
     * �жϵ�ǰ����ķ��飬�Ƿ���շ����λ�ù�ϵ�����ڹ�ϵ
     * @param mImageView ������ķ���
     * @return true:���� false:������
     */
    public boolean isHasByNullImageView(ImageView mImageView){
    	//�ֱ��ȡ��ǰ�շ����λ�����������λ�ã�ͨ����x y ���߶���1�ķ�ʽ�ж�
    	GameData mNulllGameData=(GameData)iv_null_ImageView.getTag();
    	GameData mGameData=(GameData)mImageView.getTag();
    	if(mNulllGameData.y==mGameData.y&&mGameData.x+1==mNulllGameData.x){//��ǰ����ķ����ڿշ�����ϱ�
    		return true;
    	}else if(mNulllGameData.y==mGameData.y&&mGameData.x-1==mNulllGameData.x){//��ǰ����ķ����ڿշ�����±�
    		return true;
    	}else if(mNulllGameData.y==mGameData.y+1&&mGameData.x==mNulllGameData.x){//��ǰ����ķ����ڿշ�������
    		return true;
    	}else if(mNulllGameData.y==mGameData.y-1&&mGameData.x==mNulllGameData.x){//��ǰ����ķ����ڿշ�����ұ�
    		return true;
    	}
    	return false;
    }
    /*ÿ����ϷС������Ҫ�󶨵�����*/
    class GameData{
    	/*ÿ��С�����ʵ��λ��x*/
    	public int x=0;
    	/*ÿ��С�����ʵ��λ��y*/
    	public int y=0;
    	/*ÿ��С�����ͼƬ*/
    	public Bitmap bm;
    	/*ÿ��С�����ͼƬ��λ��*/
    	public int p_x=0;
    	/*ÿ��С�����ͼƬ��λ��*/
    	public int p_y=0;
		public GameData(int x, int y, Bitmap bm) {
			super();
			this.x = x;
			this.y = y;
			this.bm = bm;
			this.p_x = x;
			this.p_y = y;
		}
		/**
		 * �ж�ÿ��С�����λ�ã��Ƿ���ȷ
		 * @return true:��ȷ  false:����ȷ
		 */
		public boolean isTrue() {
			if(x==p_x&&y==p_y){
				return true;
			}
			return false;
		}
    }
}

