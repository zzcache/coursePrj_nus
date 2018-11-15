package nus.day04.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import nus.day04.model.Customer;

@Stateless
public class CustomerBean {
    
    // Get a reference to the JDBC connection pool
    
    @Resource(lookup = "jdbc/sample")
    private DataSource sampleDs;
    
    public Optional<Customer> findByCsutomerId (Integer custId) 
         throws SQLException {
        
        try ( Connection conn = sampleDs.getConnection()) {
            // perform query
            PreparedStatement ps = conn.prepareStatement(
                    "select * from customer where customer_id = ?");
            ps.setInt(1, custId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                // return an empty optional
                return (Optional.empty());                
            }
            
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setAddressline1(rs.getString("addressline1"));
            customer.setAddressline2(rs.getString("addressline2"));
            customer.setCity(rs.getString("city"));
            customer.setState(rs.getString("state"));
            customer.setZip(rs.getString("zip"));
            customer.setPhone(rs.getString("phone"));
            customer.setEmail(rs.getString("email"));
            customer.setFax(rs.getString("fax"));
            customer.setCreditLimit(rs.getInt("credit_limit"));
            customer.setDiscountCode(rs.getString("discount_code"));
            
            return (Optional.of(customer));
            
        }
        
    }
}
