package csd214.bookstore;

import com.github.javafaker.Faker;
import csd214.bookstore.entities.*;
import csd214.bookstore.pojos.*;
import csd214.bookstore.repositories.IRepository;
import csd214.bookstore.services.BookstoreService;
import csd214.bookstore.services.MobileService;

import java.util.List;
import java.util.Scanner;

public class App {
    // Database Infrastructure
    private IRepository<ProductEntity> repository;
    private MobileService mobileService;
    private BookstoreService bookstoreService;

    // UI & Logic
    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);

    public App(IRepository<ProductEntity> repository, MobileService mobileService, BookstoreService bookstoreService) {
        this.repository = repository;
        this.mobileService = mobileService;
        this.bookstoreService = bookstoreService;
    }

    public void shutdown() {
    }

    public void run() {
        // Optional: Populate only if empty
        long count = repository.count();
        if (count == 0) {
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
                case 99: System.out.println("Goodbye."); break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // 1. ADD ITEMS (POJO Input -> Entity Save)
    // ==========================================
    public void addItem() {
        System.out.println("\n--- Add an item ---");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. DiscMag");
        System.out.println("4. Ticket");
        System.out.println("5. Pen");
        System.out.println("6. Notebook");
        System.out.println("7. Iphone");
        System.out.println("99. Back");

        int choice = getIntInput();
        if (choice == 99) return;

        try {
            switch(choice) {
                case 1:
                    Book bPojo = new Book();
                    bPojo.initialize(input); // Use POJO for input
                    // Map to Entity
                    BookEntity bEnt = new BookEntity();
                    bEnt.setTitle(bPojo.getTitle());
                    bEnt.setPrice(bPojo.getPrice());
                    bEnt.setCopies(bPojo.getCopies());
                    bEnt.setAuthor(bPojo.getAuthor());
                    repository.save(bEnt);
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
                case 7:
                    Iphone iPojo = new Iphone();
                    iPojo.initialize(input); // Use POJO for input
                    // Map to Entity
                    IphoneEntity iEnt = new IphoneEntity(
                            iPojo.getName(),
                            iPojo.getPrice(),
                            iPojo.getColor(),
                            iPojo.getStorage(),
                            iPojo.isHasMagSafe()
                    );
                    repository.save(iEnt);
                    break;
                default:
                    System.out.println("Invalid type.");
            }

            System.out.println("Item saved to Database!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 2. LIST ITEMS (Query Entities)
    // ==========================================
    public void listAny() {
        // Query database for all products
        List<ProductEntity> results = repository.findAll();

        System.out.println("\n--- Inventory List (" + results.size() + ") ---");
        for (int i = 0; i < results.size(); i++) {
            System.out.println("[" + i + "] " + results.get(i));
        }
    }

    // ==========================================
    // 3. EDIT ITEMS (Entity -> POJO -> Entity)
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
                pojo.edit(input); // User interaction
                // Map Back
                be.setAuthor(pojo.getAuthor());
                be.setTitle(pojo.getTitle());
                be.setPrice(pojo.getPrice());
                be.setCopies(pojo.getCopies());
                repository.save(be);
            }
            else if (entity instanceof PenEntity) {
                PenEntity pe = (PenEntity) entity;
                Pen pojo = new Pen(pe.getBrand(), pe.getPrice(), pe.getColor());
                pojo.edit(input);
                // Map Back
                pe.setBrand(pojo.getBrand());
                pe.setPrice(pojo.getPrice());
                pe.setColor(pojo.getColor());
                repository.save(pe);
            }
            else if (entity instanceof IphoneEntity) {
                IphoneEntity ie = (IphoneEntity) entity;
                Iphone pojo = new Iphone(ie.getProductId(), ie.getName(), ie.getPrice(), ie.getColor(), ie.getStorage(), ie.isHasMagSafe());
                pojo.edit(input); // User interaction
                // Map Back
                ie.setName(pojo.getName());
                ie.setPrice(pojo.getPrice());
                ie.setColor(pojo.getColor());
                ie.setStorage(pojo.getStorage());
                ie.setHasMagSafe(pojo.isHasMagSafe());
                repository.save(ie);
            }
            // ... (Other types would follow similar pattern) ...
            else {
                System.out.println("Editing not fully implemented for this type in this demo.");
            }

            System.out.println("Update successful.");

        } catch (Exception e) {
            e.printStackTrace();
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
    public void sellItem() {
        List<ProductEntity> results = repository.findAll();
        listAny();
        System.out.println("Select index to sell:");
        int idx = getIntInput();
        if (idx < 0 || idx >= results.size()) return;

        ProductEntity item = results.get(idx);

        // Business Logic
        if (item instanceof PublicationEntity) {
            PublicationEntity pub = (PublicationEntity) item;
            if (pub.getCopies() > 0) {
                pub.setCopies(pub.getCopies() - 1);
                System.out.println("Sold: " + pub.getTitle());
                // Add to transient cash till
                cashTill.sellItem(new SaleableItem() {
                    public void sellItem() {}
                    public double getPrice() { return pub.getPrice(); }
                });
                repository.save(pub);
            } else {
                System.out.println("Out of stock!");
            }
        } else {
            System.out.println("Sold " + item.getName());
            cashTill.sellItem(new SaleableItem() {
                public void sellItem() {}
                public double getPrice() { return item.getPrice(); }
            });
            repository.save(item);
        }
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