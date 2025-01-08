public class LandCard extends Card {
    private boolean entersTapped;
    private int manaProduced;

    public LandCard(String name, String rarity, String set, boolean entersTapped, int manaProduced) {
        super(name, 0, rarity, set); // Mana cost is always 0 for Land cards
        this.entersTapped = entersTapped;
        this.manaProduced = manaProduced;
    }

    public boolean doesEnterTapped() {
        return entersTapped;
    }

    public int getManaProduced() {
        return manaProduced;
    }

    @Override
    public String getType() {
        return "Land";
    }

    @Override
    public void displayCard() {
        System.out.println("Name: " + getName());
        System.out.println("Mana Cost: " + getManaCost());
        System.out.println("Rarity: " + getRarity());
        System.out.println("Set: " + getSet());
        System.out.println("Enters Tapped: " + entersTapped);
        System.out.println("Mana Produced: " + manaProduced);
        System.out.println("Favorite: " + (isFavorite() ? "Yes" : "No"));
        System.out.println("---------------------------");
    }
}
