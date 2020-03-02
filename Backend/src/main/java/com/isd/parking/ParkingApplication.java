package com.isd.parking;

import com.isd.parking.config.security.CustomPasswordEncoder;
import com.isd.parking.repository.GroupRepository;
import com.isd.parking.service.ldap.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.isd.parking.utils.ColorConsole.*;

@SpringBootApplication
@ComponentScan(basePackages = {"com.isd.parking.*"})
@Profile("default")
@EnableJpaRepositories
@EnableScheduling
@PropertySource("classpath:application.properties")
@Slf4j
public class ParkingApplication {

    private final LdapContextSource contextSource;
 
    private final UserService userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public ParkingApplication(LdapContextSource contextSource, UserService userRepository, GroupRepository groupRepository) {
        this.contextSource = contextSource;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);

        System.out.println(RED + "This text is red!" + RESET);
        System.out.println(new CustomPasswordEncoder().getH("aRduin1$"));

        System.out.println(GREEN_BACKGROUND + "This text has a green background but default text!" + RESET);
        System.out.println(RED + "This text has red text but a default background!" + RESET);
        System.out.println(RED + GREEN_BACKGROUND + "This text has a green background and red text!" + RESET);
    }

    /*@PostConstruct
    public void setup(){
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

        System.exit(-1);
    }*/
}
