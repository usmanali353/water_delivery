package fyp.water_delivery.Model;

public class Driver {
    public Driver(String name, String email, String password, String phone, String CNIC) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        this.CNIC = CNIC;
    }
   public Driver(){

   }
    String Name;
    String Email;
    String Password;
    String Phone;
    String CNIC;

    public double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(double driverLat) {
        this.driverLat = driverLat;
    }

    public double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(double driverLng) {
        this.driverLng = driverLng;
    }

    double driverLat;
    double driverLng;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }
}
