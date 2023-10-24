package org.example;

import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.ProductDbRepository;
import org.example.repositories.UserDbRepository;

import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Shop {
    public static void main(String[] args){
        ProductDbRepository productDbRepository = new ProductDbRepository();
        UserDbRepository userDbRepository = new UserDbRepository();
        Scanner scanner = new Scanner(System.in);
        System.out.print("login: ");
        String login = scanner.next();
        User user = userDbRepository.getUser(login);
        if (user!=null){
            int n = 0;
            while (true) {
                System.out.print("1.add new product\n2.get products\n3.buy product\n4.remove product\n5.check balance\n6.quit\n");
                n = scanner.nextInt();
                if (n==1){
                    System.out.print("name: ");
                    String name = scanner.next();
                    System.out.print("manufacturer: ");
                    String manufacturer = scanner.next();
                    System.out.print("count: ");
                    int count = scanner.nextInt();
                    System.out.print("price: ");
                    int price = scanner.nextInt();
                    productDbRepository.addProduct(new Product(name,manufacturer,count,price));
                } else if (n==2) {
                    List<Product>productList = productDbRepository.getProducts();
                    for (Product product:productList){
                        System.out.printf(
                                "id: %d name %s manufacturer %s count %d price %d ¥\n",
                                product.id,
                                product.getName(),
                                product.getManufacturer(),
                                product.getCount(),
                                product.getPrice()
                        );
                    }

                    n=0;
                } else if (n==3) {
                    System.out.print("name: ");
                    String name = scanner.next();
                    System.out.print("manufacturer: ");
                    String manufacturer = scanner.next();
                    Product product =productDbRepository.getProduct(name,manufacturer);
                    if (product!=null){
                        productDbRepository.buyProduct(product,user);
                    }
                    else{
                        System.out.println("Product not found");
                    }
                    n=0;
                }
                else if (n==4){
                    System.out.print("name: ");
                    String name = scanner.next();
                    System.out.print("manufacturer: ");
                    String manufacturer = scanner.next();
                    Product product =productDbRepository.getProduct(name,manufacturer);
                    productDbRepository.removeProduct(product);
                    n=0;
                }
                else if (n==5){
                    n=0;
                    System.out.printf("%s balance: %d\n",user.getLogin(),user.getBalance());
                }
                else if (n!=6){
                    n=0;
                }
                else{
                    productDbRepository.close();
                    userDbRepository.close();
                    break;
                }
            }
        }
        else {
            System.out.println("User not found");
        }
    }
}
