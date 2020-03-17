package com.isd.parking;

import com.isd.parking.repository.GroupRepository;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.storage.util.DataSaver;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.isd.parking.*"})
@Profile("default")
@EnableJpaRepositories
@EnableScheduling
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:postgresql-config.properties"),
})
@Slf4j
public class ParkingApplication {

    private final LdapContextSource contextSource;

    private final UserLdapClient userRepository;
    private final GroupRepository groupRepository;

    private final DataSaver shutdownHandler;

    private final ColorConsoleOutput console;

    @Autowired
    public ParkingApplication(LdapContextSource contextSource, UserLdapClient userRepository, GroupRepository groupRepository, DataSaver shutdownHandler, ColorConsoleOutput console) {
        this.contextSource = contextSource;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.shutdownHandler = shutdownHandler;
        this.console = console;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ParkingApplication.class);
        application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
        ConfigurableApplicationContext context = application.run(args);
        log.info(String.valueOf(context));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // write here any instructions that should be executed
            System.out.println("Application shutdown...");
        }));
    }

    //get all beans names
    /*@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }*/

    /*@PostConstruct
    public void setup(){
        log.info("Spring LDAP CRUD Operations Binding and Unbinding Example");

        log.info("- - - - - - Managing persons");

        List<User> persons = userRepository.findAll();
        log.info("persons: " + persons);

        User john = userRepository.findById("john");
        john.setLastname("custom last name");
        userRepository.updateLastName(john);

        User jahn = userRepository.findById("jahn");
        jahn.setLastname("custom last name");
        userRepository.update(jahn);

        User user = new User("uid", "user");
        userRepository.createLdap(user);

        User jihn = userRepository.findById("jihn");
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
