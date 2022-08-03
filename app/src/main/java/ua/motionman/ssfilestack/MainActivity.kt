package ua.motionman.ssfilestack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ua.motionman.filestack.domain.model.UploadResult
import ua.motionman.filestack.ui.FilestackActivity
import ua.motionman.filestack.ui.filestacksources.SourcesFragment.Companion.BUNDLE_RESULT_KEY
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
            val result = data?.getSerializableExtra(BUNDLE_RESULT_KEY) as? Array<UploadResult>
            result?.forEach {
                Log.e("MainActivity", "onActivityResult: $it")
            }
        }
    }

    private fun handleFilstackNavigation() {
        Intent(this, FilestackActivity::class.java).apply {
            putExtra(FilestackActivity.API_KEY, "AEHrSDUikTDqTbYuRTesYz")
            putExtra(
                FilestackActivity.POLICY_KEY,
                "eyJjYWxsIjogWyJyZWFkIiwgImNvbnZlcnQiLCAicGljayIsICJzdG9yZSJdLCAiZXhwaXJ5IjogMTY1OTU5ODE3NS42NTA5NzgsICJtYXhTaXplIjogNTM2ODcwOTEyMH0="
            )
            putExtra(
                FilestackActivity.SIGNATURE_KEY,
                "a552aa7e6e29a6d34f9e70151ab3a1b9ad6ad633c7d280c345d7769e5ecd0a00"
            )
            startActivityForResult(this, 662)
        }
    }
}