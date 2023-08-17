package entities.spells.basicspells;

import entities.playercharacters.PlayerClass;
import entities.spells.BasicSpell;
import main.GameEngine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static inputs.PlayerMouseInputs.CurrentMousePosition;

public class FirstSpell {

    private final int NUMBER_OF_SPRITES = 4;
    private final int SPEED = 2;
    //  object starting position on screen. Character pos + (vector * int)
    public float spellStartingPosX, spellStartingPosY;

    public BufferedImage[] spellSprites = new BufferedImage[NUMBER_OF_SPRITES];
    public final BufferedImage BasicSpellsSpriteSheet = GameEngine.BasicSpellsSpriteSheet;
    public int xSpriteStartPos, ySpriteStartPos, spriteSize;
    public int animationTick, animationSpeed = 35, animationIndex;
    public float normalizedVectorX;
    public float normalizedVectorY;

    public static  List<FirstSpell> ListOfActiveFirstSpells = new ArrayList<>();

    public FirstSpell() {
        getVector();
        getSpellSprites();

        spellStartingPosX = (PlayerClass.playerPosX + 62) + (normalizedVectorX * 150);
        spellStartingPosY = (PlayerClass.playerPosY + 62) + (normalizedVectorY * 150);

        ListOfActiveFirstSpells.add(this);
    }

    private void getSpellSprites() {
        for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
            spellSprites[i] = BasicSpellsSpriteSheet.getSubimage(xSpriteStartPos, 18, 16, 16);
            xSpriteStartPos += 16;
        }
    }

    private void getVector() {
        CurrentMousePosition.getX();
        CurrentMousePosition.getY();
        float vectorX = (float) (CurrentMousePosition.getX() - (PlayerClass.playerPosX + 72));
        float vectorY = (float) (CurrentMousePosition.getY() - (PlayerClass.playerPosY + 72));
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

    private void moveSpell() {
        spellStartingPosX += (normalizedVectorX * SPEED);
        spellStartingPosY += (normalizedVectorY * SPEED);
    }

    public static void updateFirstSpells() {
        ListOfActiveFirstSpells.forEach(firstSpell -> {
            firstSpell.animationController();
            firstSpell.moveSpell();
        });
    }
}
