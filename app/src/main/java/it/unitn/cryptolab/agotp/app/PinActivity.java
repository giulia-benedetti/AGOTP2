package it.unitn.cryptolab.agotp.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
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
 * This class allows to insert the pin, hash it with SHA-256 and save the digest.
 * @author Ambra, Francesco, Giulia
 * @version 1.0
 */

public class PinActivity extends ActionBarActivity {

    // initialitation of the variables
    private EditText pin_input;
    private Button button_save_pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        ((ImageView) findViewById(R.id.pinbg)).setAlpha(0.2F);

        // cast of variables
        pin_input = (EditText) findViewById(R.id.pin_input);
        button_save_pin = (Button) findViewById(R.id.button_save_pin);

        // actions the button
        button_save_pin.setOnClickListener(new View.OnClickListener()

        {

            public void onClick(View view) {

         String input = pin_input.getText().toString();

                // check if the pin is of five digits
                if (input.length() != 5) {
                    // view Toast
                    Context context = getApplicationContext();
                    CharSequence text = "Il PIN deve essere di 5 numeri.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }

                // save the pin hashed in  the SharedPreferences
                SharedPreferences prefs = getSharedPreferences(
                        AGOTPApplication.Keys.KEYS_REPOSITORY, Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor = prefs.edit();
                String hash = Hash.generate(input);
                Log.d("AGOTP", "v2 === " + hash + " ===");
                editor.putString(AGOTPApplication.Keys.KEY_PIN_HASH, hash);
                editor.putBoolean(AGOTPApplication.Keys.KEY_PIN_EXISTS, true);
                editor.commit();

                // send the result to the MainActivity
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                returnIntent.putExtra("PIN", input);
                finish();

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
