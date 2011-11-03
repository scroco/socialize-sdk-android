package com.socialize.ui.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.socialize.Socialize;
import com.socialize.auth.AuthProviderType;
import com.socialize.ui.SocializeActivity;
import com.socialize.ui.SocializeUI;

public class SampleActivity extends SocializeActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		final EditText txtEntity = (EditText) findViewById(R.id.txtEntity);
		
		final EditText txtFB = (EditText) findViewById(R.id.txtFBId);
		final CheckBox chkSSO = (CheckBox) findViewById(R.id.chkFacebook);

		final Button btn = (Button) findViewById(R.id.btnCommentView);
		final Button btnClearCache = (Button) findViewById(R.id.btnClearCache);
		final Button btnActionViewAuto = (Button) findViewById(R.id.btnActionViewAuto);
		final Button btnActionViewManual = (Button) findViewById(R.id.btnActionViewManual);
		
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setupOverrides();
				
				SocializeUI.getInstance().setFacebookAppId(txtFB.getText().toString());
				SocializeUI.getInstance().setFacebookSingleSignOnEnabled(chkSSO.isChecked());
				SocializeUI.getInstance().showCommentView(SampleActivity.this, txtEntity.getText().toString());
			}
		});
		
		btnClearCache.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog dialog = ProgressDialog.show(v.getContext(), "Clearing cache", "Clearing cache...");
				
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						Socialize.getSocialize().clear3rdPartySession(AuthProviderType.FACEBOOK);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						dialog.dismiss();
					}
				}.execute((Void)null);
			}
		});
		
		btnActionViewAuto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setupOverrides();
				Intent intent = new Intent(SampleActivity.this, ActionBarAutoActivity.class);
				SocializeUI.getInstance().setEntityUrl(SampleActivity.this, intent, txtEntity.getText().toString());
				SocializeUI.getInstance().setFacebookAppId(txtFB.getText().toString());
				SocializeUI.getInstance().setFacebookSingleSignOnEnabled(chkSSO.isChecked());
				startActivity(intent);
			}
		});
		
		btnActionViewManual.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setupOverrides();
				Intent intent = new Intent(SampleActivity.this, ActionBarManualActivity.class);
				SocializeUI.getInstance().setEntityUrl(SampleActivity.this, intent, txtEntity.getText().toString());
				SocializeUI.getInstance().setFacebookAppId(txtFB.getText().toString());
				SocializeUI.getInstance().setFacebookSingleSignOnEnabled(chkSSO.isChecked());
				startActivity(intent);
			}
		});
		
	}
	
	protected void setupOverrides() {
		
		final CheckBox chkMockFB = (CheckBox) findViewById(R.id.chkMockFB);
		final CheckBox chkMockSocialize = (CheckBox) findViewById(R.id.chkMockSocialize);
		
		if(chkMockFB.isChecked()) {
			if(chkMockSocialize.isChecked()) {
				SocializeUI.getInstance().setBeanOverrides("socialize_ui_mock_beans.xml", "socialize_ui_mock_socialize_beans.xml");
			}
			else {
				SocializeUI.getInstance().setBeanOverrides("socialize_ui_mock_beans.xml");
			}
		}
		else if(chkMockSocialize.isChecked()) {
			SocializeUI.getInstance().setBeanOverrides("socialize_ui_mock_socialize_beans.xml");
		}
		else {
			SocializeUI.getInstance().setBeanOverrides((String[]) null);
		}
	}
}