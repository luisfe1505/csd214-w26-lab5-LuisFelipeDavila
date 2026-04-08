package csd214.bookstore;

import csd214.bookstore.entities.ProductEntity;
import csd214.bookstore.repositories.*;
import csd214.bookstore.services.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Lab 5: System Boot & Architecture Configuration ===");
        System.out.println("1. Volatile RAM (ArrayList - Sequential): Best for tiny datasets.");
        System.out.println("2. Volatile RAM (HashMap - Indexed): Best for large-scale in-memory speed.");
        System.out.println("3. Integration Testing (H2 Database): Best for verifying SQL/JPA logic.");
        System.out.println("4. Production (MySQL Database): Persistent storage for real-world use.");
        System.out.print("Choice: ");

        int choice = sc.nextInt();
        IRepository<ProductEntity> repository = null;

        switch (choice) {
            case 1:
                repository = new InMemoryListRepository();
                break;
            case 2:
                repository = new InMemoryMapRepository();
                break;
            case 3:
                repository = new H2Repository();
                break;
            case 4:
                repository = new MySqlRepository();
                break;
            default:
                repository = new InMemoryListRepository();
                break;
        }

        MobileService mobileService = new MobileService(repository);
        BookstoreService bookstoreService = new BookstoreService(repository);

        App app = new App(repository, mobileService, bookstoreService);

        try {
            app.run();
        } finally {
            if (repository != null) {
                repository.close();
            }
        }
    }
}