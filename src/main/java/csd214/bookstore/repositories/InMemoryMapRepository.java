package csd214.bookstore.repositories;

import csd214.bookstore.entities.ProductEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMapRepository implements IRepository<ProductEntity> {
    private Map<Long, ProductEntity> map = new HashMap<>();
    private Long idCounter = 1L;

    @Override public ProductEntity save(ProductEntity entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter++);
        }
        map.put(entity.getId(), entity);
        return entity;
    }

    @Override public ProductEntity findById(Long id) {
        return map.get(id);
    }

    @Override public List<ProductEntity> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override public void delete(Long id) {
        map.remove(id);
    }

    @Override public long count() {
        return map.size();
    }

    @Override public int deleteAll() {
        int size = map.size();
        map.clear();
        return size;
    }

    @Override public String getDataSourceType() {
        return "VOLATILE RAM (HashMap - Indexed Search)";
    }

    @Override public void close() {}
}