package com.example.pawel.promocjekrakow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import static com.example.pawel.promocjekrakow.R.id.USD;
import static java.lang.String.valueOf;

public class MainActivity extends Activity {
    public static final String TAG = "myLogs";
    public ProgressDialog pd;
    public EditText et;
    public TextView USD;
    public TextView EUR;
    String valUSD = null;
    String valEUR = null;
    Currency cur;
    Float Curs;
    String Valuta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sprawdzPolaczenie();
        pd = ProgressDialog.show(MainActivity.this, "Pobieranie...", "proszę czekać", true, false);
        USD = (TextView) findViewById(R.id.USD);
        EUR = (TextView) findViewById(R.id.EUR);
    }

    public class ParseWalute extends AsyncTask<Void, Void, Currency> {
        @Override
        protected Currency doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://kursy-walut.mybank.pl").get();
                Log.d(TAG, "Pobrano kurs");
                Element EUR = doc.select("div[class=box_mini] b[class=b2]").first();
                Element USD = doc.select("div[class=box_mini] b[class=b2]").get(2);
                valUSD = valueOf(USD.text());
                valEUR = valueOf(EUR.text());
                valUSD = valUSD.replaceAll(",", ".");
                valEUR = valEUR.replaceAll(",", ".");
                cur = new Currency(valUSD,valEUR);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return cur;
        }

        protected void onPostExecute(Currency cur) {
            TextView EUR=(TextView) findViewById(R.id.EUR);
            EUR.setText("Kurs EUR: " + cur.mEUR);
            TextView USD=(TextView) findViewById(R.id.USD);
            USD.setText("Kurs USD: " + cur.mUSD);
            USD.setBackgroundColor(Color.CYAN);
            Curs = Float.parseFloat((valUSD));
            Valuta = "USD";
            pd.dismiss();
        }
    }
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.USD:
                    Toast.makeText(MainActivity.this, "Wybrano przeliczenie z USD", Toast.LENGTH_LONG).show();
                    USD.setBackgroundColor(Color.CYAN);
                    EUR.setBackgroundColor(Color.TRANSPARENT);
                    Curs = Float.parseFloat((valUSD));
                    Valuta = "USD";
                    break;
                case R.id.EUR:
                    Toast.makeText(MainActivity.this, "Wybrano przeliczenie z EUR", Toast.LENGTH_LONG).show();
                    EUR.setBackgroundColor(Color.CYAN);
                    USD.setBackgroundColor(Color.TRANSPARENT);
                    Curs = Float.parseFloat((valEUR));
                    Valuta = "EUR";
                    break;
            }
        };
        public void goToProm(View view) {
            Intent promoIntent = new Intent(MainActivity.this, PromoActivity.class);
            startActivity(promoIntent);
        }
        public void Convert(View view) {
            TextView textViewRez = (TextView) findViewById(R.id.textViewRez);
            et = (EditText) findViewById(R.id.et);
            if (et.length()>0) {
                Float U = Float.parseFloat(et.getText().toString());
                Float rez = U * Curs;
                textViewRez.setText(et.getText().toString()+ " " + Valuta + " to jest " + rez.toString() +" PLN" );
            }
            else {Toast.makeText(MainActivity.this, "Wprowadź kwotę", Toast.LENGTH_LONG).show();}
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    public void sprawdzPolaczenie() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new ParseWalute().execute();
        } else {
            Toast.makeText(MainActivity.this, "Sprawdź polączenie z internetem", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    }


    }

