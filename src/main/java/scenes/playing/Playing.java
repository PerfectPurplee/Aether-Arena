package scenes.playing;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.Spell01;
import inputs.PlayerMouseInputs;
import networking.Client;
import networking.PacketManager;
import scenes.SceneEssentials;

import javax.swing.text.html.Option;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

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
            onlinePlayer.updatePlayerPositionOnScreenAndHitbox();
            onlinePlayer.checkIsOnlinePlayerMoving();
        });

        checkIfAnyPlayerGotHit();

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
        synchronized (OnlinePlayer.listOfAllConnectedOnlinePLayers) {
            OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
                if (onlinePlayer.isPlayerMoving)
                    g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[onlinePlayer.animationIndexMoving],
                            (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, null);
                else {
                    g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[0],
                            (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, null);
                }
            });
        }
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
                    onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
            g.setColor(Color.GREEN);
            g.fillRect((int) onlinePlayer.playerPosXScreen, (int) (onlinePlayer.playerPosYScreen - 20),
                    onlinePlayer.healthbar.setSizeOfCurrentHealthToDraw(), onlinePlayer.healthbar.healthbarHeight);
            g.setColor(Color.YELLOW);
            g.drawRect((int) onlinePlayer.playerPosXScreen, (int) (onlinePlayer.playerPosYScreen - 20),
                    onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
        });

//        Healthbar localplayer

        g.setColor(Color.black);
        g.fillRect((int) LocalPlayer.playerPosXScreen, (int) (LocalPlayer.playerPosYScreen - 20),
                localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.GREEN);
        g.fillRect((int) LocalPlayer.playerPosXScreen, (int) (LocalPlayer.playerPosYScreen - 20),
                localPlayer.healthbar.setSizeOfCurrentHealthToDraw(), localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.YELLOW);
        g.drawRect((int) LocalPlayer.playerPosXScreen, (int) (LocalPlayer.playerPosYScreen - 20),
                localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);


//        DEBUGGING

//        HITBOXES
        g.setColor(Color.red);
        g.drawRect((int) localPlayer.localPlayerHitbox.playerHitboxPosXScreen, (int) localPlayer.localPlayerHitbox.playerHitboxPosYScreen,
                (int) localPlayer.localPlayerHitbox.getWidth(), (int) localPlayer.localPlayerHitbox.getHeight());
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer ->
                g.drawRect((int) onlinePlayer.onlinePlayerHitbox.playerHitboxPosXScreen, (int) onlinePlayer.onlinePlayerHitbox.playerHitboxPosYScreen,
                        (int) onlinePlayer.onlinePlayerHitbox.getWidth(), (int) onlinePlayer.onlinePlayerHitbox.getHeight()));
        Spell01.listOfActiveSpell01s.forEach(spell01 ->
                g.drawRect((int) spell01.spell01Hitbox.spell01HitboxPosXScreen, (int) spell01.spell01Hitbox.spell01HitboxPosYScreen,
                        (int) spell01.spell01Hitbox.getWidth(), (int) spell01.spell01Hitbox.getHeight()));

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

    private void checkIfAnyPlayerGotHit() {

        synchronized (Spell01.listOfActiveSpell01s) {
            Spell01.listOfActiveSpell01s.forEach(spell -> {

                if (localPlayer.localPlayerHitbox.intersects(spell.spell01Hitbox)) {
                    if (localPlayer.healthbar.currentHealth > 0)
                        localPlayer.healthbar.currentHealth = localPlayer.healthbar.currentHealth - 10;
                }
                synchronized (OnlinePlayer.listOfAllConnectedOnlinePLayers) {
                    OnlinePlayer.listOfAllConnectedOnlinePLayers
                            .stream()
                            .filter(onlinePlayer -> onlinePlayer.onlinePlayerHitbox.intersects(spell.spell01Hitbox))
                            .forEach(onlinePlayerFiltered -> {
                                if (onlinePlayerFiltered.healthbar.currentHealth > 0) {
                                    onlinePlayerFiltered.healthbar.currentHealth = onlinePlayerFiltered.healthbar.currentHealth - 10;
                                }
                            });
                }
            });

        }


        //            FOR ONLY ONE PLAYER GETTING HIT
//            Optional<OnlinePlayer> onlinePlayerInter = OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
//                    .filter(onlinePlayer -> spell.spell01Hitbox.intersects(onlinePlayer.onlinePlayerHitbox.getBounds())).findFirst();
//            onlinePlayerInter.ifPresent(onlinePlayer -> onlinePlayer.healthbar.currentHealth = onlinePlayer.healthbar.currentHealth - 50);
    }

}

