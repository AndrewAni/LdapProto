package com.blancco.ldapproto;

import javax.naming.NamingException;
import java.util.List;

public interface UserDiscoverer {
    List<String> discover() throws NamingException;
}
