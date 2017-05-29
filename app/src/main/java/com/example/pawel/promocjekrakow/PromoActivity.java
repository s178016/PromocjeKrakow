package com.example.pawel.promocjekrakow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class PromoActivity extends Activity {
    public static final String TAG = "myLogs";
    public ArrayList<News> newsList = new ArrayList<>();
    public ProgressDialog pd;
    private ListView lv;
    String url;
    String pr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        pd = ProgressDialog.show(PromoActivity.this, "Pobieranie...", "proszę czekać", true, false);
        sprawdzPolaczenie();

    }
    public void ok(View view) {
        pd = ProgressDialog.show(PromoActivity.this, "Pobieranie...", "proszę czekać", true, false);
        Log.d(TAG, "Button clicked");
        sprawdzPolaczenie();
    }



    public void sprawdzPolaczenie() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new Parse().execute();
        } else {
            Toast.makeText(PromoActivity.this, "Sprawdź polączenie z internetem", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    }

    public class Parse extends AsyncTask<String, Void, ArrayList<News>> {
        protected ArrayList<News> doInBackground(String... Params) {
            try {
                Log.d(TAG, "Pobrano stronę");
                Document doc = Jsoup.connect("http://www.groupon.pl/oferta/krakow").get();
                Elements titles = doc.select("p[class=should-truncate deal-title]");
                Elements descs = doc.select("div[class=should-truncate show-in-list-view deal-tile-description description]");
                Elements prices = doc.select("div[class=deal-price] s[class=discount-price c-txt-price]");
                Elements hrefs = doc.select("figure[class=deal-card deal-list-tile deal-tile deal-tile-standard] a[href]");
                Elements strPrices = doc.select("div[class=deal-price] s[class=original-price]");
                Elements locs = doc.select("div[class=icon-marker should-truncate deal-location]");
                for (int i = 0; i < strPrices.size(); i++) {
                    String url = hrefs.get(i).attr("href");
                    Log.i(TAG, url);
                    newsList.add(new News(titles.get(i).text(), prices.get(i).text(), url, locs.get(i).text(),strPrices.get(i).text(),descs.get(i).text()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsList;

        }

        protected void onPostExecute(final ArrayList<News> result) {
            lv = (ListView) findViewById(R.id.lv);
            lv.setAdapter(new MyAdapter(PromoActivity.this,
                    R.layout.list_item, newsList));
            pd.dismiss();




/*
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Uri site = Uri.parse(result.get(position).getUrl());
                    Intent siteIntent = new Intent(Intent.ACTION_VIEW,site);
                    startActivity(siteIntent);
                }
            });
*/

        }
    }
}
