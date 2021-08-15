package com.example.shapeofsound;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button pickVideo;
    VideoView videoView;
    MediaController mc;
    private static final int PICK_VIDEO = 1;
    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recordButton = (Button) findViewById(R.id.recordButton);

        if (!checkCamera()) recordButton.setEnabled(false);

        pickVideo = (Button) findViewById(R.id.pickButton);
        videoView = findViewById(R.id.video);
        mc = new MediaController(MainActivity.this);
        videoView.setMediaController(mc);
        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent= new Intent(Intent.ACTION_PICK);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent,PICK_VIDEO);
            }
        });
    }

    private boolean checkCamera(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            return true;
        else return false;
    }

    public void startRecording(View view){
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myvideo.mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri=Uri.fromFile(mediaFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode == VIDEO_CAPTURE)
            Toast.makeText(this, "Video has been saved to:\n"+data.getData(),Toast.LENGTH_LONG).show();
        else if(resultCode==RESULT_CANCELED && requestCode == VIDEO_CAPTURE)
            Toast.makeText(this,"Video recording cancelled!", Toast.LENGTH_LONG).show();
        else if (requestCode == PICK_VIDEO && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
        else Toast.makeText(this,"Failed to action",Toast.LENGTH_LONG).show();
    }


}