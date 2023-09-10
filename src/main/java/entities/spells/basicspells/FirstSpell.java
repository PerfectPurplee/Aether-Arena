package entities.spells.basicspells;

import entities.playercharacters.LocalPlayer;
import main.GameEngine;
import scenes.playing.Camera;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static inputs.PlayerMouseInputs.CurrentMousePosition;

public class FirstSpell {

    private final int NUMBER_OF_SPRITES = 4;
    private final int SPEED = 2;
    //  object starting position on screen. Character pos + (vector * int)
    public float spellPosXWorld, spellPosYWorld;
    public float spellPosXScreen, spellPosYScreen;

    public BufferedImage[] spellSprites = new BufferedImage[NUMBER_OF_SPRITES];
    public final BufferedImage BasicSpellsSpriteSheet = GameEngine.BasicSpellsSpriteSheet;
    public int xSpriteStartPos, ySpriteStartPos, spriteSize;
    public int animationTick, animationSpeed = 35, animationIndex;
    public float normalizedVectorX;
    public float normalizedVectorY;
    public int mousePosXWorld, mousePosYWorld;

    public static List<FirstSpell> ListOfActiveFirstSpells = new ArrayList<>();

    public FirstSpell() {
        getVector();
        getSpellSprites();
        spellPosXWorld = (LocalPlayer.playerPosXWorld + 62) + (normalizedVectorX * 150);
        spellPosYWorld = (LocalPlayer.playerPosYWorld + 62) + (normalizedVectorY * 150);
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;

        synchronized (ListOfActiveFirstSpells) {
            ListOfActiveFirstSpells.add(this);
        }
    }

    private void getSpellSprites() {
        for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
            spellSprites[i] = BasicSpellsSpriteSheet.getSubimage(xSpriteStartPos, 18, 16, 16);
            xSpriteStartPos += 16;
        }
    }

    private void getVector() {
        mousePosXWorld = (int) (CurrentMousePosition.getX() + Camera.cameraPosX);
        mousePosYWorld = (int) (CurrentMousePosition.getY() + Camera.cameraPosY);
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

    private void moveSpellAndUpdatePosOnScreen() {
        spellPosXWorld += (normalizedVectorX * SPEED);
        spellPosYWorld += (normalizedVectorY * SPEED);
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
    }

    public static void updateFirstSpells() {
        synchronized (ListOfActiveFirstSpells) {
            ListOfActiveFirstSpells.forEach(firstSpell -> {
                firstSpell.animationController();
                firstSpell.moveSpellAndUpdatePosOnScreen();
            });
        }
    }
}
