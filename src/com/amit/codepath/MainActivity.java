package com.amit.codepath;

import java.util.ArrayList;
import java.util.Date;

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
	private DataStore dataStore;
	private EditText etTodoItems;
	private ListView lvTodoList;
	Cursor itemsCursor;

	int posBeingEdited;

	private ArrayList<String> todoItems;
	private ArrayAdapter<String> itemsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataStore = new DataStore(this);
		etTodoItems = (EditText) findViewById(R.id.etTodoItems);
		lvTodoList = (ListView) findViewById(R.id.todoList);

		todoItems = new ArrayList<String>();
		itemsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, todoItems);
		lvTodoList.setAdapter(itemsAdapter);

		itemsCursor = dataStore.selectRecords();
		int totalItems = itemsCursor.getCount();
		itemsCursor.moveToFirst();
		for (int i = 0; i < totalItems; i++) {
			todoItems.add(itemsCursor.getString(1));
			itemsCursor.moveToNext();
		}

		setupListViewListener();       
	}

	public void onSubmit(View v) {
		String item = etTodoItems.getText().toString();
		dataStore.createRecords(Long.toString(new Date().getTime()), item);
		// todoItems.add(item);
		itemsAdapter.add(item);
		etTodoItems.setText("");
		Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();

	}

	private void setupListViewListener() {
		lvTodoList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View itemView,
					int pos, long id) {
				String item = todoItems.get(pos);
				String idToDelete = "0"; 
				itemsCursor.moveToFirst();
				for (int i = 0; i < itemsCursor.getCount(); i++) {
					if (itemsCursor.getString(1).equals(item)) {
						idToDelete = itemsCursor.getString(0);
					}
				itemsCursor.moveToNext();
				}

				dataStore.removeRow(idToDelete);
				todoItems.remove(pos);
				itemsAdapter.notifyDataSetChanged();
				return true;
			}
		});

		lvTodoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
				posBeingEdited = pos;
				editIntent.putExtra("item", todoItems.get(pos));
				startActivityForResult(editIntent, REQUEST_CODE);
			}
		});

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// Extract name value from result extras
			String oldItem = todoItems.get(posBeingEdited);
			String editedItem = data.getExtras().getString("editedItem");
			todoItems.set(posBeingEdited, editedItem);
			itemsAdapter.notifyDataSetChanged();
			String idToUpdate = "0";
			// update database
			itemsCursor.moveToFirst();
			for (int i = 0; i < itemsCursor.getCount(); i++) {
				if (itemsCursor.getString(1).equals(oldItem)) {
					idToUpdate = itemsCursor.getString(0);
				}
			itemsCursor.moveToNext();
			}
			dataStore.updateRow(DataStore.ID + " = " + idToUpdate, "name", editedItem);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
