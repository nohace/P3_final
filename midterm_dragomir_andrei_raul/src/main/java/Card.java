public abstract class Card implements Favoritable {
    private String name;
    private int manaCost;
    private String rarity;
    private String set;
    private boolean favorite; // New field

    public Card(String name, int manaCost, String rarity, String set) {
        this.name = name;
        this.manaCost = manaCost;
        this.rarity = rarity;
        this.set = set;
        this.favorite = false; // Default to not favorite
    }

    // Getter and Setter for favorite
    @Override
    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getRarity() {
        return rarity;
    }

    public String getSet() {
        return set;
    }

    public abstract String getType();

    public abstract void displayCard();
}
