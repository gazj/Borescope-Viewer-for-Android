package com.example.borescopeviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.VideoView;

import com.example.borescopeviewer.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    /**
     * Borescope Stream Fixer configuration.
     *
     * See https://github.com/mkarr/boroscope_stream_fixer for details.
     */
    public String borescope_input_src = "tcp:192.168.10.123:7060"; // -i
    public String emulated_input_src = "tcp:10.0.2.2:7060/stream.mjpeg"; // emulation/testing

    /**
     * Load bsf.c as a library.
     */
    static { System.loadLibrary("bsf"); }

    /**
     * Native functions defined in bfs.c and called via JNI.
     */
    public native int bsfConnect(String input_src, String output_dst);

    private ActivityMainBinding binding;

    // Update status message on view. Ensures updates from non-main threads still work.
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

    // Debugging methods, called via JNI.
    public void log_string(String s) { Log.d("DEBUG", s); }
    public void log_char(char c) { Log.d("DEBUG", String.format("%c", c)); }
    public void log_int(int i) { Log.d("DEBUG", String.format("%d", i)); }

    // Action on view creation.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set input field defaults.
        EditText inputSourceField = (EditText) findViewById(R.id.input_source);
        inputSourceField.setText(borescope_input_src);

        // Connect button setup.
        Button connect_btn = (Button) findViewById(R.id.connect_btn);
        connect_btn.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                // Disable the form, requiring an app restart to attempt another connection.
                inputSourceField.setEnabled(false);
                connect_btn.setEnabled(false);
                // Create a new thread for bsf.c functions to run under.
                new Thread(){
                    public void run(){
                        bsfConnect(
                            inputSourceField.getText().toString(),
                            "tcp:127.0.0.1:7060"
                        );
                    }
                }.start();
            }
        }));

        // Configure the video player.
        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        // Suppress error dialog when connection is unavailable.
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        // Set a timer for the VideoView to poll the configured output destination.
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(!videoView.isPlaying()) {
                        Uri uri = Uri.parse("tcp://127.0.0.1:7060");
                        videoView.setVideoURI(uri);
                        videoView.start();
                    } else setStatusText("Streaming...", null);
                } catch (Exception e) {
                    //
                }
            }
        }, 0, 1000);
    }
}