package idi.game.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.util.Log;


public class BallObject
{
  public final float VELOCITY = 0.0001f;
  
  private FloatBuffer mVertexBuffer;
  private float[] mPosition;
  private float mWidth, mHeight;
  private float dx_, dy_;
  
  public BallObject() 
  {    
    mWidth  = 0.025f;
    mHeight = 0.025f;   
    
    float[] vertices = {
      0.0f ,  0.0f , 0,
      mWidth , 0.0f , 0,
      0.0f , -mHeight , 0,
      
      mWidth , 0.0f , 0,
      mWidth , -mHeight , 0,
      0.0f , -mHeight , 0
    };
    
    int size = vertices.length;
    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
   
    mPosition = new float[3];
    mPosition[0] = 0.0f;
    mPosition[1] = 0.0f;
    mPosition[2] = 0.0f;
    
    dx_ = dy_ = 0.01f;
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
    return mPosition;
  }

  public void move()
  {
    mPosition[0] += dx_;
    mPosition[1] += dy_;
  }
  
  float getMovementX ()
  {
    return dx_;
  }
  
  float getMovementY ()
  {
    return dy_;
  }
  
  void setMovementY(float dy)
  {
    dy_ = dy;
  }
  
  void setMovementX(float dx)
  {
    dx_ = dx;
  }
  
  public void draw(GL10 aGL)
  {
    aGL.glMatrixMode(GL10.GL_MODELVIEW);
    aGL.glPushMatrix();
    aGL.glTranslatef(mPosition[0], mPosition[1], mPosition[2]);
    aGL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    aGL.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
    aGL.glDrawArrays(GL10.GL_TRIANGLES, 0, mVertexBuffer.capacity()/3);
    aGL.glPopMatrix();
  }

}
