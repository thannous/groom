package com.opendata.groom.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import bma.groomservice.data.Poi;
import bma.groomservice.data.dataprovence.DataprovenceManager;

import com.opendata.groom.R;

public class PoiDetailsActivity extends Activity {

	private static final Logger logger = LoggerFactory
			.getLogger(PoiDetailsActivity.class);

	public static final String EXTRA_POI = "poi";

	private int getImageRcFromTheme(String theme) {
		if (DataprovenceManager.THEME_CULTURE.equals(theme)) {
			return R.drawable.ico_culture;
		} else if (DataprovenceManager.THEME_PLEINAIR.equals(theme)) {
			return R.drawable.ico_plein_air;
		} else if (DataprovenceManager.THEME_RESTAURATION.equals(theme)) {
			return R.drawable.ico_gastro;
		} else if (DataprovenceManager.THEME_SPORT.equals(theme)) {
			return R.drawable.sport;
		} else
			return -1;
	}

	private String formatAddress(Poi poi) {
		StringBuffer sb = new StringBuffer();
		if (poi.voie != null)
			sb.append(poi.voie);
		if (poi.bureaudistributeur != null)
			sb.append(" ").append(poi.bureaudistributeur);
		if (poi.codepostal != null)
			sb.append("\n").append(poi.codepostal);
		if (poi.ville != null)
			sb.append(" ").append(poi.ville);
		return sb.toString();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_details_activity);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		Intent tnt = getIntent();
		if (!tnt.hasExtra(EXTRA_POI)) {
			logger.error("Il manque le POI !");
			finish();
		}

		Poi poi = tnt.getParcelableExtra(EXTRA_POI);
		logger.debug("Poi={}", poi);

		int icon = getImageRcFromTheme(poi.theme);
		if (icon >= 0) {
			((ImageView) findViewById(R.id.ImageViewPoiDetailsActivityTitle))
					.setImageResource(icon);
		}

		((TextView) findViewById(R.id.TextViewPoiDetailsActivityTitle))
				.setText(poi.raisonsociale);

		((TextView) findViewById(R.id.TextViewPoiDetailsActivityAdresse))
				.setText(formatAddress(poi));

		((TextView) findViewById(R.id.TextViewPoiDetailsActivityTel))
				.setText(poi.tlphone != null ? poi.tlphone : "");

		WebView wv = (WebView) findViewById(R.id.WebViewPoiDetailsActivitySite);
		if (poi.adresseweb != null) {
			((TextView) findViewById(R.id.TextViewPoiDetailsActivityWeb))
					.setText(poi.adresseweb != null ? poi.adresseweb : "");

			wv.loadUrl(poi.adresseweb);
		} else {
			wv.setVisibility(WebView.INVISIBLE);
		}

		findViewById(R.id.TextViewInitActivityChat).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getApplicationContext(),
								ChatActivity.class));
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_init_2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, DashboardActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}