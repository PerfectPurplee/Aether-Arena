package scenes.playing;

import entities.playercharacters.Player1;

import java.awt.*;

public class Playing {

    Player1 player1;

    int animationTick, animationSpeed = 60, animationIndexMoving, animationIndexIdle;

    public Playing(Player1 player1) {
        this.player1 = player1;


    }

    public void update() {
        player1.moveController();
        player1.playerSpriteController();
        animationController();
    }

    public void draw(Graphics g) {
        if(player1.checkIsCharacterMoving()) {
            g.drawImage(player1.playerSpriteController()[animationIndexMoving], player1.playerPosX, player1.playerPosY, null);
        }else if(!player1.checkIsCharacterMoving()) {
            g.drawImage(player1.playerSpriteController()[animationIndexIdle], player1.playerPosX, player1.playerPosY, null);

        }
//        g.drawImage(player1.playerSpriteIDLE_DOWN[animationIndexIdle], player1.playerPosX, player1.playerPosY,null);
    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (player1.playerSpriteController() == player1.playerSpriteIDLE_UP |
                    player1.playerSpriteController() == player1.playerSpriteIDLE_DOWN |
                    player1.playerSpriteController() == player1.playerSpriteIDLE_LEFT |
                    player1.playerSpriteController() == player1.playerSpriteIDLE_RIGHT)
                if (animationIndexIdle < 1)
                    animationIndexIdle++;
                else animationIndexIdle = 0;
            else if (player1.playerSpriteController() == player1.playerSpriteUP |
                    player1.playerSpriteController() == player1.playerSpriteDOWN |
                    player1.playerSpriteController() == player1.playerSpriteLEFT |
                    player1.playerSpriteController() == player1.playerSpriteRIGHT)
                if (animationIndexMoving < 3)
                    animationIndexMoving++;
                else animationIndexMoving = 0;
            animationTick = 0;
        }
    }
}
