package ua.motionman.ssfilestack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ua.motionman.filestack.ui.FilestackActivity
import ua.motionman.ssfilestack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.filestack.setOnClickListener {
            handleFilstackNavigation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 662 && resultCode == RESULT_OK) {
            val result = data?.getBooleanExtra("IS_COMPLETE", false)
            Log.e("MainActivity", "onActivityResult: $result")
        }
    }

    private fun handleFilstackNavigation() {
        Intent(this, FilestackActivity::class.java).apply {
            putExtra(FilestackActivity.API_KEY, "AEHrSDUikTDqTbYuRTesYz")
            putExtra(
                FilestackActivity.POLICY_KEY,
                "eyJjYWxsIjogWyJyZWFkIiwgImNvbnZlcnQiLCAicGljayIsICJzdG9yZSJdLCAiZXhwaXJ5IjogMTY1ODg2NzU3My41MzYzNDYsICJtYXhTaXplIjogNTM2ODcwOTEyMH0="
            )
            putExtra(
                FilestackActivity.SIGNATURE_KEY,
                "b8c29b5e7041775e28132e41b30d409571ffde6c7713d8b3afc3640f7db73864"
            )
            startActivityForResult(this, 662)
//            startActivity(this)
        }
    }
}