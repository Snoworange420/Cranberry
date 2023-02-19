package nl.snoworange.cranberry.misc.java.animal.animals;

import nl.snoworange.cranberry.misc.java.animal.Animal;
import nl.snoworange.cranberry.misc.java.animal.AnimalUtils;
import nl.snoworange.cranberry.misc.java.animal.categories.AnimalType;

public class Dog extends Animal {

    public Dog() {
        super("Dog", AnimalType.CARNIVORE, 1035);
    }

    @Override
    public void onLiving() {
        AnimalUtils.say("woof");
        AnimalUtils.say("woof");
        AnimalUtils.say("woof");
    }

    @Override
    public void onHunger() {
        AnimalUtils.findFeedtrayNearby();
        AnimalUtils.eat(this);
    }

    @Override
    public void onSleep() {
        super.onSleep();
    }
}
