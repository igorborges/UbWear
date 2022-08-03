package com.igorbresende.ubwear

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import com.igorbresende.ubwear.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {
    var activityContext: Context? = null
    private val wearableAppCheckPayload = "AppOpenWearable"
    private val wearableAppCheckPayloadReturnACK = "AppOpenWearableACK"
    private var wearableDeviceConnected: Boolean = false

    private var currentAckFromWearForAppOpenCheck: String? = null
    private val APP_OPEN_WEARABLE_PAYLOAD_PATH = "/APP_OPEN_WEARABLE_PAYLOAD"

    private val MESSAGE_ITEM_RECEIVED_PATH: String = "/message-item-received"

    private val TAG_GET_NODES: String = "getnodes1"
    private val TAG_MESSAGE_RECEIVED: String = "receive1"

    private var messageEvent: MessageEvent? = null
    private var wearableNodeUri: String? = null

    private var finalCookies = ""
    private var gotCookies = false

    @RequiresApi(Build.VERSION_CODES.N)
    private var languageMaps = LanguageMaps().getMap(Locale.getDefault().language);

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activityContext = this
        wearableDeviceConnected = false

        binding.toolbar.title = languageMaps["appTitle"]
//        binding.tutorialTextView. = languageMaps["tutorialUrl"]
        binding.checkwearablesButton.text = languageMaps["checkConnectedDevicesButtonBefore"]
//        binding.sendmessageButton.text = languageMaps["sendMessageButton"]


        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                try {
                    val cookies: String =
                        CookieManager.getInstance().getCookie("https://m.uber.com/api/getStatus")
                    if (!gotCookies && validateCookies(cookies)) {
                        println("SUCCESS")
                        gotCookies = true
                        finalCookies = cookies
                    }
                } catch (e: Exception) {
                    println("ops...")
                    e.printStackTrace()
                }

                return super.shouldInterceptRequest(view, request)
            }

        }
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(languageMaps["uberUrl"].toString())

        binding.checkwearablesButton.setOnClickListener {
            if(!gotCookies) {
                Toast.makeText(
                    activityContext,
                    languageMaps["notLoggedIn"],
                    Toast.LENGTH_LONG
                ).show()
            } else if (!wearableDeviceConnected) {
                val tempAct: Activity = activityContext as MainActivity
                //Couroutine
                initialiseDevicePairing(tempAct)
            }
        }
    }

    private fun validateCookies(cookies: String): Boolean {
//        var reqParam = URLEncoder.encode("localeCode=pt-BR", "UTF-8")
        println(cookies)
        val mURL = URL("https://m.uber.com/api/getStatus?localeCode=pt-BR")

        with(mURL.openConnection() as HttpURLConnection) {
            setRequestProperty("content-type", "application/json")
            setRequestProperty("x-csrf-token", "x")
            setRequestProperty(
                "cookie", cookies
//                "csid=1.1661087113150.WNA+CsAyTPQcQSrwG3O/tE7JID8VI06TNmD/5vRGVOI=;  sid=QA.CAESEI33IEGRTEO0uwg0ZlqcB2YYiNuImAYiATEqJDk1ZDlmNTJhLWE5NzQtNGI5Yi05MjIzLWE5MTZkNzEwN2RkMDJAz8jksdquAO1kg1kHRKLVUO2HQZXy7Wz1hgFydfdKMVt2EXgASrMLkiPMXnnf7dBUZ0i91vxSmG4nGEFulm6RXjoBMUIIdWJlci5jb20.MxcYxlrVo52a1KYChnHoiK-EIKyTb9wFxEA6KeWEeaQ; marketing_vistor_id=7cd54e92-5e3f-4dfe-957e-20a288cd8776; sid=QA.CAESEOCWDWrQqEHSg1q69ks4WHgYlPKDmAYiATEqJDk1ZDlmNTJhLWE5NzQtNGI5Yi05MjIzLWE5MTZkNzEwN2RkMDJAzn2xtDNSsb7Xf7aUXp-mR7tmIkM50dqC7B2iWL64QYDKTwajnPDVZPF194swPddtkbgU5C8y8oByhRvjbh9RtToBMUIIdWJlci5jb20.xyyxXMTtZxXHX272e7SR36I144LJ8YhF39fawKw4ZYw; usl_rollout_id=853ae667-04de-4a08-84d3-6093a43c606b; _ua={\"session_id\":\"6d748a11-79a9-4ec0-b3ab-48b5b31ddab7\",\"session_time_ms\":1658776736658}; jwt-session=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NTg3NzY3MzcsImV4cCI6MTY1ODg2MzEzN30.brTQxOI5k3wS9GX3-AEg6cAog32x9DFjmhg8F3Hq-xQ; udi-id=0q4gqaId0qLdmFBcTeUCfq3N2VEg2oEAq7kPqd32zN/5bSuB7+E/w0PFCq/Z2EBuEOkt+uwdFFIZFXlvaOe/emUVkeFBhJSg5nS7MZbhSEFnOgUP/tfVD+eSonPL1FztPG+OsbPB01/ds0AY4tbLxYQp+KQPqE5aw5ups+iyDrjKbZM+UQInJutwtOstznMaWgofh4cd8BWJAbQDxvoXyg==z0s4r/weh4LheSKyVTVh6A==h9Jm2E4uD2/JN6QybcZPSa/eEZ+ei1pLcVAkYZwwwIw="
            )

            requestMethod = "POST"
            val wr = OutputStreamWriter(outputStream)
            val body = "{\"latitude\":-19.00,\"longitude\":-43.00}"
            wr.write(body)
            wr.flush()

            return responseCode == 200
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun initialiseDevicePairing(tempAct: Activity) {
        //Coroutine
        launch(Dispatchers.Default) {
            var getNodesResBool: BooleanArray? = null

            try {
                getNodesResBool =
                    getNodes(tempAct.applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //UI Thread
            withContext(Dispatchers.Main) {
                if (getNodesResBool!![0]) {
                    //if message Acknowlegement Received
                    if (getNodesResBool[1]) {
//                        Toast.makeText(
//                            activityContext,
//                            languageMaps["devicePaired"],
//                            Toast.LENGTH_LONG
//                        ).show()
                        wearableDeviceConnected = true

                        Toast.makeText(
                            activityContext,
                            languageMaps["successfulConfigurationToast"],
                            Toast.LENGTH_SHORT
                        ).show()

                        val nodeId: String = messageEvent?.sourceNodeId!!
                        // Set the data of the message to be the bytes of the Uri.
                        val payload: ByteArray =
                            (finalCookies).toByteArray()

                        // Send the rpc
                        // Instantiates clients without member variables, as clients are inexpensive to
                        // create. (They are cached and shared between GoogleApi instances.)
                        val sendMessageTask =
                            Wearable.getMessageClient(activityContext!!)
                                .sendMessage(nodeId, MESSAGE_ITEM_RECEIVED_PATH, payload)

                        sendMessageTask.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("send1", "Message sent successfully")
                            } else {
                                Log.d("send1", "Message failed.")
                            }
                        }

                    } else {
                        Toast.makeText(
                            activityContext,
                            languageMaps["openWearApp"],
                            Toast.LENGTH_LONG
                        ).show()
                        wearableDeviceConnected = false
                    }
                } else {
                    Toast.makeText(
                        activityContext,
                        languageMaps["noWearableFound"],
                        Toast.LENGTH_LONG
                    ).show()
                    wearableDeviceConnected = false
                }
            }
        }
    }


    private fun getNodes(context: Context): BooleanArray {
        val nodeResults = HashSet<String>()
        val resBool = BooleanArray(2)
        resBool[0] = false //nodePresent
        resBool[1] = false //wearableReturnAckReceived
        val nodeListTask =
            Wearable.getNodeClient(context).connectedNodes
        try {
            // Block on a task and get the result synchronously (because this is on a background thread).
            val nodes =
                Tasks.await(
                    nodeListTask
                )
            Log.e(TAG_GET_NODES, "Task fetched nodes")
            for (node in nodes) {
                Log.e(TAG_GET_NODES, "inside loop")
                nodeResults.add(node.id)
                try {
                    val nodeId = node.id
                    // Set the data of the message to be the bytes of the Uri.
                    val payload: ByteArray = wearableAppCheckPayload.toByteArray()
                    // Send the rpc
                    // Instantiates clients without member variables, as clients are inexpensive to
                    // create. (They are cached and shared between GoogleApi instances.)
                    val sendMessageTask =
                        Wearable.getMessageClient(context)
                            .sendMessage(nodeId, APP_OPEN_WEARABLE_PAYLOAD_PATH, payload)
                    try {
                        // Block on a task and get the result synchronously (because this is on a background thread).
                        val result = Tasks.await(sendMessageTask)
                        Log.d(TAG_GET_NODES, "send message result : $result")
                        resBool[0] = true
                        //Wait for 1000 ms/1 sec for the acknowledgement message
                        //Wait 1
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(100)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 1")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        //Wait 2
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(150)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 2")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        //Wait 3
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(200)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 3")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        //Wait 4
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(250)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 4")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        //Wait 5
                        if (currentAckFromWearForAppOpenCheck != wearableAppCheckPayloadReturnACK) {
                            Thread.sleep(350)
                            Log.d(TAG_GET_NODES, "ACK thread sleep 5")
                        }
                        if (currentAckFromWearForAppOpenCheck == wearableAppCheckPayloadReturnACK) {
                            resBool[1] = true
                            return resBool
                        }
                        resBool[1] = false
                        Log.d(
                            TAG_GET_NODES,
                            "ACK thread timeout, no message received from the wearable "
                        )
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                } catch (e1: Exception) {
                    Log.d(TAG_GET_NODES, "send message exception")
                    e1.printStackTrace()
                }
            } //end of for loop
        } catch (exception: Exception) {
            Log.e(TAG_GET_NODES, "Task failed: $exception")
            exception.printStackTrace()
        }
        return resBool
    }


    override fun onDataChanged(p0: DataEventBuffer) {
    }

    @SuppressLint("SetTextI18n")
    override fun onMessageReceived(p0: MessageEvent) {
        try {
            val s =
                String(p0.data, StandardCharsets.UTF_8)
            val messageEventPath: String = p0.path
            Log.d(
                TAG_MESSAGE_RECEIVED,
                "onMessageReceived() Received a message from watch:"
                        + p0.requestId
                        + " "
                        + messageEventPath
                        + " "
                        + s
            )
            if (messageEventPath == APP_OPEN_WEARABLE_PAYLOAD_PATH) {
                currentAckFromWearForAppOpenCheck = s
                Log.d(
                    TAG_MESSAGE_RECEIVED,
                    "Received acknowledgement message that app is open in wear"
                )

//                val sbTemp = StringBuilder()
//                sbTemp.append(binding.messagelogTextView.text.toString())
//                sbTemp.append("\nWearable device connected.")
//                Log.d("receive1", " $sbTemp")
//                binding.messagelogTextView.text = sbTemp
//                binding.textInputLayout.visibility = View.VISIBLE

//                binding.checkwearablesButton.visibility = View.GONE
                messageEvent = p0
                wearableNodeUri = p0.sourceNodeId
            } else if (messageEventPath.isNotEmpty() && messageEventPath == MESSAGE_ITEM_RECEIVED_PATH) {

                try {
//                    binding.messagelogTextView.visibility = View.VISIBLE
//                    binding.textInputLayout.visibility = View.VISIBLE
//                    binding.sendmessageButton.visibility = View.VISIBLE

                    val sbTemp = StringBuilder()
                    sbTemp.append("\n")
                    sbTemp.append(s)
                    sbTemp.append(" - (Received from wearable)")
                    Log.d("receive1", " $sbTemp")
//                    binding.messagelogTextView.append(sbTemp)
//
//                    binding.scrollviewText.requestFocus()
//                    binding.scrollviewText.post {
//                        binding.scrollviewText.scrollTo(0, binding.scrollviewText.bottom)
//                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("receive1", "Handled")
        }
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
    }


    override fun onPause() {
        super.onPause()
        try {
            Wearable.getDataClient(activityContext!!).removeListener(this)
            Wearable.getMessageClient(activityContext!!).removeListener(this)
            Wearable.getCapabilityClient(activityContext!!).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            Wearable.getDataClient(activityContext!!).addListener(this)
            Wearable.getMessageClient(activityContext!!).addListener(this)
            Wearable.getCapabilityClient(activityContext!!)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
