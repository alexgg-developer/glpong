package idi.game.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.util.Log;


public class ArrowObject
{
  
  private FloatBuffer mVertexBuffer, mTexBuffer;
  private float[] mPosition;
  private float mWidth, mHeight;
  public int mTextureID;

  private float dy_;
  private ShortBuffer mIndexBuffer;
  
  public ArrowObject(int aTextureID) 
  {
	  mTextureID = aTextureID;
  }
  
  void init(float aWidth, Boolean upsidedown) {
    mHeight = 0.6f;   
    mWidth  = aWidth;
    
    /*float[] vertices = {
      0.0f ,  0.0f , 0,
      mWidth , 0.0f , 0,
      0.0f , -mHeight , 0,
      
      mWidth , 0.0f , 0,
      mWidth , -mHeight , 0,
      0.0f , -mHeight , 0
    };*/
    
   float[] vertices = {
    0.0f , -mHeight , 0, //b-l
    mWidth , -mHeight , 0, //b-r
    0.0f ,  0.0f , 0,   //t-l 
    mWidth , 0.0f , 0 //t-r
  }; 
   
   
   /*float[] vertices = {
			-1.0f, -1.0f,  1.0f,
			1.0f, -1.0f,  1.0f,
			1.0f,  1.0f,  1.0f,
			-1.0f,  1.0f,  1.0f  
		  }; */
   
  /*float[] texCoords = {
    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,
    0.0f, 1.0f
  };*/
   
  /*float[] texCoords = {
    0.0f, 0.0f, 
    1.0f, 0.0f,
    0.0f, 1.0f,
    1.0f, 1.0f
  };*/
   float[] texCoords = new float[8];
   
   if (!upsidedown) {
	  texCoords[0] = texCoords[4] = texCoords[5] = texCoords[7] = 0.0f;    
	  texCoords[1] = texCoords[2] = texCoords[3] = texCoords[6] = 1.0f;	  
   }
   else {
	   texCoords[0] = texCoords[1] = texCoords[3] = texCoords[4] = 0.0f;    
	   texCoords[2] = texCoords[5] = texCoords[6] = texCoords[7] = 1.0f;
   }
    
   
   /*float[] vertices = {
     // X, Y, Z
     -0.5f, -0.5f, 0,
      0.5f, -0.5f, 0,
     -0.5f, 0.5f, 0,
      0.5f, 0.5f, 0,            
   };*/
   
   /*float[] vertices = {   
     // X, Y, Z
     -0.25f, -0.25f, 0, //b-l
      0.25f, -0.25f, 0, //b-r
     -0.25f, 0.25f, 0, //t-l
      0.25f, 0.25f, 0, //t-r            
   };*/
   
    int size = vertices.length;
    int verts = size / 3;
    
    
    ByteBuffer vbb = ByteBuffer.allocateDirect(size * 4); 
    vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
    mVertexBuffer = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
    mVertexBuffer.put(vertices);    // add the coordinates to the FloatBuffer
    mVertexBuffer.position(0);            // set the buffer to read the first coordinate
    
    ByteBuffer texbb = ByteBuffer.allocateDirect(verts * 2 * 4); 
    texbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
    mTexBuffer = texbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
    /*for (int i = 0; i < verts; i++) {
      for(int j = 0; j < 2; j++) {
          mTexBuffer.put(vertices[i*3+j] + 0.5f);
      }
    }*/
    mTexBuffer.put(texCoords);
    mTexBuffer.position(0);            // set the buffer to read the first coordinate
    

    ByteBuffer ibb = ByteBuffer.allocateDirect(verts * 2);
    ibb.order(ByteOrder.nativeOrder());
    mIndexBuffer = ibb.asShortBuffer();

    for(int i = 0; i < verts; i++) {
        mIndexBuffer.put((short) i);
    }
    mIndexBuffer.position(0);
   
    mPosition = new float[3];
    mPosition[0] = 0.0f;
    mPosition[1] = 0.0f;
    mPosition[2] = 0.0f;
    
    dy_ = 0.0f;
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
    aGL.glTranslatef(mPosition[0], mPosition[1] + dy_, mPosition[2]);
    //aGL.glFrontFace(GL10.GL_CW);
    aGL.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
    //aGL.glActiveTexture(GL10.GL_TEXTURE0);
    //aGL.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
    aGL.glEnable(GL10.GL_TEXTURE_2D);
    /*aGL.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
        GL10.GL_CLAMP_TO_EDGE);
    aGL.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
        GL10.GL_CLAMP_TO_EDGE);*/
    aGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
    //aGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, mVertexBuffer.capacity()/3);
    aGL.glDrawElements(GL10.GL_TRIANGLE_STRIP, mVertexBuffer.capacity()/3,
            GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
    aGL.glPopMatrix();
    aGL.glDisable(GL10.GL_TEXTURE_2D);
  }


}

class Triangle {
    public Triangle() {

        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order

        ByteBuffer vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();

        ByteBuffer tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexBuffer = tbb.asFloatBuffer();

        ByteBuffer ibb = ByteBuffer.allocateDirect(VERTS * 2);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();

        for (int i = 0; i < VERTS; i++) {
            for(int j = 0; j < 3; j++) {
                mFVertexBuffer.put(sCoords[i*3+j]);
            }
        }

        for (int i = 0; i < VERTS; i++) {
            for(int j = 0; j < 2; j++) {
                mTexBuffer.put(sCoords[i*3+j] * 2.0f + 0.5f);
            }
        }

        for(int i = 0; i < VERTS; i++) {
            mIndexBuffer.put((short) i);
        }

        mFVertexBuffer.position(0);
        mTexBuffer.position(0);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS,
                GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
    }

    public float getX(int vertex) {
        return sCoords[3*vertex];
    }

    public float getY(int vertex) {
        return sCoords[3*vertex+1];
    }

    private final static int VERTS = 3;

    private FloatBuffer mFVertexBuffer;
    private FloatBuffer mTexBuffer;
    private ShortBuffer mIndexBuffer;
    // A unit-sided equalateral triangle centered on the origin.
    private final static float[] sCoords = {
            // X, Y, Z
            -0.5f, -0.25f, 0,
             0.5f, -0.25f, 0,
             0.0f,  0.559016994f, 0
    };
}
