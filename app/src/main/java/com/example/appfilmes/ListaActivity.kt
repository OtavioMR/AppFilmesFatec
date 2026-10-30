package com.example.appfilmes

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ListaActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var lista: ListView
    private var filmes = mutableListOf<Filme>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)
        db = DBHelper(this)
        lista = findViewById(R.id.listFilmes)
        lista.setOnItemClickListener { _, _, pos, _ ->
            val filme = filmes[pos]
            AlertDialog.Builder(this)
                .setTitle(filme.titulo)
                .setMessage("${filme.tipo}\nInício: ${filme.inicio}\nTérmino: ${filme.fim}\nNota: ${filme.nota?.toString() ?: "Não informada"}\n\n${filme.opiniao}")
                .setPositiveButton("Editar") { _, _ ->
                    val tela = Intent(this, CadastroActivity::class.java)
                    tela.putExtra("id", filme.id)
                    tela.putExtra("titulo", filme.titulo)
                    tela.putExtra("tipo", filme.tipo)
                    tela.putExtra("inicio", filme.inicio)
                    tela.putExtra("fim", filme.fim)
                    tela.putExtra("opiniao", filme.opiniao)
                    filme.nota?.let { tela.putExtra("nota", it) }
                    startActivity(tela)
                }
                .setNegativeButton("Excluir") { _, _ ->
                    AlertDialog.Builder(this).setMessage("Excluir ${filme.titulo}?")
                        .setPositiveButton("Sim") { _, _ -> db.excluir(filme.id); carregar() }
                        .setNegativeButton("Não", null).show()
                }
                .setNeutralButton("Fechar", null).show()
        }
    }
    override fun onResume() { super.onResume(); carregar() }
    private fun carregar() {
        filmes = db.listar()
        lista.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            filmes.map { "${it.tipo}: ${it.titulo}" })
    }
    override fun onDestroy() { db.close(); super.onDestroy() }
}
