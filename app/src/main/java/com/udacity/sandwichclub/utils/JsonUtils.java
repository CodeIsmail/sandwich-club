package com.udacity.sandwichclub.utils;

import android.text.TextUtils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static Sandwich sandwich;

    public static Sandwich parseSandwichJson(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject nameJsonObject = jsonObject.getJSONObject("name");
            String mainName = nameJsonObject.getString("mainName");
            JSONArray otherNamesJson = nameJsonObject.getJSONArray("alsoKnownAs");

            int otherNamesSize = otherNamesJson.length();
            ArrayList<String> otherNamesList = new ArrayList<>();
            for (int i = 0; i < otherNamesSize; i++) {
                otherNamesList.add(otherNamesJson.getString(i));
            }
            String placeOfOriginString = jsonObject.getString("placeOfOrigin");
            if (TextUtils.isEmpty(placeOfOriginString))
                placeOfOriginString = "None";

            String descriptionString = jsonObject.getString("description");
            String imageString = jsonObject.getString("image");
            JSONArray ingredientJsonArray = jsonObject.getJSONArray("ingredients");

            ArrayList<String> ingredientList = new ArrayList<>();
            for (int i = 0; i < ingredientJsonArray.length(); i++) {
                ingredientList.add(ingredientJsonArray.getString(i));
            }

            sandwich = new Sandwich(mainName, otherNamesList, placeOfOriginString,
                    descriptionString, imageString, ingredientList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sandwich;
    }
}