package scenes.playing;

import entities.playercharacters.Player1;

import java.awt.*;

public class Playing {

    Player1 player1;

    int animationTick, animationSpeed = 40, animationIndex;

    public Playing(Player1 player1) {
        this.player1 = player1;


    }

    public void update() {
        animationController();
        player1.moveController();
    }

    public void draw(Graphics g) {
        g.drawImage(player1.playerSpriteRIGHT[animationIndex], player1.playerPosX, player1.playerPosY, null);
    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (animationIndex < player1.playerSpriteRIGHT.length - 1)
                animationIndex++;
            else animationIndex = 0;
            animationTick = 0;
        }
    }
}
