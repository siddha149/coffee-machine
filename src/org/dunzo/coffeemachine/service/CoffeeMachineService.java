package org.dunzo.coffeemachine.service;

import org.dunzo.coffeemachine.entity.Recipe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//the actual coffee machine implementing functionalities
public class CoffeeMachineService implements ICoffeeMachineService{

    Map<String, Recipe> servedBeverages;

    Map<String, Double> inventory;

    //generally same minimum quantity exists for all ingredients
    Double minimumQuantity;

    //parallel outlets for creating beverages
    ExecutorService outlets;

    //alert notifier
    ExecutorService alertNotifier;

    public CoffeeMachineService(int outlets) {
        this.servedBeverages=new HashMap<>();

        //concurrent hashmap since multiple outlets will try to access inventory
        this.inventory=new ConcurrentHashMap<>();

        //set default minimum quantity as 50ml
        this.minimumQuantity=50D;

        //initialise outlets of coffee machine
        this.outlets= Executors.newFixedThreadPool(outlets);

        //initialise alert notifier
        this.alertNotifier=Executors.newSingleThreadExecutor();

        //check and notify quantity of ingredients every 30s
        this.alertNotifier.execute(() -> {
            while(true){
                Set<String> insufficientIngredients = getMinimumQuantityIngredients();
                if(insufficientIngredients.size()>0)
                    alertMinimumQuantity(insufficientIngredients);
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void makeBeverage(String beverage) {
        if(!servedBeverages.containsKey(beverage)){
            System.out.println(beverage+" not served by coffee machine.");
            return;
        }

        Recipe recipe = servedBeverages.get(beverage);

        //choose one of the outlets to prepare the beverage
        outlets.execute(()-> {
            try {
                makeBeverage(beverage,recipe);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void makeBeverage(String beverage, Recipe recipe) throws InterruptedException {
        Map<String, Double> recipeIngredients = recipe.getIngredientQuantities();
        //need for synchronised since either the full beverage can be prepared or not
        synchronized (CoffeeMachineService.class){
            boolean canBePrepared=true;
            System.out.println();
            for(String recipeIngredient: recipeIngredients.keySet()){
                if(!inventory.containsKey(recipeIngredient)){
                    System.out.println(beverage + " cannot be prepared because " + recipeIngredient + " is not available");
                    canBePrepared=false;
                }
                else if(recipeIngredients.get(recipeIngredient)>inventory.get(recipeIngredient)){
                    System.out.println(beverage + " cannot be prepared because " + recipeIngredient + " is not sufficient");
                    canBePrepared=false;
                }
            }

            if(!canBePrepared){
                return;
            }

            for(String recipeIngredient: recipeIngredients.keySet()){
                inventory.put(recipeIngredient,inventory.get(recipeIngredient)-recipeIngredients.get(recipeIngredient));
            }
            System.out.println("Outlet "+Thread.currentThread().getId()+" preparing "+beverage);
            //time taken to make beverage
            Thread.sleep(5000);
            System.out.println(beverage + " is prepared");
        }
    }

    @Override
    public void refill(String ingredient, Double quantity) {
        if(!inventory.containsKey(ingredient)){
            System.out.println(ingredient + " is not added in the machine.");
            return;
        }
        inventory.put(ingredient,inventory.get(ingredient)+quantity);
        System.out.println(ingredient + " refilled.");
    }

    @Override
    public void updateMinimumQuantity(Double quantity) {
        minimumQuantity=quantity;
    }

    @Override
    public Set<String> getMinimumQuantityIngredients() {
        Set<String> minimumQuantityIngredients = new HashSet<>();
        for(Map.Entry<String, Double> ingredient:inventory.entrySet()){
            if(ingredient.getValue()<minimumQuantity)
                minimumQuantityIngredients.add(ingredient.getKey());
        }
        return minimumQuantityIngredients;
    }

    @Override
    public void alertMinimumQuantity(Set<String> ingredients) {
        System.out.println();
        for (String ingredient:ingredients){
            System.out.println(ingredient+" is running low.");
        }
    }

    @Override
    public Set<String> getBeverages() {
        return servedBeverages.keySet();
    }

    @Override
    public void addBeverage(String beverage, Recipe recipe) {
        if(servedBeverages.containsKey(beverage)){
            System.out.println(beverage + " is already added in the machine.");
            return;
        }
        servedBeverages.put(beverage,recipe);
    }

    @Override
    public void removeBeverage(String beverage) {
        if(!servedBeverages.containsKey(beverage)){
            System.out.println(beverage + " is not present in the machine.");
            return;
        }
        servedBeverages.remove(beverage);
        System.out.println(beverage + " is removed from the machine.");
    }

    @Override
    public void addIngredient(String ingredient) {
        if(inventory.containsKey(ingredient)){
            System.out.println(ingredient + " is already added in the machine.");
            return;
        }
        inventory.put(ingredient,0D);
    }

    @Override
    public void removeIngredient(String ingredient) {
        if(!inventory.containsKey(ingredient)){
            System.out.println(ingredient + " is not present in the machine.");
            return;
        }
        inventory.remove(ingredient);
        System.out.println(ingredient + " is removed from the machine.");
    }
}
