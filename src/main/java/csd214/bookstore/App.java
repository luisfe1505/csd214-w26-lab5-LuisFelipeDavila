package csd214.bookstore;

import com.github.javafaker.Faker;
import csd214.bookstore.entities.*;
import csd214.bookstore.pojos.*;
import csd214.bookstore.repositories.IRepository;
import csd214.bookstore.services.BookstoreService;

import java.util.List;
import java.util.Scanner;

public class App {
    private IRepository<ProductEntity> repository;
    private BookstoreService service; // The Chef

    // INJECTION: App doesn't use the 'new' keyword for repos anymore
    public App(IRepository<ProductEntity> repository) {
        this.repository = repository;
        this.service = new BookstoreService(repository);
    }
    // UI & Logic
    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);

    public void shutdown() {
        if (repository != null) {
            repository.close();
        }
    }

    public void run() {
        System.out.println("Running on: " + repository.getDataSourceType());
        // Use the new count() method from our in-class exercise
        if (repository.count() == 0) {
            populate();
        }

        int choice = 0;
        while (choice != 99) {
            System.out.println("\n***********************");
            System.out.println("      JPA STORE        ");
            System.out.println("***********************");
            System.out.println(" 1. Add Items");
            System.out.println(" 2. Edit Items");
            System.out.println(" 3. Delete Items");
            System.out.println(" 4. Sell item(s)");
            System.out.println(" 5. List items");
            System.out.println(" 6. System Reset (Wipe DB)");
            System.out.println("99. Quit");
            System.out.println("***********************");
            System.out.print("Enter choice: ");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                choice = 0;
            }

            switch (choice) {
                case 1: addItem(); break;
                case 2: editItem(); break;
                case 3: deleteItem(); break;
                case 4: sellItem(); break;
                case 5: listAny(); break;
                case 6: systemReset(); break; // New Bulk Delete Feature
                case 99: System.out.println("Goodbye."); break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // 1. ADD ITEMS
    // ==========================================
    public void addItem() {
        System.out.println("\n--- Add an item ---");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. DiscMag");
        System.out.println("4. Ticket");
        System.out.println("5. Pen");
        System.out.println("6. Notebook");
        System.out.println("99. Back");

        int choice = getIntInput();
        if (choice == 99) return;

        try {
            switch(choice) {
                case 1:
                    Book bPojo = new Book();
                    bPojo.initialize(input);
                    BookEntity bEnt = new BookEntity();
                    bEnt.setTitle(bPojo.getTitle());
                    bEnt.setPrice(bPojo.getPrice());
                    bEnt.setCopies(bPojo.getCopies());
                    bEnt.setAuthor(bPojo.getAuthor());
                    repository.save(bEnt); // Clean Repository Save
                    break;
                case 2:
                    Magazine mPojo = new Magazine();
                    mPojo.initialize(input);
                    MagazineEntity mEnt = new MagazineEntity();
                    mEnt.setTitle(mPojo.getTitle());
                    mEnt.setPrice(mPojo.getPrice());
                    mEnt.setCopies(mPojo.getCopies());
                    mEnt.setOrderQty(mPojo.getOrderQty());
                    mEnt.setCurrentIssue(mPojo.getCurrentIssue());
                    repository.save(mEnt);
                    break;
                case 3:
                    DiscMag dPojo = new DiscMag();
                    dPojo.initialize(input);
                    DiscMagEntity dEnt = new DiscMagEntity();
                    dEnt.setTitle(dPojo.getTitle());
                    dEnt.setPrice(dPojo.getPrice());
                    dEnt.setCopies(dPojo.getCopies());
                    dEnt.setOrderQty(dPojo.getOrderQty());
                    dEnt.setCurrentIssue(dPojo.getCurrentIssue());
                    dEnt.setHasDisc(dPojo.isHasDisc());
                    repository.save(dEnt);
                    break;
                case 4:
                    Ticket tPojo = new Ticket();
                    tPojo.initialize(input);
                    TicketEntity tEnt = new TicketEntity();
                    tEnt.setDescription(tPojo.getDescription());
                    tEnt.setPrice(tPojo.getPrice());
                    tEnt.setName("Ticket: " + tPojo.getDescription());
                    repository.save(tEnt);
                    break;
                case 5:
                    Pen pPojo = new Pen();
                    pPojo.initialize(input);
                    PenEntity pEnt = new PenEntity();
                    pEnt.setBrand(pPojo.getBrand());
                    pEnt.setPrice(pPojo.getPrice());
                    pEnt.setColor(pPojo.getColor());
                    pEnt.setName(pPojo.getColor() + " " + pPojo.getBrand() + " Pen");
                    repository.save(pEnt);
                    break;
                case 6:
                    Notebook nPojo = new Notebook();
                    nPojo.initialize(input);
                    NotebookEntity nEnt = new NotebookEntity();
                    nEnt.setBrand(nPojo.getBrand());
                    nEnt.setPrice(nPojo.getPrice());
                    nEnt.setPageCount(nPojo.getPageCount());
                    nEnt.setName(nPojo.getPageCount() + "pg " + nPojo.getBrand() + " Notebook");
                    repository.save(nEnt);
                    break;
                default:
                    System.out.println("Invalid type.");
            }
            System.out.println("Item saved to Database via Repository!");

        } catch (Exception e) {
            System.out.println("Error saving item: " + e.getMessage());
        }
    }

    // ==========================================
    // 2. LIST ITEMS
    // ==========================================
    public void listAny() {
        List<ProductEntity> results = repository.findAll();
        System.out.println("\n--- Inventory List (" + results.size() + ") ---");
        for (int i = 0; i < results.size(); i++) {
            System.out.println("[" + i + "] " + results.get(i));
        }
    }

    // ==========================================
    // 3. EDIT ITEMS
    // ==========================================
    public void editItem() {
        List<ProductEntity> results = repository.findAll();
        if (results.isEmpty()) {
            System.out.println("No items to edit.");
            return;
        }

        listAny();
        System.out.println("Select index to edit:");
        int idx = getIntInput();

        if (idx < 0 || idx >= results.size()) return;

        ProductEntity entity = results.get(idx);

        try {
            // Manual Mapping: Entity -> POJO to use 'edit()' method
            if (entity instanceof BookEntity) {
                BookEntity be = (BookEntity) entity;
                Book pojo = new Book(be.getAuthor(), be.getTitle(), be.getPrice(), be.getCopies());
                pojo.edit(input);

                be.setAuthor(pojo.getAuthor());
                be.setTitle(pojo.getTitle());
                be.setPrice(pojo.getPrice());
                be.setCopies(pojo.getCopies());
            }
            else if (entity instanceof PenEntity) {
                PenEntity pe = (PenEntity) entity;
                Pen pojo = new Pen(pe.getBrand(), pe.getPrice(), pe.getColor());
                pojo.edit(input);
                pe.setBrand(pojo.getBrand());
                pe.setPrice(pojo.getPrice());
                pe.setColor(pojo.getColor());
            }
            else {
                System.out.println("Editing not fully implemented for this type in this demo.");
                return;
            }

            repository.save(entity); // Automatically performs UPDATE (Merge)
            System.out.println("Update successful.");

        } catch (Exception e) {
            System.out.println("Error updating item.");
        }
    }

    // ==========================================
    // 4. DELETE ITEMS
    // ==========================================
    public void deleteItem() {
        List<ProductEntity> results = repository.findAll();
        listAny();
        System.out.println("Select index to delete:");
        int idx = getIntInput();

        if (idx < 0 || idx >= results.size()) return;

        ProductEntity toRemove = results.get(idx);

        repository.delete(toRemove.getId());
        System.out.println("Deleted.");
    }

    // ==========================================
    // 5. SELL ITEMS
    // ==========================================
// Inside src/main/java/csd214/bookstore/App.java

    public void sellItem() {
        // 1. Fetch current list to show the user
        List<ProductEntity> results = repository.findAll();
        if (results.isEmpty()) {
            System.out.println("Inventory is empty. Nothing to sell.");
            return;
        }

        // 2. Display the list so the user can choose an index
        listAny();
        System.out.print("Select index to sell: ");
        int idx = getIntInput();

        // 3. Validate the choice
        if (idx < 0 || idx >= results.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        // 4. Get the Database ID of the selected item
        ProductEntity item = results.get(idx);
        Long dbId = item.getId();

        // 5. DELEGATION: Pass the ID to the Service (The Chef)
        // The App doesn't care HOW the sale happens, it just tells the service to do it.
        service.performSale(dbId);

        // 6. Update the UI-side Cash Till
        // We create a temporary SaleableItem wrapper to pass the price to the Till
        cashTill.sellItem(new SaleableItem() {
            @Override
            public void sellItem() { /* Logic already handled by service */ }
            @Override
            public double getPrice() { return item.getPrice(); }
        });

        System.out.println("Transaction complete.");
    }

    // ==========================================
    // 6. SYSTEM RESET (Bulk Delete Exercise)
    // ==========================================
    public void systemReset() {
        System.out.println("\n--- Initiating System Reset ---");
        int deletedCount = repository.deleteAll();
        System.out.println("Success! " + deletedCount + " items were permanently destroyed.");
    }

    // ==========================================
    // POPULATE (Faker -> Entities)
    // ==========================================
    public void populate() {
        System.out.println("Populating Database with Faker...");
        Faker faker = new Faker();

        for (int i = 0; i < 3; i++) {
            // Book
            BookEntity b = new BookEntity();
            b.setAuthor(faker.book().author());
            b.setTitle(faker.book().title());
            b.setPrice(faker.number().randomDouble(2, 10, 50));
            b.setCopies(faker.number().numberBetween(1, 20));
            repository.save(b);

            // Ticket
            TicketEntity t = new TicketEntity();
            String band = faker.rockBand().name();
            t.setDescription("Concert: " + band);
            t.setPrice(faker.number().randomDouble(2, 50, 150));
            t.setName("Ticket: " + band);
            repository.save(t);

            // Pen
            PenEntity p = new PenEntity();
            p.setBrand(faker.company().name());
            p.setColor(faker.color().name());
            p.setPrice(faker.number().randomDouble(2, 1, 5));
            p.setName("Pen " + p.getBrand());
            repository.save(p);
        }
        System.out.println("Database Ready.");
    }

    private int getIntInput() {
        try {
            String line = input.nextLine();
            if (line.trim().isEmpty()) return -1;
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}