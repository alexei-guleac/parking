package com.isd.parking.security.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@Slf4j
public class LdapBeans {

    private final LdapContextSource contextSource;

    @Autowired
    public LdapBeans(LdapContextSource contextSource) {
        this.contextSource = contextSource;
    }

    @Bean
    LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {

        /*
          Specificity here : we don't get the Role by reading the members of available groups (which is implemented by
          default in Spring security LDAP), but we retrieve the groups from the field memberOf of the user.
         */
        class MyLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

            private final String[] GROUP_ATTRIBUTE = {"cn"};
            private final String GROUP_MEMBER_OF = "memberOf";
            private SpringSecurityLdapTemplate ldapTemplate;

            MyLdapAuthoritiesPopulator(ContextSource contextSource) {
                ldapTemplate = new SpringSecurityLdapTemplate(contextSource);
            }

            @Override
            public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {

                String roles = "";
                String[] groupDns = userData.getStringAttributes(GROUP_MEMBER_OF);
                log.info(String.valueOf(userData));
                log.info(Arrays.toString(groupDns));

                // if user entry contains memberOf attribute
                if (!(groupDns == null || groupDns.length == 0)) {
                    roles = Stream.of(groupDns).map(groupDn -> {
                        LdapName groupLdapName = (LdapName) ldapTemplate.retrieveEntry(groupDn, GROUP_ATTRIBUTE).getDn();
                        // split DN in its different components et get only the last one (cn = my_group)
                        // getValue() allows to only get get the value of the pair (cn => my_group)
                        return groupLdapName.getRdns().stream().map(Rdn::getValue).reduce((a, b) -> b).orElse(null);
                    }).map(x -> (String) x).collect(Collectors.joining(","));
                }
                return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
            }
        }

        return new MyLdapAuthoritiesPopulator(contextSource);
    }
}
