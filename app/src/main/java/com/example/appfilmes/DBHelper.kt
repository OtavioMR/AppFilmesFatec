package com.example.appfilmes;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DBHelper(context: Context) : SQLiteOpenHelper(context, "diario.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""CREATE TABLE filmes (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 titulo TEXT NOT NULL, tipo TEXT NOT NULL,
 inicio TEXT, fim TEXT, opiniao TEXT, nota INTEGER CHECK (nota BETWEEN 0 AND 10)
 )""")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE filmes ADD COLUMN nota INTEGER CHECK (nota BETWEEN 0 AND 10)")
        }
    }
    fun inserir(f: Filme) {
        val valores = valores(f)
        writableDatabase.insert("filmes", null, valores)
    }
    fun atualizar(f: Filme) {
        writableDatabase.update("filmes", valores(f), "id=?", arrayOf(f.id.toString()))
    }
    fun excluir(id: Long) {
        writableDatabase.delete("filmes", "id=?", arrayOf(id.toString()))
    }
    fun listar(): MutableList<Filme> {
        val lista = mutableListOf<Filme>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM filmes ORDER BY id DESC", null)
        while (cursor.moveToNext()) {
            lista.add(Filme(cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3) ?: "",
                    cursor.getString(4) ?: "", cursor.getString(5) ?: "",
                    if (cursor.isNull(6)) null else cursor.getInt(6)))
        }
        cursor.close()
        return lista
    }
    private fun valores(f: Filme) = ContentValues().apply {
        put("titulo", f.titulo)
        put("tipo", f.tipo)
        put("inicio", f.inicio)
        put("fim", f.fim)
        put("opiniao", f.opiniao)
        if (f.nota == null) putNull("nota") else put("nota", f.nota)
    }
}
