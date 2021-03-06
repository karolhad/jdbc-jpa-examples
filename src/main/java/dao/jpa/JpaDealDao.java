package dao.jpa;

import dao.DealDao;
import dto.Deal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

class JpaDealDao implements DealDao {

   private EntityManager entityManager;

   JpaDealDao(EntityManager entityManager) {
      this.entityManager = entityManager;
   }

   public List<Deal> find(String customerLastName) {
      TypedQuery<Deal> query = entityManager.createQuery("select d from Deal d  where d.account.customer.lastName = :lastName", Deal.class);

      query.setParameter("lastName", customerLastName);

      return query.getResultList();
   }

   @Override
   public List<Deal> find(String instrumentName, String customerLastName, String accountName) {
      final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      final CriteriaQuery<Deal> query = builder.createQuery(Deal.class);

      final Root<Deal> deal = query.from(Deal.class);

      List<Predicate> criteria = new ArrayList<>();
      if (customerLastName != null) {
         criteria.add(builder.equal(deal
               .get("account")
               .get("customer")
               .get("lastName"), customerLastName));
      }
      if (instrumentName != null) {
         criteria.add(builder.equal(deal
               .get("instrument")
               .get("name"), instrumentName));
      }
      if (accountName != null) {
         criteria.add(builder.equal(deal
               .get("account")
               .get("name"), accountName));
      }

      if (!criteria.isEmpty()) {
         query
               .select(deal)
               .
                     where(builder.and(
                           criteria
                                 .stream()
                                 .toArray(Predicate[]::new))
                     );
      }

      return entityManager
            .createQuery(query)
            .getResultList();

   }

   public Deal get(int id) {
      return entityManager.find(Deal.class, id);
   }
}
