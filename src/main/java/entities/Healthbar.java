package entities;

import java.util.ArrayList;
import java.util.List;

public class Healthbar {


    public final int maxHealth;
    public int currentHealth;
    public int healthbarWidth, healthbarHeight;
    public float healthbarPositionOnScreenX;
    public float healthbarPositionOnScreenY;
    public int offsetY;

//    public static List<Healthbar> listOfAllHealthbars = new ArrayList<>();

    public Healthbar(int maxHealth, float playerPosOnScreenX, float playerPosOnScreenY) {
        healthbarWidth = 72;
        healthbarHeight = 10;
        offsetY = 20;
        this.maxHealth = maxHealth;
        currentHealth = maxHealth - 50;
        healthbarPositionOnScreenX = playerPosOnScreenX;
        healthbarPositionOnScreenY = playerPosOnScreenY - offsetY;
    }

    public int setSizeOfCurrentHealthToDraw() {
        return (int)(((float) currentHealth / maxHealth) * healthbarWidth);
    }
}
