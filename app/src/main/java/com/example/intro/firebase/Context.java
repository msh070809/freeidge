package com.example.intro.firebase;

public class Context {

    String recipe;
    String image;

    public Context(String recipe, String image) {
        this.recipe = recipe;
        this.image = image;
    }
    public Context(){}

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
