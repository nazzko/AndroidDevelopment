package com.example.lab2.gamedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab2.MainActivity;
import com.example.lab2.R;
import com.example.lab2.network.GbObjectResponse;
import com.example.lab2.network.GbSingleObjectResponse;
import com.example.lab2.network.GiantBombService;
import com.example.lab2.network.RestApi;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_GAME_NAME = "EXTRA_GAME_NAME";
    private static final String EXTRA_GAME_DECK = "EXTRA_GAME_DECK";
    private static final String EXTRA_GAME_GUID = "EXTRA_GAME_GUID";
    private static final String EXTRA_GAME_PICTURE_URL = "EXTRA_GAME_PICTURE_URL";
    private static final String EXTRA_GAME_PICTURE_URL_SUPER = "EXTRA_GAME_PICTURE_URL_SUPER";

    private GiantBombService service = RestApi.creteService(GiantBombService.class);
    private ProgressBar progressBar;
    private ViewGroup vgContent;
    private TextView tvDescription;
    @Nullable private Call<GbSingleObjectResponse> call;

    public static Intent makeIntent(Context context, GbObjectResponse game) {
        return new Intent(context, GameDetailsActivity.class)
                .putExtra(GameDetailsActivity.EXTRA_GAME_NAME, game.getName())
                .putExtra(GameDetailsActivity.EXTRA_GAME_DECK, game.getDeck())
                .putExtra(GameDetailsActivity.EXTRA_GAME_GUID, game.getGuid())
                .putExtra(GameDetailsActivity.EXTRA_GAME_DECK, game.getDeck())
                .putExtra(GameDetailsActivity.EXTRA_GAME_PICTURE_URL, game.getImage().getSmallUrl())
                /*.putExtra(GameDetailsActivity.EXTRA_GAME_PICTURE_URL_SUPER, game.getImage().getSuperUrl())*/;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        tvDescription = findViewById(R.id.tvDescription);
        vgContent = findViewById(R.id.vgContent);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();

        String gameName = intent.getStringExtra(EXTRA_GAME_NAME);
        String gamePicUrl = intent.getStringExtra(EXTRA_GAME_PICTURE_URL);
        String gameDeck = intent.getStringExtra(EXTRA_GAME_DECK);
        String guid = intent.getStringExtra(EXTRA_GAME_GUID);

        ImageView ivPicture = findViewById(R.id.ivPicture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvDeck = findViewById(R.id.tvDeck);

        tvDeck.setText(gameDeck == null ? getString(R.string.no_deck) : gameDeck);
        toolbar.setTitle(gameName);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.get().load(gamePicUrl).into(ivPicture);

        loadGameDetails(guid);
        pitchToZoom();

    }

    private void pitchToZoom() {
        Intent intent = getIntent();
        final ImageView ivPicture = findViewById(R.id.ivPicture);
        final String gamePicUrl = intent.getStringExtra(EXTRA_GAME_PICTURE_URL);
        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameDetailsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.photo_view);
                Picasso.get().load(gamePicUrl).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    private void loadGameDetails(String guid) {
        showLoading();
        call = service.getGameDetails(guid);
        //noinspection ConstantConditions
        call.enqueue(new Callback<GbSingleObjectResponse>() {
            @Override
            public void onResponse(Call<GbSingleObjectResponse> call, Response<GbSingleObjectResponse> response) {
                showContent();
                GbSingleObjectResponse gbResponse = response.body();
                if (gbResponse != null) {
                    String description = gbResponse.getResults().getDescription();
                    CharSequence text = description == null ? getString(R.string.no_description) : Html.fromHtml(description);
                    tvDescription.setText(text);
                }
            }

            @Override
            public void onFailure(Call<GbSingleObjectResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    showContent();
                    Toast.makeText(GameDetailsActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showLoading() {
        vgContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        vgContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

}