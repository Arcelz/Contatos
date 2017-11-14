package com.projetoifgoiano.bancosql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Usuario on 09/11/2017.
 */

public class ContatinhoDAO {
    private ContatinhoDBHelper contatinhoDBHelper;

    public ContatinhoDAO(Context context) {
        this.contatinhoDBHelper = new ContatinhoDBHelper(context);
    }

    public long inserirContatinho(Contatinho contatinho) {
        SQLiteDatabase db = this.contatinhoDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContatinhoContract.COLUNA_NOME, contatinho.getNome());
        values.put(ContatinhoContract.COLUNA_TELEFONE, contatinho.getTelefone());
        values.put(ContatinhoContract.COLUNA_INFO, contatinho.getInfos());
        return db.insert(ContatinhoContract.NOME_TABELA, null, values);
    }

    public boolean deleteContatinho(Integer id) {
        SQLiteDatabase db = this.contatinhoDBHelper.getWritableDatabase();
        String condicao = ContatinhoContract.COLUNA_ID + " =?";
        String[] arguments = {id.toString()};
        return (db.delete(ContatinhoContract.NOME_TABELA, condicao, arguments) > 0);
    }

    public boolean editarContatino(Contatinho contatinho) {
        SQLiteDatabase db = this.contatinhoDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContatinhoContract.COLUNA_NOME, contatinho.getNome());
        values.put(ContatinhoContract.COLUNA_TELEFONE, contatinho.getTelefone());
        values.put(ContatinhoContract.COLUNA_INFO, contatinho.getInfos());
        String condicao = ContatinhoContract.COLUNA_ID + " = ?";
        String[] arguments = {contatinho.getId().toString()};
        return (db.update(ContatinhoContract.NOME_TABELA, values, condicao, arguments) > 0);
    }

    public ArrayList<Contatinho> buscarTodosContatinhos() {
        SQLiteDatabase db = this.contatinhoDBHelper.getReadableDatabase();
        String[] colunas = {ContatinhoContract.COLUNA_ID, ContatinhoContract.COLUNA_NOME, ContatinhoContract.COLUNA_TELEFONE, ContatinhoContract.COLUNA_INFO};
        Cursor cursor = db.query(ContatinhoContract.NOME_TABELA, colunas, null, null, null, null, ContatinhoContract.COLUNA_NOME + " ASC");
        ArrayList<Contatinho> contatin = new ArrayList<Contatinho>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Contatinho contatinho = new Contatinho();
            contatinho.setId(cursor.getInt(cursor.getColumnIndex(ContatinhoContract.COLUNA_ID)));
            contatinho.setNome(cursor.getString(cursor.getColumnIndex(ContatinhoContract.COLUNA_NOME)));
            contatinho.setTelefone(cursor.getString(cursor.getColumnIndex(ContatinhoContract.COLUNA_TELEFONE)));
            contatinho.setInfos(cursor.getString(cursor.getColumnIndex(ContatinhoContract.COLUNA_INFO)));
            contatin.add(contatinho);
        }
        cursor.close();
        return contatin;
    }
    public Contatinho buscarUmContatinho(Contatinho contatinho) {
        SQLiteDatabase db = this.contatinhoDBHelper.getReadableDatabase();
        String[] colunas = {ContatinhoContract.COLUNA_ID, ContatinhoContract.COLUNA_NOME, ContatinhoContract.COLUNA_TELEFONE, ContatinhoContract.COLUNA_INFO};
        String condicao = ContatinhoContract.COLUNA_ID+" = ?";
        String[] args = {contatinho.getId().toString()};
        Cursor cursor = db.query(ContatinhoContract.NOME_TABELA, colunas, condicao, args, null, null, ContatinhoContract.COLUNA_NOME + " ASC");
        Contatinho contatin = new Contatinho();
        while (cursor.moveToNext()) {
            contatin.setId(cursor.getInt(cursor.getColumnIndex(ContatinhoContract.COLUNA_ID)));
            contatin.setNome(cursor.getString(cursor.getColumnIndex(ContatinhoContract.COLUNA_NOME)));
            contatin.setTelefone(cursor.getString(cursor.getColumnIndex(ContatinhoContract.COLUNA_TELEFONE)));
            contatin.setInfos(cursor.getString(cursor.getColumnIndex(ContatinhoContract.COLUNA_INFO)));
        }
        cursor.close();
        return contatin;
    }

}
