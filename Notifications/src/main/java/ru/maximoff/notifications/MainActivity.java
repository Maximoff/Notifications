package ru.maximoff.notifications;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CheckBox;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final Notification notification = new Notification(this);
		final EditText head = findViewById(R.id.mainEditText1);
		final EditText text = findViewById(R.id.mainEditText2);
		final EditText color = findViewById(R.id.mainEditText3);
		final CheckBox ongoing = findViewById(R.id.mainCheckBox1);
		Button send = findViewById(R.id.mainButton1);
		send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					try {
						notification.sendNotification(head.getText().toString(), text.getText().toString(), Color.parseColor(color.getText().toString()), ongoing.isChecked());
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
					}
				}
			});
		Button cancel = findViewById(R.id.mainButton2);
		cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					notification.cancel();
				}
			});
    }

	@Override
	public void onBackPressed() {
		finish();
	}
}
