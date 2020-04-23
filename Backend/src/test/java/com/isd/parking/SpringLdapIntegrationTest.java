package com.isd.parking;

import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import com.isd.parking.storage.ldap.UserLdapClient;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Slf4j
public class SpringLdapIntegrationTest {

    private final UserLdapClient userLdapService;

    private final LdapContextSource contextSource;

    private final UserLdapClient userRepository;

    // private final GroupRepository groupRepository;

    @Value("${ldap.partitionSuffix}")
    private String ldapPartitionSuffix;

    @Autowired
    public SpringLdapIntegrationTest(UserLdapClient userLdapService,
                                     LdapContextSource contextSource,
                                     UserLdapClient userRepository) {
        this.userLdapService = userLdapService;
        this.contextSource = contextSource;
        this.userRepository = userRepository;
    }

    @Test
    public void testGetAllUsers() {
        List<UserLdap> users = userLdapService.findAll();
        assertNotNull(users);

        log.info("Users found:" + users);
        log.info("Users count:" + users.size());

        assertEquals(users.size(), 8);
    }

    @Test
    public void testGetAllUsersNames() {
        List<String> users = userLdapService.getAllUserNames();
        assertNotNull(users);
        assertEquals(users.size(), 8);
    }

    @Test
    public void testFindPerson() {
        UserLdap user = userLdapService.findByDn("uid=user1,ou=people,dc=springframework,dc=org");
        assertNotNull(user);
        assertEquals(user.getCn(), "Test A A");
    }

    @Test
    public void testCreateUser() {
        //userLdapService.create("test_user", new CustomBcryptPasswordEncoder().encode("qwerty123"));
        userLdapService.createUser(new UserLdap("test_user", "Test U", "U", new CustomBcryptPasswordEncoder().encode("qwerty123")));

        UserLdap user = userLdapService.findByDn("uid=test_user,ou=people,dc=springframework,dc=org");
        assertNotNull(user);
        log.info("User found:" + user);
    }

    /*@Test
    public void testCreate() {
        userLdapService.create("test_user", new CustomBcryptPasswordEncoder(console).encode("qwerty123"));
        //userLdapService.createUser(new User("test_user", "Test U", "U", new CustomBcryptPasswordEncoder().encode("qwerty123")));

        User user = userLdapService.findByDn("uid=test_user,ou=people,dc=springframework,dc=org");
        assertNotNull(user);
        log.info("User found:" + user);
    }*/

    @Test
    public void givenLdapClient_whenCorrectCredentials_thenSuccessfulLogin() {
        Boolean isValid = userLdapService.authenticate("user1", "aRduin1$");
        assertEquals(true, isValid);
    }

    @Test
    public void testFindAllUsers() {
        List<UserLdap> users = userLdapService.findAll();
        assertNotNull(users);

        log.info("Users found:" + users);
        log.info("Users count:" + users.size());

        assertEquals(users.size(), 8);
    }

    @Test
    public void testSearchUser() {
        List<String> users = userLdapService.getUserNameById("user1");
        assertNotNull(users);
        assertEquals(users.get(0), "Test A");
    }

    @Test
    public void testBasicAuth() {
        RestTemplate rest = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity;
        ResponseEntity<String> response;
        String url = "http://localhost:8080/login";
        String csrfToken = "";
        System.out.println("1---------------------");
        try {
            rest.getMessageConverters()
                    .add(new StringHttpMessageConverter());
            headers.setContentType(
                    MediaType.APPLICATION_FORM_URLENCODED);

            entity = new HttpEntity<>("", headers);
            response = rest.exchange(url, HttpMethod.GET, entity,
                    String.class);
            System.out.println(response.getBody());
            Document doc = Jsoup.parse(Objects.requireNonNull(response.getBody()));
            Elements inputs = doc.getElementsByTag("input");
            for (Element input : inputs) {
                if (input.toString().contains("_csrf")) {
                    csrfToken = input.val();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("2---------------------");
        try {
            rest.getMessageConverters()
                    .add(new StringHttpMessageConverter());
            headers.setContentType(
                    MediaType.APPLICATION_FORM_URLENCODED);

            entity = new HttpEntity<>(
                    "username=abhijit&password=abcd123&_csrf="
                            + csrfToken,
                    headers);
            response = rest.exchange(url, HttpMethod.POST,
                    entity, String.class);
            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
