package csd214.bookstore.repositories;
import csd214.bookstore.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements IRepository<ProductEntity> {
    private List<ProductEntity> db = new ArrayList<>();
    private Long idCounter = 1L;

    @Override public ProductEntity save(ProductEntity e) {
        if (e.getId() == null) { e.setId(idCounter++); db.add(e); }
        return e;
    }
    @Override public ProductEntity findById(Long id) { return db.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null); }
    @Override public List<ProductEntity> findAll() { return new ArrayList<>(db); }
    @Override public void delete(Long id) { db.removeIf(p -> p.getId().equals(id)); }
    @Override public long count() { return db.size(); }
    @Override public int deleteAll() { int size = db.size(); db.clear(); return size; }
    @Override public String getDataSourceType() { return "VOLATILE RAM (ArrayList)"; }
    @Override public void close() {}
}