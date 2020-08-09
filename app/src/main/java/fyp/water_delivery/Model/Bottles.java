package fyp.water_delivery.Model;

import com.google.firebase.firestore.Blob;

public class Bottles {
    String Name;
    int Price;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    int quantity;
    String weight;
    Blob Image;
   public Bottles(){

   }
    public Bottles(String name, int price, String weight, Blob image,int quantity) {
        Name = name;
        Price = price;
        this.weight = weight;
        Image = image;
        this.quantity=quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Blob getImage() {
        return Image;
    }

    public void setImage(Blob image) {
        Image = image;
    }
}
