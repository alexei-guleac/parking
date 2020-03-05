package com.isd.parking;

import com.isd.parking.config.security.CustomPasswordEncoder;
import com.isd.parking.model.Group;
import com.isd.parking.model.User;
import com.isd.parking.repository.GroupRepository;
import com.isd.parking.service.ldap.UserService;
import com.isd.parking.utils.ColorConsoleOutput;
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

    private final UserService userLdapService;

    private final LdapContextSource contextSource;

    private final UserService userRepository;

    private final GroupRepository groupRepository;

    private final ColorConsoleOutput console;

    @Value("${ldap.partitionSuffix}")
    private String ldapPartitionSuffix;

    @Autowired
    public SpringLdapIntegrationTest(UserService userLdapService, LdapContextSource contextSource, UserService userRepository, GroupRepository groupRepository, ColorConsoleOutput console) {
        this.userLdapService = userLdapService;
        this.contextSource = contextSource;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.console = console;
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userLdapService.getAllUsers();
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
        User user = userLdapService.findUser("uid=user1,ou=people,dc=springframework,dc=org");
        assertNotNull(user);
        assertEquals(user.getFullName(), "Test A A");
    }

    @Test
    public void testCreateUser() {
        //userLdapService.create("test_user", new CustomPasswordEncoder().encode("qwerty123"));
        userLdapService.createUser(new User("test_user", "Test U", "U", new CustomPasswordEncoder(console).encode("qwerty123")));

        User user = userLdapService.findUser("uid=test_user,ou=people,dc=springframework,dc=org");
        assertNotNull(user);
        log.info("User found:" + user);
    }

    @Test
    public void testCreate() {
        userLdapService.create("test_user", new CustomPasswordEncoder(console).encode("qwerty123"));
        //userLdapService.createUser(new User("test_user", "Test U", "U", new CustomPasswordEncoder().encode("qwerty123")));

        User user = userLdapService.findUser("uid=test_user,ou=people,dc=springframework,dc=org");
        assertNotNull(user);
        log.info("User found:" + user);
    }

    @Test
    public void givenLdapClient_whenCorrectCredentials_thenSuccessfulLogin() {
        Boolean isValid = userLdapService.authenticate("user1", "aRduin1$");
        assertEquals(true, isValid);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userLdapService.findAll();
        assertNotNull(users);

        log.info("Users found:" + users);
        log.info("Users count:" + users.size());

        assertEquals(users.size(), 8);
    }

    @Test
    public void testSearchUser() {
        List<String> users = userLdapService.searchUser("user1");
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

    /*@Test
    public void testRandomPortWithValueAnnotation() throws LDAPException {
        EnvironmentTestUtils.addEnvironment(this.context, "spring.ldap.embedded.base-dn:dc=spring,dc=org");
        this.context.register(EmbeddedLdapAutoConfiguration.class, LdapClientConfiguration.class, PropertyPlaceholderAutoConfiguration.class);
        this.context.refresh();
        LDAPConnection connection = this.context.getBean(LDAPConnection.class);
        assertThat(connection.getConnectedPort()).isEqualTo(this.context.getEnvironment().getProperty("local.ldap.port", Integer.class));
    }*/

    @Test
    public void testLdapRepositories()  {
        log.info("Spring LDAP CRUD Operations Binding and Unbinding Example");

        log.info("- - - - - - Managing persons");

        List<User> persons = userRepository.findAll();
        log.info("persons: " + persons);

        User john = userRepository.findOne("john");
        john.setLastName("custom last name");
        userRepository.updateLastName(john);

        User jahn = userRepository.findOne("jahn");
        jahn.setLastName("custom last name");
        userRepository.update(jahn);

        User user = new User("uid", "user");
        userRepository.createLdap(user);

        User jihn = userRepository.findOne("jihn");
        userRepository.delete(jihn);

        persons = userRepository.findAll();
        log.info("persons: " + persons);

        log.info("- - - - - - Managing groups");

        List<Group> groups = groupRepository.findAll();
        log.info("groups: " + groups);

        groupRepository.removeMemberFromGroup("developers", jihn);

        groupRepository.addMemberToGroup("managers", jihn);

        groups = groupRepository.findAll();
        log.info("groups: " + groups);
    }
}
