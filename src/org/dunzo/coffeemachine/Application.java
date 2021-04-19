package org.dunzo.coffeemachine;

import org.dunzo.coffeemachine.entity.Recipe;
import org.dunzo.coffeemachine.service.CoffeeMachineService;

public class Application {
    public static void main(String[] args) throws InterruptedException {

        //this test class demonstrates all the functionalities of coffee machine
        //a different test class or junit could have been used for the same

        //initialise coffee machine and set the outlets of the machine
        int outlets=3;
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(outlets);

        //add ingredients in the machine
        coffeeMachineService.addIngredient("hot_water");
        coffeeMachineService.refill("hot_water",500D);
        coffeeMachineService.addIngredient("hot_milk");
        coffeeMachineService.refill("hot_milk",500D);
        coffeeMachineService.addIngredient("ginger_syrup");
        coffeeMachineService.refill("ginger_syrup",100D);
        coffeeMachineService.addIngredient("sugar_syrup");
        coffeeMachineService.refill("sugar_syrup",100D);
        coffeeMachineService.addIngredient("tea_leaves_syrup");
        coffeeMachineService.refill("tea_leaves_syrup",100D);
        System.out.println();

        //add beverages in coffee machine
        Recipe recipe = new Recipe();
        recipe.addIngredientQuantity("hot_water",100D);
        recipe.addIngredientQuantity("hot_milk",100D);
        recipe.addIngredientQuantity("ginger_syrup",10D);
        recipe.addIngredientQuantity("sugar_syrup",10D);
        recipe.addIngredientQuantity("tea_leaves_syrup",30D);
        coffeeMachineService.addBeverage("hot_tea",recipe);

        recipe = new Recipe();
        recipe.addIngredientQuantity("hot_water",100D);
        recipe.addIngredientQuantity("hot_milk",400D);
        recipe.addIngredientQuantity("ginger_syrup",30D);
        recipe.addIngredientQuantity("sugar_syrup",50D);
        recipe.addIngredientQuantity("tea_leaves_syrup",30D);
        coffeeMachineService.addBeverage("hot_coffee",recipe);

        recipe = new Recipe();
        recipe.addIngredientQuantity("hot_water",300D);
        recipe.addIngredientQuantity("ginger_syrup",30D);
        recipe.addIngredientQuantity("sugar_syrup",50D);
        recipe.addIngredientQuantity("tea_leaves_syrup",30D);
        coffeeMachineService.addBeverage("black_tea",recipe);

        recipe = new Recipe();
        recipe.addIngredientQuantity("hot_water",100D);
        recipe.addIngredientQuantity("ginger_syrup",30D);
        recipe.addIngredientQuantity("sugar_syrup",50D);
        recipe.addIngredientQuantity("green_mixture",30D);
        coffeeMachineService.addBeverage("green_tea",recipe);

        //Display available beverages
        System.out.println("Coffee Machine Serves:");
        for(String beverage:coffeeMachineService.getBeverages()){
            System.out.print(beverage+" ");
        }
        System.out.println();
        System.out.println();

        //make an unavailable beverages
        coffeeMachineService.makeBeverage("hot_milk");

        //make available beverages
        coffeeMachineService.makeBeverage("hot_tea");
        coffeeMachineService.makeBeverage("hot_coffee");
        coffeeMachineService.makeBeverage("green_tea");
        coffeeMachineService.makeBeverage("black_tea");

        //wait for few seconds to see the alert
        //also refill methods will be executed which will affect the output
        Thread.sleep(20000);

        //refill ingredients
        System.out.println();
        coffeeMachineService.refill("hot_milk",100D);
        coffeeMachineService.refill("sugar_syrup",100D);
        coffeeMachineService.refill("tea_leaves_syrup",100D);
        System.out.println();

        //try to refill unknown ingredient
        coffeeMachineService.refill("elaichi_syrup",100D);

        //make available beverages
        coffeeMachineService.makeBeverage("hot_tea");
        coffeeMachineService.makeBeverage("black_tea");
        coffeeMachineService.makeBeverage("green_tea");
        coffeeMachineService.makeBeverage("hot_coffee");

        Thread.sleep(10000);

        //update minimum quantity to modify alert
        coffeeMachineService.updateMinimumQuantity(70D);

        //remove beverage which isn't present
        coffeeMachineService.removeBeverage("elaichi_tea");

        //remove beverage which is present
        coffeeMachineService.removeBeverage("black_tea");

        //remove ingredient which isn't present
        coffeeMachineService.removeIngredient("elaichi_syrup");

        //remove ingredient which is present
        coffeeMachineService.removeIngredient("hot_milk");
    }
}
