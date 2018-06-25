package com.blancco.ldapproto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class LdapProtoApplication implements ApplicationRunner {
    private final UserDiscoverer nativeJndiUserDiscoverer;
    private final UserDiscoverer ldapTemplateUserDiscoverer;

    @Autowired
    public LdapProtoApplication(UserDiscoverer nativeJndiUserDiscoverer, UserDiscoverer ldapTemplateUserDiscoverer) {
        this.nativeJndiUserDiscoverer = nativeJndiUserDiscoverer;
        this.ldapTemplateUserDiscoverer = ldapTemplateUserDiscoverer;
    }

    public static void main(String[] args) {
		SpringApplication.run(LdapProtoApplication.class, args);
	}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Native JNDI user discovery:");
        nativeJndiUserDiscoverer.discover().forEach(System.out::println);
        System.out.println("Spring LDAP template user discovery:");
        ldapTemplateUserDiscoverer.discover().forEach(System.out::println);
    }
}
