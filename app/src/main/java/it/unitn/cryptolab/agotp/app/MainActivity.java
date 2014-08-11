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
import android.widget.TextView;

/**
 * This class manages the lifecycle of the app. At first, in the onCreate method,
 * it sets blocked equal true and then calls the sanityCheck method that starts the needed activities.
 * Moreover this class implements the onPause method that forces to insert the pin again
 * and also the onResume method that forces to insert the pin again and allows to generate the OTP value.
 *
 * @author Ambra, Francesco, Giulia
 * @version 1.0
 */

public class MainActivity extends ActionBarActivity {

//initialize the variables
    private SharedPreferences prefs;

    private static final int PIN_ACTIVITY = 0;
    private static final int SEED_ACTIVITY = 1;
    private static final int PIN_ACTIVITY_CHECK = 2;
    private static long StartTime = 0;
    private static String otp;

    private boolean blocked;
    private String PIN = "";

    private Button button_otp;
    private TextView password_otp;



    //metodo onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the blocked variable equal true
        blocked = true;

        prefs = getSharedPreferences(
                AGOTPApplication.Keys.KEYS_REPOSITORY, Context.MODE_PRIVATE
        );

        sanityCheck();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // if the pin has not been inserted
        if (blocked) {
        Log.d("AGOTP", "Siamo in onResume, blocked=true");
            pinCheck();

        }

        else {
            Log.d("AGOTP", "Siamo in onResume, blocked=false");
            continueOnCreate();

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        // set blocked equal true in order to reinserted the pin again.
        blocked = true;
        PIN = "";

    }

    /**
     * This method allows to receive the results from the SeedActivity and the PinActivity.
     * If the seed has been inserted it sets blocked equal true and so the pin has to be inserted again.
     * If the pin has been inserted it sets blocked equal false so it can continue the SanityCheck.
     * If the pin has been inserted for the second time it sets blocked equal false.
     *
     * @param requestCode A number that identifies from which Intent the results came back.
     * @param resultCode A number that checks if the operation is successful or not.
     * @param data An Intent tha carries the result data.
     */

    protected void onActivityResult (int requestCode, int resultCode, Intent data){

        if (requestCode == SEED_ACTIVITY){
            if (resultCode == RESULT_OK){
                // check
                Log.d ("AGOTP", "SIamo nell'onActivityResult, SEED_ACTIVITY == OK");
                blocked = true;
            }
        }
        else if (requestCode == PIN_ACTIVITY){
            if (resultCode == RESULT_OK) {
                //check
                Log.d ("AGOTP", "SIamo nell'onActivityResult, PIN_ACTIVITY == OK");

                blocked = false;
                PIN = data.getStringExtra("PIN");
            }
        }

        else if (requestCode == PIN_ACTIVITY_CHECK){
            if (resultCode == RESULT_OK) {
                //check
                Log.d ("AGOTP", "SIamo nell'onActivityResult, PIN_ACTIVITY_CHECK == OK");
               blocked = false;
               PIN =  data.getStringExtra("PIN");
            }
        }

        sanityCheck();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        

        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * This method checks if the pin and the seed has already been inserted.
     */

    private void sanityCheck() {

        // view if the pin has been inserted
        boolean pin_exists = prefs.getBoolean(AGOTPApplication.Keys.KEY_PIN_EXISTS, false);

        // if the pin does not exist, start the PinActivity
        if (!pin_exists){

            Intent intent = new Intent(this, PinActivity.class);

            startActivityForResult(intent, PIN_ACTIVITY);
            return;

        }

        //view if the seed has been inserted
        boolean seed_exists = prefs.getBoolean(AGOTPApplication.Keys.KEY_SEED_EXISTS, false);

        // if the seed does not exist, start the SeedActivity
        if (!seed_exists){

            Intent intent = new Intent(this, SeedActivity.class);
            intent.putExtra("PIN", PIN);

            startActivityForResult(intent, SEED_ACTIVITY);
            return;

        }

    }

    /**
     * This method starts the PinCheckActivity if the pin exits.
     */

    private void pinCheck() {

        if (blocked) {
            boolean pin_exists = prefs.getBoolean(AGOTPApplication.Keys.KEY_PIN_EXISTS, false);
            if (pin_exists) {

                Intent intent = new Intent(this, PinCheckActivity.class);

                startActivityForResult(intent, PIN_ACTIVITY_CHECK);
                return;

            }
        }

    }

    /**
     * This method allows to generate the OTP value after the insertion of the pin and
     * the seed and the further check of the pin.
     */

    private void continueOnCreate() {

        button_otp = (Button) findViewById(R.id.button_otp);
        password_otp = (TextView) findViewById(R.id.password_otp);

        if (!blocked) {
            //((TextView) findViewById(R.id.pin_saved)).setText("PIN: " + PIN);

            String seed_cipher = prefs.getString(AGOTPApplication.Keys.KEY_SEED, "");

            //check
            String seed = SeedManager.loadSeed(PIN);
            Log.d("AGOTP", "Siamo nella MainActivity, stampiamo il seed: " + seed);
            //seed = prefs.getString(AGOTPApplication.Keys.KEY_SEED, "");

            // ((TextView) findViewById(R.id.seed_saved)).setText("SEED:" + seed);

            // action the button that generates the OTP value

            button_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //We check if the button is pressed after a 30-seconds interval.
                    //If it is,another password will be generated, if not the
                    //password will be the same.
                    if(StartTime == 0) {
                        String seed = SeedManager.loadSeed(PIN);
                        // initialize the time
                        StartTime = System.currentTimeMillis();
                        int time = (int) StartTime;
                        otp = HOTPAlgorithm.gen(seed, time, 6);
                        password_otp.setText(otp);
                    }

                    else{
                        int time = (int) System.currentTimeMillis();
                        if(time-((int)StartTime) >= 30000){
                            String seed = SeedManager.loadSeed(PIN);
                            otp = HOTPAlgorithm.gen(seed,time,6);
                            password_otp.setText(otp);
                            StartTime = (long) time;
                        }
                        else{
                            password_otp.setText(otp);
                        }
                    }

                }
            });


        }

    }

}
