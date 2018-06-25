package com.blancco.ldapproto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Component
public class NativeJndiUserDiscoverer implements UserDiscoverer {
    private final String providerUrl;
    private final String securityPrincipal;
    private final String securityCredentials;
    private final String searchFilter;

    public NativeJndiUserDiscoverer(
            @Value("${provider.url}") String providerUrl,
            @Value("${security.principal}") String securityPrincipal,
            @Value("${security.credentials}") String securityCredentials,
            @Value("${search.filter}") String searchFilter) {
        this.providerUrl = providerUrl;
        this.securityPrincipal = securityPrincipal;
        this.securityCredentials = securityCredentials;
        this.searchFilter = searchFilter;
    }

    @Override
    public List<String> discover() throws NamingException {
        Hashtable<Object, Object> environment = new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, providerUrl);
        environment.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        environment.put(Context.SECURITY_CREDENTIALS, securityCredentials);
        InitialDirContext context = new InitialDirContext(environment);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> searchResults = context.search("", searchFilter, searchControls);
        try {
            List<String> results = new ArrayList<>();
            while (searchResults.hasMoreElements()) {
                SearchResult searchResult = searchResults.nextElement();
                results.add(searchResult.getAttributes().get("cn").get().toString());
            }
            return results;
        } finally {
            searchResults.close();
        }
    }
}
