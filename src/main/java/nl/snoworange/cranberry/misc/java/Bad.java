package nl.snoworange.cranberry.misc.java;

public class Bad {

    private static Bad instance;
    public Good good;

    public Bad() {
        instance = this;
    }

    public static Bad getSkill() {
        return instance;
    }

    public Good getGood() {
        return good;
    }
}
