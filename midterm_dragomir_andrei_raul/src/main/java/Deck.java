import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cardsInDeck = new ArrayList<>();
    private final int MAX_CARDS = 60;

    public void addCardToDeck(Card card) throws DeckFullException {
        if (cardsInDeck.size() >= MAX_CARDS) throw new DeckFullException("Deck is full!");
        cardsInDeck.add(card);
    }

    public List<Card> getCardsInDeck() {
        return new ArrayList<>(cardsInDeck);
    }
}
