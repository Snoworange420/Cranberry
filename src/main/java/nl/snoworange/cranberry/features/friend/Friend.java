package nl.snoworange.cranberry.features.friend;

import java.util.ArrayList;
import java.util.List;

public class Friend {

    private List<String> friends = new ArrayList<>();

    public Friend instance;

    public Friend() {
        instance = this;
    }

    public Friend getInstance() {
        return instance;
    }

    public List<String> getFriends() {
        return friends;
    }

    public boolean isFriend(String name) {
        for (String friend : friends) {
            if (name.equalsIgnoreCase(friend)) {
                return true;
            }
        }

        return false;
    }

    public void addFriend(String name) {
        friends.add(name);
    }

    public void removeFriend(String name) {
        friends.remove(name);
    }
}
