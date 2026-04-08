package csd214.bookstore.services;

import csd214.bookstore.entities.ProductEntity;
import csd214.bookstore.entities.MobileEntity;
import csd214.bookstore.repositories.IRepository;

public class MobileService {

    private IRepository<ProductEntity> repository;

    public MobileService(IRepository<ProductEntity> repository) {
        this.repository = repository;
    }

    public void upgradePhone(Long id, int extraGb, double extraCost) {
        ProductEntity item = repository.findById(id);

        if (item != null && item instanceof MobileEntity) {
            MobileEntity phone = (MobileEntity) item;

            phone.setStorage(phone.getStorage() + extraGb);
            phone.setPrice(phone.getPrice() + extraCost);

            repository.save(phone);
        }
    }
}