package scenes.playing;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.Spell01;
import inputs.PlayerMouseInputs;
import networking.Client;
import networking.PacketManager;
import scenes.SceneEssentials;

import java.awt.*;
import java.io.IOException;

public class Playing implements SceneEssentials {

    LocalPlayer localPlayer;
    Camera camera;
    public Client client;


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
        localPlayer.updatePlayerPositionOnScreenAndPlayerHitbox();
//      spellCastController creates spells, but also sends data to server
        if (!Spell01.QSpellCreatedOnThisMousePress) {
            localPlayer.spellCastController();
        }
        Spell01.updateAllSpells01();


//        Online player update
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {

            onlinePlayer.currentPlayerSpriteOnlinePlayer = onlinePlayer.playerSpriteController();
            onlinePlayer.animationController();
            onlinePlayer.updatePlayerPositionOnScreen();
            onlinePlayer.checkIsOnlinePlayerMoving();
        });

//        Send data to server
        sendMouseDraggedMovementPacket();

    }

    public synchronized void draw(Graphics g) {

//        RYSOWANIE LOKALNYCH OBIEKTOW

//        Rysowanie Kamery
        g.drawImage(camera.WHOLE_MAP.getSubimage(Camera.cameraPosX, Camera.cameraPosY, camera.Camera_Width,
                camera.Camera_Height), 0, 0, null);


//        Rysowanie Localplayera
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
                        (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, null);
            else {
                g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[0],
                        (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, null);
            }
        });

//        Rysowanie Zaklec
        synchronized (Spell01.listOfActiveSpell01s) {
            Spell01.listOfActiveSpell01s.forEach(spell01 -> {
                g.drawImage(spell01.spellSprites[spell01.animationIndex], (int) spell01.spellPosXScreen,
                        (int) spell01.spellPosYScreen, 32, 32, null);
            });
        }

//        Heathbar onlineplayers

        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
            g.setColor(Color.black);
            g.fillRect((int) onlinePlayer.playerPosXScreen, (int) (onlinePlayer.playerPosYScreen - 20),
                    localPlayer.healthbar.healthbarWidth,localPlayer.healthbar.healthbarHeight);
            g.setColor(Color.GREEN);
            g.fillRect((int) onlinePlayer.playerPosXScreen, (int) (onlinePlayer.playerPosYScreen - 20),
                    localPlayer.healthbar.setSizeOfCurrentHealthToDraw(),localPlayer.healthbar.healthbarHeight);
            g.setColor(Color.YELLOW);
            g.drawRect((int) onlinePlayer.playerPosXScreen, (int) (onlinePlayer.playerPosYScreen - 20),
                    localPlayer.healthbar.healthbarWidth,localPlayer.healthbar.healthbarHeight);
                });

//        Healthbar localplayer

        g.setColor(Color.black);
        g.fillRect((int) LocalPlayer.playerPosXScreen, (int) (LocalPlayer.playerPosYScreen - 20),
                localPlayer.healthbar.healthbarWidth,localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.GREEN);
        g.fillRect((int) LocalPlayer.playerPosXScreen, (int) (LocalPlayer.playerPosYScreen - 20),
                localPlayer.healthbar.setSizeOfCurrentHealthToDraw(),localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.YELLOW);
        g.drawRect((int) LocalPlayer.playerPosXScreen, (int) (LocalPlayer.playerPosYScreen - 20),
                localPlayer.healthbar.healthbarWidth,localPlayer.healthbar.healthbarHeight);


//        DEBUGGING

//        HITBOXES
        g.setColor(Color.red);
        g.drawRect((int) localPlayer.localPlayerHitbox.playerHitboxPosXScreen, (int) localPlayer.localPlayerHitbox.playerHitboxPosYScreen,
                (int) localPlayer.localPlayerHitbox.getWidth(), (int) localPlayer.localPlayerHitbox.getHeight());
//        g.drawRect((int) LocalPlayer.playerPosXWorld, (int) LocalPlayer.playerPosYWorld,
//                localPlayer.playerSpriteUP[1].getWidth(),localPlayer.playerSpriteUP[1].getHeight());

    }


    private void sendMouseDraggedMovementPacket() {
        if (PlayerMouseInputs.mouseDragging) {
            try {
                Client.socket.send(PacketManager.movementRequestPacket(localPlayer.mouseClickXPos, localPlayer.mouseClickYPos, Client.ClientID));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
