package it.unitn.cryptolab.agotp.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This class allows to insert the seed, encrypts it with AES-256 and save the ciphertext.
 * @author Ambra, Francesco, Giulia
 * @version 1.0
 */

public class SeedActivity extends ActionBarActivity {

// initialitation of the variables
    private EditText seed_input;
    private Button button_save_seed;
    private String PIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed);

        ((ImageView) findViewById(R.id.seedbg)).setAlpha(0.2F);

        PIN = getIntent().getStringExtra("PIN");

        // cast of the variables
        seed_input = (EditText) findViewById(R.id.seed_input);
        button_save_seed = (Button) findViewById(R.id.button_save_seed);
        // actions the button
        button_save_seed.setOnClickListener(new View.OnClickListener()

        {

            public void onClick(View view) {

                String input = seed_input.getText().toString();

                // check if the pin is of ten characters
                if (seed_input.length() != 10) {
                    // view the Toast
                    Context context = getApplicationContext();
                    CharSequence text = "Il seed deve essere composto da 10 caratteri alfanumerici.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }

                // save the encrypted seed in the SharedPreferences

                SeedManager.saveSeed(PIN, input);

                // send the result to the MainActivity
                Intent returnIntent = new Intent();
                setResult (RESULT_OK, returnIntent);
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
