package com.amit.codepath;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected static final int REQUEST_CODE = 1;
	private int countForId;
	private DataStore dataStore;
	private EditText etItems;
	private ListView lvTodoList;
	
	int posBeingEdited;
	
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataStore = new DataStore(this);
        etItems = (EditText) findViewById(R.id.etTodoItems);
        lvTodoList = (ListView) findViewById(R.id.todoList);
        
        todoItems = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, todoItems);
        lvTodoList.setAdapter(itemsAdapter);
        
        Cursor itemsCursor = dataStore.selectRecords();
       int totalItems = itemsCursor.getCount();
       countForId = totalItems;
       // int itemsToShow = totalItems > 6 ? 6 : totalItems;
       itemsCursor.moveToFirst();
       for (int i = 0; i < totalItems; i++) {
    	   todoItems.add(itemsCursor.getString(1));
    	   itemsCursor.moveToNext();
       }
       
       setupListViewListener();
        
    }


    private void setupListViewListener() {
		lvTodoList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				todoItems.remove(pos);
				itemsAdapter.notifyDataSetChanged();
				// dataStore.removeRow();
				return true;
			}
			
		});
		
		lvTodoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				 Intent i = new Intent(MainActivity.this, EditItemActivity.class);
				 posBeingEdited = pos;
				 i.putExtra("item", todoItems.get(pos));
				  startActivityForResult(i, REQUEST_CODE);
			}
		});
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onSubmit(View v) {
    	String item = etItems.getText().toString();
    	dataStore.createRecords(++countForId, item);
    	// todoItems.add(item);
    	itemsAdapter.add(item);
    	etItems.setText("");
    	Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // REQUEST_CODE is defined above
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
         // Extract name value from result extras
         String editedItem = data.getExtras().getString("editedItem");
         // Toast the name to display temporarily on screen
         todoItems.set(posBeingEdited, editedItem);
         itemsAdapter.notifyDataSetChanged();
      }
    }
    
}
