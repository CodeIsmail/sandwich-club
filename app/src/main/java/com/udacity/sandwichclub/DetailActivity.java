package com.udacity.sandwichclub;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        final Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        loadImage(sandwich, ingredientsIv);


        ingredientsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(sandwich, ingredientsIv);
            }
        });


        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        TextView otherNamesTextView = findViewById(R.id.also_known_tv);
        TextView originTextView = findViewById(R.id.origin_tv);
        TextView ingredientsTextView = findViewById(R.id.ingredients_tv);
        TextView descriptionTextView = findViewById(R.id.description_tv);

        String namesFormatedString = formattedString(sandwich.getAlsoKnownAs());
        String ingredientsFormatedString = formattedString(sandwich.getIngredients());
        otherNamesTextView.setText(namesFormatedString);
        originTextView.setText(sandwich.getPlaceOfOrigin());
        ingredientsTextView.setText(ingredientsFormatedString);
        descriptionTextView.setText(sandwich.getDescription());


    }

    private String formattedString(List<String> list) {

        StringBuilder formattedString = new StringBuilder();

        if (list.size() == 0) {
            formattedString.append("None");
        } else {
            for (int i = 0; i < list.size(); i++) {
                if ((i + 1) == list.size()) {
                    formattedString.append(list.get(i)).append(".");
                    break;
                } else {
                    formattedString.append(list.get(i)).append(", ");
                }
            }
        }

        return formattedString.toString();
    }

    private void loadImage(Sandwich sandwich, ImageView ingredientsIv) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            Picasso.with(this)
                    .load(sandwich.getImage())
                    .into(ingredientsIv);
        else {
            Toast.makeText(this, "Loading image failed. Tap image to try again!",
                    Toast.LENGTH_LONG).show();
            Picasso.with(this)
                    .load(R.mipmap.ic_launcher)
                    .into(ingredientsIv);
        }
    }
}