package de.minh.smartar.dirtapp

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.os.SystemClock


class PrivateRenderer : GLSurfaceView.Renderer {
    var mTriangle: Triangle? = null
    var mRect: Rect? = null

    companion object {

        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            val shader = GLES20.glCreateShader(type)

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            return shader
        }

        fun checkGlError(glOperation: String) {
            var error: Int = GLES20.glGetError()
            while (error != GLES20.GL_NO_ERROR) {
                Log.e("minhdeb", "$glOperation: glError $error")
                error = GLES20.glGetError()
                throw RuntimeException("$glOperation: glError $error")
            }
        }
    }

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private var mMVPMatrix = FloatArray(16)
    private var mProjectionMatrix = FloatArray(16)
    private var mViewMatrix = FloatArray(16)
    private var mRotationMatrix = FloatArray(16)
    private var mAngle: Float = 0.toFloat()

    override fun onDrawFrame(p0: GL10?) {
        var scratch = FloatArray(16)
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f)
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
        mRect?.draw(mMVPMatrix)

        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 0f, 1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0)

        mTriangle?.draw()
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height.toFloat()
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio,
                -1f, 1f, 3f, 7f)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f)
        // initialize a triangle
        mTriangle = Triangle()
        // initialize a rectangle
        mRect = Rect()
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    fun getAngle(): Float {
        return mAngle
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    fun setAngle(angle: Float) {
        mAngle = angle
    }

}