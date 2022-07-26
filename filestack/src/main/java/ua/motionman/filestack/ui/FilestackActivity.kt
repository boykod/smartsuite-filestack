package ua.motionman.filestack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ua.motionman.filestack.R
import ua.motionman.filestack.databinding.FilestackActivityBinding
import ua.motionman.filestack.utils.client.ClientProvider
import ua.motionman.filestack.utils.delegate.viewBinding

class FilestackActivity : AppCompatActivity() {

    private val binding by viewBinding(FilestackActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initClient()
    }

    private fun initClient() {
        val apiKey = intent?.getStringExtra(API_KEY)
        val policy = intent?.getStringExtra(POLICY_KEY)
        val signature = intent?.getStringExtra(SIGNATURE_KEY)

        if (!apiKey.isNullOrEmpty() && !policy.isNullOrEmpty() && !signature.isNullOrEmpty()) {
            ClientProvider.instance.initClient(apiKey, policy, signature)
        }
    }

    override fun onBackPressed() {
        val currentDestinationId = findNavController(R.id.nav_host_fragment).currentDestination?.id

        if (currentDestinationId != null) {
            when (currentDestinationId) {
                R.id.uploadingProgressFragment -> {}
                else -> super.onBackPressed()
            }
        }
    }

    companion object {
        const val API_KEY = "api_key"
        const val POLICY_KEY = "policy_key"
        const val SIGNATURE_KEY = "signature_key"
    }
}