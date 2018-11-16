
package nus.day05.business;

import java.util.Optional;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import nus.day05.model.Customer;

@Stateless
public class CustomerBean {
 /*   
    @Resource(lookup = "jdbc/sample")
    private DataSource sampleDS;
 */
    
    @PersistenceContext private EntityManager em;
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)    
    public Optional<Customer> findByCustomerId(Integer custId) {
        Customer customer = em.find(Customer.class, custId);
        return (Optional.ofNullable(customer));
    }
}
