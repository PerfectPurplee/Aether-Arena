package entities.spells.basicspells;

import datatransferobjects.Spell01DTO;
import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import main.GameEngine;
import networking.Client;
import scenes.playing.Camera;

import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static main.EnumContainer.*;


public class Spell01 {


    private static final int NUMBER_OF_SPRITES = 4;
    private static final int SPEED = 2;
    public static final long SPELL01COOLDOWN = 3000; // 3 seconds in milliseconds
    //  object starting position on screen. Character pos + (vector * int)
    public float spellPosXWorld, spellPosYWorld;
    public float spellPosXScreen, spellPosYScreen;

    public static BufferedImage[] spellSprites = new BufferedImage[NUMBER_OF_SPRITES];
    public final BufferedImage BasicSpellsSpriteSheet = GameEngine.BasicSpellsSpriteSheet;
    public int xSpriteStartPos, ySpriteStartPos, spriteSize;
    public int animationTick, animationSpeed = 35, animationIndex;
    public float normalizedVectorX;
    public float normalizedVectorY;
    public int mousePosXWorld, mousePosYWorld;

    public int spellCasterClientID;
    public final int spellID;

    public static List<Spell01> listOfActiveSpell01s = new ArrayList<>();

    public static boolean QSpellCreatedOnThisMousePress = false;

    public Spell01(LocalPlayer playerCastingThisSpell) {
        getVector();
        getSpellSprites();
        spellPosXWorld = (LocalPlayer.playerPosXWorld + 62) + (normalizedVectorX * 150);
        spellPosYWorld = (LocalPlayer.playerPosYWorld + 62) + (normalizedVectorY * 150);
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;

        spellCasterClientID = Client.ClientID;
        spellID = playerCastingThisSpell.counterOfThisPlayerQSpells;
        playerCastingThisSpell.counterOfThisPlayerQSpells++;


        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s.add(this);
        }
    }

    public Spell01(Spell01DTO spell01DTO) {
        getSpellSprites();
        normalizedVectorX = spell01DTO.normalizedVectorX;
        normalizedVectorY = spell01DTO.normalizedVectorY;
        spellPosXWorld = spell01DTO.spellPosXWorld;
        spellPosYWorld = spell01DTO.spellPosYWorld;
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;

        spellCasterClientID = spell01DTO.spellCasterClientID;
        spellID = spell01DTO.spellID;


        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s.add(this);
        }
    }

    private void getSpellSprites() {
        for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
            spellSprites[i] = BasicSpellsSpriteSheet.getSubimage(xSpriteStartPos, 18, 16, 16);
            xSpriteStartPos += 16;
        }
    }

    private void getVector() {
        mousePosXWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getX());
        mousePosYWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getY());
        float vectorX = (float) (mousePosXWorld - (LocalPlayer.playerPosXWorld + 72));
        float vectorY = (float) (mousePosYWorld - (LocalPlayer.playerPosYWorld + 72));
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (animationIndex < NUMBER_OF_SPRITES - 1)
                animationIndex++;
            else animationIndex = 0;
            animationTick = 0;
        }
    }

    private void spellPositionUpdate() {
        spellPosXWorld += (normalizedVectorX * SPEED);
        spellPosYWorld += (normalizedVectorY * SPEED);
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
    }

    public static void updateAllSpells01() {
        synchronized (listOfActiveSpell01s) {
            listOfActiveSpell01s = listOfActiveSpell01s.stream().filter(spell01 ->
                            spell01.spellPosXWorld >= -64 && spell01.spellPosYWorld >= -64 &&
                            spell01.spellPosXWorld <= Camera.WHOLE_MAP.getWidth() + 64 &&
                            spell01.spellPosYWorld <= Camera.WHOLE_MAP.getHeight() + 64).collect(Collectors.toList());


            listOfActiveSpell01s.forEach(spell01 -> {
                spell01.animationController();
                spell01.spellPositionUpdate();
//                System.out.println("Caster:  " + spell01.spellCasterClientID +  "SpellID: " + spell01.spellID + "Pos X: "
//                        + spell01.spellPosXWorld + "Pos Y" + spell01.spellPosYWorld);
            });
        }
    }
}
