package org.example;

import org.example.models.User;
import org.example.repositories.UserDbRepository;

import java.util.List;
import java.util.Scanner;

public class UserManager {
    public static void main(String[] args) {
        UserDbRepository repository = new UserDbRepository();
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        while (true) {
            System.out.print("1.add new user\n2.get users\n3.delete user\n4.set balance\n5.quit\n");
            n = scanner.nextInt();
            if (n==1){
                System.out.print("login: ");
                String login = scanner.next();
                System.out.print("\nbalance: ");
                int balance = scanner.nextInt();
                User user = new User(login,balance);
                repository.addUser(user);
            } else if (n==2) {
                List<User> userList = repository.getUsers();
                for (User user:userList){
                    System.out.printf(
                            "id:%d login:%s balance:%d\n",
                            user.id,
                            user.getLogin(),
                            user.getBalance()
                    );
                }
                n=0;
            } else if (n==3) {
                System.out.print("login: ");
                String login = scanner.next();
                repository.deleteUser(login);
                n=0;
            }
            else if (n==4){
                n=0;
                System.out.print("login: ");
                String login = scanner.next();
                System.out.print("money: ");
                int money = scanner.nextInt();
                repository.deposit(money,login);
            }
            else if (n!=5){
                n=0;
            }
            else {
                repository.close();
                break;
            }
        }
    }

}