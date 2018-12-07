package com.example.lab2.gamedetails;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab2.MainActivity;
import com.example.lab2.R;
import com.example.lab2.database.DatabaseHelper;
import com.example.lab2.network.FavGame;
import com.example.lab2.network.GbObjectResponse;
import com.example.lab2.network.GbSingleObjectResponse;
import com.example.lab2.network.GiantBombService;
import com.example.lab2.network.RestApi;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String EXTRA_GAME_NAME = "EXTRA_GAME_NAME";
    private static final String EXTRA_GAME_DECK = "EXTRA_GAME_DECK";
    private static final String EXTRA_GAME_GUID = "EXTRA_GAME_GUID";
    private static final String EXTRA_GAME_DESCRIPTION = "EXTRA_GAME_DESCRIPTION";
    private static final String EXTRA_GAME_PICTURE_URL = "EXTRA_GAME_PICTURE_URL";
    private static final Bitmap EXTRA_GAME_PICTURE = null;

    private String gameId = "";
    private GiantBombService service = RestApi.creteService(GiantBombService.class);
    private ProgressBar progressBar;
    private ViewGroup vgContent;
    private TextView tvDescription;
    public ImageView favImage;

    @Nullable private Call<GbSingleObjectResponse> call;
    private Button addToFavButton;

    public static Intent makeIntent(Context context, GbObjectResponse game) {
        return new Intent(context, GameDetailsActivity.class)
                .putExtra(GameDetailsActivity.EXTRA_GAME_NAME, game.getName())
                .putExtra(GameDetailsActivity.EXTRA_GAME_DECK, game.getDeck())
                .putExtra(GameDetailsActivity.EXTRA_GAME_GUID, game.getGuid())
                .putExtra(GameDetailsActivity.EXTRA_GAME_PICTURE_URL, game.getImage().getSmallUrl());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        tvDescription = findViewById(R.id.tvDescription);
        vgContent = findViewById(R.id.vgContent);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        gameId = intent.getStringExtra(EXTRA_GAME_GUID);

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
        addToFavorites();

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

    private void addToFavorites() {
        addToFavButton = findViewById(R.id.addToFavButton);
        addToFavButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        favImage = findViewById(R.id.ivPicture);
        tvDescription = findViewById(R.id.tvDescription);

        Intent intent = getIntent();

        String guid = intent.getStringExtra(EXTRA_GAME_GUID);
        String gameName = intent.getStringExtra(EXTRA_GAME_NAME);
        String gameDeck = intent.getStringExtra(EXTRA_GAME_DECK);
        String description = tvDescription.getText().toString();
        Bitmap image = ((BitmapDrawable)favImage.getDrawable()).getBitmap();

        DatabaseHelper databaseHelper = DatabaseHelper.createInstance(this);
        databaseHelper.insertValues(guid, gameName, gameDeck, description, image);
        addToFavButton.setBackground(getResources().getDrawable(R.drawable.ic_star_black_24dp));
    }

}
