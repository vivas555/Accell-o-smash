package ca.csf.tp3.controllers;

import ca.csf.tp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO : Ceci est un controlleur REST. C'est le point d'entrée d'un service web.
@RestController
//TODO : L'annotation "ResquestMapping" sert à définir l'URL memant vers la classe. Ici, toutes les urls commencent par "/samples"é
@RequestMapping("/database")
public class AcceleroSmashController {

    private AcceleroSmashRepository acceleroSmashRepository;

    @Autowired
    public AcceleroSmashController(AcceleroSmashRepository acceleroSmashRepository) {
        this.acceleroSmashRepository = acceleroSmashRepository;
    }

    @RequestMapping(value = "/accounts/", method = RequestMethod.PUT)
    public String addNewAccount(@RequestBody Account account) throws CriticalRepositoryException {
        return acceleroSmashRepository.createAccount(account);
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public String getAccountById(@PathVariable("id") String id) throws CriticalRepositoryException, NotFoundRepositoryException {
        return acceleroSmashRepository.retrieveAccount(id);
    }

    @RequestMapping(value = "/collisions/", method = RequestMethod.PUT)
    public String addNewCollision(@RequestBody Collision collision) throws CriticalRepositoryException {
        return acceleroSmashRepository.createCollision(collision);
    }

    @RequestMapping(value = "/collisions/{id}", method = RequestMethod.GET)
    public String getCollisionByUser(@PathVariable("id") String user) throws CriticalRepositoryException, NotFoundRepositoryException {
        return acceleroSmashRepository.retrieveCollisionFromUser(user);
    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public List<Sample> getAllSamples() throws CriticalRepositoryException {
//        return acceleroSmashRepository.retrieveAll();
//    }

//    @RequestMapping(value = "/", method = RequestMethod.POST)
//    public Sample updateExistingSample(@RequestBody Sample sample) throws CriticalRepositoryException, NotFoundRepositoryException {
//        acceleroSmashRepository.update(sample);
//        return sample;
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public void deleteExistingSample(@PathVariable("id") int id) throws CriticalRepositoryException, NotFoundRepositoryException {
//        acceleroSmashRepository.delete(id);
//    }

    @ExceptionHandler(NotFoundRepositoryException.class)
   // @ResponseStatus(HttpStatus.NOT_FOUND)
    public void onNotFound(NotFoundRepositoryException e) {
        //TODO : Ici, on attrape les expcetion du modèle et on les gère. Dans ce cas, le code de retour est "404" et on imprime l'exception dans la console.
        e.printStackTrace();
    }

    @ExceptionHandler(CriticalRepositoryException.class)
   // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void onCriticalError(CriticalRepositoryException e) {
        //TODO : Normalement, on devrait jamais arriver ici, mais si cela arrive, alors on lance imprime la stack trace
        e.printStackTrace();
    }

}
