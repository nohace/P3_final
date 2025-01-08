public class SpellCard extends Card {
    private String effect;

    public SpellCard(String name, int manaCost, String rarity, String set, String effect) {
        super(name, manaCost, rarity, set);
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }

    @Override
    public String getType() {
        return "Spell";
    }

    @Override
    public void displayCard() {
        System.out.println("Name: " + getName());
        System.out.println("Mana Cost: " + getManaCost());
        System.out.println("Rarity: " + getRarity());
        System.out.println("Set: " + getSet());
        System.out.println("Effect: " + effect);
        System.out.println("Favorite: " + (isFavorite() ? "Yes" : "No"));
        System.out.println("---------------------------");
    }
}
