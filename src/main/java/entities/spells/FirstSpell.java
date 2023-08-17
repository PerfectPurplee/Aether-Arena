package entities.spells;

public class FirstSpell extends BasicSpell {

 private final int NUMBER_OF_SPRITES = 4;

    public FirstSpell(int xSpriteStartPos, int ySpriteStartPos, int spriteSize) {
        super(xSpriteStartPos, ySpriteStartPos, spriteSize);
        super.getSpellSprites(NUMBER_OF_SPRITES);


    }
}
