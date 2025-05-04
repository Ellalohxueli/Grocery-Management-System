package Assessment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Admin extends BaseMenu {

    ArrayList<String> ulist = new ArrayList<>();
    // creat an arraylist to store the data of user in text file
    ArrayList<String> infolist = new ArrayList<>();

    @Override
    public void displayMenu() {
        System.out.println(" ");
        System.out.println("Welcome back,Admin");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("Choose what you want to do");
        System.out.println("--------------------------------");
        System.out.println("1. User Entry");
        System.out.println("2. Item Entry");
        System.out.println("3. Supplier Entry");
        System.out.println("4. Daily Item-wise Sales Entry");
        System.out.println("5. Purchase Requisition");
        System.out.println("6. Purchaser Order");
        System.out.println("-1. Exit");
        System.out.println("--------------------------------");
        System.out.println("Enter your choice");
    }

    //Override
    public void processChoice() {
        SalesManager sm = new SalesManager();
        //create an instance for salesmanager class
        int choice;
        do {
            displayMenu();
            //call the method
            choice = readChoice();
            //read the choice and convert it into integer data form

            if (choice == -1) {
                System.out.println("Exiting...");
                return;
            } else if (choice == 0) {
                System.out.println("Invalid Input. Please try again.");
            } else {

                switch (choice) {
                    case 1:
                        Admin.UserMenu adminmenu = new UserMenu();
                        /*
                        Admin is the outer class, UserMenu is the inner class
                        so if want to call the method in inner class, first have to call the outer class instance first.
                        however, this UserMenu is the inner class of Admin (which is outer class), so just call for inner class
                         */
                        adminmenu.processChoice();
                        // after create the instance of UserMenu(inner class), than call for the method
                        break;

                    case 2:
                        SalesManager.itemMenu itemMenu = sm.new itemMenu();
                        itemMenu.processChoice();
                        break;
                    case 3:

                        SalesManager.supplierMenu SupplierMenu = sm.new supplierMenu();
                        SupplierMenu.processChoice();
                        break;
                    case 4:
                        SalesManager.salesMenu SalesMenu = sm.new salesMenu();
                        SalesMenu.processChoice();

                        break;
                    case 5:
                        SalesManager.prMenu PrMenu = sm.new prMenu();
                        PrMenu.processChoice();
                        break;
                    case 6:
                        PurchaseManager pm = new PurchaseManager();
                        PurchaseManager.PurchaseOrder po = pm.new PurchaseOrder();
                        po.processChoice();
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

    class UserMenu extends BaseMenu implements menu, addfunction, editfunction, viewfunction, deletefunction {

        /*
          The relationship between UserMenu and BaseMenu is abstract ("is a" relationship)
        while all the function after the word "implements" is interface
         */
        @Override
        public void displayMenu() {
            //  int choice = 0;
            System.out.println("Welcome to User Entry");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Choose what you want to do");
            System.out.println("--------------------------------");
            System.out.println("1. Register user");
            System.out.println("2. View user ");
            System.out.println("3. Edit User");
            System.out.println("4.Delete User ");
            System.out.println("-1. Exit");
            System.out.println("--------------------------------");
            System.out.println("Enter your choice");
        }

        public void processChoice() {
            int choice;
            do {
                displayMenu();
                choice = readChoice();
                switch (choice) {
                    case 1:
                        add();//calling the method
                        break;
                    case 2:
                        view();
                        break;
                    case 3:
                        edit();
                        break;
                    case 4:
                        delete();
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
            boolean addmore;
            do {
                String[] myArr;
                //the variable "myArr" is declared to hold an array of string
                Scanner sc = new Scanner(System.in);
                /* 
            scanner is used to read the user input
            and here is creating an instance for scanner
                 */
                System.out.println("Name: ");
                String n = sc.nextLine();//get the user input in next Line
                while (n.trim().isBlank()) {
                    System.out.println("Please don't leave blank: ");
                    n = sc.nextLine();
                }
                /*
            the code above is saying that if the user enter a blank input,
            the system will prompt user to enter again.
                 */
                System.out.println("Location:");
                String d = sc.nextLine();
                while (d.trim().isBlank()) {
                    System.out.println("Please don't leave blank: ");
                    d = sc.nextLine();
                }
                System.out.println("Email: ");
                String e = sc.nextLine();
                while (e.trim().isBlank()) {
                    System.out.println("Please don't leave blank: ");
                    e = sc.nextLine();
                }
                System.out.println("Phone Number: ");
                String pn = sc.nextLine();
                while (pn.trim().isBlank()) {
                    System.out.println("Please don't leave blank: ");
                    pn = sc.nextLine();
                }
                //username=UN;
                ulist.clear();
                //clear the elements in list
                try {
                    File file1 = new File("Userinfo.txt");
                    // get the file
                    Scanner sc4 = new Scanner(file1);
                    while (sc4.hasNextLine()) {
                        String info = sc4.nextLine();
                        myArr = info.split(" ");
                        ulist.add(myArr[0]);
                    }
                    /*
                if there are the line in textfile (which means that there are data recorded in file)
                the scanner will read the line and added the first element in each line into the arrayList.
                the line (myArr=info.split(" ")): is declared that the element in one line is spilt by one blank space
                     */
                } catch (IOException e1) {
                    System.out.println("File not found");
                    // if there is no such file
                }
                System.out.println("Enter username");
                String u = sc.nextLine();
                boolean userExists;
                while (u.trim().isBlank()) {
                    System.out.println("Please don't leave blank: ");
                    u = sc.nextLine();
                }
                System.out.println(ulist); // print out the information get in file
                do {
                    userExists = false; // set the boolean to false statement
                    for (String existingUsername : ulist) {
                        if (u.equals(existingUsername)) {
                            userExists = true;
                            break;
                        }
                    }
                    /*
                loop the ulist to check if the username is exist in file or not
                ## ulist is contained the data of the first element in each line, 
                ## which mean that all the username (1st elements) is stored into the new list
                if username exist, than set boolean userExist = true
                     */
                    if (userExists) {
                        System.out.println("Username existed");
                        u = sc.nextLine();
                    }
                    /*
                if it is true, than the system will noticed the user and prompt user to enter another username
                     */
                } while (userExists);

                System.out.println("Set up password: ");
                String pwd = sc.nextLine();
                while (pwd.trim().isBlank()) {
                    System.out.println("Please don't leave blank: ");
                    pwd = sc.nextLine();
                }
                int choice1;
                do {

                    try {
                        System.out.println("Select the user type");
                        System.out.println("1. Admin");
                        System.out.println("2. Purchase Manager");
                        System.out.println("3. Sales Manage");
                        choice1 = sc.nextInt();
                    } catch (InputMismatchException ex) {
                        sc.nextLine(); // Clear the invalid input from the scanner buffer
                        System.out.println("Invalid input. Please enter a valid number.");
                        choice1 = 0; // Set choice to 0 to repeat the loop
                    } catch (NoSuchElementException ex1) {
                        sc.nextLine(); // Clear the invalid input from the scanner buffer
                        System.out.println("Invalid input. Please enter a valid number.");
                        choice1 = 0; // Set choice to 0 to repeat the loop
                    }
                    //choice = sc.nextInt();

                    if (choice1 == 1) {
                        User u1 = new User(n, d, e, pn, u, pwd, UserType.Admin);
                        /*
                    the data in the bracket is the variable that want to pass into the constructor of User class
                    so in here, i am creating a new instance for class User and passing the variables into the constructor
                         */
                        int save1 = 0;
                        do {
                            System.out.println("Do you want to save the above details to text file?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            String save = sc.next();
                            if (save.trim().isBlank()) {
                                System.out.println("Please don't leave blank!");
                            } else {
                                save1 = Integer.parseInt(save);
                                if (save1 == 1) {
                                    u1.wri2file();
                                    // calling the method of User calss
                                    System.out.println("User added successfulyl");
                                } else if (save1 == 2) {
                                    System.out.println("Discard the change!");
                                    processChoice();
                                } else {
                                    System.out.println("Invalid input");
                                    save1 = 0;
                                }
                            }
                        } while (save1 != 1 && save1 != 2);
                    } else if (choice1 == 2) {
                        User u1 = new User(n, d, e, pn, u, pwd, UserType.PM);
                        int save1 = 0;
                        do {
                            System.out.println("Do you want to save the above details to text file?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            String save = sc.next();
                            if (save.trim().isBlank()) {
                                System.out.println("Please don't leave blank!");
                            } else {
                                save1 = Integer.parseInt(save);
                                if (save1 == 1) {
                                    u1.wri2file();
                                    System.out.println("User added successfulyl");
                                } else if (save1 == 2) {
                                    System.out.println("Discard the change!");
                                    processChoice();
                                } else {
                                    System.out.println("Invalid input");
                                    save1 = 0;
                                }
                            }
                        } while (save1 != 1 && save1 != 2);
                    } else if (choice1 == 3) {
                        User u1 = new User(n, d, e, pn, u, pwd, UserType.SM);
                        int save1 = 0;
                        do {
                            System.out.println("Do you want to save the above details to text file?");
                            System.out.println("1. Yes");
                            System.out.println("2. No");
                            String save = sc.next();
                            if (save.trim().isBlank()) {
                                System.out.println("Please don't leave blank!");
                            } else {
                                save1 = Integer.parseInt(save);
                                if (save1 == 1) {
                                    u1.wri2file();
                                    System.out.println("User added successfulyl");
                                } else if (save1 == 2) {
                                    System.out.println("Discard the change!");

                                    processChoice();
                                } else {
                                    System.out.println("Invalid input");
                                    save1 = 0;
                                }
                            }
                        } while (save1 != 1 && save1 != 2);
                    } else {
                        System.out.println("Invalid input");
                        choice1 = 0;
                    }
                } while (choice1 != 1 && choice1 != 2 && choice1 != 3);
                String adduser = readInput("Add more user? (yes/no)").toLowerCase();
                addmore = adduser.equals("yes");
            } while (addmore);
        }

        @Override
        public void view() {
            File file1 = new File("UserDetails.txt");
            System.out.println("List of user:");
            System.out.println("----------------------------");
            try {
                Scanner sc3 = new Scanner(file1);
                //get the information in file
                while (sc3.hasNextLine()) {
                    String info = sc3.nextLine();
                    System.out.println(info);
                }
                System.out.println("----------------------------");
                System.out.println("----------------------------");
            } catch (IOException e) {
                System.out.println("File not found");
            }
        }

        @Override
        public void edit() {
            int number = 0;
            Scanner sc6 = new Scanner(System.in);
            do {
                int count = 0;
                ulist.clear();
                infolist.clear();
                try {
                    File myfile2 = new File("Userinfo.txt");
                    Scanner sc7 = new Scanner(myfile2);
                    while (sc7.hasNextLine()) {
                        String infoline = sc7.nextLine();
                        String[] infodetails = infoline.split(" ");
                        infolist.add(infoline);
                    }
                    File myfile = new File("UserDetails.txt");
                    Scanner sc5 = new Scanner(myfile);
                    while (sc5.hasNextLine()) {
                        String line = sc5.nextLine();
                        String[] myArr = line.split(" ");
                        count = count + 1;
                        System.out.println(count + " " + line);
                        ulist.add(line);
                    }
                    /*
                    Since in this project i have two file to store user details,
                    so the code above is to get all the line in file to the array
                     */
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
            String editline = ulist.get(number - 1);
            String[] details = editline.split(" ");
            String info1 = infolist.get(number - 1);
            String[] infodata = info1.split(" ");
            /*
            in common, the index of  first element/line in programming language is set to be 0
             if user enter "1" , which mean that he/she wants to edit the first line
            so the index should be (1-1)=0;
            the variable "number" is use to get the line number (which the number is start as 1),
            so if want to get the correct index of line, the variable should be minus 1
            Also,the element in line is split with a blank space,
            and the String array is used to store the element in line
             */
            for (int i = 0; i < details.length; i++) {
                System.out.println((i + 1) + " " + details[i]);
            }
            int elem1 = 0;
            do {
                System.out.println("Enter the number details u want to change");
                String element = sc6.nextLine();
                if (element.trim().isBlank()) {
                    System.out.println("Please enter the number");
                    System.out.println("--------------------------------------------");
                    elem1 = 0;
                } else {
                    try {
                        elem1 = Integer.parseInt(element);//parse the data from string to integer
                        if (elem1 > details.length || elem1 < 1) {
                            System.out.println("Please enter the valid code");
                            System.out.println("--------------------------------------------");
                            elem1 = 0;
                            /*
                            if the number enter is bigger than the number of element that store in each line,
                            or the number is smaller than 1 (which mean is equal or less than 0),
                            then the system should print error message to user and prompt user to enter again the number
                             */
                        } else if (elem1 == 5) {
                            System.out.println("username cannot be changed");
                            elem1 = 0;                
                           //the username is the identification of the user, so there is no allow to change the username                           
                        } else {
                            int chocies = 0;
                            if (elem1 == 7) {
                                try {
                                    System.out.println("Select the user type");
                                    System.out.println("1. Admin");
                                    System.out.println("2. Purchase Manager");
                                    System.out.println("3. Sales Manage");
                                    chocies = sc6.nextInt();
                                } catch (InputMismatchException ex) {
                                    sc6.nextLine(); // Clear the invalid input from the scanner buffer
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    chocies = 0; // Set choice to 0 to repeat the loop
                                } catch (NoSuchElementException ex1) {
                                    sc6.nextLine(); // Clear the invalid input from the scanner buffer
                                    System.out.println("Invalid input. Please enter a valid number.");
                                    chocies = 0; // Set choice to 0 to repeat the loop
                                }
                                if (chocies == 1) {
                                    UserType newvalue = UserType.Admin;
                                    details[6] = newvalue.toString();
                                    infodata[2] = newvalue.toString();
                                    /*
                                    cause there are 2 file to store the user details and info, so if the user is trying to change the
                                    information such as password and usertype, the information should change in both file at the same time
                                     */
                                } else if (chocies == 2) {
                                    UserType newvalue = UserType.PM;
                                    details[6] = newvalue.toString();
                                    infodata[2] = newvalue.toString();
                                } else if (chocies == 3) {
                                    UserType newvalue = UserType.SM;
                                    details[6] = newvalue.toString();
                                    infodata[2] = newvalue.toString();
                                }
                            } else {
                                System.out.println("Modify");
                                String newvalue = sc6.nextLine();
                                while (newvalue.isBlank()) {
                                    System.out.println("Don't leave blank");
                                }
                                if (elem1 == 6) {
                                    details[elem1 - 1] = newvalue;
                                    infodata[elem1 - 4] = newvalue;
                                } else {
                                    details[elem1 - 1] = newvalue;
                                }
                            }
                            String updatedline = String.join(" ", details);
                            String updata = String.join(" ", infodata);
                            if (elem1 == 6 || elem1 == 7) {
                                ulist.set(number - 1, updatedline);
                                //make the specific line to update with the new (modify) line
                                infolist.set(number - 1, updata);
                                try (PrintWriter pw = new PrintWriter("UserDetails.txt")) {
                                    for (String updated : ulist) {
                                        pw.println(updated);//write all the line, including the updated line to the file
                                    }
                                    System.out.println("Data edited successfully");
                                }
                                try (PrintWriter pw1 = new PrintWriter("Userinfo.txt")) {
                                    for (String updated1 : infolist) {
                                        pw1.println(updated1);
                                    }
                                }
                                /*
                                if user is trying to modify the password and usertype details, 
                                then both file should update at the same time
                                 */
                            } else {
                                ulist.set(number - 1, updatedline);
                                try (PrintWriter pw = new PrintWriter("UserDetails.txt")) {
                                    for (String updated : ulist) {
                                        pw.println(updated);
                                    }
                                    System.out.println("Data edited successfully");
                                    /*
                                    if the user is not going to modify the password and usertype, 
                                    then there is no need to modify the other file that only store the information 
                                    for username, password and usertype
                                     */
                                }
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid wrong");
                        elem1 = 0;
                        /*
                        if the user is enter the datatype which is different from the previous setting,
                        such as if the setting is prompting user to enter the numeric type,
                        but the user enter the string type,
                        then the system will show the exception above
                         */
                    } catch (FileNotFoundException fnfe) {
                        System.out.println("File  not found");
                        processChoice();
                        /*
                        if the file which trying to get is not found, 
                        then the system will show this error message,
                        and back to the menu page
                         */
                    }
                }
            } while (elem1 == 0);
        }

        @Override
        public void delete() {
            Scanner sn = new Scanner(System.in);
            int count = 0;
            int delete = 0;
            try {
                ulist.clear();
                infolist.clear();
                File myfile2 = new File("Userinfo.txt");
                Scanner sc7 = new Scanner(myfile2);
                while (sc7.hasNextLine()) {
                    String infoline = sc7.nextLine();
                    infolist.add(infoline);
                }
                File myfile = new File("UserDetails.txt");
                Scanner sc5 = new Scanner(myfile);
                while (sc5.hasNextLine()) {
                    String line = sc5.nextLine();
                    count = count + 1;
                    System.out.println(count + " " + line);
                    ulist.add(line);
                }
                System.out.println("Select the number of line you want to delete");
                String delet1 = sn.nextLine();
                if (delet1.trim().isBlank()) {
                    System.out.println("Please enter the number");
                    System.out.println("--------------------------------------------");
                    delete = 0;
                } else {
                    delete = Integer.parseInt(delet1);
                    if (delete > count || delete < 1) {
                        System.out.println("Please enter the valid number");
                        System.out.println("--------------------------------------------");
                        delete = 0;
                    } else {
                        try (PrintWriter pw = new PrintWriter("UserDetails.txt")) {
                            for (int i = 0; i < ulist.size(); i++) {
                                if (i != delete - 1) {
                                    pw.println(ulist.get(i));
                                }
                            }
                            /*
                            if the line is not equal to the index of list that want to delete,
                            then rewrite other's line into the arraylist, and write into the file */
                        }
                        try (PrintWriter pw1 = new PrintWriter("Userinfo.txt")) {
                            for (int j = 0; j < infolist.size(); j++) {
                                if (j != delete - 1) {
                                    pw1.println(infolist.get(j));
                                }
                            }
                            System.out.println("Data deleted successfully");
                        }
                    }
                }
            } catch (FileNotFoundException f) {
                System.out.println("File Not found");
                Admin.UserMenu adminMenu = new Admin.UserMenu();
                adminMenu.processChoice();
            } catch (IOException eq) {
                System.out.println("Something wrong");
                delete = 0;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid wrong");
                delete = 0;
            }
        }
    }

}
