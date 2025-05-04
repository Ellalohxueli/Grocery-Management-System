package Assessment;

import java.util.Scanner;

interface addfunction
{
    public void add();
}

interface editfunction 
{
    public void edit();
}

interface deletefunction
{
    public void delete();
}


interface viewfunction
{
    public void view();
}

interface write2file
{
    public void wri2file();
}

interface menu
{
    public void displayMenu();
    public void processChoice();
}


abstract class BaseMenu implements menu {
    protected Scanner scanner = new Scanner(System.in);

    protected int readChoice() {
        int choice;
            try {
            choice = Integer.parseInt(scanner.nextLine()); //read from user input
        } catch (NumberFormatException e) {
            choice = 0; //default value
        }
        return choice;
    }

    protected String readInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }    
}

