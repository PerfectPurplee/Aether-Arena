package scenes.playing;

import entities.playercharacters.PlayerClass;

import java.awt.*;

public class Playing {

    PlayerClass playerClass;

    int animationTick, animationSpeed = 60, animationIndexMoving, animationIndexIdle;

    public Playing(PlayerClass playerClass) {
        this.playerClass = playerClass;


    }

    public void update() {
        playerClass.moveController();
        playerClass.playerSpriteController();
        animationController();
    }

    public void draw(Graphics g) {
        if (playerClass.checkIsCharacterMoving()) {
            g.drawImage(playerClass.playerSpriteController()[animationIndexMoving], (int) playerClass.playerPosX, (int) playerClass.playerPosY, 144, 144,  null);
        } else if (!playerClass.checkIsCharacterMoving()) {
            g.drawImage(playerClass.playerSpriteController()[animationIndexIdle], (int) playerClass.playerPosX, (int) playerClass.playerPosY, 144,144, null);

        }
//        g.drawImage(player1.playerSpriteIDLE_DOWN[animationIndexIdle], player1.playerPosX, player1.playerPosY,null);
    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_UP |
                    playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_DOWN |
                    playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_LEFT |
                    playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_RIGHT)
                if (animationIndexIdle < 1)
                    animationIndexIdle++;
                else animationIndexIdle = 0;
            else if (playerClass.playerSpriteController() == playerClass.playerSpriteUP |
                    playerClass.playerSpriteController() == playerClass.playerSpriteDOWN |
                    playerClass.playerSpriteController() == playerClass.playerSpriteLEFT |
                    playerClass.playerSpriteController() == playerClass.playerSpriteRIGHT)
                if (animationIndexMoving < 3)
                    animationIndexMoving++;
                else animationIndexMoving = 0;
            animationTick = 0;
        }
    }
}
