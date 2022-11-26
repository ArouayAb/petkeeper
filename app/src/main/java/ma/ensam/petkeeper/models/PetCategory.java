package ma.ensam.petkeeper.models;

public class PetCategory {
    private String name;
    private String img;

    public PetCategory(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public boolean isActive() {
        return this.img.contains("_active");
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
