package com.neverstop_sharing.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neverstop_sharing.mynotesapp.CustomOnItemClickListener;
import com.neverstop_sharing.mynotesapp.FormAddUpdateActivity;
import com.neverstop_sharing.mynotesapp.R;
import com.neverstop_sharing.mynotesapp.entity.Note;

import java.util.LinkedList;

import static com.neverstop_sharing.mynotesapp.DatabaseContract.CONTENT_URI;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Cursor listNotes;
    private Activity activity;

    public NoteAdapter(Activity activity){
        this.activity = activity;
    }



    public void setListNotes(Cursor listNotes){
        this.listNotes = listNotes;
    }



    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.NoteViewHolder holder, int position) {
        final Note note = getItem(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDate.setText(note.getDate());
        holder.tvDescription.setText(note.getDescription());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity,FormAddUpdateActivity.class);
                Uri uri = Uri.parse(CONTENT_URI+"/"+note.getId());
                intent.setData(uri);
                activity.startActivityForResult(intent,FormAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    private Note getItem(int position) {
        if (!listNotes.moveToPosition(position)){
            throw new IllegalStateException("Position Invalid");
        }
        return new Note(listNotes);
    }

    @Override
    public int getItemCount() {
        if (listNotes == null)return 0;
        return listNotes.getCount();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDescription,tvDate;
        CardView cvNote;
        public NoteViewHolder(View itemView){
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_item_title);
            tvDate = (TextView)itemView.findViewById(R.id.tv_item_date);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_item_description);
            cvNote = (CardView)itemView.findViewById(R.id.cv_item_note);
        }
    }
}
