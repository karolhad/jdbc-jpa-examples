package dao.jdbc;

import dto.Customer;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcCustomerDaoTest {

   private JdbcCustomerDao customerDao;

   @Before
   public void setUp() {
      customerDao = new JdbcCustomerDao();
   }

   @Test
   public void getCustomerById() throws SQLException {
      Customer customer = customerDao.get(1001);

      assertThat(customer).isNotNull();
   }

   @Test
   public void allCustomers() throws SQLException {
      List<Customer> customers = customerDao.findAll();

      assertThat(customers).isNotNull();

      print(customers);

      assertThat(customers.size()).isGreaterThanOrEqualTo(3);
   }

   @Test
   public void customersWithFirstNameJames() throws SQLException {
      List<Customer> customers = customerDao.findByFirstName("James");

      print(customers);

      assertThat(customers).hasSize(2);
   }

   @Test
   public void sqlInjection() throws SQLException {
      List<Customer> customers = customerDao.findByFirstName("James'; DELETE FROM customers; --'");

      print(customers);

      assertThat(customers).hasSize(0);
   }

   @Test
   public void allCustomersManyTimes() throws SQLException {
      for (int i = 0; i < 200; i++) {
         allCustomers();
      }
   }


   @Test
   public void addCustomer() throws SQLException {
      Integer id = customerDao.create("Jan", "Kowalski");

      assertThat(id).isNotNull();
   }

   private void print(List<Customer> allCustomers) {
      allCustomers.forEach(System.out::println);
   }

}
