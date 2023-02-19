package nl.snoworange.cranberry.misc.java.animal;

import nl.snoworange.cranberry.misc.java.animal.other.Food;

import java.io.File;

public class AnimalUtils {

    public static void eat(Animal animal) {

        Food food;

        if (!animal.hasHunger || animal.isSleeping || animal.saturation >= animal.hungerStrength) return;

        switch (animal.getName()) {
            case "Cat":
                food = Food.CATFOOD;
                break;
            case "Dog":
                food = Food.DOGFOOD;
                break;
            case "Rat":
                food = Food.CHEESE;
                break;
            default:
                food = Food.AIR; //eat air if the animal can't be found
        }

        if (food.isRotten()) return;

        putInMouth(animal, food);

        animal.saturation += animal.hungerStrength / 4;
    }

    public static boolean putInMouth(Animal animal, Food food) {
        if (!animal.isHasmouth()) {
            return false;
        }

        if (food == null) {
            return false;
        }

        return true;
    }

    //do stuff
    public static void findFeedtrayNearby() {

    }

    public static void consumeJarAsCheese(Animal animal, File file) {

        if (!animal.getName().equalsIgnoreCase("Rat")) return;

        if (file.isFile() && file.getName().endsWith(".jar")) {
            animal.saturation = 6942069;
        }
    }

    public static void say(String message) {

    }
}
