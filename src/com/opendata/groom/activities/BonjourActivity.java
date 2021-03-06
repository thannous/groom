package com.opendata.groom.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.opendata.groom.GroomApplication;
import com.opendata.groom.R;

public class BonjourActivity extends Activity {

	Logger logger = LoggerFactory.getLogger(BonjourActivity.class);

	EditText nomEDT = null;
	EditText dureeEDT = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		// si le nom est déjà renseigné on zappe cet écran
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String accountName = prefs.getString("accountName", "");
		String accountNbJour = prefs.getString("accountNbJour", "");

		setContentView(R.layout.activity_bonjour);

		nomEDT = (EditText) findViewById(R.id.EditTextActivityBonjourNom);
		dureeEDT = (EditText) findViewById(R.id.EditTextActivityBonjourDureeSejour);

		// remplissage automatique
		nomEDT.setText(accountName);
		dureeEDT.setText(accountNbJour);

		Typeface androgyne = Typeface.createFromAsset(getAssets(),
				"Androgyne_TB.otf");
		((TextView) findViewById(R.id.bonjour_question1))
				.setTypeface(androgyne);
		((TextView) findViewById(R.id.bonjour_question2))
				.setTypeface(androgyne);
		findViewById(R.id.TextViewActivityBonjourFooter).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						GroomApplication app = ((GroomApplication) getApplication());
						if (nomEDT.getText() != null
								&& !nomEDT.getText().toString().trim()
										.isEmpty()
								&& dureeEDT.getText() != null
								&& !dureeEDT.getText().toString().trim()
										.isEmpty()) {
							SharedPreferences prefs = PreferenceManager
									.getDefaultSharedPreferences(getApplicationContext());

							app.accountName = nomEDT.getText().toString();
							try {
								int nbJours = Integer.parseInt(dureeEDT
										.getText().toString());
								app.accountNbJour = nbJours;
							} catch (NumberFormatException nfe) {
								logger.error("Ce n'est pas une duree", nfe);
								dureeEDT.setText(1);
							}

							// sauvegarde pour la prochaine fois
							prefs.edit()
									.putString("accountName",
											nomEDT.getText().toString().trim())
									.commit();
							prefs.edit()
									.putString(
											"accountNbJour",
											dureeEDT.getText().toString()
													.trim()).commit();

							Intent intent = new Intent(BonjourActivity.this,
									InitActivity.class);
							startActivity(intent);
							finish();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									BonjourActivity.this);
							builder.setTitle("Attention!");
							builder.setMessage(getString(R.string.complet_champs));
							builder.setIcon(R.drawable.ic_launcher);
							builder.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							builder.show();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bonjour, menu);
		return true;
	}
}
