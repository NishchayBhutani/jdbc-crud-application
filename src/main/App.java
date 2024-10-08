import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class App {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        try(Connection connection =
//                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbctest", "postgres", "admin")) {
                    dataSource.getConnection()) {
            System.out.println(connection.isValid(0));
//            String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/Users.sql")));
//            Statement statement = connection.createStatement();
//            statement.execute(sql);

            // CRUD

            // select
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
            ps.setString(1, "John");
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - " + resultSet.getString("name"));
            }

            // insert
            PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users(name) VALUES (?)");
            psInsert.setString(1, "John");
            int insertCount = psInsert.executeUpdate();
            System.out.println("insertCount = " + insertCount);

            // update
            PreparedStatement psUpdate = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?");
            psUpdate.setString(1, "Updated John");
            psUpdate.setInt(2, 1);
            int updateCount = psUpdate.executeUpdate();
            System.out.println("updateCount = " + updateCount);

            // delete
            PreparedStatement psDelete = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            psDelete.setInt(1, 1);
            int deleteCount = psDelete.executeUpdate();
            System.out.println("deleteCount = " + deleteCount);

        } catch (SQLException e) {
            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
        } finally {
            System.out.println("connection closed");
        }
    }

    private static DataSource createDataSource() {
        // create a hikari connection pool datasource
        // springboot uses hikaricp by default. It is transitively imported with the following Spring Boot starters:
        // spring-boot-starter-jdbc or spring-boot-starter-data-jpa.
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/jdbctest");
        ds.setUsername("postgres");
        ds.setPassword("admin");
        return ds;
    }
}