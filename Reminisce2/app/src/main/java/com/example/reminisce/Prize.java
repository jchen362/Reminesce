package com.example.reminisce;

public class Prize {
    private String title;
    private String image;
    private String description;
    private String rarity;

    public Prize(String image, String title, String description, String rarity) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.rarity = rarity;
    }

    public String getTitle() {
        return title;
    }
    public String getImage() { return image; }
    public String getDescription() {
        return description;
    }
    public String getRarity() {return rarity;}
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setImage(String image) {this.image = image; }
    public void setRarity(String rarity) {this.rarity = rarity;}

}
