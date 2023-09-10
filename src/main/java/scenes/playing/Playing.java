package scenes.playing;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.FirstSpell;

import java.awt.*;

public class Playing {

    LocalPlayer localPlayer;
    Camera camera;


    public Playing(LocalPlayer localPlayer, Camera camera) {
        this.localPlayer = localPlayer;

        this.camera = camera;


    }

    public synchronized void update() {

//        Local player and spells update

        localPlayer.moveController();
        localPlayer.currentPlayerSprite = localPlayer.playerSpriteController();
        localPlayer.animationController();
        camera.updateCameraPosition();
        localPlayer.updatePlayerPositionOnScreen();
        FirstSpell.updateFirstSpells();

//        Online player update
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {

          onlinePlayer.currentPlayerSpriteOnlinePlayer = onlinePlayer.playerSpriteController();
            onlinePlayer.animationController();
            onlinePlayer.updatePlayerPositionOnScreen();
            onlinePlayer.checkIsOnlinePlayerMoving();
        });

    }

    public synchronized void draw(Graphics g) {

//        RYSOWANIE LOKALNYCH OBIEKTOW

//        Rysowanie Kamery
        g.drawImage(camera.WHOLE_MAP.getSubimage(Camera.cameraPosX, Camera.cameraPosY, camera.Camera_Width,
                camera.Camera_Height), 0, 0, null);


//        Rysowanie postaci
        if (localPlayer.isPlayerMoving) {
            g.drawImage(localPlayer.currentPlayerSprite[localPlayer.animationIndexMoving],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen, null);
        } else {
            g.drawImage(localPlayer.currentPlayerSprite[0],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen, null);

        }

        //       Rysowanie ONLINE postaci

        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
            if (onlinePlayer.isPlayerMoving)
                g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[onlinePlayer.animationIndexMoving],
                        (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen,72,72, null);
            else {
                g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[0],
                        (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen,72,72, null);
            }
        });

//        Rysowanie Zaklec
        FirstSpell.ListOfActiveFirstSpells.forEach(firstSpell -> {
            g.drawImage(firstSpell.spellSprites[firstSpell.animationIndex], (int) firstSpell.spellPosXScreen,
                    (int) firstSpell.spellPosYScreen, 32, 32, null);
        });

//        DEBUGGING
//        g.drawRect((int) LocalPlayer.playerPosXWorld, (int) LocalPlayer.playerPosYWorld,
//                localPlayer.playerSpriteUP[1].getWidth(),localPlayer.playerSpriteUP[1].getHeight());

    }

}
