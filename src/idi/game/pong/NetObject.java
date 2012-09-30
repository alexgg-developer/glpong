package idi.game.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.util.Log;


public class NetObject
{
  private FloatBuffer mVertexBuffer;
  private float[] mPosition;
  private float mWidth, mHeight;
  
  public NetObject() 
  {
    
    mWidth  = 0.025f;
    mHeight = 0.075f;   
    float space = 0.025f;
    float mHalfWidth  = 0.0125f;
    float mHalfHeight = 0.0375f;
    
    int numQuads = 22;
    float[] vertices = new float[numQuads*6*3];
    for(int i = 0; i < numQuads/2; i++) {
      vertices[0 + 18*i] = -mHalfWidth;
      vertices[1 + 18*i] = mHalfHeight - (space + mHeight) * i;
      vertices[2 + 18*i] = 0.0f;
      
      vertices[3 + 18*i] = mHalfWidth;
      vertices[4 + 18*i] = mHalfHeight - (space + mHeight) * i;
      vertices[5 + 18*i] = 0.0f;
      
      vertices[6 + 18*i] = -mHalfWidth;
      vertices[7 + 18*i] = -mHalfHeight - (space + mHeight) * i;
      vertices[8 + 18*i] = 0.0f;
      
      vertices[9 + 18*i] = mHalfWidth;
      vertices[10 + 18*i] = mHalfHeight - (space + mHeight) * i;
      vertices[11 + 18*i] = 0.0f;
      
      vertices[12 + 18*i] = mHalfWidth;
      vertices[13 + 18*i] = -mHalfHeight - (space + mHeight) * i;
      vertices[14 + 18*i] = 0.0f;
      
      vertices[15 + 18*i] = -mHalfWidth;
      vertices[16 + 18*i] = -mHalfHeight - (space + mHeight) * i;
      vertices[17 + 18*i] = 0.0f;
    }
    
    for(int i = 11; i < numQuads; i++) {
      vertices[0 + 18*i] = -mHalfWidth;
      vertices[1 + 18*i] = mHalfHeight + (space + mHeight) * (i-10);
      vertices[2 + 18*i] = 0.0f;
      
      vertices[3 + 18*i] = mHalfWidth;
      vertices[4 + 18*i] = mHalfHeight + (space + mHeight) * (i-10);
      vertices[5 + 18*i] = 0.0f;
      
      vertices[6 + 18*i] = -mHalfWidth;
      vertices[7 + 18*i] = -mHalfHeight + (space + mHeight) * (i-10);
      vertices[8 + 18*i] = 0.0f;
      
      vertices[9 + 18*i] = mHalfWidth;
      vertices[10 + 18*i] = mHalfHeight + (space + mHeight) * (i-10);
      vertices[11 + 18*i] = 0.0f;
      
      vertices[12 + 18*i] = mHalfWidth;
      vertices[13 + 18*i] = -mHalfHeight + (space + mHeight) * (i-10);
      vertices[14 + 18*i] = 0.0f;
      
      vertices[15 + 18*i] = -mHalfWidth;
      vertices[16 + 18*i] = -mHalfHeight + (space + mHeight) * (i-10);
      vertices[17 + 18*i] = 0.0f;
    }
    
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
