package entities.spells.basicspells;

import datatransferobjects.Spell01DTO;
import entities.playercharacters.LocalPlayer;
import main.AssetLoader;
import main.EnumContainer;
import main.GameEngine;
import networking.Client;
import scenes.playing.Camera;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static main.EnumContainer.ServerClientConnectionCopyObjects;


public class Spell01 {

    LocalPlayer localPlayer;
    public final BufferedImage BasicSpellsSpriteSheet = GameEngine.BasicSpellsSpriteSheet;
    public BufferedImage[] spellSprites_START = new BufferedImage[NUMBER_OF_SPRITES];
    public BufferedImage[] spellSprites_FLYING = new BufferedImage[NUMBER_OF_SPRITES];
    public BufferedImage[] spellSprites_END = new BufferedImage[NUMBER_OF_SPRITES];

    public BufferedImage[] currentSpellSprites;
    private EnumContainer.AllQspellStates current_Spell_State;

    public static final int NUMBER_OF_SPRITES = 5;
    private static final int SPEED = 2;
    public static final long SPELL01COOLDOWN = 1000; // 1 seconds in milliseconds
    private final int RANGE = 500;
    private float distanceTraveled = 0;
    //  object starting position on screen. Character pos + (vector * int)
    public float spellPosXWorld, spellPosYWorld;
    public float spellPosXScreen, spellPosYScreen;

    public int xSpriteStartPos, ySpriteStartPos, spriteSize;
    public int animationTick, animationSpeed = 15, animationIndex;
    public float normalizedVectorX;
    public float normalizedVectorY;
    public int mousePosXWorld, mousePosYWorld;

    public int spellCasterClientID;
    public final int spellID;
    //    If true, object will be soon removed from active spell list;
    public boolean playerGotHit;
    private boolean flagForRemoval;
    public Spell01Hitbox spell01Hitbox;

    public static List<Spell01> listOfActiveSpell01s = new ArrayList<>();

    public static boolean QSpellCreatedOnThisMousePress = false;

    public Spell01(LocalPlayer playerCastingThisSpell) {
        localPlayer = playerCastingThisSpell;

        getVector();
        getSpellSprites();
        current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_START;
        setCurrent_Spell_Sprite();
        spellPosXWorld = ((localPlayer.localPlayerHitbox.x + (localPlayer.localPlayerHitbox.width / 2 - 32) + (normalizedVectorX * 125)));
        spellPosYWorld = ((localPlayer.localPlayerHitbox.y + localPlayer.localPlayerHitbox.height / 2 - 32) + (normalizedVectorY * 125));
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;

        spellCasterClientID = Client.ClientID;
        spellID = localPlayer.counterOfThisPlayerQSpells;
        localPlayer.counterOfThisPlayerQSpells++;

        spell01Hitbox = new Spell01Hitbox();
        playerGotHit = false;
        flagForRemoval = false;


        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s.add(this);
        }
    }

    public Spell01(Spell01DTO spell01DTO) {
        getSpellSprites();
        current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_START;
        setCurrent_Spell_Sprite();
        normalizedVectorX = spell01DTO.normalizedVectorX;
        normalizedVectorY = spell01DTO.normalizedVectorY;
        spellPosXWorld = spell01DTO.spellPosXWorld;
        spellPosYWorld = spell01DTO.spellPosYWorld;
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;

        spellCasterClientID = spell01DTO.spellCasterClientID;
        spellID = spell01DTO.spellID;

        spell01Hitbox = new Spell01Hitbox();
        playerGotHit = false;
        flagForRemoval = false;


        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s.add(this);
        }
    }

