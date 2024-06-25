package com.example.borescopeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.example.borescopeviewer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    /**
     * Borescope Stream Fixer configuration.
     *
     * See https://github.com/mkarr/boroscope_stream_fixer for details.
     */
    public String input_src = "tcp:10.0.2.2:7060/live/stream"; // -i
    public String output_dst = "tcp::7060"; // -o

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

    public void setStatusText(String text)
    {
        TextView status_text = (TextView) findViewById(R.id.status_text);
        status_text.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get button instances.
        Button connect_btn = (Button) findViewById(R.id.connect_btn);
        Button disconnect_btn = (Button) findViewById(R.id.disconnect_btn);

        // Define actions for button clicks.
        connect_btn.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                bsfConnect(input_src, output_dst);
            }
        }));
    }
}