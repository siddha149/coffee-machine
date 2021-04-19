package org.dunzo.coffeemachine.service;

import org.dunzo.coffeemachine.entity.Recipe;

import java.util.Set;

//basic functionality of a coffee machine to be implemented by all coffee machines
public interface ICoffeeMachineService {
    void makeBeverage(String beverage) throws InterruptedException;
    void refill(String ingredient, Double quantity);
    void updateMinimumQuantity(Double quantity);
    Set<String> getMinimumQuantityIngredients();
    void alertMinimumQuantity(Set<String> ingredients);
    Set<String> getBeverages();
    void addBeverage(String beverage, Recipe recipe);
    void removeBeverage(String beverage);
    void addIngredient(String ingredient);
    void removeIngredient(String ingredient);
}
