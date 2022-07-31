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
                "eyJjYWxsIjogWyJyZWFkIiwgImNvbnZlcnQiLCAicGljayIsICJzdG9yZSJdLCAiZXhwaXJ5IjogMTY1OTM4MTQ3NS4wMzE1MzgsICJtYXhTaXplIjogNTM2ODcwOTEyMH0="
            )
            putExtra(
                FilestackActivity.SIGNATURE_KEY,
                "084228c71ebddf3157c9b3b2b8df233c377b6ba7a1535fad5cc2a1faed2ac780"
            )
            startActivityForResult(this, 662)
        }
    }
}