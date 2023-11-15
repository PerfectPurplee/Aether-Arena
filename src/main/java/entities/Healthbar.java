package entities;

public class Healthbar {


    public final int maxHealth;
    public int currentHealth;
    public int currentHealthToDraw;
    public int healthbarWidth, healthbarHeight;
    public int healthbarPositionOnScreenX;
    public int healthbarPositionOnScreenY;
    public int offsetY;

    public Healthbar(int maxHealth, float playerHitboxPosOnScreenX, float playerHitboxPosOnScreenY) {
        healthbarWidth = 72;
        healthbarHeight = 10;
        offsetY = 20;
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
        currentHealthToDraw = setSizeOfCurrentHealthToDraw();
        healthbarPositionOnScreenX = (int) (playerHitboxPosOnScreenX);
        healthbarPositionOnScreenY = (int) (playerHitboxPosOnScreenY - offsetY);
    }

    public int setSizeOfCurrentHealthToDraw() {
        return (int)(((float) currentHealth / maxHealth) * healthbarWidth);
    }
}
