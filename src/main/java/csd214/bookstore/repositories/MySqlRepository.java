package csd214.bookstore.repositories;

public class MySqlRepository extends JpaRepository {
    public MySqlRepository() {
        super("mysql-pu", "MySQL (Production)"); }
}

