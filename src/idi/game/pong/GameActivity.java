package idi.game.pong;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import android.app.Activity;

import android.os.Bundle;
import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGL10;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;


import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import android.view.KeyEvent;
import android.view.MotionEvent;

import android.os.SystemClock;

public class GameActivity extends Activity 
{
    //private final static boolean DEBUG_CHECK_GL_ERROR = true;
  
    /** Called when the activity is first created. */
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GameSurfaceView(this);
        mGLSurfaceView.requestFocus();
        mGLSurfaceView.setFocusableInTouchMode(true);
        setContentView(mGLSurfaceView);
        
        /*if (DEBUG_CHECK_GL_ERROR) {
          mGLSurfaceView.setGLWrapper(new GameSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return GLDebugHelper.wrap(gl, GLDebugHelper.CONFIG_CHECK_GL_ERROR, null);
            }});
      }*/
    }

    
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
    

    
	public boolean onKeyDown (int keyCode, KeyEvent event)
    {
      switch(keyCode){  
        case KeyEvent.KEYCODE_BACK:
          try {
            GameActivity.this.finish();
          } catch (Throwable e) {
            e.printStackTrace();
          }  
          break;
      }   
      
      return true;    
    }

    private GameSurfaceView mGLSurfaceView;
}


class GameSurfaceView extends GLSurfaceView 
{  
  private final int   KUP     = 0;
  private final int   KDOWN   = 1;
  private GameRenderer mRenderer;
  private float mPreviousX, mPreviousY;
  private float mSixthWidth, m5SixthWidth;
  private Activity mActivity;
  private GLWrapper mGLWrapper;
  //private Triangle mTriangle;
  //private GLThread mGLThread;
  
  public GameSurfaceView(Activity aContext) 
  {
    super(aContext);
    // Turn on error-checking and logging
    //setDebugFlags(DEBUG_CHECK_GL_ERROR);
    mRenderer = new GameRenderer();
    mRenderer.setActivity(aContext);
    setRenderer(mRenderer);
    mSixthWidth  = getWidth() / 6.0f;
    m5SixthWidth = 5.0f * mSixthWidth;    
    mActivity = aContext;
    //mTriangle = new Triangle();
  }
  
  
public void onPause()
  {
    super.onPause();
  }
  
  public interface GLWrapper {
    GL wrap(GL gl);
  }
  
  public void setGLWrapper(GLWrapper glWrapper) {
   mGLWrapper = glWrapper;
  }
  
  
public void onResume()
  {
    super.onResume();
  }
  
  /*** INPUT ***/
  
  public boolean onTouchEvent(final MotionEvent aEvent) 
  {    
    queueEvent(new Runnable(){
      
	public void run() {
    	if(!mRenderer.matchEnded())
    		mRenderer.movePlayers(aEvent.getX(), aEvent.getY());
    	else
    		mRenderer.restartGame();
    	
    	
      }});
    
    return true;    
  }
  
   
  public boolean onTrackballEvent(final MotionEvent e) 
  {
    float y = e.getY();    
    float dy = y - mPreviousY;
    
    queueEvent(new Runnable(){
      
	public void run() {
        float y = e.getY();    
        float dy = y - mPreviousY;
        mRenderer.movePlayer1(dy);
      }});
        
    mPreviousY = y;
    
    return true;    
  }

  
public boolean onKeyDown (int keyCode, KeyEvent event)
  {
    switch(keyCode){
      case KeyEvent.KEYCODE_DPAD_UP:
        queueEvent(new Runnable(){
          
		public void run() {
            mRenderer.setKey(KUP, true);
          }});        
        break;    
        
      case KeyEvent.KEYCODE_DPAD_DOWN:
        queueEvent(new Runnable(){
          
		public void run() {
            mRenderer.setKey(KDOWN, true);
          }});        
        break;
      case KeyEvent.KEYCODE_BACK:
        mActivity.finish();
        break;
    }   
    
    return true;    
  }
  
  
public boolean onKeyUp (int keyCode, KeyEvent event)
  {
    switch(keyCode){
      case KeyEvent.KEYCODE_DPAD_UP:        
        queueEvent(new Runnable(){
          
		public void run() {
            mRenderer.setKey(KUP, false);
          }});
        break;    
      case KeyEvent.KEYCODE_DPAD_DOWN:
        queueEvent(new Runnable(){
          
		public void run() {
            mRenderer.setKey(KDOWN, false);
          }});        
        break;
    }   
    
    return true;    
  }
  
