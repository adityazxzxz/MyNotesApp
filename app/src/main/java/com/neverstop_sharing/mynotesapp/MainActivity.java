package com.neverstop_sharing.mynotesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.neverstop_sharing.mynotesapp.adapter.NoteAdapter;
import com.neverstop_sharing.mynotesapp.db.NoteHelper;
import com.neverstop_sharing.mynotesapp.entity.Note;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.neverstop_sharing.mynotesapp.DatabaseContract.CONTENT_URI;
import static com.neverstop_sharing.mynotesapp.FormAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView rvNotes;
    ProgressBar progressBar;
    FloatingActionButton fabAdd;

    private Cursor list;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");

        rvNotes = (RecyclerView)findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        fabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);


        adapter = new NoteAdapter(this);
        adapter.setListNotes(list);
        rvNotes.setAdapter(adapter);
        new LoadNoteSync().execute();
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this,FormAddUpdateActivity.class);
            startActivityForResult(intent,FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    private class LoadNoteSync extends AsyncTask<Void,Void,Cursor>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            /*if (list.size() > 0){
                list.clear();
            }*/
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI,null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list = notes;
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                new LoadNoteSync().execute();
                showSnackbarMessage("Satu item berhasil ditambahkan");
            }
        }else if(requestCode == REQUEST_UPDATE){
            if(resultCode==FormAddUpdateActivity.RESULT_UPDATE){
                new LoadNoteSync().execute();
                showSnackbarMessage("Satu item berhasil diubah");
            }
        }else if(resultCode==FormAddUpdateActivity.RESULT_DELETE){
            /*int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION,0);
            list.remove(position);
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();*/
            new LoadNoteSync().execute();
            showSnackbarMessage("Satu item berhasil dihapus");
        }
    }

    private void showSnackbarMessage(String msg) {
        Snackbar.make(rvNotes,msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
