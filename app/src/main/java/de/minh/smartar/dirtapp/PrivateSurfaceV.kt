package de.minh.smartar.dirtapp

import android.content.Context
import android.opengl.GLSurfaceView

class PrivateSurfaceV(context: Context) : GLSurfaceView(context) {
    private var mRenderer: PrivateRenderer? = null


    init {
        setEGLContextClientVersion(2)

        mRenderer = PrivateRenderer()
        setRenderer(mRenderer)
    }

}