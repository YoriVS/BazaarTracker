package org.example.bazaartracker.crafting;

import java.util.ArrayList;

public class Recipe {
    private final Ingredient A1;
    private final Ingredient A2;
    private final Ingredient A3;
    private final Ingredient B1;
    private final Ingredient B2;
    private final Ingredient B3;
    private final Ingredient C1;
    private final Ingredient C2;
    private final Ingredient C3;

    public Recipe(Ingredient A1, Ingredient A2, Ingredient A3,
                  Ingredient B1, Ingredient B2, Ingredient B3,
                  Ingredient C1, Ingredient C2, Ingredient C3) {
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
        this.B1 = B1;
        this.B2 = B2;
        this.B3 = B3;
        this.C1 = C1;
        this.C2 = C2;
        this.C3 = C3;
    }

    public Ingredient getA1() {
        return A1;
    }
    public Ingredient getA2() {
        return A2;
    }
    public Ingredient getA3() {
        return A3;
    }
    public Ingredient getB1() {
        return B1;
    }
    public Ingredient getB2() {
        return B2;
    }
    public Ingredient getB3() {
        return B3;
    }
    public Ingredient getC1() {
        return C1;
    }
    public Ingredient getC2() {
        return C2;
    }
    public Ingredient getC3() {
        return C3;
    }

    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(A1);
        ingredients.add(A2);
        ingredients.add(A3);
        ingredients.add(B1);
        ingredients.add(B2);
        ingredients.add(B3);
        ingredients.add(C1);
        ingredients.add(C2);
        ingredients.add(C3);

        return ingredients;
    }


}
