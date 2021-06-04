package com.example.intro.firebase;

import java.util.ArrayList;

public class TotalRecipe {
    private String Title;
    private String Image;
    private String Ingredient;
    private ArrayList<Context> context;

    public ArrayList<Context> getContext() {
        return context;
    }

    public void setContext(ArrayList<Context> context) {
        this.context = context;
    }

    public String getIngredient() {
        return Ingredient;
    }
    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


}
