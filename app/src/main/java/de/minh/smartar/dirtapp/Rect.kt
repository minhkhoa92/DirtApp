package de.minh.smartar.dirtapp

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Rect {
    private var vertexBuffer: FloatBuffer? = null
    private var drawListBuffer: ShortBuffer? = null

    companion object {
        val COORDS_PER_VERTEX = 3
        private val vertexShaderCode = (// This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        // The matrix must be included as a modifier of gl_Position.
                        // Note that the uMVPMatrix factor *must be first* in order
                        // for the matrix multiplication product to be correct.
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}")

        private val fragmentShaderCode = (
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}")
        private val rectCoords: FloatArray = floatArrayOf(

                //0.5
                -1.0f, 1.0f, 0.0f,  // top left
                -1.0f, -1.0f, 0.0f,  // bottom left
                1.0f, -1.0f, 0.0f,   // bottom right
                1.0f, 1.0f, 0.0f    // top right
        )

    }

    private val drawOrder: ShortArray = shortArrayOf(
            0, 1, 2, 0, 2, 3 // order to draw vertices
    )


    // Set color with red, green, blue and alpha (opacity) values
    var color: FloatArray = floatArrayOf(0.63671875f, 0.02f, 0.0f, 1.0f)

    private var mProgram: Int = 0

    init {
        var bb: ByteBuffer? = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                rectCoords.size * 4)
        // use the device hardware's native byte order
        bb?.order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb?.asFloatBuffer()
        // add the coordinates to the FloatBuffer
        vertexBuffer?.put(rectCoords)
        // set the buffer to read the first coordinate
        vertexBuffer?.position(0)

        // initialize byte buffer for the draw list
        var dlb: ByteBuffer = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.size * 2)
        dlb.order(ByteOrder.nativeOrder())
        drawListBuffer = dlb.asShortBuffer()
        drawListBuffer?.put(drawOrder)
        drawListBuffer?.position(0)


        // prepare shaders and OpenGL program
        val vertexShader = PrivateRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        val fragmentShader = PrivateRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram()

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader)

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader)

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram)

    }

    private var mPositionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private var mMVPMatrixHandle: Int = 0

    public fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer)

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        PrivateRenderer.checkGlError("glGetUniformLocation")

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        PrivateRenderer.checkGlError("glUniformMatrix4fv")

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

}