import java.util.Scanner;

public class ShoppingCart {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // --- LOGIN ---
        String correctUsername = "admin";
        String correctPassword = "1234";

        System.out.print("Enter Username: ");
        String username = sc.next();

        System.out.print("Enter Password: ");
        String password = sc.next();

        if (username.equals(correctUsername) && password.equals(correctPassword)) {
            System.out.println("Login Successful!\n");
        } else {
            System.out.println("Invalid Credentials");
            return; 
        }

        //  SHOPPING CART 
        System.out.print("Enter Product Name: ");
        String productName = sc.next();

        System.out.print("Enter Quantity: ");
        int quantity = sc.nextInt();

        System.out.print("Enter Price per Item: ");
        double price = sc.nextDouble();

        double totalAmount = quantity * price;
        double discount = 0;

        if (totalAmount > 5000) {
            discount = totalAmount * 0.10;
        }

        double finalAmount = totalAmount - discount;

        //  DISPLAY BILL 
        System.out.println("\n=== Bill Summary ===");
        System.out.println("Product Name  : " + productName);
        System.out.println("Total Amount  : " + totalAmount);
        System.out.println("Discount      : " + discount);
        System.out.println("Final Amount  : " + finalAmount);
    }
}