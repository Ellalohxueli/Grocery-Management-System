package Assessment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PurchaseManager extends BaseMenu {

    //Override
    @Override
    public void displayMenu() {
        System.out.println(" ");
        System.out.println("Welcome back, Purchase Manager");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("Choose what you want to do");
        System.out.println("--------------------------------");
        System.out.println("1. View Item");
        System.out.println("2. View Suppliers");
        System.out.println("3. View Requisition");
        System.out.println("4. Purchase Order");
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

            switch (choice) {
                case 1:
                    viewfunction viewIFunction = new ItemView();
                    viewIFunction.view();
                    break;
                case 2:
                    viewfunction viewFunction = new SupplierView();
                    viewFunction.view();
                    //view();
                    break;
                case 3:
                    SalesManager sm = new SalesManager();
                    SalesManager.prMenu salespr = sm.new prMenu();
                    salespr.view();
                    break;
                case 4:
                    PurchaseOrder po = new PurchaseOrder();
                    po.processChoice();
                    break;
                case -1:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    class PurchaseOrder extends BaseMenu implements menu, addfunction, editfunction, deletefunction, viewfunction {

        /*
    class purchase order has the relationship with BaseMenu, it is the subclass of BaseMenu
         */
        ArrayList<String> poList = new ArrayList<>();
        ArrayList<String> prList = new ArrayList<>();
        ArrayList<String> codeList = new ArrayList<>();

        @Override
        public void displayMenu() {
            System.out.println(" ");
            System.out.println("Welcome to Purchase Order Entry");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Choose what you want to do");
            System.out.println("--------------------------------");
            System.out.println("1. Add Purchase Order");
            System.out.println("2. Delete Purchase Order");
            System.out.println("3. Edit Purchase Order");
            System.out.println("4. View Purchase Order");
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
                /*this method is used to read the input from user
            and convert the input to integer type */
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
            boolean found = false;//initialize the boolean to false
            do {
                try {
                    codeList.clear();
                    File prFile = new File("pr_data.txt");
                    Scanner prScanner = new Scanner(prFile);
                    prList.clear();
                    //the following code is to display the purchase requisitiin that the 4 element
                    //is not equal to "Done"
                    System.out.println("List of Purchase Requisition");
                    System.out.println("-------------------------------");
                    while (prScanner.hasNextLine()) {
                        String prinfo = prScanner.nextLine();
                        String[] details = prinfo.split(" ");
                        prList.add(prinfo);
                        codeList.add(details[0]);
                        if (!details[4].equals("Done")) {
                            System.out.println(prinfo);
                        }
                        //if the 4th element in line is not = "Done" then print the line.
                    }
                } catch (FileNotFoundException fnfo) {
                    System.out.println("PR file not found in the system");
                    return;  // Exit the method if PR file is not found
                }

                try {
                    File poFile = new File("PO.txt");
                    Scanner poScanner = new Scanner(poFile);
                    poList.clear();
                    while (poScanner.hasNextLine()) {
                        String po = poScanner.nextLine();
                        poList.add(po);
                    }
                } catch (FileNotFoundException fnfo) {
                    System.out.println("PO file not found in the system");
                    return;  // Exit the method if PO file is not found
                }
                System.out.println("Select the Purchase Requisition id");
                Scanner sc1 = new Scanner(System.in);
                //get the user input of which pr the user wants to select
                String prcode = sc1.nextLine();
                if (prcode.trim().isEmpty()) {
                    System.out.println("Don't leave it blank!");
                    continue;  // Restart the loop if PR code is blank
                    //if the input is empty, the system will restart the loop
                } else if (!codeList.contains(prcode)) {
                    System.out.println("No such Purchase Requisition id");
                    continue;  // Restart the loop if PR code is not found
                }
                for (String line : prList) {//for every single line in prList
                    String[] infodetails = line.split(" ");//get the element in line
                    if (line.startsWith(prcode)) {
                        //if the line (which is the 1 element) is start with the user input
                        int newNum;
                        if (poList.isEmpty()) {//check if the poList is empty or not
                            newNum = 1;//if yes, then assign value 1 to arguement "newNum"
                        } else {
                            int old = 0;  // Initialize the old value to 0
                            for (String poline : poList) {//get the line in poList
                                String[] elements = poline.split(" ");//split the line with a blank space
                                if (elements.length > 0 && elements[0].length() >= 2) {
                                    //if the element in line is more than 0 and the first element's lengths is >=2
                                    try {
                                        int poNum = Integer.parseInt(elements[0].substring(2));
                                        /*
                                        elements[0].substring(2) is to get the character after the 1 index
                                        for example, the "word", then the substring(2) will get "rd".
                                         */
                                        old = Math.max(old, poNum);
                                        //compare and assigned the latest maximum number to arguement "old"
                                    } catch (NumberFormatException e) {
                                        System.out.println("Something went wrong ");
                                    } catch (StringIndexOutOfBoundsException e) {
                                        System.out.println("Something went wrong ");
                                    }
                                } else {
                                    System.out.println("Invalid input found ");
                                }
                            }
                            newNum = old + 1;
                        }
                        System.out.println("1. Approved");
                        System.out.println("2. Rejected");
                        System.out.println("Enter your decision");
                        System.out.println("--------------------------------------------");
                        Scanner sc2 = new Scanner(System.in);//get input from user
                        String goal1 = sc2.nextLine();
                        if (goal1.trim().isEmpty()) {//if input is empty
                            System.out.println("Don't leave it blank!");
                            continue;  // Restart the loop if the input is blank
                        }
                        int goal = Integer.parseInt(goal1);//convert the input to integer datatype
                        if (goal == 1) {
                            infodetails[4] = "Done";//update the 5th element in line to "Done"
                            String status = "Approved";
                            // Construct the new PO entry
                            String newPO = "PO" + String.format("%02d", newNum) + " " + prcode + " " + infodetails[1] + " "
                                    + infodetails[2] + " " + infodetails[3] + " " + "Done" + " " + infodetails[5] + " " + status;
                            poList.add(newPO);
                            // Save the updated PO list to the file
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("PO.txt"))) {
                                for (String poEntry : poList) {
                                    writer.write(poEntry);
                                    writer.newLine();
                                }
                                System.out.println("Purchase Order generated successfully!");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            // Mark the PR as done in the PR list
                            for (String prLine : prList) {
                                if (prLine.startsWith(prcode)) {
                                    String[] prDetails = prLine.split(" ");
                                    prDetails[4] = "Done";
                                    String updatedPR = String.join(" ", prDetails);
                                    prList.set(prList.indexOf(prLine), updatedPR);
                                    break;
                                }
                            }//and update it to the pr file
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("pr_data.txt"))) {
                                for (String pr : prList) {
                                    writer.write(pr);
                                    writer.newLine();
                                }
                                System.out.println("Purchase Requisition updated successfully!");
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        } else if (goal == 2) {
                            infodetails[4] = "Done";
                            String status = "Rejected";
                            // Construct the new PO entry
                            String newPO = "PO" + String.format("%02d", newNum) + " " + prcode + " " + infodetails[1] + " "
                                    + infodetails[2] + " " + infodetails[3] + " " + "Done" + " " + infodetails[5] + " " + status;
                            poList.add(newPO);
                            // Save the updated PO list to the file
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("PO.txt"))) {
                                for (String poEntry : poList) {
                                    writer.write(poEntry);
                                    writer.newLine();
                                }
                                System.out.println("Purchase Order generated successfully!");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            // Mark the PR as done in the PR list
                            for (String prLine : prList) {
                                if (prLine.startsWith(prcode)) {
                                    String[] prDetails = prLine.split(" ");
                                    prDetails[4] = "Done";
                                    String updatedPR = String.join(" ", prDetails);
                                    prList.set(prList.indexOf(prLine), updatedPR);
                                    break;
                                }
                            }
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("pr_data.txt"))) {
                                for (String pr : prList) {
                                    writer.write(pr);
                                    writer.newLine();
                                }
                                System.out.println("Purchase Requisition updated successfully!");
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        } else {
                            System.out.println("Invalid input");
                        }
                    }
                }
                System.out.println("Do you want to continue generate purchase order?");
                //ask if the user wants to continue generate or not
                System.out.println("Enter yes if you wish to continue, enter random to quit this operation");
                Scanner sc4 = new Scanner(System.in);
                String answer = sc4.nextLine();
                if (answer.toLowerCase().equals("yes")) {
                    continue;
                }//if yes, loop back to the method
                else {
                    found = true;
                }  //if no exit the loop
            } while (!found);//loop when the found = false
        }

        @Override
        public void edit() {
            System.out.println("You are only able to edit the po you have done before");
            System.out.println("-------------------------------------------------------");
            int number = 0;
            Scanner sc6 = new Scanner(System.in);
            do {
                int count = 0;
                poList.clear();
                try {
                    File poFile = new File("PO.txt");
                    Scanner poScanner = new Scanner(poFile);
                    System.out.println("Reading PO.txt...");
                    while (poScanner.hasNextLine()) {
                        String poInfo = poScanner.nextLine();
                        count = count + 1;
                        System.out.println(count + " " + poInfo);
                        poList.add(poInfo);
                    }
                    System.out.println("Enter the number of line you want to edit");
                    String num1 = sc6.nextLine();
                    if (num1.trim().isBlank()) {
                        System.out.println("Please enter the number");
                        System.out.println("--------------------------------------------");
                        number = 0;
                    } else {
                        number = Integer.parseInt(num1);
                        if (number > count || number < 1) {
                            System.out.println("Please enter the valid number");
                            System.out.println("--------------------------------------------");
                            number = 0;
                        } else {
                            String editline = poList.get(number - 1);
                            String[] details = editline.split(" ");
                            if ("Pending".equals(details[5])) {
                                System.out.println("You can't modify this purchase order");
                                number = 0;
                            }
                            /*
                        if the 6 elements in each line is = "Pending", which means that the 
                        user hasn't processed with this purchase order,
                        so it can't be edited
                             */
                        }
                    }
                } catch (FileNotFoundException eq) {
                    System.out.println("No such file");
                    processChoice();
                } catch (NumberFormatException nfe) {
                    System.out.println("Something wrong");
                    number = 0;
                }
            } while (number == 0);
            String editline = poList.get(number - 1);
            String[] details = editline.split(" ");
            int decision = 0;
            do {
                System.out.println("--------------------------------------------");
                System.out.println("Reminder for Purchase Manager!!!");
                System.out.println("You are not able to change other details");
                System.out.println("You only have the rights to modify the decision for po");
                System.out.println("Please select the decision you want to make for this po");
                System.out.println("------------------------------------------------");
                System.out.println("1. Approved");
                System.out.println("2. Rejected");
                String decision1 = sc6.nextLine();
                if (decision1.trim().isBlank()) {
                    System.out.println("Please enter your decision");
                    System.out.println("--------------------------------------------");
                    decision = 0;
                } else {
                    try {
                        decision = Integer.parseInt(decision1);
                        if (decision != 1 && decision != 2) {
                            System.out.println("Please enter the valid code");
                            System.out.println("--------------------------------------------");
                            decision = 0;
                        } else if (decision == 1) {
                            details[8] = "Approved";
                            String updatedline = String.join(" ", details);
                            poList.set(number - 1, updatedline);
                            try (PrintWriter pw = new PrintWriter("PO.txt")) {
                                for (String newline : poList) {
                                    pw.println(newline);
                                }
                            }
                            System.out.println("PO edit successfully");
                        } else if (decision == 2) {
                            details[8] = "Rejected";
                            String updatedline = String.join(" ", details);
                            poList.set(number - 1, updatedline);
                            try (PrintWriter pw = new PrintWriter("PO.txt")) {
                                for (String newline : poList) {
                                    pw.println(newline);
                                }
                            }
                            System.out.println("PO edit successfully");
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid wrong");
                        decision = 0;
                    } catch (FileNotFoundException fnfe) {
                        System.out.println("File  not found");
                    }
                }
            } while (decision == 0);
        }

        @Override
        public void delete() {
            Scanner sn = new Scanner(System.in);
            int delete = 0;
            do {
                try {
                    int count = 0;
                    poList.clear();
                    File myfile2 = new File("PO.txt");
                    Scanner sc7 = new Scanner(myfile2);
                    while (sc7.hasNextLine()) {
                        String poline = sc7.nextLine();
                        count = count + 1;
                        System.out.println(count + " " + poline);
                        poList.add(poline);
                        //get all the line in po file and display it
                    }
                    System.out.println("Select the number of line you want to delete");
                    String delet1 = sn.nextLine();
                    //prompt user to select which line to remove
                    if (delet1.trim().isBlank()) {
                        System.out.println("Please enter the number");
                        System.out.println("--------------------------------------------");
                        delete = 0;
                        //if the input is blank, then let user to enter again
                    } else {
                        delete = Integer.parseInt(delet1);
                        if (delete > count || delete < 1) {
                            System.out.println("Please enter the valid number");
                            System.out.println("--------------------------------------------");
                            delete = 0;
                            /*
                        if the input is more than the size of array list, then let user enter again
                             */
                        } else {
                            try (PrintWriter pw = new PrintWriter("PO.txt")) {
                                for (int i = 0; i < poList.size(); i++) {
                                    if (i != delete - 1) {
                                        pw.println(poList.get(i));
                                    }
                                    /*
                                get the index of the line and rewrite the line except for the line 
                                which the admin wants to remove
                                     */
                                }
                            }
                            System.out.println("PO delete successfully");
                        }
                    }
                } catch (FileNotFoundException f) {
                    System.out.println("File Not found");
                    Admin ad = new Admin();
                    ad.processChoice();
                } catch (IOException eq) {
                    System.out.println("Something wrong");
                    delete = 0;
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid wrong");
                    delete = 0;
                }
            } while (delete == 0);
        }

        @Override
        public void view() {
            try {
                System.out.println("List of Purchase Order:");
                System.out.println("----------------------------");
                File poFile = new File("PO.txt");
                Scanner poScanner = new Scanner(poFile);
                System.out.println("Reading PO.txt...");
                while (poScanner.hasNextLine()) {
                    String poInfo = poScanner.nextLine();
                    System.out.println(poInfo);
                }
            } catch (IOException e) {
                System.out.println("Something error");
            }
        }
    }

}

class SupplierView implements viewfunction {

    public void view() {
        File viewS = new File("supplier.txt");
        try {
            Scanner vs = new Scanner(viewS);
            ArrayList<String> IList = new ArrayList<>();

            while (vs.hasNextLine()) {
                String SupplierInfo = vs.nextLine();
                IList.add(SupplierInfo);
            }

            System.out.println("List of Suppliers:");
            System.out.println("----------------------------");
            for (String supplierInfo : IList) {
                System.out.println(supplierInfo);
            }
            //System.out.println(IList);
            System.out.println("----------------------------");

        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}

class ItemView implements viewfunction {

    public void view() {
        File viewI = new File("item.txt");
        try {
            Scanner vi = new Scanner(viewI);
            ArrayList<String> IList = new ArrayList<>();

            while (vi.hasNextLine()) {
                String ItemInfo = vi.nextLine();
                IList.add(ItemInfo);
            }

            System.out.println("List of Items:");
            System.out.println("----------------------------");
            for (String itemInfo : IList) {
                System.out.println(itemInfo);
            }
            System.out.println("----------------------------");
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
