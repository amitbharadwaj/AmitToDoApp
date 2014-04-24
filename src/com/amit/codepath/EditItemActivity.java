package com.amit.codepath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	
	EditText etEditItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(getIntent().getStringExtra("item"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	
	public void onDoneEditing(View v) {
		Intent data = new Intent();
		data.putExtra("editedItem", etEditItem.getText().toString());
		setResult(RESULT_OK, data);
		finish();
	}
}
