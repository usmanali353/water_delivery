package fyp.water_delivery.Model;

import java.util.ArrayList;

public class Orders {
    String customerName;
    String address;
    String email;
    String phone;
    String assignTo;

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    int totalAmount;

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;
    double deliveryLatitude,deliveryLongitude;
    ArrayList<cartItems> orderedItems;

    public Orders() {

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(double deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public double getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(double deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public ArrayList<cartItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(ArrayList<cartItems> orderedItems) {
        this.orderedItems = orderedItems;
    }
    public Orders(String customerName, String address, String email, String phone, double deliveryLatitude, double deliveryLongitude, ArrayList<cartItems> orderedItems,String status,String userId,String date,int totalAmount) {
        this.customerName = customerName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryLongitude = deliveryLongitude;
        this.orderedItems = orderedItems;
        this.Status=status;
        this.userId=userId;
        this.date=date;
        this.totalAmount=totalAmount;
    }

}
