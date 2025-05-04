package Assessment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class SalesManager extends BaseMenu {

    //Override
    @Override
    public void displayMenu() {
        System.out.println(" ");
        System.out.println("Welcome back, Sales Manager");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("Choose what you want to do");
        System.out.println("--------------------------------");
        System.out.println("1. Item Entry");
        System.out.println("2. Supplier Entry");
        System.out.println("3. Daily Item-wise Sales Entry");
        System.out.println("4. Purchase Requisition");
        System.out.println("5. View Purchaser Order");
        System.out.println("-1. Exit");
        System.out.println("--------------------------------");
        System.out.println("Enter your choice");
    }

    //Override
    public void processChoice() {
        int choice;
        do {
            displayMenu();
            choice = readChoice();

            if (choice == -1) {
                System.out.println("Exiting...");
                return;
            } else if (choice == 0) {
                System.out.println("Invalid Input. Please try again.");
            } else {

                switch (choice) {
                    case 1:
                        itemMenu ItemMenu = new itemMenu();
                        ItemMenu.processChoice();
                        break;
                    case 2:
                        supplierMenu SupplierMenu = new supplierMenu();
                        SupplierMenu.processChoice();
                        break;
                    case 3:
                        salesMenu SalesMenu = new salesMenu();
                        SalesMenu.processChoice();

                        break;
                    case 4:
                        prMenu PrMenu = new prMenu();
                        PrMenu.processChoice();
                        break;
                    case 5:
                        PurchaseManager pm=new PurchaseManager();
                       PurchaseManager.PurchaseOrder po=pm.new PurchaseOrder();
                       po.view();
                        break;
                    case -1:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;

                }
            }
        } while (true);
    }

    class supplierMenu extends BaseMenu implements menu, addfunction, deletefunction, editfunction {

        public void displayMenu() {
            System.out.println(" ");
            System.out.println("Welcome to Supplier Entry");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Choose what you want to do");
            System.out.println("--------------------------------");
            System.out.println("1. Add Supplier");
            System.out.println("2. Delete Supplier");
            System.out.println("3. Edit Supplier");
            System.out.println("-1. Exit");
            System.out.println("--------------------------------");
            System.out.println("Select an option");
        }

        //Override
        public void processChoice() {
            int choice;
            do {
                displayMenu();
                choice = readChoice();

                switch (choice) {
                    case 1:
                        add();
                        break;
                    case 2:
                        delete();
                        break;
                    case 3:
                        edit();
                        break;
                    case -1:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (true);
        }

        public void add() {
            Set<String> existingIds = loadExistingIds(); //load existing supplier IDs from the file 

            String supplierId;
            do {
                supplierId = readInput("Enter Supplier ID");
                if (supplierId.isBlank()) {
                    System.out.println("Supplier ID cannot be blank.");
                } else if (existingIds.contains(supplierId)) {
                    System.out.println("Supplier ID already exist. Please enter a new ID.");
                }
            } while (supplierId.isBlank() || existingIds.contains(supplierId));
            existingIds.add(supplierId);

            //validate and add new ID, Name and Item ID
            String supplierName;
            do {
                supplierName = readInput("Enter Supplier Name");
                if (supplierName.isBlank()) {
                    System.out.println("Invalid Supplier Name");
                }
            } while (supplierName.isBlank());

            Set<String> itemIds = new HashSet<>();
            boolean addMoreItems; //prompt user to add item IDs for the supplier
            do {
                String itemId;
                do {
                    itemId = readInput("Enter Item ID");
                    if (itemId.isBlank()) {
                        System.out.println("Item ID cannot be blank.");
                    } else if (itemIds.contains(itemId)) {
                        System.out.println("Item ID already exists. Please enter a new item ID");
                    }
                } while (itemId.isBlank() || itemIds.contains(itemId));
                itemIds.add(itemId);

                String moreItems = readInput("Add another item for this supplier? (yes/no)").toLowerCase();
                addMoreItems = moreItems.equals("yes");
            } while (addMoreItems);

            Supplier s1 = new Supplier(supplierId, supplierName, itemIds);
            s1.wri2file();

            System.out.println("Supplier added successfully!");
        }

        private static Set<String> loadExistingIds() {
            Set<String> existingIds = new HashSet<>();
            try {
                File file = new File("supplier.txt");
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 1) {
                            existingIds.add(parts[0]);
                        }
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return existingIds;
        }

        @Override
        public void delete() {
            SupplierView supplierView = new SupplierView();
            supplierView.view();

            Scanner sc = new Scanner(System.in);

            boolean found = false;
            do {
                System.out.println("Enter Supplier ID to delete: ");
                String supplierIdToDelete = sc.nextLine();

                //boolean found = false;
                if (supplierIdToDelete.isBlank()) {
                    System.out.println("Cannot be blank.");
                    continue;
                }

                List<String> updatedSupplierList = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith(supplierIdToDelete + " ")) {
                            updatedSupplierList.add(line);
                        } else {
                            found = true; //Supplier code found in the file 
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (!found) {
                    System.out.println("Supplier ID not found. Please try again.");
                    //return;
                } else {

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier.txt"))) {
                        for (String line : updatedSupplierList) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    System.out.println("Supplier deleted successfully!");
                }
            } while (!found);
        }

        //edit 
        public void edit() {
            SupplierView supplierView = new SupplierView();
            supplierView.view();

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Supplier ID to edit: ");
            String supplierIdToEdit = sc.nextLine();

            boolean supplierFound = false;

            List<String> IList = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(supplierIdToEdit + " ")) {
                        supplierFound = true;
                        System.out.println("Enter new Supplier Name: ");
                        String newName = sc.nextLine();

                        //prompt for supplier name again if it's blank
                        while (newName.isBlank()) {
                            System.out.println("Supplier Name cannot be blank. Please enter a valid name: ");
                            newName = sc.nextLine();
                        }

                        Set<String> newItemIds = new HashSet<>();
                        boolean addMoreItems;
                        do {
                            System.out.println("Enter new Item ID: ");
                            String newItemId = sc.nextLine();
                            if (newItemId.isBlank()) {
                                break;
                            }
                            newItemIds.add(newItemId);

                            //prompt for item id again if it's blank
                            while (newItemId.isBlank()) {
                                System.out.println("Item ID cannot be blank. Please enter a valid ID: ");
                                newItemId = sc.nextLine();
                            }

                            System.out.println("Add another new item for this supplier? (yes/no)");
                            String moreItems = sc.nextLine().toLowerCase();
                            addMoreItems = moreItems.equals("yes");
                        } while (addMoreItems);

                        String itemIdsStr = String.join(" ", newItemIds);

                        line = supplierIdToEdit + " " + newName + " " + itemIdsStr;
                    }
                    IList.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (!supplierFound) {
                System.out.println("Supplier code not found. Please try again.");
                edit();
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier.txt"))) {
                for (String line : IList) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Supplier information edited successfully!");
        }

    }

    class itemMenu extends BaseMenu implements menu, addfunction, deletefunction, editfunction {

        private List<Item> items;

        public void displayMenu() {
            System.out.println(" ");
            System.out.println("Welcome to Item Entry");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Choose what you want to do");
            System.out.println("--------------------------------");
            System.out.println("1. Add Item");
            System.out.println("2. Delete Item");
            System.out.println("3. Edit Item");
            System.out.println("-1. Exit");
            System.out.println("--------------------------------");
            System.out.println("Select an option");
        }

        public void processChoice() {
            int choice;
            do {
                displayMenu();
                choice = readChoice();

                switch (choice) {
                    case 1:
                        add();
                        break;
                    case 2:
                        delete();
                        break;
                    case 3:
                        edit();
                        break;
                    case -1:
                        System.out.println("Existing...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (true);
        }

        @Override
        public void add() {
            Set<String> existingItemIds = loadExistingItemIds();
            Map<String, Set<String>> supplierData = loadSupplierData();

            Scanner sc = new Scanner(System.in);

            String itemId;
            do {
                System.out.println("Enter Item ID");
                itemId = sc.nextLine().trim();
                if (itemId.isBlank()) {
                    System.out.println("Item ID cannot be blank");
                } else if (existingItemIds.contains(itemId)) {
                    System.out.println("Item ID already exists. Please enter a new ID");
                }
            } while (itemId.isBlank() || existingItemIds.contains(itemId));
            existingItemIds.add(itemId);

            String itemName;
            do {
                System.out.println("Enter Item Name: ");
                itemName = sc.nextLine().trim();
                if (itemName.isBlank()) {
                    System.out.println("Item Name cannot be blank.");
                }
            } while (itemName.isBlank());

            int quantity;
            do {
                System.out.println("Enter quantity: ");
                try {
                    quantity = Integer.parseInt(sc.nextLine().trim());
                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity. Please enter a valid quantity.");
                    quantity = -1;
                }
            } while (quantity <= 0);

            double price;
            do {
                System.out.println("Enter price: ");
                try {
                    price = Double.parseDouble(sc.nextLine().trim());
                    if (price <= 0) {
                        System.out.println("Price must be greater than 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price. Please enter a valid number.");
                    price = -1;
                }
            } while (price <= 0);

            Set<String> supplierIds = getSupplierIdsForItem(itemId, supplierData);

            Item i1 = new Item(itemId, itemName, price, quantity, supplierIds);
            i1.wri2file();

            System.out.println("Item added successfully!");
            System.out.println("Supplier IDs for item " + itemId + ": " + supplierIds);
        }

        private static Set<String> loadExistingItemIds() {
            Set<String> existingAItemIds = new HashSet<>();
            try {
                File file = new File("item.txt");
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 1) {
                            existingAItemIds.add(parts[0]);
                        }
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return existingAItemIds;
        }

        private static Set<String> autoMatchSupplierIds(String itemId, Map<String, Set<String>> supplierData) {
            return supplierData.getOrDefault(itemId, new HashSet<>());
        }

        private static Map<String, Set<String>> loadSupplierData() {
            Map<String, Set<String>> supplierMap = new HashMap<>();

            try {
                File file = new File("supplier.txt");
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 2) {
                            String supplierId = parts[0];
                            for (int i = 1; i < parts.length; i++) {
                                String itemId = parts[i];
                                supplierMap.computeIfAbsent(itemId, k -> new HashSet<>()).add(supplierId);
                            }
                        }
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return supplierMap;
        }

        private static Set<String> getSupplierIdsForItem(String itemId, Map<String, Set<String>> supplierData) {
            return supplierData.getOrDefault(itemId, new HashSet<>());
        }

        public void delete() {
            ItemView itemView = new ItemView();
            itemView.view();

            Scanner sc = new Scanner(System.in);

            boolean found = false;
            do {
                System.out.println("Enter Item ID to delete:");
                String itemIdToDelete = sc.nextLine();

                if (itemIdToDelete.isBlank()) {
                    System.out.println("Cannot be blank.");
                    continue;
                }

                List<String> updatedItemList = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith(itemIdToDelete + " ")) {
                            updatedItemList.add(line);
                        } else {
                            found = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (!found) {
                    System.out.println("Item ID not found. Please try again.");
                } else {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("item.txt"))) {
                        for (String line : updatedItemList) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("Item deleted successfully!");
                }
            } while (!found);
        }

        public void edit() {
            ItemView itemView = new ItemView();
            itemView.view();

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Item ID to edit: ");
            String itemIdToEdit = sc.nextLine();

            boolean itemFound = false;
            List<String> itemList = new ArrayList<>();

            Map<String, Set<String>> supplierData = loadSupplierData1();

            try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(itemIdToEdit + " ")) {
                        itemFound = true;

                        String[] parts = line.split(" ");
                        String itemName = parts[1]; //get the existing item name 

                        String newName;
                        do {
                            System.out.println("Enter new Item Name: ");
                            newName = sc.nextLine().trim();
                            if (newName.isBlank()) {
                                System.out.println("Item Name cannot be blank.");
                            }
                        } while (newName.isBlank());

                        int newQuantity = -1;
                        do {
                            System.out.println("Enter new quantity: ");
                            try {
                                newQuantity = Integer.parseInt(sc.nextLine().trim());
                                if (newQuantity <= 0) {
                                    System.out.println("Quantity must be greater than 0.");
                                    newQuantity = -1;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid quantity. Please enter a valid quantity.");
                            }
                        } while (newQuantity <= 0);

                        double newPrice = -1;
                        do {
                            System.out.println("Enter new price: ");
                            try {
                                newPrice = Double.parseDouble(sc.nextLine().trim());
                                if (newPrice <= 0) {
                                    System.out.println("Price must be greater than 0.");
                                    newPrice = -1;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid price. Please try again.");
                            }
                        } while (newPrice <= 0);

                        Set<String> newSupplierIds = autoMatchSupplierIds(itemIdToEdit, supplierData);
                        String newSupplierIdsStr = "[" + String.join(" ", newSupplierIds) + "]";

                        line = itemIdToEdit + " " + newName + " " + newQuantity + " " + newPrice + " " + newSupplierIdsStr;
                    }
                    itemList.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (!itemFound) {
                System.out.println("Item ID not found. Please try again.");
                edit();
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("item.txt"))) {
                for (String line : itemList) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Item information edited successfully!");
        }

        private static Map<String, Set<String>> loadSupplierData1() {
            Map<String, Set<String>> supplierMap = new HashMap<>();

            try (BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 2) {
                        String supplierId = parts[0];
                        for (int i = 1; i < parts.length; i++) {
                            String itemId = parts[i];
                            supplierMap.computeIfAbsent(itemId, k -> new HashSet<>()).add(supplierId);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return supplierMap;
        }
    }

    class salesMenu extends BaseMenu implements menu, addfunction, deletefunction, editfunction {

        private List<Item> items = new ArrayList<>();

        //Override
        public void displayMenu() {
            System.out.println(" ");
            System.out.println("Welcome to Daily Item Sales Entry");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Choose what you want to do");
            System.out.println("--------------------------------");
            System.out.println("1. Add Sales");
            System.out.println("2. Delete Sales");
            System.out.println("3. Edit Sales");
            System.out.println("-1. Exit");
            System.out.println("--------------------------------");
            System.out.println("Select an option");
        }

        //Override
        public void processChoice() {
            int choice;
            do {
                displayMenu();
                choice = readChoice();

                switch (choice) {
                    case 1:
                        add();
                        break;
                    case 2:
                        delete();
                        break;
                    case 3:
                        edit();
                        break;
                    case -1:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (true);
        }

        public void add() {
            File itemFile = new File("item.txt");

            try {
                readItemsFromFile(itemFile);

                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter -1 at any time to return to the main menu.");

                while (true) {
                    // Display existing Item IDs for review
                    displayItemIdList();
                    System.out.print("Enter item code: ");
                    String itemCode = scanner.nextLine();

                    if (itemCode.equals("-1")) {
                        System.out.println("Returning to the main menu...");
                        break;
                    }

                    // Generate a unique sales ID
                    int SalesId = generateSalesId();

                    boolean itemFound = processSalesAndUpdateQuantity(itemCode, SalesId, scanner);

                    if (!itemFound) {
                        System.out.println("Item code not found. Please enter a valid item code.");
                    }
                }

            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
            }
        }

        private int generateSalesId() {

            int randomNum = new Random().nextInt(10000);
            return randomNum;
        }

        private void readItemsFromFile(File file) throws IOException {
            items.clear();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 5) {
                        String[] supplierIdArray = parts[4].split(" ");
                        Set<String> supplierIds = new HashSet<>(Arrays.asList(supplierIdArray));
                        int quantity = (int) Double.parseDouble(parts[2]); // Parse quantity as double and then cast it to int
                        double itemPrice = Double.parseDouble(parts[3]);
                        Item existingItem = new Item(parts[0], parts[1], itemPrice, quantity, supplierIds);
                        items.add(existingItem);
                    } else {
                        System.out.println("Cannot read item from line: " + line);
                    }
                }
            }
        }

        private boolean processSalesAndUpdateQuantity(String itemCode, int SalesId, Scanner scanner) {
            for (Item item : items) {
                if (item.itemId.equals(itemCode)) {
                    System.out.println("Item Name: " + item.itemName);
                    System.out.println("Supplier ID: " + String.join(", ", item.supplierIds));

                    while (true) {
                        System.out.print("Enter quantity sold: ");
                        if (scanner.hasNextInt()) {
                            int quantitySold = scanner.nextInt();
                            scanner.nextLine();  // Consume the newline character

                            if (quantitySold == -1) {
                                System.out.println("Returning to the menu.");
                                return false;

                            } else if (quantitySold < 0) {
                                System.out.println("Invalid quantity. Please enter a valid quantity.");
                            } else {
                                int currentQuantity = item.quantity;
                                int updatedQuantity = currentQuantity - quantitySold;
                                if (updatedQuantity >= 0) {
                                    item.quantity = updatedQuantity;
                                    double itemPrice = item.itemPrice;
                                    double totalSales = quantitySold * itemPrice;
                                    System.out.println("Total Sales: " + totalSales);
                                    System.out.println("Successfully added!");

                                    // Update items and write sales only if quantitySold > 0
                                    if (quantitySold > 0) {
                                        try {
                                            writeItemsToFile(new File("item.txt"));
                                            writeSaleToFile(new File("sales.txt"), SalesId, itemCode, quantitySold, item.itemPrice);
                                        } catch (IOException e) {
                                            System.out.println("Error writing to files: " + e.getMessage());
                                        }
                                    }

                                    return true;
                                } else {
                                    System.out.println("Insufficient stock! Please enter a valid quantity.");
                                }
                            }
                        } else {
                            String invalidInput = scanner.next();
                            System.out.println("Invalid input: " + invalidInput + ". Please enter a valid quantity.");
                        }
                    }
                }
            }
            return false;
        }

        private void writeItemsToFile(File file) throws IOException {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (Item updatedItem : items) {
                    String supplierIds = String.join(" ", updatedItem.supplierIds);  // Convert set to string
                    bw.write(updatedItem.itemId + " "
                            + updatedItem.itemName + " "
                            + updatedItem.quantity + " "
                            + updatedItem.itemPrice + " "
                            + supplierIds);  // Write the string representation of supplierIds
                    bw.newLine();
                }
            }
        }

        private void writeSaleToFile(File file, int SalesId, String itemCode, int quantitySold, double itemPrice) throws IOException {
            if (quantitySold > 0) {  // Only write if quantitySold > 0
                try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
                    double totalSales = itemPrice * quantitySold;
                    pw.printf("%d %s %d %.2f%n", SalesId, itemCode, quantitySold, totalSales);
                }
            }
        }

        public int getUserInputInt(String prompt) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(prompt);
            return scanner.nextInt();
        }

        public void displayItemIdList() {
            System.out.println("Existing Item IDs and Names:");
            for (Item item : items) {
                System.out.println(item.itemId + " " + item.itemName);
            }
        }

        private static Set<String> loadExistingIds() {
            Set<String> existingIds = new HashSet<>();
            try {
                File file = new File("supplier.txt");
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 1) {
                            existingIds.add(parts[0]);
                        }
                    }
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return existingIds;
        }

        public void delete() {
            Scanner sc = new Scanner(System.in);

            while (true) {

                displaySalesIdList();

                System.out.println("Enter Sales ID to delete (or enter -1 to exit): ");
                int salesIdToDelete = sc.nextInt();
                sc.nextLine();

                if (salesIdToDelete == -1) {
                    System.out.println("Returning to the main menu...");
                    break;
                }

                boolean deleted = processDeletion(salesIdToDelete, items);

                if (deleted) {
                    System.out.println("Sales record deleted successfully!");
                    delete();
                } else {
                    System.out.println("Sales ID not found. Please try again.");
                    delete();
                }
            }
        }

        private boolean processDeletion(int salesIdToDelete, List<Item> items) {
            List<String> SList = new ArrayList<>();
            boolean found = false;

            int deletedQuantity = 0; // Initialize deleted quantity
            String itemCode = null; // Initialize itemCode
            try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    int currentSalesId = Integer.parseInt(parts[0]);

                    if (currentSalesId == salesIdToDelete) {
                        itemCode = parts[1]; // Extract itemCode from the line
                        deletedQuantity = Integer.parseInt(parts[2]);
                        found = true; // Mark the record as found
                    } else {
                        SList.add(line); // Add other lines to SList
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (found) {
                addItemQuantity(itemCode, deletedQuantity, items); // Add back quantity to item.txt
                updateSalesFile(SList); // Update sales.txt
            }

            return found;
        }

        private void addItemQuantity(String itemCode, int quantityToAdd, List<Item> items) {
            for (Item item : items) {
                if (item.itemId.equals(itemCode)) {
                    item.quantity += quantityToAdd;
                    break;
                }
            }

            // Create a temporary list to hold the updated lines
            List<String> updatedLines = new ArrayList<>();

            // Update item.txt with new quantities
            try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    String existingItemCode = parts[0];

                    if (existingItemCode.equals(itemCode)) {
                        int existingQuantity = Integer.parseInt(parts[2]);
                        existingQuantity += quantityToAdd;
                        line = parts[0] + " " + parts[1] + " " + existingQuantity + " " + parts[3] + " " + parts[4];
                    }

                    updatedLines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Write the updated lines back to item.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("item.txt"))) {
                for (String updatedLine : updatedLines) {
                    bw.write(updatedLine);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void updateSalesFile(List<String> SList) {
            // Update sales.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt"))) {
                for (String line : SList) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void displaySalesIdList() {
            System.out.println("Existing Sales Data:");
            try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 4) {
                        int salesId = Integer.parseInt(parts[0]);
                        String itemId = parts[1];
                        int quantity = Integer.parseInt(parts[2]);
                        double itemPrice = Double.parseDouble(parts[3]);

                        System.out.println("Sales ID: " + salesId);
                        System.out.println("Item ID: " + itemId);
                        System.out.println("Quantity: " + quantity);
                        System.out.println("Item Price: " + itemPrice);
                        System.out.println();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //edit 
        public void edit() {
            Scanner scanner = new Scanner(System.in);

            while (true) {

                displaySalesIdList();
                System.out.println("Enter Sales ID to edit (or enter -1 to cancel): ");
                int salesIdToEdit = scanner.nextInt();
                scanner.nextLine();

                if (salesIdToEdit == -1) {
                    System.out.println("Canceling edit.");
                    return; // Exit the loop
                }

                ArrayList<String> updatedSalesData = new ArrayList<>();
                boolean found = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))) {
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break; // Exit the loop if end of file is reached
                        }

                        String[] parts = line.split(" ");
                        int currentSalesId = Integer.parseInt(parts[0]);

                        if (currentSalesId == salesIdToEdit) {
                            found = true;
                            System.out.println("Editing Sales Entry: " + line);

                            String newItemCode;
                            do {
                                System.out.print("Enter new item code: ");
                                newItemCode = scanner.nextLine();
                            } while (getItemPrice(newItemCode) == -1);

                            System.out.print("Enter new quantity sold: ");
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            double newTotalSales = newQuantity * getItemPrice(newItemCode);

                            // Update line with new values
                            line = salesIdToEdit + " " + newItemCode + " " + newQuantity + " " + newTotalSales;
                            System.out.println("Updated Sales Entry: " + line);
                        }

                        updatedSalesData.add(line);
                    }

                    if (!found) {
                        System.out.println("Sales ID not found.");
                    } else {
                        // Write the updated sales data back to the file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt"))) {
                            for (String updateLine : updatedSalesData) {
                                writer.write(updateLine);
                                writer.newLine();
                            }
                            System.out.println("Sales entry updated successfully!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private double getItemPrice(String itemCode) {
            try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 3 && parts[0].equals(itemCode)) {
                        return Double.parseDouble(parts[parts.length - 2]); // Assuming price is the last value
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Item not found.");
            return -1; // Return -1 to indicate item not found
        }

        private Item findItemByCode(String itemCode) {
            for (Item item : items) {
                if (item.itemId.equals(itemCode)) {
                    return item;
                }
            }
            return null;
        }

    }

    public class prMenu extends BaseMenu implements menu, addfunction, deletefunction, editfunction, viewfunction {

        private List<Item> items; // You need to define this list
        private List<PurchaseRequisition> prList;

        public prMenu() {
            prList = loadPurchaseRequisitions("pr_data.txt"); // Initialize prList
        }

        @Override
        public void displayMenu() {
            System.out.println(" ");
            System.out.println("Welcome to Purchase Requisition Entry");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Choose what you want to do");
            System.out.println("--------------------------------");
            System.out.println("1. Add Purchase Requisition");
            System.out.println("2. Delete Purchase Requisition");
            System.out.println("3. Edit Purchase Requisition");
            System.out.println("4. View Purchase Requisition");
            System.out.println("-1. Exit");
            System.out.println("--------------------------------");
            System.out.println("Select an option");
        }

        @Override
        public void processChoice() {
            int choice;
            do {
                displayMenu();
                choice = readChoice();

                switch (choice) {
                    case 1:
                        add();
                        break;
                    case 2:
                        delete();
                        break;
                    case 3:
                        edit();
                        break;
                    case 4:
                        view();
                        break;
                    case -1:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (true);
        }

        @Override
        public void add() {

            Scanner scanner = new Scanner(System.in);
            List<PurchaseRequisition> items = new ArrayList<>();
            Map<String, String> userInfo = new HashMap<>();

            // Generate a random PR ID
            int prId = new Random().nextInt(10000) + 1; // Change 10000 to any desired range

            // Read user info from "userinfo.txt" file into a Map
            try {
                File userInfoFile = new File("userinfo.txt");
                Scanner fileScanner = new Scanner(userInfoFile);
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(" ");
                    String userName = parts[0];
                    String role = parts[1];
                    userInfo.put(userName, role);
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("User info file not found.");
                return;
            }

            // Check items below reorder level or low in stock
            List<String> lowStockItems = getLowStockItems("item.txt");
            if (!lowStockItems.isEmpty()) {
                System.out.println("Items below reorder level or low in stock:");
                for (String item : lowStockItems) {
                    System.out.println(item);
                }
            } else {
                System.out.println("No items below reorder level or low in stock.");
            }

            // Get user input for name
            String name = null;
            while (name == null) {
                System.out.print("Enter your name: ");
                name = scanner.nextLine();

                if (name.equals("-1")) {
                    System.out.println("Going back to the menu...");
                    return; // Return to the menu
                }

                // Validate if entered name exists in user info
                if (!userInfo.containsKey(name)) {
                    System.out.println("Invalid name. Please try again.");
                    name = null;
                }
            }

            // Prompt user to enter item code from available options
            List<String> itemCodes = readItemCodesFromFile("item.txt");
            if (itemCodes.isEmpty()) {
                System.out.println("No item codes found.");
                return;
            }

            System.out.println("Item Id:");
            for (String code : itemCodes) {
                System.out.println(code);
            }

            while (true) {
                System.out.print("Enter item ID (if return menu key in -1): ");
                String itemId = scanner.nextLine();

                if (itemId.equals("-1")) {
                    System.out.println("Going back to the menu...");
                    return;
                }

                // Validate if entered item ID is in the available options
                if (!itemCodes.contains(itemId)) {
                    System.out.println("Invalid item ID. Please try again.");
                    continue;
                }

                // Validate date required format
                Date dateRequired = null;
                while (dateRequired == null) {
                    System.out.print("Enter date required (dd-MM-yyyy): ");
                    String dateRequiredStr = scanner.nextLine();

                    if (dateRequiredStr.equals("-1")) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        dateFormat.setLenient(false);
                        dateRequired = dateFormat.parse(dateRequiredStr);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please try again.");
                    }
                }

                // Validate quantity
                int quantity = 0;
                while (quantity <= 0) {
                    System.out.print("Enter quantity: ");
                    String quantityStr = scanner.nextLine();

                    if (quantityStr.equals("-1")) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    try {

                        quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) {
                            System.out.println("Invalid quantity. Please enter a valid positive number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity format. Please enter a valid number.");
                    }
                }

                // Validate input supplier ID based on the associated item ID
                Map<String, List<String>> supplierIdsMap = readSupplierIdsFromFile("supplier.txt");

                List<String> supplierIdsForItemId = supplierIdsMap.get(itemId);
                System.out.println("Supplier IDs for itemId " + itemId + ": " + supplierIdsForItemId);

                if (supplierIdsForItemId == null || supplierIdsForItemId.isEmpty()) {
                    System.out.println("No supplier IDs found for the selected item.");
                } else {
                    boolean validSupplierId = false;
                    String supplierId = null; // Initialize supplierId

                    while (!validSupplierId) { // Change do-while to while loop
                        System.out.print("Enter supplier ID: ");
                        String inputSupplierId = scanner.nextLine();

                        if (inputSupplierId.equals("-1")) {
                            System.out.println("Going back to the menu...");
                            return; // Return to the menu
                        }

                        if (supplierIdsForItemId.contains(inputSupplierId)) {
                            validSupplierId = true;
                            supplierId = inputSupplierId;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate = dateFormat.format(dateRequired);

                            System.out.println("prId: " + prId);
                            System.out.println("itemId: " + itemId);
                            System.out.println("quantity: " + quantity);
                            System.out.println("dateRequired: " + formattedDate);
                            System.out.println("name: " + name);
                            System.out.println("supplierId: " + supplierId);

                            // Create a new PurchaseRequisition object with the entered details
                            PurchaseRequisition pr = new PurchaseRequisition(prId, itemId, quantity, dateRequired, "Pending", name, supplierId);

                            // Add the PR object to your list of items
                            items.add(pr);

                            // Save the PR details to a text file
                            String fileName = "pr_data.txt";
                            pr.saveToFile(fileName);

                            System.out.println("Purchase Requisition added successfully");
                            // Show the details of the added requisition
                            System.out.println("Added Purchase Requisition Details:");
                            System.out.println("prId: " + prId);
                            System.out.println("itemId: " + itemId);
                            System.out.println("quantity: " + quantity);
                            System.out.println("dateRequired: " + formattedDate);
                            System.out.println("name: " + name);
                            System.out.println("supplierId: " + supplierId);

                            add();

                        } else {
                            System.out.println("Invalid supplier ID for the selected item. Please try again.");
                        }
                    }
                }

                return;
            }
        }

        private List<String> getLowStockItems(String fileName) {
            List<String> lowStockItems = new ArrayList<>();
            int lowStockLevel = 10; // Define your custom low stock level here

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split("\\s+");
                    if (data.length >= 3) {
                        String itemId = data[0];
                        int currentStock = Integer.parseInt(data[2]);

                        if (currentStock <= lowStockLevel) {
                            lowStockItems.add(itemId);
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }

            return lowStockItems;
        }

        public Map<String, List<String>> readSupplierIdsFromFile(String fileName) {
            Map<String, List<String>> supplierIdsMap = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split("\\s+");
                    if (data.length >= 2) {
                        String supplierId = data[0];
                        for (int i = 1; i < data.length; i++) {
                            String itemId = data[i];

                            // Initialize supplierIds list if it's not already
                            supplierIdsMap.putIfAbsent(itemId, new ArrayList<>());

                            supplierIdsMap.get(itemId).add(supplierId);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return supplierIdsMap;
        }

        @Override
        public void delete() {
            // Read the existing PR data from the file and display it
            List<PurchaseRequisition> prList = readPurchaseRequisitionsFromFile("pr_data.txt");

            if (prList.equals("-1")) {
                System.out.println("Going back to the menu...");
                return; // Return to the menu
            }
            if (prList.isEmpty()) {
                System.out.println("No purchase requisitions found.");
                return;
            }

            System.out.println("Purchase Requisitions:");
            for (PurchaseRequisition pr : prList) {
                System.out.println(pr);
            }

            Scanner scanner = new Scanner(System.in);

            // Get the prId to delete from the user
            System.out.print("Enter the PR ID to delete: ");
            int prIdToDelete = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            boolean found = false;

            // Remove the matching PurchaseRequisition object
            Iterator<PurchaseRequisition> iterator = prList.iterator();
            while (iterator.hasNext()) {
                PurchaseRequisition pr = iterator.next();
                if (pr.getPrId() == prIdToDelete) {
                    iterator.remove();
                    found = true;
                    System.out.println("Purchase Requisition with PR ID " + prIdToDelete + " deleted.");
                    break;
                }
            }

            // Write the updated list back to the file
            writePurchaseRequisitionsToFile("pr_data.txt", prList);

            if (!found) {
                System.out.println("No purchase requisition found with PR ID " + prIdToDelete);
            }
        }

        private List<PurchaseRequisition> readPurchaseRequisitionsFromFile(String fileName) {
            List<PurchaseRequisition> prList = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split("\\s+");
                    if (data.length >= 7) {
                        int prId = Integer.parseInt(data[0]);
                        String itemId = data[1];
                        int quantity = Integer.parseInt(data[2]);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date dateRequired = dateFormat.parse(data[3]);
                        String requestStatus = data[4];
                        String name = data[5];
                        String supplierId = data[6];

                        PurchaseRequisition pr = new PurchaseRequisition(prId, itemId, quantity, dateRequired, requestStatus, name, supplierId);
                        prList.add(pr);
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return prList;
        }

        private void writePurchaseRequisitionsToFile(String fileName, List<PurchaseRequisition> prList) {
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(fileName))) {
                for (PurchaseRequisition pr : prList) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = dateFormat.format(pr.getDateRequired());

                    printWriter.println(pr.getPrId() + " " + pr.getItemId() + " " + pr.getQuantity() + " " + formattedDate
                            + " " + pr.getRequestStatus() + " " + pr.getName() + " " + pr.getSupplierId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void view() {
            File file1 = new File("pr_data.txt");
            try {
                System.out.println("List of Purchase Requisition:");
                System.out.println("----------------------------");
                Scanner sc3 = new Scanner(file1);
                while (sc3.hasNextLine()) {
                    String info = sc3.nextLine();
                    System.out.println(info);
                }
                System.out.println("----------------------------");
            } catch (IOException e) {
                System.out.println("File not found");
            }
        }

        @Override
        public void edit() {
            prList.clear();
            prList.addAll(loadPurchaseRequisitions("pr_data.txt"));
            // Print the PR information before editing
            System.out.println("PR Information Before Editing:");
            for (PurchaseRequisition pr : prList) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(pr.getDateRequired());
                System.out.println(pr.getPrId() + " " + pr.getItemId() + " " + pr.getQuantity() + " " + formattedDate + " " + pr.getName() + " " + pr.getSupplierId());
            }

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("-------------------------------------------- \n");
                System.out.print("Enter the PR ID to edit (or -1 to exit): ");
                int prIdToEdit = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (prIdToEdit == -1) {
                    System.out.println("Exiting PR editing...");
                    return; // Return to the menu
                }

                PurchaseRequisition prToEdit = null;
                for (PurchaseRequisition pr : prList) {
                    if (pr.getPrId() == prIdToEdit) {
                        prToEdit = pr;
                        break;
                    }
                }

                if (prToEdit == null) {
                    System.out.println("PR ID not found. Please try again.");
                    continue; // Continue with the loop
                }

                System.out.println("Editing PR with ID: " + prToEdit.getPrId());

                // Editing Item ID
                List<String> itemCodes = readItemCodesFromFile("item.txt");
                System.out.println("Item Id:");
                for (String code : itemCodes) {
                    System.out.println(code);
                }

                String updatedItemId;
                while (true) {
                    System.out.print("Enter updated Item ID: ");
                    updatedItemId = scanner.nextLine();

                    if (updatedItemId.equals("-1")) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    if (!itemCodes.contains(updatedItemId)) {
                        System.out.println("Invalid item ID. Please try again.");
                    } else {
                        break; // Valid item ID entered
                    }
                }
                prToEdit.setItemId(updatedItemId);

                // Editing Quantity
                int updatedQuantity;
                while (true) {
                    System.out.print("Enter updated Quantity: ");
                    updatedQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    if (updatedQuantity == -1) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    if (updatedQuantity <= 0) {
                        System.out.println("Invalid quantity. Please enter a valid positive number.");
                    } else {
                        break; // Valid quantity entered
                    }
                }
                prToEdit.setQuantity(updatedQuantity);

                // Editing Date Required
                Date updatedDateRequired;
                while (true) {
                    System.out.print("Enter updated date required (dd-MM-yyyy): ");
                    String updatedDateRequiredStr = scanner.nextLine();

                    if (updatedDateRequiredStr.equals("-1")) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        updatedDateRequired = dateFormat.parse(updatedDateRequiredStr);
                        break; // Valid date entered
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please try again.");
                    }
                }
                prToEdit.setDateRequired(updatedDateRequired);

                // Editing Name
                Map<String, String> userInfo = readUserInfoFromFile("Userinfo.txt");
                String updatedName;
                while (true) {
                    System.out.print("Enter updated Name: ");
                    updatedName = scanner.nextLine();

                    if (updatedName.equals("-1")) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    if (!userInfo.containsKey(updatedName)) {
                        System.out.println("Invalid name. Please try again.");
                    } else {
                        break; // Valid name entered
                    }
                }
                prToEdit.setName(updatedName);

                // Validate input supplier ID based on the associated item ID
                Map<String, List<String>> supplierIdsMap = readSupplierIdsFromFile("supplier.txt");
                List<String> supplierIdsForItemId = supplierIdsMap.get(prToEdit.getItemId());

                if (supplierIdsForItemId == null || supplierIdsForItemId.isEmpty()) {
                    System.out.println("No supplier IDs found for the selected item.");
                    return; // Return to the menu
                }

                System.out.println("Available Supplier IDs for the item: " + supplierIdsForItemId);
                String updatedSupplierId;
                while (true) {
                    System.out.print("Enter updated Supplier ID: ");
                    updatedSupplierId = scanner.nextLine();

                    if (updatedSupplierId.equals("-1")) {
                        System.out.println("Going back to the menu...");
                        return; // Return to the menu
                    }

                    if (supplierIdsForItemId.contains(updatedSupplierId)) {
                        prToEdit.setSupplierId(updatedSupplierId);

                        // Save the updated PR details to the file
                        String fileName = "pr_data.txt";
                        savePurchaseRequisitions(fileName, prList);

                        System.out.println("Purchase Requisition updated successfully");
                        return; // Return to the menu
                    } else {
                        System.out.println("Invalid supplier ID for the selected item. Please try again.");
                    }
                }
            }
        }

        private List<String> readItemCodesFromFile(String fileName) {
            List<String> itemCodes = new ArrayList<>();

            try {
                File file = new File(fileName);
                Scanner fileScanner = new Scanner(file);
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(" ");
                    String itemId = parts[0];
                    itemCodes.add(itemId);
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + fileName);
            }

            return itemCodes;
        }

        private List<PurchaseRequisition> loadPurchaseRequisitions(String fileName) {
            List<PurchaseRequisition> prList = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split("\\s+");
                    if (data.length >= 7) {
                        int prId = Integer.parseInt(data[0]);
                        String itemId = data[1];
                        int quantity = Integer.parseInt(data[2]);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date dateRequired = dateFormat.parse(data[3]);
                        String requestStatus = data[4];
                        String name = data[5];
                        String supplierId = data[6];

                        PurchaseRequisition pr = new PurchaseRequisition(prId, itemId, quantity, dateRequired, requestStatus, name, supplierId);
                        prList.add(pr);
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return prList;
        }

        private void savePurchaseRequisitions(String fileName, List<PurchaseRequisition> prList) {
            try {
                FileWriter writer = new FileWriter(fileName, false);
                PrintWriter printWriter = new PrintWriter(writer);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                for (PurchaseRequisition pr : prList) {
                    String formattedDate = dateFormat.format(pr.getDateRequired());
                    printWriter.println(pr.getPrId() + " " + pr.getItemId() + " " + pr.getQuantity() + " " + formattedDate + " " + pr.getRequestStatus()
                            + " " + pr.getName() + " " + pr.getSupplierId());
                }

                printWriter.close();
            } catch (IOException e) {
                System.out.println("Error saving PR data to file.");
            }
        }

        private Map<String, String> readUserInfoFromFile(String fileName) {
            Map<String, String> userInfo = new HashMap<>();

            try {
                File userInfoFile = new File(fileName);
                Scanner fileScanner = new Scanner(userInfoFile);
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(" ");
                    String userName = parts[0];
                    String role = parts[1];
                    userInfo.put(userName, role);
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("User info file not found.");
            }

            return userInfo;
        }
    }

}
