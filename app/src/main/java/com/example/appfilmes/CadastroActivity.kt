package com.example.appfilmes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val titulo = findViewById<EditText>(R.id.edtTitulo)
        val tipo = findViewById<Spinner>(R.id.spTipo)
        val inicio = findViewById<EditText>(R.id.edtInicio)
        val fim = findViewById<EditText>(R.id.edtFim)
        val opiniao = findViewById<EditText>(R.id.edtOpiniao)
        val nota = findViewById<EditText>(R.id.edtNota)
        val opcoes = listOf("Filme", "Série")
        tipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcoes)

        val id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            titulo.setText(intent.getStringExtra("titulo"))
            tipo.setSelection(opcoes.indexOf(intent.getStringExtra("tipo")).coerceAtLeast(0))
            inicio.setText(intent.getStringExtra("inicio"))
            fim.setText(intent.getStringExtra("fim"))
            opiniao.setText(intent.getStringExtra("opiniao"))
            if (intent.hasExtra("nota")) nota.setText(intent.getIntExtra("nota", 0).toString())
        }
        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            val textoNota = nota.text.toString().trim()
            val valorNota = textoNota.toIntOrNull()
            if (titulo.text.toString().isBlank()) {
                titulo.error = "Informe o título"
            } else if (textoNota.isNotEmpty() && (valorNota == null || valorNota !in 0..10)) {
                nota.error = "Informe uma nota inteira de 0 a 10"
            } else {
                val filme = Filme(id, titulo.text.toString().trim(), tipo.selectedItem.toString(),
                    inicio.text.toString(), fim.text.toString(), opiniao.text.toString(), valorNota)
                val db = DBHelper(this)
                if (id == 0L) db.inserir(filme) else db.atualizar(filme)
                db.close()
                Toast.makeText(this, "Registro salvo", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
