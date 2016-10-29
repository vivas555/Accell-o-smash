package ca.csf.tp3.config;

import ca.csf.tp3.CriticalRepositoryException;
import ca.csf.tp3.AcceleroSmashRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

//TODO : Ceci est une classe de configuration Spring. Content un lot de dépendances qui peuvent servir aux controlleurs ou à d'autres dépendances.
@Configuration
public class AppConfig {

    //TODO : Ceci est une dépendance. Elle est nécessaire au "AcceleroSmashController".
    @Bean
    public AcceleroSmashRepository getAcceleroSmashRepository() throws CriticalRepositoryException {
        File databasePath = new File("AcceleroSmashDB_Directory");
        return new AcceleroSmashRepository(databasePath.getAbsolutePath(), "AcceleroSmashDB", "admin", "rjfsw3rj8fsfjwi3rwj8fj892rjejfksfj89df9jr23fjseifsjf");
    }
}
