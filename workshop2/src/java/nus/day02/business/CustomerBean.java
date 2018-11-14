package nus.day02.business;

import java.util.Optional;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nus.day02.model.Customer;
import nus.day02.model.CustomerException;
import nus.day02.model.DiscountCode;

@Stateless
public class CustomerBean {
    
    @PersistenceContext private EntityManager em;
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Optional<Customer> findByCustomerId (Integer custId) {
       Customer c = em.find(Customer.class, custId);
       return (Optional.ofNullable(c));
    }
    
    public void addNewCustomer(Customer customer)  throws CustomerException {
        DiscountCode discountCode = em.find(DiscountCode.class, customer.getDiscountCode().getDiscountCode());
        System.out.print("info >> " + customer.toString());
        
        if (null == discountCode) {
            throw new CustomerException("Discount code not found");
        }
        
        customer.setDiscountCode(discountCode);
        em.persist(customer);
    }
}
