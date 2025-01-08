public class CreatureCard extends Card {
    private int power;
    private int toughness;

    public CreatureCard(String name, int manaCost, String rarity, String set, int power, int toughness) {
        super(name, manaCost, rarity, set);
        this.power = power;
        this.toughness = toughness;
    }

    public int getPower() {
        return power;
    }

    public int getToughness() {
        return toughness;
    }

    @Override
    public String getType() {
        return "Creature";
    }

    @Override
    public void displayCard() {
        System.out.println("Name: " + getName());
        System.out.println("Mana Cost: " + getManaCost());
        System.out.println("Rarity: " + getRarity());
        System.out.println("Set: " + getSet());
        System.out.println("Power: " + power);
        System.out.println("Toughness: " + toughness);
        System.out.println("Favorite: " + (isFavorite() ? "Yes" : "No"));
        System.out.println("---------------------------");
    }
}
