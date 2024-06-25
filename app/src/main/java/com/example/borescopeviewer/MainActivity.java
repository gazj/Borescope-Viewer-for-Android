package com.example.borescopeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

import com.example.borescopeviewer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * Borescope Stream Fixer configuration.
     *
     * See https://github.com/mkarr/boroscope_stream_fixer for details.
     */
    public String borescope_input_src = "tcp:192.168.10.123:7060"; // -i
    public String emulated_input_src = "tcp:10.0.2.2:7060/live/stream"; // emulation/testing
    public String output_dst = "bsf_output"; // -o

    /*

    /**
     * Load bsf.c as a library.
     */
    static { System.loadLibrary("bsf"); }

    /**
     * Native functions defined in bfs.c and called via JNI.
     */
    public native int bsfConnect(String input_src, String output_dst);

    private ActivityMainBinding binding;

    public void setStatusText(String text, String code)
    {
        // UI updates cannot occur as a result of JNI calls from other threads.
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                TextView status_text = (TextView) findViewById(R.id.status_text);
                String new_text = code.isEmpty()
                    ? String.format("%s", text)
                    : String.format("%s (%s)", text, code);
                status_text.setText(new_text);
            }
        });
    }

    public void log_string(String s)
    {
        Log.d("DEBUG", s);
    }

    public void log_char(char c)
    {
        Log.d("DEBUG", String.format("%c", c));
    }

    public void log_int(int i)
    {
        Log.d("DEBUG", String.format("%d", i));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get editable text fields
        EditText inputSourceField = (EditText) findViewById(R.id.input_source);
        EditText outputDestinationField = (EditText) findViewById(R.id.output_destination);

        // Set text field defaults.
        inputSourceField.setText(emulated_input_src);
        outputDestinationField.setText(output_dst);

        // Connect button setup.
        Button connect_btn = (Button) findViewById(R.id.connect_btn);
        connect_btn.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                // Disable the form, requiring an app restart to attempt another connection.
                inputSourceField.setEnabled(false);
                outputDestinationField.setEnabled(false);
                connect_btn.setEnabled(false);
                // Create a new thread for bsf.c functions to run under.
                new Thread(){
                    public void run(){
                        bsfConnect(
                            inputSourceField.getText().toString(),
                            outputDestinationField.getText().toString()
                        );
                    }
                }.start();
            }
        }));
    }
}