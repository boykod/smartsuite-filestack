package ua.motionman.filestack.utils.client

import com.filestack.Client
import com.filestack.Config

class ClientProvider {

    private var client: Client? = null

    fun initClient(apiKey: String, policy: String, signature: String) {
        val config = Config(apiKey, policy, signature)
        client = Client(config)
    }

    fun getClient(): Client? {
        return client
    }

    companion object {
        val instance = ClientProvider()
    }

}