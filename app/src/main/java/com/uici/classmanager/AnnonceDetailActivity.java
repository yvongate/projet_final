package com.uici.classmanager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uici.classmanager.models.Annonce;

public class AnnonceDetailActivity extends AppCompatActivity {

    private TextView textViewTitre;
    private TextView textViewDate;
    private TextView textViewContenu;
    private LinearLayout layoutMediaContainer;
    private ImageView imageViewMedia;
    private LinearLayout layoutAudioControls;
    private Button buttonPlayPause;
    private VideoView videoView;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_detail);

        textViewTitre = findViewById(R.id.textViewTitre);
        textViewDate = findViewById(R.id.textViewDate);
        textViewContenu = findViewById(R.id.textViewContenu);
        layoutMediaContainer = findViewById(R.id.layoutMediaContainer);
        imageViewMedia = findViewById(R.id.imageViewMedia);
        layoutAudioControls = findViewById(R.id.layoutAudioControls);
        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        videoView = findViewById(R.id.videoView);

        Annonce annonce = getIntent().getParcelableExtra("annonce");

        if (annonce != null) {
            textViewTitre.setText(annonce.getTitre());
            textViewDate.setText(annonce.getDatePublication());
            textViewContenu.setText(annonce.getContenu());

            loadMedia(annonce);
        }
    }

    private void loadMedia(Annonce annonce) {
        String type = annonce.getTypeRessource();
        String path = annonce.getCheminRessource();

        if (type == null || type.equals(Annonce.TYPE_AUCUNE) || path == null) {
            layoutMediaContainer.setVisibility(View.GONE);
            return;
        }

        layoutMediaContainer.setVisibility(View.VISIBLE);

        try {
            Uri uri = Uri.parse(path);

            switch (type) {
                case Annonce.TYPE_IMAGE:
                    imageViewMedia.setVisibility(View.VISIBLE);
                    layoutAudioControls.setVisibility(View.GONE);
                    videoView.setVisibility(View.GONE);
                    imageViewMedia.setImageURI(uri);
                    break;

                case Annonce.TYPE_AUDIO:
                    imageViewMedia.setVisibility(View.GONE);
                    layoutAudioControls.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    setupAudioPlayer(uri);
                    break;

                case Annonce.TYPE_VIDEO:
                    imageViewMedia.setVisibility(View.GONE);
                    layoutAudioControls.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    setupVideoPlayer(uri);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors du chargement de la ressource: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            layoutMediaContainer.setVisibility(View.GONE);
        }
    }

    private void setupAudioPlayer(Uri uri) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();

            buttonPlayPause.setOnClickListener(v -> {
                if (isPlaying) {
                    mediaPlayer.pause();
                    buttonPlayPause.setText("Lecture");
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    buttonPlayPause.setText("Pause");
                    isPlaying = true;
                }
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                buttonPlayPause.setText("Lecture");
                isPlaying = false;
            });
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la préparation de l'audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupVideoPlayer(Uri uri) {
        try {
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(mp -> {
                videoView.start();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la préparation de la vidéo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