//    public void setCurrent_Spell_State() {
//
//        Current_Spell = EnumContainer.AllPlayerStates.MOVING_LEFT;
//
//    }

    public void setCurrent_Spell_Sprite() {

        switch (current_Spell_State) {

            case Q_SPELL_START -> {
                currentSpellSprites = spellSprites_START;
            }
            case Q_SPELL_FLYING -> {
                currentSpellSprites = spellSprites_FLYING;
            }
            case Q_SPELL_END -> {
                currentSpellSprites = spellSprites_END;
            }
        }
    }

    private void calculateDistanceTraveled(float x1, float x2, float y1, float y2) {
        distanceTraveled += (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void getSpellSprites() {
        BufferedImage[] spellAssets;
        for (EnumContainer.AllQspellStates element : EnumContainer.AllQspellStates.values()) {
            switch (element) {
                case Q_SPELL_START -> spellAssets = AssetLoader.QSpellFireBallCastStart;
                case Q_SPELL_FLYING -> spellAssets = AssetLoader.QSpellFireballCastFlying;
                case Q_SPELL_END -> spellAssets = AssetLoader.QSpellFireBallCastEnd;
                default -> spellAssets = AssetLoader.QSpellViolet;
            }
            BufferedImage[] spellAssetsRotated = new BufferedImage[NUMBER_OF_SPRITES];
            double angle = Math.atan2(normalizedVectorY, normalizedVectorX);
            Graphics2D g2d;
            for (int i = 0; i < spellAssets.length; i++) {
                BufferedImage spellAsset = new BufferedImage(spellAssets[i].getWidth(), spellAssets[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
                g2d = spellAsset.createGraphics();

                AffineTransform transform = new AffineTransform();
                transform.rotate(angle, (double) spellAsset.getWidth() / 2, (double) spellAsset.getHeight() / 2);
                g2d.setTransform(transform);
                g2d.drawImage(spellAssets[i], 0, 0, null);
                spellAssetsRotated[i] = spellAsset;
            }
            if (element == EnumContainer.AllQspellStates.Q_SPELL_START) spellSprites_START = spellAssetsRotated;
            else if (element == EnumContainer.AllQspellStates.Q_SPELL_FLYING) spellSprites_FLYING = spellAssetsRotated;
            else spellSprites_END = spellAssetsRotated;
        }
    }

    private void getVector() {
        mousePosXWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getX());
        mousePosYWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getY());
        float vectorX = (float) (mousePosXWorld - (localPlayer.localPlayerHitbox.x + localPlayer.localPlayerHitbox.width / 2));
        float vectorY = (float) (mousePosYWorld - (localPlayer.localPlayerHitbox.y + localPlayer.localPlayerHitbox.height / 2));
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            switch (current_Spell_State) {
                case Q_SPELL_START -> {
                    if (animationIndex < NUMBER_OF_SPRITES - 1) animationIndex++;
                    else {
                        animationIndex = 0;
                        current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_FLYING;
                    }
                    animationTick = 0;
                }
                case Q_SPELL_FLYING -> {
                    if (animationIndex < NUMBER_OF_SPRITES - 1) animationIndex++;
                    else animationIndex = 0;
                    animationTick = 0;
                }
                case Q_SPELL_END -> {
                    if (animationIndex < NUMBER_OF_SPRITES - 1) animationIndex++;
                    else {
                        animationIndex = 0;
                        flagForRemoval = true;
                    }
                    animationTick = 0;
                }
            }

        }
    }

    private void spellPositionUpdate() {
        calculateDistanceTraveled(spellPosXWorld, (spellPosXWorld + normalizedVectorX * SPEED), spellPosYWorld, (spellPosYWorld + normalizedVectorY * SPEED));
        if (distanceTraveled <= RANGE && !playerGotHit) {
            spellPosXWorld += (normalizedVectorX * SPEED);
            spellPosYWorld += (normalizedVectorY * SPEED);
            spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
            spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
        } else if (current_Spell_State != EnumContainer.AllQspellStates.Q_SPELL_END) {
            current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_END;
            spell01Hitbox = null;
        }
    }

    public static void updateAllSpells01() {
        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s = listOfActiveSpell01s.stream().filter(
                    spell01 -> spell01.spellPosXWorld >= -64 &&
                            spell01.spellPosYWorld >= -64 &&
                            spell01.spellPosXWorld <= Camera.WHOLE_MAP.getWidth() + 64 &&
                            spell01.spellPosYWorld <= Camera.WHOLE_MAP.getHeight() + 64 &&
                            !spell01.flagForRemoval).collect(Collectors.toList());


            listOfActiveSpell01s.forEach(spell01 -> {
                spell01.setCurrent_Spell_Sprite();
                spell01.spellPositionUpdate();
                spell01.animationController();
                spell01.updateSpellHitboxWorldAndPosOnScreen();
//                System.out.println("Caster:  " + spell01.spellCasterClientID +  "SpellID: " + spell01.spellID + "Pos X: "
//                        + spell01.spellPosXWorld + "Pos Y" + spell01.spellPosYWorld
            });
        }
    }

    public void updateSpellHitboxWorldAndPosOnScreen() {
        if (Objects.nonNull(spell01Hitbox)) {
            spell01Hitbox.x = spellPosXWorld;
            spell01Hitbox.y = spellPosYWorld;
            spell01Hitbox.spell01HitboxPosXScreen = spellPosXScreen;
            spell01Hitbox.spell01HitboxPosYScreen = spellPosYScreen;
        }
    }

    public class Spell01Hitbox extends Rectangle2D.Float {

        public float spell01HitboxPosXScreen, spell01HitboxPosYScreen;

        Spell01Hitbox() {
            super(spellPosXWorld, spellPosYWorld, spellSprites_FLYING[0].getWidth(), spellSprites_FLYING[0].getHeight());
        }

    }
}
