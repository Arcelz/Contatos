package com.projetoifgoiano.bancosql;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] id;
    String[] nome;
    int[] img = {R.drawable.user};
    String[] telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buscaEAdapta();
        final ContatinhoDAO contatinhoDAO = new ContatinhoDAO(getApplicationContext());

        final ListView listView = (ListView) findViewById(R.id.listaContatinho);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selecionadoId = ((TextView) view.findViewById(R.id.id)).getText().toString();
                Intent intent = new Intent(MainActivity.this, ListaContatinho.class);
                intent.putExtra("editar", true);
                intent.putExtra("id", selecionadoId);
                startActivity(intent);
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaContatinho.class);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.id);
                if (contatinhoDAO.deleteContatinho(Integer.parseInt(textView.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "Deletado com sucesso", Toast.LENGTH_LONG).show();
                    buscaEAdapta();
                }
                return true;
            }
        });
    }

    public void buscaEAdapta() {
        ContatinhoDAO contatinhoDAO = new ContatinhoDAO(getApplicationContext());
        ArrayList<Contatinho> contatinhos = contatinhoDAO.buscarTodosContatinhos();
        id = new String[contatinhos.size()];
        nome = new String[contatinhos.size()];
        telefone = new String[contatinhos.size()];
        int i = 0;
        for (Contatinho c : contatinhos) {
            id[i] = c.getId().toString();
            nome[i] = c.getNome();
            telefone[i] = c.getTelefone();
            i++;
        }
        ListView listView = (ListView) findViewById(R.id.listaContatinho);
        Adapter adapter = new Adapter(getApplicationContext(), img, id, nome, telefone);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscaEAdapta();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class Adapter extends ArrayAdapter<String> {
        Context context;
        int[] img;
        String id[];
        String nome[];
        String telefone[];

        Adapter(Context c, int[] img, String[] id, String[] nome, String[] telefone) {
            super(c, R.layout.row, R.id.id, nome);
            this.img = img;
            this.id = id;
            this.nome = nome;
            this.telefone = telefone;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView imageView = (ImageView) row.findViewById(R.id.iconFoto);
            TextView textViewNome = row.findViewById(R.id.contatoNome);
            TextView textId = row.findViewById(R.id.id);
            TextView textViewTelefone = row.findViewById(R.id.contatoTelefone);
            File directory = cw.getDir("dirName", Context.MODE_PRIVATE);
            Bitmap bitmap = BitmapFactory.decodeFile(directory + "/" + id[position] + ".jpg");
            imageView.setImageBitmap(bitmap);
            Log.d("Direto",directory + "/" + id[position] + ".jpg");
            textViewNome.setText(nome[position]);
            textId.setText(id[position]);
            textViewTelefone.setText(telefone[position]);
            return row;
        }
    }
}
