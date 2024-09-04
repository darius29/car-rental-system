package sda.academy;

import static sda.academy.cli.CommandLineInterface.executeCommand;
import static sda.academy.cli.CommandLineInterface.showMainMenu;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMainMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            executeCommand(choice, scanner);
        }

    }
}