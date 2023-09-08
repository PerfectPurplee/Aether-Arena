package entities.playercharacters;

import scenes.playing.Camera;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayer extends LocalPlayer {

    public float playerPosXWorld, playerPosYWorld;
    public float playerPosXScreen, playerPosYScreen;

    private int animationTick, animationSpeed = 60;
    public int animationIndexMoving, animationIndexIdle;

    public int onlinePlayerID;

    public static List<OnlinePlayer> listOfAllConnectedOnlinePLayers = new ArrayList<>();

    public OnlinePlayer(int onlinePlayerID) {

        this.onlinePlayerID = onlinePlayerID;
        Current_Player_State = PlayerState.MOVING_UP;
        getPlayerSprites("/Character02.png");

        listOfAllConnectedOnlinePLayers.add(this);

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
