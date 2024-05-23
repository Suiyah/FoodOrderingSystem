import java.util.Scanner;

public class FoodOrderingSystem
{
    static Scanner scanner = new Scanner(System.in);
    static User[] users = {
            new User("john", "j0n", "customer"),
            new User("gary", "g4ry", "customer"),
            new User("peter", "p33t", "customer"),
            new User("chris", "kris", "courier"),
            new User("charls", "cherry", "courier"),
            new User("cory", "cord", "admin"),
            new User("kim", "mike", "admin")
    };

    static String[] foodItems = {"Pizza", "Burger", "Hotdog"};
    static int[] foodPrices = {40, 25, 10};
    static int[] foodStock = {15, 15, 15};
    static Order[] orders = new Order[100];
    static int orderCount = 0;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter Username: ");
            String username = scanner.next();
            System.out.println("Enter Password: ");
            String password = scanner.next();

            User user = authenticate(username, password);
            if (user != null) {
                switch (user.getRole()) {
                    case "customer":
                        handleCustomer(user);
                        break;
                    case "courier":
                        handleCourier();
                        break;
                    case "admin":
                        handleAdmin();
                        break;
                }
            } else {
                System.out.println("Invalid login, please try again.");
            }
        }
    }

    public static User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static void handleCustomer(User user) {
        String customerName = user.getUsername();
        String[] orderedItems = new String[foodItems.length];
        int[] quantities = new int[foodItems.length];
        int totalCost = 0;
        int itemCount = 0;

        while (true) {
            System.out.println("Menu:");
            for (int i = 0; i < foodItems.length; i++) {
                if (foodStock[i] > 0) {
                    System.out.println((i + 1) + ". " + foodItems[i] + " = $" + foodPrices[i] + " (" + foodStock[i] + " available)");
                }
            }

            System.out.println("Enter the number of the food item you want to order:");
            int choice = scanner.nextInt() - 1;

            if (choice < 0 || choice >= foodItems.length || foodStock[choice] <= 0) {
                System.out.println("Invalid choice or item out of stock.");
                continue;
            }

            System.out.println("How many?");
            int quantity = scanner.nextInt();

            if (quantity > foodStock[choice]) {
                System.out.println("Oh sorry, we're out of " + foodItems[choice] + ".");
                continue;
            }

            foodStock[choice] -= quantity;
            orderedItems[itemCount] = foodItems[choice];
            quantities[itemCount] = quantity;
            totalCost += quantity * foodPrices[choice];
            itemCount++;

            System.out.println("Anything else? (Y/N)");
            String more = scanner.next();
            if (more.equalsIgnoreCase("N")) {
                break;
            }
        }

        if (itemCount == 0) {
            System.out.println("No items ordered.");
            return;
        }

        System.out.println("Total Cost: $" + totalCost);
        System.out.println("Enter your address:");
        scanner.nextLine(); // consume the leftover newline
        String address = scanner.nextLine();

        orders[orderCount++] = new Order(customerName, orderedItems, quantities, totalCost, address);
        System.out.println("Thank you for ordering! Your food will arrive soon.");
    }

    public static void handleCourier() {
        System.out.println("Orders:");
        for (int i = 0; i < orderCount; i++) {
            System.out.println((i + 1) + ". " + orders[i]);
        }
        System.out.println("Enter Y to leave to login page");
        String leave = scanner.next();
    }

    public static void handleAdmin() {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Show information of orders placed by the customers");
            System.out.println("2. Show food management");
            System.out.println("3. Exit to login page");

            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Orders:");
                for (int i = 0; i < orderCount; i++) {
                    System.out.println((i + 1) + ". " + orders[i]);
                }
                System.out.println("1. Exit to leave the order's page");
                System.out.println("2. Exit to login page");
                int exitChoice = scanner.nextInt();
                if (exitChoice == 2) {
                    break;
                }
            } else if (choice == 2) {
                while (true) {
                    System.out.println("Food Management:");
                    for (int i = 0; i < foodItems.length; i++) {
                        System.out.println((i + 1) + ". " + foodItems[i] + " = " + foodStock[i]);
                    }
                    System.out.println("Enter the index of the food item to increase stock followed by the quantity (e.g., '1 5' to add 5 pizzas):");
                    int foodIndex = scanner.nextInt() - 1;
                    int quantity = scanner.nextInt();

                    if (foodIndex >= 0 && foodIndex < foodItems.length) {
                        foodStock[foodIndex] += quantity;
                    }

                    System.out.println("Enter Y to leave to Admin page");
                    String leave = scanner.next();
                    if (leave.equalsIgnoreCase("Y")) {
                        break;
                    }
                }
            } else if (choice == 3) {
                break;
            }
        }
    }
}