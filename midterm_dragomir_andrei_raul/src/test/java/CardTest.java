import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void testCreatureCardConstructor() {
        CreatureCard creatureCard = new CreatureCard("Elf Warrior", 2, "Common", "ELD", 3, 3);
        assertEquals("Elf Warrior", creatureCard.getName());
        assertEquals(2, creatureCard.getManaCost());
        assertEquals("Common", creatureCard.getRarity());
        assertEquals("ELD", creatureCard.getSet());
        assertEquals(3, creatureCard.getPower());
        assertEquals(3, creatureCard.getToughness());
    }

    @Test
    public void testSpellCardConstructor() {
        SpellCard spellCard = new SpellCard("Lightning Bolt", 1, "Uncommon", "M21", "Deal 3 damage");
        assertEquals("Lightning Bolt", spellCard.getName());
        assertEquals(1, spellCard.getManaCost());
        assertEquals("Uncommon", spellCard.getRarity());
        assertEquals("M21", spellCard.getSet());
        assertEquals("Deal 3 damage", spellCard.getEffect());
    }

    @Test
    public void testLandCardConstructor() {
        LandCard landCard = new LandCard("Forest", "Common", "ELD", true, 2);
        assertEquals("Forest", landCard.getName());
        assertEquals("Common", landCard.getRarity());
        assertEquals("ELD", landCard.getSet());
        assertTrue(landCard.doesEnterTapped());
        assertEquals(2, landCard.getManaProduced());
    }
}
