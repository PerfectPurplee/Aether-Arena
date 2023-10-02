package entities;

import java.util.ArrayList;
import java.util.List;

public class Healthbar {


    public final int maxHealth;
    public int currentHealth;
    public int healthbarWidth, healthbarHeight;
    public int healthbarPositionOnScreenX;
    public int healthbarPositionOnScreenY;
    public int offsetY;

//    public static List<Healthbar> listOfAllHealthbars = new ArrayList<>();

    public Healthbar(int maxHealth, float playerPosOnScreenX, float playerPosOnScreenY) {
        healthbarWidth = 72;
        healthbarHeight = 10;
        offsetY = 20;
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
        healthbarPositionOnScreenX = (int) (playerPosOnScreenX + 36);
        healthbarPositionOnScreenY = (int) (playerPosOnScreenY - offsetY);
    }

    public int setSizeOfCurrentHealthToDraw() {
        return (int)(((float) currentHealth / maxHealth) * healthbarWidth);
    }
}
