package ru.maximoff.notifications;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final EditText head = findViewById(R.id.mainEditText1);
		final EditText text = findViewById(R.id.mainEditText2);
		final EditText number = findViewById(R.id.mainEditText3);
		Button send = findViewById(R.id.mainButton1);
		send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					try {
						Notification.sendNotification(MainActivity.this, head.getText().toString(), text.getText().toString(), Color.parseColor(number.getText().toString()));
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
					}
				}
			});
    }

	@Override
	public void onBackPressed() {
		finish();
	}
}
