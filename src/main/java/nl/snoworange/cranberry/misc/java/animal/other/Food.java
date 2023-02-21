package nl.snoworange.cranberry.misc.java.animal.other;

public enum Food {

    DOGFOOD,
    CATFOOD,
    CHEESE,
    AIR;

    public boolean rotten = false;

    public boolean isRotten() {
        return rotten;
    }
}
