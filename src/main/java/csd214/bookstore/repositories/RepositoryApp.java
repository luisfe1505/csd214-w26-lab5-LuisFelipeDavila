package csd214.bookstore.repositories;

import csd214.bookstore.entities.*;
import java.util.Date;
import java.util.List;

public class RepositoryApp {
    public static void main(String[] args) {
        System.out.println("Repository Pattern Demo (Lecture 7 Architecture)");

        // 1. Instantiate a concrete Repository implementation.
        // In Step 04, we use MySqlRepository or H2Repository.
        IRepository<ProductEntity> repo = new MySqlRepository();

        try {
            // Demonstrate the new DataSource abstraction
            System.out.println("Connected to: " + repo.getDataSourceType());

            // 2. Create Items
            System.out.println("\n--- Saving Items ---");
            repo.save(new BookEntity("Robert C. Martin", "Clean Code", 45.00, 10));
            repo.save(new TicketEntity("Java Conference", 299.99));

            // Note: Constructor order must match the entity class:
            // title, price, copies, orderQty, date, hasDisc
            repo.save(new DiscMagEntity("Retro Gamer", 15.00, 20, 100, new Date(), true));

            // 3. List All (Polymorphic)
            System.out.println("\n--- All Items ---");
            List<ProductEntity> items = repo.findAll();
            for (ProductEntity p : items) {
                System.out.println(p);
            }

            // 4. Update
            if (!items.isEmpty()) {
                ProductEntity first = items.get(0);
                System.out.println("\n--- Updating Item ID: " + first.getId() + " ---");

                if (first instanceof TicketEntity) {
                    ((TicketEntity) first).setPrice(10.00);
                } else if (first instanceof PublicationEntity) {
                    ((PublicationEntity) first).setPrice(999.99);
                }

                // The Repository figures out this is an UPDATE because ID is not null
                repo.save(first);
                System.out.println("Updated: " + repo.findById(first.getId()));
            }

            // 5. Demonstrate the new count feature
            System.out.println("\n--- Database Statistics ---");
            System.out.println("Total items in inventory: " + repo.count());

            // 6. Delete Demo
            if (items.size() > 1) {
                Long idToDelete = items.get(1).getId();
                System.out.println("\n--- Deleting Item ID: " + idToDelete + " ---");
                repo.delete(idToDelete);
            }

            System.out.println("\nFinal count: " + repo.count());

        } finally {
            // Critical: Always close the repository to release DB connections
            repo.close();
        }
    }
}