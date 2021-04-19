package org.dunzo.coffeemachine.entity;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
    Map<String, Double> ingredientQuantities;

    public Recipe() {
        this.ingredientQuantities = new HashMap<>();
    }

    public Map<String, Double> getIngredientQuantities() {
        return ingredientQuantities;
    }

    public void addIngredientQuantity(String ingredient, Double quantity){
        ingredientQuantities.put(ingredient,quantity);
    }
}
