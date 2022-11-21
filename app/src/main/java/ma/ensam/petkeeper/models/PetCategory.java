package ma.ensam.petkeeper.models;

public class PetCategory {
    private String name;
    private String img;
    private boolean isActive;

    public PetCategory(String name, String img, boolean isActive) {
        this.name = name;
        this.img = img;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
