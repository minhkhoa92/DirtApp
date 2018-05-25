package de.minh.smartar.dirtapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.in_and_awesome.*
import android.opengl.GLES10
import javax.microedition.khronos.opengles.GL10
import android.widget.TextView
import android.content.pm.ConfigurationInfo
import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.content.Context


class MainActivity : AppCompatActivity() {

    var surfaceV: PrivateSurfaceV? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        surfaceV = PrivateSurfaceV(this)
        setContentView(surfaceV)


    }

    private fun replaceHackySol2(someString : String) {
        hackySolution2.text = someString
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
