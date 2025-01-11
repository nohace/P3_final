import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class CardCollectionTest {

    @Test
    public void testAddCard() {
        CardCollection collection = new CardCollection();
        CreatureCard creatureCard = new CreatureCard("Elf Warrior", 2, "Common", "ELD", 3, 3);

        collection.addCard(creatureCard);
        assertEquals(1, collection.getAllCards().size());
        assertEquals("Elf Warrior", collection.getAllCards().get(0).getName());
    }

    @Test
    public void testRemoveCard() throws InterruptedException {
        CardCollection collection = new CardCollection();
        SpellCard spellCard1 = new SpellCard("Lightning Bolt", 1, "Uncommon", "M21", "Deal 3 damage");
        SpellCard spellCard2 = new SpellCard("Fireball", 2, "Common", "M21", "Deal X damage");

        collection.addCard(spellCard1);
        collection.addCard(spellCard2);

        assertEquals(2, collection.getAllCards().size());

        // threads to remove cards concurrently
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            Card removedCard = collection.removeCard("Lightning Bolt");
            assertNotNull(removedCard); // Ensures the card is removed
            assertEquals("Lightning Bolt", removedCard.getName());
        });

        executor.submit(() -> {
            Card removedCard = collection.removeCard("Fireball");
            assertNotNull(removedCard); // Ensures the card is removed
            assertEquals("Fireball", removedCard.getName());
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(0, collection.getAllCards().size());
    }

    @Test
    public void testFilterCardsByTypeWithThreads() throws InterruptedException {
        CardCollection collection = new CardCollection();
        collection.addCard(new CreatureCard("Elf Warrior", 2, "Common", "ELD", 3, 3));
        collection.addCard(new SpellCard("Lightning Bolt", 1, "Uncommon", "M21", "Deal 3 damage"));
        collection.addCard(new LandCard("Forest", "Common", "ELD", true, 2));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Thread 1: Filter for Creature cards
        executor.submit(() -> {
            long creatureCount = collection.getAllCards().stream()
                    .filter(card -> card.getType().equalsIgnoreCase("Creature"))
                    .count();
            assertEquals(1, creatureCount);
        });

        // Thread 2: Filter for Land cards
        executor.submit(() -> {
            long landCount = collection.getAllCards().stream()
                    .filter(card -> card.getType().equalsIgnoreCase("Land"))
                    .count();
            assertEquals(1, landCount);
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
