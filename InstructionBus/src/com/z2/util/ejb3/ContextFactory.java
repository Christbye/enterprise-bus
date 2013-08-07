package com.z2.util.ejb3;

import javax.naming.Context;
import javax.naming.NamingException;

public interface ContextFactory {
    public Context createContext() throws NamingException;
}
