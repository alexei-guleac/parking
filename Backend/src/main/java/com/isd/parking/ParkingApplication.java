package com.isd.parking;

import com.isd.parking.repository.GroupRepository;
import com.isd.parking.service.ldap.UserService;
import com.isd.parking.utils.ColorConsoleOutput;
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


    private final ColorConsoleOutput console;

    @Autowired
    public ParkingApplication(LdapContextSource contextSource, UserService userRepository, GroupRepository groupRepository, ColorConsoleOutput console) {
        this.contextSource = contextSource;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.console = console;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
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
