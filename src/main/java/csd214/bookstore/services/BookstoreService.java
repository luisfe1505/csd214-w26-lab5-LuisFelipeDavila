package csd214.bookstore.services;

import csd214.bookstore.entities.ProductEntity;
import csd214.bookstore.entities.PublicationEntity;
import csd214.bookstore.repositories.IRepository;

public class BookstoreService {
    private IRepository<ProductEntity> repository;

    // Dependency Injection via Constructor
    public BookstoreService(IRepository<ProductEntity> repository) {
        this.repository = repository;
    }

    public void performSale(Long id) {
        ProductEntity item = repository.findById(id);
        if (item == null) return;

        if (item instanceof PublicationEntity pub) {
            if (pub.getCopies() > 0) {
                pub.setCopies(pub.getCopies() - 1);
                repository.save(pub);
                System.out.println("Sold 1 copy of: " + pub.getTitle());
            } else {
                System.out.println("Out of stock!");
            }
        } else {
            System.out.println("Sold: " + item.getName());
        }
    }
}