package entities.playercharacters;

import scenes.playing.Camera;

public class OnlinePlayer extends LocalPlayer {

    public float playerPosXWorld, playerPosYWorld;
    public float playerPosXScreen, playerPosYScreen;

    private int animationTick, animationSpeed = 60;
    public int animationIndexMoving, animationIndexIdle;


    public OnlinePlayer(int playerPosXWorld, int playerPosYWorld) {

        this.playerPosXWorld = playerPosXWorld;
        this.playerPosYWorld = playerPosYWorld;
        Current_Player_State = PlayerState.MOVING_UP;
        getPlayerSprites("/Character02.png");

    }

    @Override
    public void updatePlayerPositionOnScreen() {
        playerPosXScreen = playerPosXWorld - Camera.cameraPosX;
        playerPosYScreen = playerPosYWorld - Camera.cameraPosY;
    }


    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (playerSpriteController() == playerSpriteIDLE_UP |
                    playerSpriteController() == playerSpriteIDLE_DOWN |
                    playerSpriteController() == playerSpriteIDLE_LEFT |
                    playerSpriteController() == playerSpriteIDLE_RIGHT)
                if (animationIndexIdle < 1)
                    animationIndexIdle++;
                else animationIndexIdle = 0;
            else if (playerSpriteController() == playerSpriteUP |
                    playerSpriteController() == playerSpriteDOWN |
                    playerSpriteController() == playerSpriteLEFT |
                    playerSpriteController() == playerSpriteRIGHT)
                if (animationIndexMoving < 3)
                    animationIndexMoving++;
                else animationIndexMoving = 0;
            animationTick = 0;
        }
    }
}
