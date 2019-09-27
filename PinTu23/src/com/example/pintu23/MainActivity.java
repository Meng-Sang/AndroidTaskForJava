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
	/**判断游戏是否开始*/
	private boolean isGameStart=false;
	/**利用二维数组创建若干个游戏小方块*/
	private ImageView[][] iv_game_arr = new ImageView[3][5];
	/**游戏主界面*/
	private GridLayout gl_main_game;
	/**当前空方块的实例的保存*/
	private ImageView iv_null_ImageView;
	/**当前手势*/
	private GestureDetector mDetector;
	
	@Override//手势监听
	public boolean onTouchEvent(MotionEvent event) {
		return mDetector.onTouchEvent(event);
	}
	
	@Override//视图上半部分移动
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
        /**初始化游戏的若干个小方块*/
        //获取一张大图
        Bitmap bigBm = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic5)).getBitmap();
        int tuWandH = bigBm.getWidth()/5;//每个小图片的宽和高
        int ivWandH = getWindowManager().getDefaultDisplay().getWidth()/5;//小方块的宽高应该是整个屏幕的宽/5
        for (int i = 0; i < iv_game_arr.length; i++) {
			for (int j = 0; j < iv_game_arr[0].length; j++) {
				Bitmap bm= Bitmap.createBitmap(bigBm, j*tuWandH, i*tuWandH,tuWandH,tuWandH);//切成行和列切成若干个图片
				
				iv_game_arr[i][j] = new ImageView(this);
				iv_game_arr[i][j].setImageBitmap(bm);//设置每一个游戏小方块的图案
				//小方块的宽高应该是整个屏幕的宽/5
				iv_game_arr[i][j].setLayoutParams(new RelativeLayout.LayoutParams(ivWandH, ivWandH));
				//设置方块之间的间距
				iv_game_arr[i][j].setPadding(1, 1, 1, 1);
				
				iv_game_arr[i][j].setTag(new GameData(i,j,bm));//绑定自定义的数据
				//监听
				iv_game_arr[i][j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						boolean flag=isHasByNullImageView((ImageView)v);
//						Toast.makeText(MainActivity.this, "位置关系是否存在:"+flag, Toast.LENGTH_SHORT).show();
						if(flag){
							changDataByImageView((ImageView) v); 
						}
					}
				});
				
				
			}
		}
        
        /*初始化游戏主界面，并添加若干个小方块*/
        gl_main_game = (GridLayout)findViewById(R.id.gl_main_game);
        for (int i = 0; i < iv_game_arr.length; i++) {
        	for (int j = 0; j < iv_game_arr[0].length; j++) {
        		gl_main_game.addView(iv_game_arr[i][j]);
			}
		}
        /**
    	 * 设置最后一个方块为空的
    	 */
    	setNullImageView(iv_game_arr[2][4]);
    	/**初始化  随机打乱顺序方块*/
    	randommove();
    	isGameStart=true;//开始状态
    }
    /**
     * 根据手势的方向，获取空方块相应的相邻位置，如果存在方块，那么进行数据交换
     * @param typr 1:上 2:下 3:左 4:右
     */
    public void changeByDir(int type){
    	changeByDir(type,true);
    }
    /**
     * 根据手势的方向，获取空方块相应的相邻位置，如果存在方块，那么进行数据交换
     * @param type 
     * 				1:上 2:下 3:左 4:右
     * @param isAnim 
     * 				true:有动画 false:无动画
     */
    
    public void changeByDir(int type,boolean isAnim){
    	//获取当前空方块的位置
    	GameData mNullGameData=(GameData) iv_null_ImageView.getTag();
    	//根据方向，设置相应的相邻的位置的坐标，
    	int new_x = mNullGameData.x;
    	int new_y = mNullGameData.y;
    	if(type==1){//要移动的方块在当前 空方块的下面
    		new_x++;
    	}else if(type==2){
    		new_x--;
    	}else if(type==3){
    		new_y++;
    	}else if(type==4){
    		new_y--;
    	}
    	//判断这个新坐标，是否存在
    	if(new_x>=0&&new_x<iv_game_arr.length&&new_y>=0&&new_y<iv_game_arr[0].length){
    		//存在的话，开始移动
    		if(isAnim){
    			changDataByImageView(iv_game_arr[new_x][new_y]);
    		}else{
    		//不存在，直接交换
    			changDataByImageView(iv_game_arr[new_x][new_y],isAnim);
    		}
    	}else{
    		//什么也不做
    	}
    }
    /**
     * 判断游戏结束的方法
     */
    public void isGameOver(){
    	boolean isGameOver=true;
    	//要遍历每个游戏 小方块
    	for (int i = 0; i < iv_game_arr.length; i++) {
			for (int j = 0; j < iv_game_arr[0].length; j++) {
				//为空的方块数据不判断跳过
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
    	//根据一个开关变量决定游戏是否结束，结束时给提示
    	if(isGameOver){
    		Toast.makeText(this, "游戏结束", Toast.LENGTH_SHORT).show();
    	}
    }
    /**
     * 手势判断，是向左滑，还是向右滑
     * @param start_x 手势的起始点x
     * @param start_y 手势的起始点y
     * @param end_x 手势的终止点x
     * @param end_y 手势的终止点y
     * @return 1:上 2:下 3:左 4:右
     */
    public int getDirByGes(float start_x,float start_y,float end_x,float end_y){
    	boolean isLeftOrRight=(Math.abs(start_x-end_x)>Math.abs(start_y-end_y))?true:false;//是否是左右
    	
    	if(isLeftOrRight){//左右
    		boolean isLeft=(start_x-end_x)>0?true:false;//
    		if(isLeft){
    			return 3;
    		}else{
    			return 4;
    		}
    	}else{//上下
    		boolean isUp=(start_y-end_y)>0?true:false;//
    		if(isUp){
    			return 1;
    		}else{
    			return 2;
    		}
    	}
    }
    //随机打乱顺序 里面是循环的次数
    public void randommove(){
    	//打乱的次数
    	for(int i=0;i<100;i++){
    		//根据手势开始交换，无动画
    		int type=(int)(Math.random()*4)+1;//1到4
    		changeByDir(type, false);
    		
    	}
    	
    }
    /**
     * 利用动画结束之后，交换两个方块的数据
     * @param mImageView
     */
    public void changDataByImageView(final ImageView mImageView){
    	changDataByImageView(mImageView,true);
    	
    }
    /**
     * 利用动画结束之后，交换两个方块的数据
     * @param mImageView
     * 				点击的方块
     * @param isAnim true:有动画  false:无动画
     */
    public void changDataByImageView(final ImageView mImageView,boolean isAnim){
    	
    	if(!isAnim){
    		//无动画效果
    		GameData mGameData=(GameData)mImageView.getTag();
			iv_null_ImageView.setImageBitmap(mGameData.bm);
			GameData mNullGameData=(GameData)iv_null_ImageView.getTag();
			mNullGameData.bm=mGameData.bm;
			mNullGameData.p_x=mGameData.p_x;
			mNullGameData.p_y=mGameData.p_y;
			setNullImageView(mImageView);//设置当前点击的是空方块
			if(isGameStart){
	    		isGameOver();//成功时，会弹一个toast
	    	}
			
    		return;
    	}
    	//创建一个动画，设置好方向，移动的距离
    	TranslateAnimation translateAnimation=null;
    	if(mImageView.getX()>iv_null_ImageView.getX()){//当前点击的方块在空方块的下边
    		//往上移动
    		translateAnimation = new TranslateAnimation(0.1f, -mImageView.getWidth(),0.1f,0.1f);
    	}else if(mImageView.getX()<iv_null_ImageView.getX()){//当前点击的方块在空方块的下边
    		//往下移动
    		translateAnimation = new TranslateAnimation(0.1f, mImageView.getWidth(),0.1f,0.1f);
    	}else if(mImageView.getY()>iv_null_ImageView.getY()){//当前点击的方块在空方块的下边
    		//往左移动
    		translateAnimation = new TranslateAnimation(0.1f, 0.1f,0.1f,-mImageView.getWidth());
    	}else if(mImageView.getY()<iv_null_ImageView.getY()){//当前点击的方块在空方块的下边
    		//往右移动
    		translateAnimation = new TranslateAnimation(0.1f, 0.1f, 0.1f,mImageView.getWidth());
    	}
    	//设置动画的时长
    	translateAnimation.setDuration(30);
    	//设置动画结束之后是否停留
    	translateAnimation.setFillAfter(true);
    	//设置动画之后要真正把数据交换了   设置动画的监听
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
				setNullImageView(mImageView);//设置当前点击的是空方块
				if(isGameStart){
		    		isGameOver();//成功时，会弹一个toast
		    	}
			}
		});
    	//执行动画
    	mImageView.startAnimation(translateAnimation);
    	
    }
    /**
     * 设置某一个方块为空方块
     * @param mImageView当前要设置为空的方块 的实例
     */
    
    public void setNullImageView(ImageView mImageView){
    	mImageView.setImageBitmap(null);
    	iv_null_ImageView=mImageView;
    }
    /**
     * 判断当前点击的方块，是否与空方块的位置关系是相邻关系
     * @param mImageView 所点击的方块
     * @return true:相邻 false:不相邻
     */
    public boolean isHasByNullImageView(ImageView mImageView){
    	//分别获取当前空方块的位置与点击方块的位置，通过想x y 两边都差1的方式判断
    	GameData mNulllGameData=(GameData)iv_null_ImageView.getTag();
    	GameData mGameData=(GameData)mImageView.getTag();
    	if(mNulllGameData.y==mGameData.y&&mGameData.x+1==mNulllGameData.x){//当前点击的方块在空方块的上边
    		return true;
    	}else if(mNulllGameData.y==mGameData.y&&mGameData.x-1==mNulllGameData.x){//当前点击的方块在空方块的下边
    		return true;
    	}else if(mNulllGameData.y==mGameData.y+1&&mGameData.x==mNulllGameData.x){//当前点击的方块在空方块的左边
    		return true;
    	}else if(mNulllGameData.y==mGameData.y-1&&mGameData.x==mNulllGameData.x){//当前点击的方块在空方块的右边
    		return true;
    	}
    	return false;
    }
    /*每个游戏小方块上要绑定的数据*/
    class GameData{
    	/*每个小方块的实际位置x*/
    	public int x=0;
    	/*每个小方块的实际位置y*/
    	public int y=0;
    	/*每个小方块的图片*/
    	public Bitmap bm;
    	/*每个小方块的图片的位置*/
    	public int p_x=0;
    	/*每个小方块的图片的位置*/
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
		 * 判断每个小方块的位置，是否正确
		 * @return true:正确  false:不正确
		 */
		public boolean isTrue() {
			if(x==p_x&&y==p_y){
				return true;
			}
			return false;
		}
    }
}

