package idi.game.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.util.Log;


public class ScoreBoardObject
{  
  private FloatBuffer mVertexBuffer;
  private float[] mPosition;
  private float mWidth, mHeight, mPaddingX;
  private int mScore;
  private boolean mMatchStarted;

  private float dy_;
  
  public ScoreBoardObject() 
  {
    mScore = 0;	  
    mWidth  = 0.025f;
    mHeight = 0.20f;      
    mPosition = new float[3];
    mPosition[0] = 0.0f;
    mPosition[1] = 0.0f;
    mPosition[2] = 0.0f;    
    dy_ = 0.0f;
    mPaddingX = 0.10f;
    setScore(mScore);
  } 
  
  public void setScore(int score)
  {	  	  	  
	  mScore = score;
	  switch(score)
	  {	  
	  	case 0: {
	  		/*
	  		 * 	-- top left / top right
	  		 * | left top
	  		 * | left bottom
	  		 * 	-- bottom left / bottom right
	  		 * 
	  		 */
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,

	  				
  	  		  //Right	
  		      mPaddingX ,  0.0f , 0,
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mPaddingX , -mHeight , 0,
  		      
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mWidth + mPaddingX , -mHeight , 0,
  		      mPaddingX , -mHeight , 0,	     
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	    }
	  	case 1: {
	  		float[] vertices = {
  	  		  //Right	
  		      mPaddingX ,  0.0f , 0,
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mPaddingX , -mHeight , 0,
  		      
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mWidth + mPaddingX , -mHeight , 0,
  		      mPaddingX , -mHeight , 0,	     
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	  	}
	  	case 2: {
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,

	  				
  	  		  //Right	
  		      mPaddingX , -mHeight/2.0f, 0,
  		      mWidth + mPaddingX , -mHeight/2.0f, 0,
  		      mPaddingX , -mHeight , 0,
  		      
  		      mWidth + mPaddingX , -mHeight/2.0f, 0,
  		      mWidth + mPaddingX , -mHeight , 0,
  		      mPaddingX , -mHeight , 0,	     
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight/2.0f , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight/2.0f, 0,
		      0.0f , -mHeight/2.0f , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	  	}
	  	case 3: {
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	     
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	  	}
	  	case 4:{
	  		/*
	  		 * 	-- top left / top right
	  		 * | left top
	  		 * | left bottom
	  		 * 	-- bottom left / bottom right
	  		 * 
	  		 */
	  		float[] vertices = {
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	  				
  	  		  //Right	
  		      mPaddingX ,  0.0f , 0,
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mPaddingX , -mHeight/2.0f , 0,
  		      
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mWidth + mPaddingX , -mHeight/2.0f , 0,
  		      mPaddingX , -mHeight/2.0f , 0,	     
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	    }

	  	case 5: {
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,

	  				
  	  		  //Right	
  		      mPaddingX , 0.0f, 0,
  		      mWidth + mPaddingX , 0, 0,
  		      mPaddingX , -mHeight/2.0f , 0,
  		      
  		      mWidth + mPaddingX , 0, 0,
  		      mWidth + mPaddingX , -mHeight/2.0f , 0,
  		      mPaddingX , -mHeight/2.0f , 0,	     
	  		  //Left	
		      0.0f , -mHeight/2.0f , 0,
		      mWidth , -mHeight/2.0f, 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , -mHeight/2.0f, 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight, 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	  	}
	  	case 6:{
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,

	  				
  	  		  //Right	
  		      mPaddingX , 0.0f, 0,
  		      mWidth + mPaddingX , 0, 0,
  		      mPaddingX , -mHeight , 0,
  		      
  		      mWidth + mPaddingX , 0, 0,
  		      mWidth + mPaddingX , -mHeight, 0,
  		      mPaddingX , -mHeight, 0,	     
	  		  //Left	
		      0.0f , -mHeight/2.0f , 0,
		      mWidth , -mHeight/2.0f, 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , -mHeight/2.0f, 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight, 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	  	}
	  	case 7: {
	  		/*
	  		 * 	-- top left / top right
	  		 * | left top
	  		 * | left bottom
	  		 * 	-- bottom left / bottom right
	  		 * 
	  		 */
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,  
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	    }
	  	case 8:{
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,

	  				
  	  		  //Right	
  		      mPaddingX ,  0.0f , 0,
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mPaddingX , -mHeight , 0,
  		      
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mWidth + mPaddingX , -mHeight , 0,
  		      mPaddingX , -mHeight , 0,	     
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate	
	  	
	  		break;
	  	}
	  	case 9: {
	  		/*
	  		 * 	-- top left / top right
	  		 * | left top
	  		 * | left bottom
	  		 * 	-- bottom left / bottom right
	  		 * 
	  		 */
	  		float[] vertices = {
	  				//Top
	  				0.0f,  0.0f, 0,
	  				0, mWidth, 0,
	  				mPaddingX + mWidth , 0.0f, 0,
  
	  				0, mWidth, 0,
	  				mPaddingX + mWidth, mWidth, 0,
	  				mPaddingX + mWidth, 0.0f, 0,
	  				//Middle
	  				0.0f,  -mHeight/2.0f, 0,
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth , -mHeight/2.0f, 0,
  
	  				0, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, mWidth -mHeight/2.0f, 0,
	  				mPaddingX + mWidth, -mHeight/2.0f, 0,
	
	  				//Down
	  				0.0f,  -mHeight, 0,
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth , -mHeight, 0,
  
	  				0, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, mWidth -mHeight, 0,
	  				mPaddingX + mWidth, -mHeight, 0,

	  				
  	  		  //Right	
  		      mPaddingX ,  0.0f , 0,
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mPaddingX , -mHeight/2.0f , 0,
  		      
  		      mWidth + mPaddingX , 0.0f , 0,
  		      mWidth + mPaddingX , -mHeight/2.0f , 0,
  		      mPaddingX , -mHeight/2.0f , 0,	     
	  		  //Left	
		      0.0f ,  0.0f , 0,
		      mWidth , 0.0f , 0,
		      0.0f , -mHeight , 0,
		      
		      mWidth , 0.0f , 0,
		      mWidth , -mHeight, 0,
		      0.0f , -mHeight , 0
		    };
		    
		    int size = vertices.length;
		    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
		    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
		    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
	  		break;
	    }
	  }
  }

  public void addScore()
  {
	  //mScore++;
	  setScore(mScore + 1);
  }
  
  public int getScore()
  {	  
	  return mScore;
  }
  
  public void setPosition(float[] aPosition)
  {
    mPosition[0] = aPosition[0];
    mPosition[1] = aPosition[1];
    mPosition[2] = aPosition[2];
  }
  
  public float getWidth()
  {
    return mWidth;
  }
  
  public float getHeight()
  {
    return mHeight;
  }
  
  public FloatBuffer getVertexBuffer()
  {
    return mVertexBuffer;
  }


  public float[] getPosition()
  {    
    float[] actualPosition = new float[3];
    
    actualPosition[0] = mPosition[0];
    actualPosition[1] = mPosition[1] + dy_;
    actualPosition[2] = mPosition[2];
    
    return actualPosition;
  }

  public void draw(GL10 aGL)
  {
    aGL.glMatrixMode(GL10.GL_MODELVIEW);
    aGL.glPushMatrix();
    //aGL.glFrontFace(GL10.GL_CW);
    aGL.glTranslatef(mPosition[0], mPosition[1] + dy_, mPosition[2]);
    aGL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    aGL.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
    aGL.glDrawArrays(GL10.GL_TRIANGLES, 0, mVertexBuffer.capacity()/3);
    aGL.glPopMatrix();
  }

  public void move(float dy)
  {    
    Boolean isTop    = mPosition[1] + dy_ + dy > 1.0f;
    Boolean isBottom = mPosition[1] + dy + dy_ < -1 + mHeight;
    
    if (!isTop  && !isBottom)
      dy_ += dy;
  }

}

