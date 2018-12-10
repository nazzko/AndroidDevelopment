package com.example.lab2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab2.R;
import com.example.lab2.database.DatabaseHelper;
import com.example.lab2.entities.FavGame;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FavGameDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_GAME_NAME = "EXTRA_GAME_NAME";
    private static final String EXTRA_GAME_DECK = "EXTRA_GAME_DECK";
    private static final String EXTRA_GAME_GUID = "EXTRA_GAME_GUID";
    private static final String EXTRA_GAME_DESCRIPTION = "EXTRA_GAME_DESCRIPTION";
    private static final String EXTRA_GAME_PICTURE_URL = "EXTRA_GAME_PICTURE_URL";
    private static final Bitmap EXTRA_GAME_PICTURE = null;

    private Button addToFavButton;
    private ImageView favImage;

    public static Intent makeFavIntent(Context context, FavGame game) {
        return new Intent(context, FavGameDetailsActivity.class)
                .putExtra(FavGameDetailsActivity.EXTRA_GAME_GUID, game.getId())
                .putExtra(FavGameDetailsActivity.EXTRA_GAME_NAME, game.getName())
                .putExtra(FavGameDetailsActivity.EXTRA_GAME_DECK, game.getDeck())
                .putExtra(FavGameDetailsActivity.EXTRA_GAME_DESCRIPTION, game.getDescription());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Intent intent = getIntent();

        String gameName = intent.getStringExtra(EXTRA_GAME_NAME);
        String gameDeck = intent.getStringExtra(EXTRA_GAME_DECK);
        String gameDescription = intent.getStringExtra(EXTRA_GAME_DESCRIPTION);


        ImageView ivPicture = findViewById(R.id.ivPicture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvDeck = findViewById(R.id.tvDeck);
        TextView tvDescription = findViewById(R.id.tvDescription);

        tvDeck.setText(gameDeck == null ? getString(R.string.no_deck) : gameDeck);
        CharSequence text = tvDescription == null ? getString(R.string.no_description) : Html.fromHtml(gameDescription);
        tvDescription.setText(text);
        toolbar.setTitle(gameName);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ivPicture.setImageBitmap(game.getImage());

        checkExist();
//        pitchToZoom();
        addToFavorites();
    }

    private void checkExist() {
        addToFavButton = findViewById(R.id.addToFavButton);
        DatabaseHelper databaseHelper = DatabaseHelper.createInstance(this);
        Intent intent = getIntent();
        String guid = intent.getStringExtra(EXTRA_GAME_GUID);

        if(databaseHelper.checkExist(guid)){
            addToFavButton.setBackground(getResources().getDrawable(R.drawable.ic_star_black_24dp));
        }
    }

    private void pitchToZoom() {
        Intent intent = getIntent();
        final ImageView ivPicture = findViewById(R.id.ivPicture);
        final String gamePicUrl = intent.getStringExtra(EXTRA_GAME_PICTURE_URL);
        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FavGameDetailsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.photo_view);
                Picasso.get().load(gamePicUrl).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorites() {
        addToFavButton = findViewById(R.id.addToFavButton);
        addToFavButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        favImage = findViewById(R.id.ivPicture);

        Intent intent = getIntent();
        String guid = intent.getStringExtra(EXTRA_GAME_GUID);

        DatabaseHelper databaseHelper = DatabaseHelper.createInstance(this);
        databaseHelper.removeGame(guid);
        addToFavButton.setBackground(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
    }
}