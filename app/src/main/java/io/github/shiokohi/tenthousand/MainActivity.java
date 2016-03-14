package io.github.shiokohi.tenthousand;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    //TODO: Verschiedene Projekte auswählen
    //TODO: Zeiten rückwirkend abschätzen (jede woche eine stunde geübt seit...)
    //TODO: Preferences API nutzen?
    //TODO: Gesamtzeit getrennt in Stunden und Minuten darstellen
    //TODO: Statt Texteingabe vielleicht Dropdown Liste zur Auswahl einer Zeit?
    //TODO: Angabe von Prozent der Gesamtzeit auch grafisch

    //Const
    private static final String MSG_NO_INPUT = "Bitte Felder ausfüllen";

    //TextView
    private TextView txtView_top = null;
    private TextView txt_bottom = null;
    private TextView txt_gesamt = null;

    //EditText
    private EditText txt_eingabe = null;

    //Button
    private Button cmd_eingabe = null;
    private Button cmd_daten_loeschen = null;

    private Integer time_until_now = 0;
    private Integer time_total = 0;
    private Integer saved_time = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        this.txtView_top = (TextView) findViewById(R.id.txtView_top);
        this.txt_bottom = (TextView) findViewById(R.id.txt_bottom);
        this.txt_gesamt = (TextView) findViewById(R.id.txt_gesamt);
        this.txt_eingabe = (EditText) findViewById(R.id.txt_eingabe);
        this.cmd_eingabe = (Button) findViewById(R.id.cmd_eingabe);
        this.cmd_daten_loeschen = (Button) findViewById(R.id.cmd_deleteAll);


        //Gespeicherte Daten laden
        Context context = this;
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        saved_time = sharedPref.getInt("time", 0);
        //TEst der Datenübername per Toast
        // Toast.makeText(this, "Aus dem Speicher geladene Zeit: "+saved_time, Toast.LENGTH_SHORT).show();
        time_total = saved_time;
        //  Toast.makeText(this, "Time Total: "+time_total, Toast.LENGTH_SHORT).show();

        txt_gesamt.setText(Integer.toString(time_total));

        //Listener des Eingabebuttons
        cmd_eingabe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Check ob der User etwas eingegeben hat.
                if (checkUserInput()) {
                    time_until_now = Integer.parseInt(txt_eingabe.getText().toString());
                    time_total += time_until_now;
                    txt_gesamt.setText(Integer.toString(time_total));
                    // Toast.makeText(v.getContext(), "Erfolgreich übernommen. Zeit bis jetzt: "+time_total, Toast.LENGTH_SHORT).show();
                    Toast.makeText(v.getContext(), "Du übst schon "+ calculateTotalPercentage(time_total) + " Prozent.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(v.getContext(), "Bitte einen Wert eingeben", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Dieser Button löscht einfach nur die Daten
        cmd_daten_loeschen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                time_total = 0;
                txt_gesamt.setText(Integer.toString(time_total));
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        //Hier werden die Daten in den permanenten Speicher der Preferences geschrieben
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("time", time_total);

        // Commit the edits!
        editor.commit();
        //Toast.makeText(this, "In den Speicher geschriebene: "+time_total, Toast.LENGTH_SHORT).show();
    }



   /* // Validierung des Inputs
    private boolean isValidNumber(String number) {
        if (number != null) {
            return true;
        }
        return false;
    }*/

    //Validierung des Inputs
    private boolean checkUserInput(){
        //Initialisieren
        boolean valid = true;
        String strInput = txt_eingabe.getText().toString();
        //Toast.makeText(this, "Hier in checkUserInput", Toast.LENGTH_SHORT).show();
        //Überprüfen
        if (strInput.equalsIgnoreCase("")){
            valid = false;
            //Toast.makeText(this, "Input nicht gültig", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    //Hier wird der Prozentsatz der 10k h berechnet
    //TODO: QS: Hier wird immer .0 als Nachkommastelle ausgegeben?
    //TODO: App kann abstürzen bei zu hohen Werten (double? float?)
    public static double calculateTotalPercentage(int a ){
        double percentage;
        percentage = a/10000/60;
        return percentage;
    }
}
