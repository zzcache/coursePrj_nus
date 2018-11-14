package nus.day02.model;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CustomerException  extends Exception{
    public CustomerException() {
        super();
    }
    
    public CustomerException(String msg) {
        super(msg);
    }
}
