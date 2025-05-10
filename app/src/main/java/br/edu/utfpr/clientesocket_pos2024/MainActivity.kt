package br.edu.utfpr.clientesocket_pos2024

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var rbHora: RadioButton
    private lateinit var rbData: RadioButton
    private lateinit var btEnviar: Button
    private lateinit var tvResposta: TextView
    private lateinit var progressBar: ProgressBar

    private val ip = "192.168.68.101"
    private val port = 12345

    private lateinit var clienteSocket : Socket
    private lateinit var input : BufferedReader
    private lateinit var output : BufferedWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rbHora = findViewById(R.id.rbHora)
        rbData = findViewById(R.id.rbData)
        btEnviar = findViewById(R.id.btEnviar)
        tvResposta = findViewById(R.id.tvResposta)
        progressBar = findViewById(R.id.progressBar)


        btEnviar.setOnClickListener {
            btEnviarOnClick()
        }

    } // fim do onCreate()

    private fun btEnviarOnClick() {

        progressBar.visibility = View.VISIBLE

        Thread {

            if ( ! ::clienteSocket.isInitialized  ) {
                clienteSocket = Socket(ip, port) //linha bloqueante

                input = clienteSocket.getInputStream().bufferedReader()
                output = clienteSocket.getOutputStream().bufferedWriter()
            }

            when (rbHora.isChecked) {
                true -> output.write("hora\n")
                false -> output.write("data\n")
            }
            output.flush()

            val resposta = input.readLine() //linha bloqueante

            runOnUiThread {
                Thread.sleep(1000)
                tvResposta.text = resposta
                progressBar.visibility = View.GONE
            }

        }.start()

    }

    override fun onStop() {
        super.onStop()
        clienteSocket.close()
    }

}