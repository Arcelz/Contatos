package com.projetoifgoiano.bancosql;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ListaContatinho extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatinho);
        Intent intent = getIntent();
        TextView textViewId = (TextView) findViewById(R.id.contatoFormId);
        final EditText textViewNome = (EditText) findViewById(R.id.contatoFormNome);
        final EditText textViewTelefone = (EditText) findViewById(R.id.contatoFormTelefone);
        final EditText textViewInfo = (EditText) findViewById(R.id.contatoFormInfo);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final ContatinhoDAO contatinhoDAO = new ContatinhoDAO(getApplicationContext());
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonSalvar);
        if (intent.getBooleanExtra("editar", false)) {
            Contatinho contatinho = new Contatinho();
            contatinho.setId(Integer.valueOf(intent.getStringExtra("id")));
            final Contatinho novo = contatinhoDAO.buscarUmContatinho(contatinho);
            try {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("dirName", Context.MODE_PRIVATE);
                File file = new File(directory, novo.getId() + ".jpg");
                FileInputStream streamIn = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Log.d("Direto", e.getMessage());
            }

            textViewId.setText(novo.getId().toString());
            textViewNome.setText(novo.getNome().toString());
            textViewTelefone.setText(novo.getTelefone().toString());
            textViewInfo.setText(novo.getInfos().toString());

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    novo.setNome(textViewNome.getText().toString());
                    novo.setTelefone(textViewTelefone.getText().toString());
                    novo.setInfos(textViewInfo.getText().toString());
                    if (contatinhoDAO.editarContatino(novo)) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        salvarImg(novo.getId(), bitmap);
                        Toast.makeText(getApplicationContext(), "Alterado com sucesso", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        } else {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contatinho contatinho = new Contatinho();
                    contatinho.setNome(textViewNome.getText().toString());
                    contatinho.setTelefone(textViewTelefone.getText().toString());
                    contatinho.setInfos(textViewInfo.getText().toString());
                    long c = contatinhoDAO.inserirContatinho(contatinho);
                    if (c > 0) {
                        Toast.makeText(getApplicationContext(), "Criado com sucesso", Toast.LENGTH_LONG).show();
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        salvarImg((int)c, bitmap);
                        finish();
                    }
                }
            });
        }
        ImageView imageButton = (ImageView) findViewById(R.id.imageView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectImage = BitmapFactory.decodeStream(imageStream);
                ImageView imageButton = (ImageView) findViewById(R.id.imageView);
                imageButton.setImageBitmap(selectImage);
            } catch (FileNotFoundException e) {
                Log.d("Direto", e.getMessage());
            }
        }
    }

    public void salvarImg(int id, Bitmap bm) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("dirName", Context.MODE_PRIVATE);
        File file = new File(directory, id + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.d("Direto", e.getMessage());
        }
    }
}
