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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_and_awesome)

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
        val supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000

        val info = configurationInfo.reqGlEsVersion.toString(16)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        if (supportsEs3) {
            hackySolution.text = "es3 is supported"
            replaceHackySol2(info)
        } else if (supportsEs2) {
            hackySolution.text = "es2 is supported"
            replaceHackySol2(info)
        } else {
            hackySolution.text = "es2 is not supported"
        }
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
