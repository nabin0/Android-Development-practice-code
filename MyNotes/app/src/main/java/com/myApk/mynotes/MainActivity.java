package com.myApk.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    //Creating arraylist ,array adapter ,  Shared preferences
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter noteAdapter;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.myApk.mynotes" , Context.MODE_PRIVATE);

        HashSet<String> notesSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if(notesSet == null){
            notes.add("Welcome Note");
        }else{
            notes = new ArrayList(notesSet);
        }

        noteAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(noteAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editNoteIntent = new Intent(getApplicationContext(), Note_editor.class);
                editNoteIntent.putExtra("noteId", i);
                startActivity(editNoteIntent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int deleteNoteId = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure??")
                        .setMessage("Do You want to delete this!!!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(deleteNoteId);
                                noteAdapter.notifyDataSetChanged();

                                HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", noteSet).apply();

                            }
                        }).setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
    //Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.addNote){
             Intent newIntent = new Intent(getApplicationContext(), Note_editor.class);
             startActivity(newIntent);
             return  true;
         }
         return  false;
    }
}