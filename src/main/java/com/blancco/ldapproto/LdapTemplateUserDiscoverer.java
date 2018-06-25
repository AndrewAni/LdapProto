package com.blancco.ldapproto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextSource;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.List;

@Component
public class LdapTemplateUserDiscoverer implements UserDiscoverer {
    private final String providerUrl;
    private final String securityPrincipal;
    private final String securityCredentials;
    private final String searchFilter;

    public LdapTemplateUserDiscoverer(
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
        DirContextSource contextSource = new DirContextSource();
        contextSource.setUrl(providerUrl);
        contextSource.setPassword(securityCredentials);
        contextSource.setUserDn(securityPrincipal);
        contextSource.afterPropertiesSet();

        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setIgnorePartialResultException(true);
        return ldapTemplate.search(
                "",
                searchFilter,
                SearchControls.SUBTREE_SCOPE,
                (AttributesMapper<String>) a -> a.get("cn").get().toString());
    }
}
