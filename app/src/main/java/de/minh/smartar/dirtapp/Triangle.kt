package de.minh.smartar.dirtapp

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import android.opengl.GLES20


class Triangle {
    companion object {
        private val vertexShaderCode = "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

        private val fragmentShaderCode = ("precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}")
        private val triangleCoords: FloatArray = floatArrayOf(
                // in counterclockwise order:
                0.0f, 1.0f, 0.0f, // top
                -1.0f, -1.0f, 0.0f, // bottom left
                1.0f, -1.0f, 0.0f  // bottom right
        )

        val COORDS_PER_VERTEX = 3

    }

    private var mProgram: Int = 0


    private var vertexBuffer: FloatBuffer? = null

    // Set color with red, green, blue and alpha (opacity) values
    var color: FloatArray = floatArrayOf(0.0f, 0.5f, 0.0f, 1.0f)

    init {
        var bb: ByteBuffer? = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.size * 4)
        // use the device hardware's native byte order
        bb?.order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb?.asFloatBuffer()
        // add the coordinates to the FloatBuffer
        vertexBuffer?.put(triangleCoords)
        // set the buffer to read the first coordinate
        vertexBuffer?.position(0)

        //compile code
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

    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    public fun draw() {
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

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

}