  private class GameRenderer implements GLSurfaceView.Renderer 
  {
    private final int   KUP                     = 0;
    private final int   KDOWN                   = 1;
    private final int   numKeys                 = 2;
    private final float TRACKBALL_SCALE_FACTOR  = 0.001f;
    private final float KEY_VELOCITY            = 0.0159f;
    private final float TOUCH_VELOCITY          = 0.03f;
    
    private Boolean[] key;
    private BallObject  mBall;
    private float         mRed, mGreen, mBlue, mRatio, mWidth, mHeight, mHalfHeight;
    private StickObject   mPlayer1, mPlayer2;
    private NetObject     mNet;
    private ArrowObject mArrowLeftUp, mArrowRightUp, mArrowLeftDown, mArrowRightDown;
    private float mBoardLeft, mBoardRight, mBoardBottom, mBoardTop;
    private boolean mEndGame;
    private Random randomFloat;
    private Activity mActivity;
    private int upArrowTex;
    private ScoreBoardObject mScoreBoardPlayer1, mScoreBoardPlayer2;
	  private Paint mLabelPaint;
    private int mLabelWinner, mLabelClick, mLabelPlayer, mLabelMsPF;
    private float[] mScratch = new float[8];
    private final static int SAMPLE_PERIOD_FRAMES = 12;
    private final static float SAMPLE_FACTOR = 1.0f / SAMPLE_PERIOD_FRAMES;
    private long mStartTime;
    private Triangle mTriangle;
    private int mFrames;
    private int mMsPerFrame;
    
    
	public void onSurfaceCreated(GL10 aGL, EGLConfig aConfig) 
    {
      mGreen = mBlue = mRed = 0.0f;
      // Enable use of vertex arrays
      aGL.glDisable(GL10.GL_DITHER);
      aGL.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
          GL10.GL_FASTEST);
      aGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      aGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
      aGL.glDisable(GL10.GL_CULL_FACE);
      aGL.glClearColor(.5f, .0f, .5f, 1);
      aGL.glShadeModel(GL10.GL_SMOOTH);
      aGL.glDisable(GL10.GL_DEPTH_TEST);
      aGL.glEnable(GL10.GL_TEXTURE_2D);      
      /*
       * Create our texture. This has to be done each time the
       * surface is created.
       */
      int[] textures = new int[1];
      // Generate N textures (N, array with textures)
      aGL.glGenTextures(1, textures, 0);
      aGL.glEnable(GL10.GL_BLEND);
      aGL.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);


      upArrowTex   = textures[0];
      //int downArrowTex = textures[1];
      //Specifies the kind of texture (1D, 2D..)
      aGL.glBindTexture(GL10.GL_TEXTURE_2D, upArrowTex);
      //aGL.glBindTexture(GL10.GL_TEXTURE_2D, downArrowTex);

      aGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
              GL10.GL_NEAREST);
      aGL.glTexParameterf(GL10.GL_TEXTURE_2D,
              GL10.GL_TEXTURE_MAG_FILTER,
              GL10.GL_LINEAR);

      aGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
              GL10.GL_CLAMP_TO_EDGE);
      aGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
              GL10.GL_CLAMP_TO_EDGE);

      aGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
              GL10.GL_BLEND);      
      

      InputStream is = mActivity.getResources()
              .openRawResource(R.drawable.uparrow);
      Bitmap bitmap;
      try {
          bitmap = BitmapFactory.decodeStream(is);
          
      } finally {
          try {
              is.close();
          } catch(IOException e) {
              // Ignore.
          }
      }
      
      //bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
      GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
      int bmSize = bitmap.getRowBytes() * bitmap.getHeight() * 4; 
      ByteBuffer data = ByteBuffer.allocateDirect(bmSize);
      data.order(ByteOrder.BIG_ENDIAN);
      data.position(0);
      bitmap.copyPixelsToBuffer(data);
      //aGL.glTexImage2D(GL10.GL_TEXTURE_2D, 0, 3, bitmap.getWidth(), bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, data);
      //aGL.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, data);
      //GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap, 0); 
      bitmap.recycle();
      aGL.glDisable(GL10.GL_TEXTURE_2D);
      mPlayer1        	 = new StickObject(); //Izquierda
      mPlayer2        	 = new StickObject(); //Derecha
      mNet            	 = new NetObject();
      mBall           	 = new BallObject();
      mArrowLeftUp    	 = new ArrowObject(upArrowTex);
      mArrowRightUp   	 = new ArrowObject(upArrowTex);
      mArrowLeftDown  	 = new ArrowObject(upArrowTex);
      mArrowRightDown 	 = new ArrowObject(upArrowTex);
      mScoreBoardPlayer1 = new ScoreBoardObject();
      mScoreBoardPlayer2 = new ScoreBoardObject();
      mTriangle 		 = new Triangle();

      //mProjector = new Projector();
      key             = new Boolean[numKeys];
      for (int i = 0; i < numKeys; ++i)
        key[i] = false;
      mEndGame = false;
      randomFloat = new Random();           
      
    }
    

    public void movePlayers(float x, float y)
    {
      if (x <= mSixthWidth) {
        float dy = TOUCH_VELOCITY;
        if ( y >= mHalfHeight )
          dy = -dy;
        
        mPlayer2.move(dy);
      }
      if (x >= m5SixthWidth) {
        float dy = TOUCH_VELOCITY;
        if ( y >= mHalfHeight )
          dy = -dy;
        
        mPlayer1.move(dy);
      }      	
    }
    
    public void setActivity (Activity aContext)
    {
      mActivity = aContext;
    }
    
    public void movePlayer1(float dx)
    { 
      mPlayer1.move(dx * TRACKBALL_SCALE_FACTOR);    
    }
    
    
	public void onSurfaceChanged(GL10 aGL, int aWidth, int aHeight) 
    
    {
      aGL.glViewport(0, 0, aWidth, aHeight);  

      // make adjustments for screen ratio
      mWidth       = aWidth;
      mHeight      = aHeight;
      mHalfHeight  = aHeight/2.0f;
      mSixthWidth  = mWidth / 6.0f;
      m5SixthWidth = 5.0f * mSixthWidth;
      mRatio = (float) aWidth / aHeight;
      aGL.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
      aGL.glLoadIdentity();                        // reset the matrix to its default state
      //aGL.glFrustumf(-mRatio, mRatio, -1, 1, 3, 7);  // apply the projection matrix
      aGL.glOrthof(-mRatio, mRatio, -1f, 1f, 3f, 7f);      
      float[] position = { (mRatio/3.0f)* 2.0f - mPlayer1.getWidth(), 1.0f, 0.0f };
      mBoardLeft = position[0];
      mPlayer2.setPosition(position);
      position[0] = -(mRatio/3.0f) * 2.0f;
      mPlayer1.setPosition(position);
      mBoardRight   = position[0];
      mBoardBottom = -1.0f;
      mBoardTop    = 1.0f;
      float[] arrowPosition = { (mRatio/3.0f)* 2.0f, 1.0f, 0.0f };
      Boolean down = false;
      mArrowLeftUp.init(mRatio/3.0f, down);
      mArrowLeftUp.setPosition(arrowPosition);
      
      down = true;
      arrowPosition[1] = -0.4f;
      mArrowLeftDown.init(mRatio/3.0f, down);
      mArrowLeftDown.setPosition(arrowPosition);
      ///---
      arrowPosition[0] = -mRatio;   
      ///---
      down = false;
      arrowPosition[1] = 1.0f;
      mArrowRightUp.init(mRatio/3.0f, down);   
      mArrowRightUp.setPosition(arrowPosition);

      down = true;
      arrowPosition[1] = -0.4f;
      mArrowRightDown.init(mRatio/3.0f, down);
      mArrowRightDown.setPosition(arrowPosition);      
      
      float[] scorePosition = { -mRatio / 3.0f, 0.9f, 0.0f };
      mScoreBoardPlayer1.setPosition(scorePosition);
      scorePosition[0] = mRatio / 3.0f;
      mScoreBoardPlayer2.setPosition(scorePosition);
      
    }
      
    public void setColor(float r, float g, float b) 
    {
      mRed   = r;
      mGreen = g;
      mBlue  = b;
    }
    
    public void setKey(int aKey, Boolean aStatus)
    {
      key[aKey] = aStatus;
    }
    
    public void checkCollisions()
    {    
      float[] ballPosition = mBall.getPosition();
      float[] player1Position, player2Position;      
      boolean topCollision, bottomCollision, leftCollision, rightCollision, stickLeftCollision, stickRightCollision;
      bottomCollision = rightCollision = stickRightCollision = false;
      topCollision    = ballPosition[1] > mBoardTop;      
      if (!topCollision)
        bottomCollision = ballPosition[1] < mBoardBottom;
      
      player2Position    = mPlayer2.getPosition();
      leftCollision      = ballPosition[0] > mBoardLeft  - mPlayer2.getWidth();
      stickLeftCollision = ballPosition[1] >= player2Position[1] - mPlayer2.getHeight() &&
                           ballPosition[1] <= player2Position[1];
        
      if (!leftCollision && !stickLeftCollision) {
        player1Position = mPlayer1.getPosition();
        rightCollision  = ballPosition[0] < mBoardRight + mPlayer1.getWidth();
        stickRightCollision = ballPosition[1] >= player1Position[1] - mPlayer1.getHeight() &&
                             ballPosition[1] <= player1Position[1];
      }
      
      if (!mEndGame) {
        if (topCollision) {
          ballPosition[1] = mBoardTop;
          mBall.setMovementY(-mBall.getMovementY());
          mBall.setPosition(ballPosition);
        }
        
        if (bottomCollision) {
          ballPosition[1] = mBoardBottom;
          mBall.setMovementY(-mBall.getMovementY());
          mBall.setPosition(ballPosition);
        }
        
        if (leftCollision && !stickLeftCollision) {
          float[] pos = {0.0f, 0.0f, 0.0f};
          mBall.setPosition(pos);
          if(mScoreBoardPlayer1.getScore() < 9)
        	  mScoreBoardPlayer1.addScore();
          else
        	  mEndGame = true;
        }
          
        if (leftCollision && stickLeftCollision) {
          float dx  = -mBall.getMovementX();
          float dy  = mBall.getMovementY();
          
          if (dx >= 0.0f)
            dx += mBall.VELOCITY;
          else
            dx -= mBall.VELOCITY;
          if (dy >= 0.0f)
            dy += mBall.VELOCITY;
          else 
            dy -= mBall.VELOCITY;
          
          ballPosition[0] = mBoardLeft - mPlayer2.getWidth();
  
          mBall.setMovementX(dx);
          mBall.setMovementY(dy);
          mBall.setPosition(ballPosition);
          goPsyche();          
        }
        
        if (rightCollision && !stickRightCollision) {    
          float[] pos = {0.0f, 0.0f, 0.0f};
          mBall.setPosition(pos);
          if(mScoreBoardPlayer2.getScore() < 9)
        	  mScoreBoardPlayer2.addScore();
          else
        	  mEndGame = true;
        }
        
        if (rightCollision && stickRightCollision) {
          float dx  = -mBall.getMovementX();
          float dy = mBall.getMovementY();
          
          if (dx >= 0.0f)
            dx += mBall.VELOCITY;
          else
            dx -= mBall.VELOCITY;
          if (dy >= 0.0f)
            dy += mBall.VELOCITY;
          else 
            dy -= mBall.VELOCITY;
          
          ballPosition[0] = mBoardRight + mPlayer1.getWidth();          
          mBall.setMovementX(dx);
          mBall.setMovementY(dy);
          mBall.setPosition(ballPosition);
          goPsyche();
          //mScoreBoardPlayer1.addScore();
        }
      }
    }
    
    public void goPsyche()
    {      
      mRed   = randomFloat.nextFloat();
      mGreen = randomFloat.nextFloat();
      mBlue  = randomFloat.nextFloat();
    }
    
	public void onDrawFrame(GL10 aGL) 
    {
      listenKeyboard();
      checkCollisions();      
      mBall.move();
      //aGL.glDisable(GL10.GL_TEXTURE_2D);
      aGL.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
          GL10.GL_MODULATE);
      aGL.glClearColor(mRed, mBlue, mGreen, 1.0f);
      aGL.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);      
      // Set GL_MODELVIEW transformation mode
      aGL.glMatrixMode(GL10.GL_MODELVIEW);
      aGL.glLoadIdentity();
      //OBS/VRP/UP
      GLU.gluLookAt(aGL, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
      //aGL.glTranslatef(mRatio = -1.0f = 0.0f);
      mPlayer1.draw(aGL);
      float[] position = mPlayer1.getPosition();
      mPlayer2.draw(aGL);
      mBall.draw(aGL);
      mNet.draw(aGL);
      //mTriangle.draw(aGL);
      mArrowLeftUp.draw(aGL);
      mArrowLeftDown.draw(aGL);
      mArrowRightUp.draw(aGL);
      mArrowRightDown.draw(aGL);
      mScoreBoardPlayer1.draw(aGL);
      mScoreBoardPlayer2.draw(aGL); 

    } 
    
    public Boolean matchEnded()
    {
    	return mEndGame;
    }
    
    public void restartGame() {
    	mScoreBoardPlayer1.setScore(0);
    	mScoreBoardPlayer2.setScore(0);
    	float[] pos = {0.0f, 0.0f, 0.0f};
        mBall.setPosition(pos);
        mBall.setMovementX(0.01f);
        mBall.setMovementY(0.01f);
        mEndGame = false;
    }
    
    private void listenKeyboard()
    {      
      if(key[KUP]) {
        mPlayer1.move(KEY_VELOCITY);
      }
      if(key[KDOWN]) {
        mPlayer1.move(-KEY_VELOCITY);
      }
    }
    

    /*private void drawMsPF(GL10 gl, float rightMargin) 
    {
        long time = SystemClock.uptimeMillis();
        if (mStartTime == 0) {
            mStartTime = time;
        }
        if (mFrames++ == SAMPLE_PERIOD_FRAMES) {
            mFrames = 0;
            long delta = time - mStartTime;
            mStartTime = time;
            mMsPerFrame = (int) (delta * SAMPLE_FACTOR);
        }
        if (mMsPerFrame > 0) {
            mNumericSprite.setValue(mMsPerFrame);
            float numWidth = mNumericSprite.width();
            float x = rightMargin - numWidth;
            mNumericSprite.draw(gl, x, 0, mWidth, mHeight);
        }
    }*/

    /*private void drawLabel(GL10 gl, int triangleVertex, int labelId) 
    {
        float x = mTriangle.getX(triangleVertex);
        float y = mTriangle.getY(triangleVertex);
        mScratch[0] = x;
        mScratch[1] = y;
        mScratch[2] = 0.0f;
        mScratch[3] = 1.0f;
        mProjector.project(mScratch, 0, mScratch, 4);
        float sx = mScratch[4];
        float sy = mScratch[5];
        float height = mLabels.getHeight(labelId);
        float width = mLabels.getWidth(labelId);
        float tx = sx - width * 0.5f;
        float ty = sy - height * 0.5f;
        mLabels.draw(gl, tx, ty, labelId);
    }*/
    
  }
}

