package it.unitn.cryptolab.agotp.app;

import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This class checks if the pin now inserted equals the pin inserted before by comparing the hash codes.
 * @author Ambra, Francesco, Giulia
 * @version 1.0
 */

public class PinCheckActivity extends ActionBarActivity {

    //initialize the variables
    private EditText pin_input2;
    private Button button_save_pin2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_check);

        final Context context = getApplicationContext();

        ((ImageView) findViewById(R.id.pinbg2)).setAlpha(0.2F);

        // cast the variables
        pin_input2 = (EditText) findViewById(R.id.pin_input2);
        button_save_pin2 = (Button) findViewById(R.id.button_save_pin2);

        // action the button
        button_save_pin2.setOnClickListener(new View.OnClickListener()

        {

            public void onClick(View view) {

                String input2 = pin_input2.getText().toString();
                // check if the pin is of five digits
                if (input2.length() != 5) {

                    // view the Toast
                    CharSequence text = "Il PIN deve essere di 5 numeri.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;

                }


                SharedPreferences prefs = getSharedPreferences(
                        AGOTPApplication.Keys.KEYS_REPOSITORY, Context.MODE_PRIVATE
                );

                // hash of pin that inserted now
                String hash1 = Hash.generate(input2);

                String hash = prefs.getString(AGOTPApplication.Keys.KEY_PIN_HASH, "");

                if(hash.length() > 45){
                  hash = hash.substring(0,45);
                }
                // check
                Log.d("AGOTP","Siamo nella PinCheckActivity,l'hash del primo pin: " + hash);
                Log.d("AGOTP", "Siamo nella PinCheckActivity, l'hash del secondo pin: " + hash1);
                Log.d("AGOTP", "Siamo nella PinCheckActivity,lunghezza del primo pin hashato - lunghezza secondo pin hashato" + hash.length() + "-" + hash1.length());
                Log.d("AGOTP", "Siamo nella PinCheckActivity,sono uguali i due pin hashati? " + Boolean.toString(hash.equals(hash1)));

                // check if the pin now inserted equals the pin inserted before by comparing the hash codes.
                if (hash.equals(hash1)) {

                    //CharSequence text3 = "Il PIN è corretto";
                    //int duration = Toast.LENGTH_SHORT;
                    //Toast toast = Toast.makeText(context, text3, duration);
                    //toast.show();

                    //send the result to the MainActivity
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    returnIntent.putExtra("PIN", input2);
                    finish();

                    return;

                }

                else {

                    //view the Toast
                    CharSequence text3 = "Il PIN è errato";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text3, duration);
                    toast.show();

                    return;
                }

            }

        });
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.seed, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }
