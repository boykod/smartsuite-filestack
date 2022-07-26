package ua.motionman.filestack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.motionman.filestack.R
import ua.motionman.filestack.utils.client.ClientProvider

class FilestackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_activity)

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

    companion object {
        const val API_KEY = "api_key"
        const val POLICY_KEY = "policy_key"
        const val SIGNATURE_KEY = "signature_key"
    }
}