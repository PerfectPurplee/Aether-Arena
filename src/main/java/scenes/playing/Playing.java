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

//        Camera Updates
        camera.updateEverythingForCamera();

//        Local player and spells update

        localPlayer.moveController();
        localPlayer.currentPlayerSprite = localPlayer.setCurrentPlayerSprite();
        localPlayer.animationController();
        localPlayer.updatePlayerPositionOnScreenAndPlayerHitbox();
        localPlayer.updateHealthBarCurrentHealthAndPositionOnScreen();
//      spellCastController creates spells, but also sends data to server
        if (!Spell01.QSpellCreatedOnThisMousePress) {
            localPlayer.spellCastController();
        }
        Spell01.updateAllSpells01();


//        Online player update
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {

            onlinePlayer.currentPlayerSpriteOnlinePlayer = onlinePlayer.setCurrentOnlinePlayerSprite();
            onlinePlayer.animationController();
            onlinePlayer.updatePlayerPositionOnScreenAndHitbox();
            onlinePlayer.checkIsOnlinePlayerMoving();
        });

        checkIfAnyPlayerGotHit();

//        Send data to server
//        Movement request packet while mouse is dragging
        sendMouseDraggedMovementPacket();

    }

    public synchronized void draw(Graphics g) {

//        RYSOWANIE LOKALNYCH OBIEKTOW

//        Rysowanie Kamery
        g.drawImage(camera.currentCameraPosition, 0, 0, null);


//        Rysowanie Localplayera
        if (localPlayer.isPlayerMoving) {
            g.drawImage(localPlayer.currentPlayerSprite[localPlayer.animationIndexMoving],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen, null);
        } else {
            g.drawImage(localPlayer.currentPlayerSprite[localPlayer.animationIndexIdle],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen, 256, 256, null);

        }


        //       Rysowanie ONLINE postaci
        synchronized (OnlinePlayer.listOfAllConnectedOnlinePLayers) {
            OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
                if (onlinePlayer.isPlayerMoving)
                    g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[onlinePlayer.animationIndexMoving],
                            (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, 144, 144, null);
                else {
                    g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[0],
                            (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, 144, 144, null);
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
//        Rysowanie Obiektów Mapy


//        Heathbar onlineplayers

        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
            g.setColor(Color.black);
            g.fillRect((int) onlinePlayer.playerPosXScreen + 36, (int) (onlinePlayer.playerPosYScreen - 20),
                    onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
            g.setColor(Color.GREEN);
            g.fillRect((int) onlinePlayer.playerPosXScreen + 36, (int) (onlinePlayer.playerPosYScreen - 20),
                    onlinePlayer.healthbar.setSizeOfCurrentHealthToDraw(), onlinePlayer.healthbar.healthbarHeight);
            g.setColor(Color.YELLOW);
            g.drawRect((int) onlinePlayer.playerPosXScreen + 36, (int) (onlinePlayer.playerPosYScreen - 20),
                    onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
        });

//        Healthbar localplayer

        g.setColor(Color.black);
        g.fillRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.GREEN);
        g.fillRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                localPlayer.healthbar.currentHealthToDraw, localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.YELLOW);
        g.drawRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);


//        DEBUGGING

//        HITBOXES
//        g.setColor(Color.red);
//        g.drawRect((int) localPlayer.localPlayerHitbox.playerHitboxPosXScreen, (int) localPlayer.localPlayerHitbox.playerHitboxPosYScreen,
//                (int) localPlayer.localPlayerHitbox.getWidth(), (int) localPlayer.localPlayerHitbox.getHeight());
//        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer ->
//                g.drawRect((int) onlinePlayer.onlinePlayerHitbox.playerHitboxPosXScreen, (int) onlinePlayer.onlinePlayerHitbox.playerHitboxPosYScreen,
//                        (int) onlinePlayer.onlinePlayerHitbox.getWidth(), (int) onlinePlayer.onlinePlayerHitbox.getHeight()));
//        Spell01.listOfActiveSpell01s.forEach(spell01 ->
//                g.drawRect((int) spell01.spell01Hitbox.spell01HitboxPosXScreen, (int) spell01.spell01Hitbox.spell01HitboxPosYScreen,
//                        (int) spell01.spell01Hitbox.getWidth(), (int) spell01.spell01Hitbox.getHeight()));

//        g.drawRect((int) LocalPlayer.playerPosXWorld, (int) LocalPlayer.playerPosYWorld,
//                localPlayer.playerSpriteIDLE_RIGHT[1].getWidth(),localPlayer.playerSpriteMOVE_RIGHT[1].getHeight());

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
                        localPlayer.healthbar.currentHealth = localPlayer.healthbar.currentHealth - 50;
                    spell.flagForRemoval = true;
                }
                synchronized (OnlinePlayer.listOfAllConnectedOnlinePLayers) {
                    OnlinePlayer.listOfAllConnectedOnlinePLayers
                            .stream()
                            .filter(onlinePlayer -> onlinePlayer.onlinePlayerHitbox.intersects(spell.spell01Hitbox))
                            .forEach(onlinePlayerFiltered -> {
                                if (onlinePlayerFiltered.healthbar.currentHealth > 0) {
                                    onlinePlayerFiltered.healthbar.currentHealth = onlinePlayerFiltered.healthbar.currentHealth - 50;
                                }
                                spell.flagForRemoval = true;
                            });
                }

            });
//            synchronized (Spell01.listOfActiveSpell01s) {
//                Spell01.listOfActiveSpell01s = Spell01.listOfActiveSpell01s.stream().filter(spell01 -> !spell01.flagForRemoval).collect(Collectors.toList());
//            }

        }


    }
    //            FOR ONLY ONE PLAYER GETTING HIT
//            Optional<OnlinePlayer> onlinePlayerInter = OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
//                    .filter(onlinePlayer -> spell.spell01Hitbox.intersects(onlinePlayer.onlinePlayerHitbox.getBounds())).findFirst();
//            onlinePlayerInter.ifPresent(onlinePlayer -> onlinePlayer.healthbar.currentHealth = onlinePlayer.healthbar.currentHealth - 50);

}

