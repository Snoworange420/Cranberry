package nl.snoworange.cranberry.misc.java.animal.categories;

public enum AnimalType {

    INVERTEBRATA("Invertebrata"),
    VERTEBRATA("Vertebrata"),
    MAMMALS("Mammals"),
    REPTILES("Reptiles"),
    AMPHIBIANS("Amphibians"),
    HERBIVORE("Herbivore"),
    OMNIVORE("Omnivore"),
    CARNIVORE("Carnivore");

    public String type;

    AnimalType(String type) {
        this.type = type;
    }

    public String getAnimalType() {
        return this.type;
    }
}
