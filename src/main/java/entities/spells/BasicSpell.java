package entities.spells;

import entities.playercharacters.PlayerClass;
import inputs.PlayerMouseInputs;
import main.GameEngine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static inputs.PlayerMouseInputs.*;

public abstract class BasicSpell {

    public BufferedImage spellSprites;
    public final BufferedImage BasicSpellsSpriteSheet = GameEngine.BasicSpellsSpriteSheet;
    public int xSpriteStartPos, ySpriteStartPos, spriteSize;
    int animationTick, animationSpeed = 60, animationIndex;
    public float normalizedVectorX;
    public float normalizedVectorY;


    public static List<BasicSpell> AllActiveSpells = new ArrayList<>();

    public BasicSpell() {
        getVector();
        getSpellSprites(3);

    }

    public void getSpellSprites(int numberOfSpritesForAnimation) {

//        for (int i = 0; i < numberOfSpritesForAnimation; i++) {
            spellSprites = BasicSpellsSpriteSheet.getSubimage(0, 0, 16, 16);

//        }
    }

    public void animationController(int numberOfSpritesForAnimation) {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (animationIndex < numberOfSpritesForAnimation)
                animationIndex++;
            else animationIndex = 0;
            animationTick = 0;
        }
    }

    public void update(int speed) {
//        AllActiveSpells.forEach(spell -> spell);
    }

    public void getVector() {
        CurrentMousePosition.getX();
        CurrentMousePosition.getY();
        float vectorX = (float) (CurrentMousePosition.getX() - PlayerClass.playerPosX);
        float vectorY = (float) (CurrentMousePosition.getY() - PlayerClass.playerPosY);
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }
}
