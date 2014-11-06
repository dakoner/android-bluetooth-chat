/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.morrildl.garduino;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Garduino extends Activity {
	private Handler handler;
	private CommThread thread;
	private ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String text = (String)msg.obj;

                ((TextView)findViewById(R.id.serial_output)).append(text);
            }
        };
	}

	@Override
	public void onStart() {
		super.onStart();
		dialog = ProgressDialog.show(this, "Connecting", "Searching for a Bluetooth serial port...");
		thread = new CommThread(BluetoothAdapter.getDefaultAdapter(), dialog, handler);
		thread.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		if (thread != null)
			thread.cancel();
		thread = null;
	}
}
