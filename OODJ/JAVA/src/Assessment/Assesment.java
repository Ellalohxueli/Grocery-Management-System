package Assessment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Assesment {

    public static void main(String[] args) {
        ArrayList<User> ulist = new ArrayList<User>();
        System.out.println("Welcome to SIGMA sdn bhd");
        System.out.println("Please login to your account");
        String u;
        String p;
        String username;
        String password;
        String[] myArr;
        Scanner sc = new Scanner(System.in);
        try {
            File file1 = new File("Userinfo.txt");
            Scanner sc4 = new Scanner(file1);
            ulist.clear();
            while (sc4.hasNextLine()) {
                String info = sc4.nextLine();
                myArr = info.split(" ");
                UserType ut = UserType.valueOf(myArr[2]);
                User u1 = new User(myArr[0], myArr[1], ut);
                ulist.add(u1);
            }
            boolean logintrue;
            do {
                System.out.println("username");
                u = sc.nextLine();
                System.out.println("password");
                p = sc.nextLine();
                logintrue = false;
                OUTER:
                for (User user : ulist) {
                    if (user.getUN().equals(u) && user.getPWD().equals(p)) {
                        logintrue = true;

                    }
                    if (logintrue) {
                        if (null != user.getUT()) {
                            switch (user.getUT()) {
                                case Admin -> {
                                    Admin ad = new Admin();
                                    ad.processChoice();
                                    break OUTER;
                                }
                                case SM -> {
                                    SalesManager sm = new SalesManager();
                                    sm.processChoice();
                                    
                                    break OUTER;
                                }
                                case PM -> {
                                    PurchaseManager pm = new PurchaseManager();
                                    pm.processChoice();
                                    break OUTER;
                                }
                                
                            }
                        }
                    }
                }
                if (!logintrue) {
                    System.out.println("False");
                }
            } while (!logintrue);

        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

}
