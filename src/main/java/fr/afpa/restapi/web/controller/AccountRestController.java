package fr.afpa.restapi.web.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.afpa.restapi.AccountNotFoundException;
import fr.afpa.restapi.dao.AccountDao;
import fr.afpa.restapi.model.Account;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TODO ajouter la/les annotations nécessaires pour faire de
 * "AccountRestController" un contrôleur de REST API
 */
@RestController
public class AccountRestController {
  
    private final AccountDao accountDao;

    @Autowired
    AccountRestController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * TODO implémenter un constructeur
     * 
     * TODO injecter {@link AccountDao} en dépendance par injection via constructeur
     * Plus d'informations ->
     * https://keyboardplaying.fr/blogue/2021/01/spring-injection-constructeur/
     */

   
    

    /**
     * TODO implémenter une méthode qui traite les requêtes GET et qui renvoie une
     * liste de comptes
     */
    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/accountMap")
    List<Account> all() {
        return accountDao.findAll();
    }

    // end::get-aggregate-root[]
    /**
     * TODO implémenter une méthode qui traite les requêtes GET avec un identifiant
     * "variable de chemin" et qui retourne les informations du compte associé
     * Plus d'informations sur les variables de chemin ->
     * https://www.baeldung.com/spring-pathvariable
     */
    @GetMapping("/account/{id}")
    Account one(@PathVariable Long id) {

        return accountDao.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * TODO implémenter une méthode qui traite les requêtes POST
     * Cette méthode doit recevoir les informations d'un compte en tant que "request
     * body", elle doit sauvegarder le compte en mémoire et retourner ses
     * informations (en json)
     * Tutoriel intéressant -> https://stackabuse.com/get-http-post-body-in-spring/
     * Le serveur devrai retourner un code http de succès (201 Created)
     **/
    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    Account newAccount(@RequestBody Account newAccount) {
        return accountDao.save(newAccount);
    }
    
    /**
     * TODO implémenter une méthode qui traite les requêtes PUT
     */
    @PutMapping("/account/{id}")
    Account replaceAccount(@RequestBody Account newAccount, @PathVariable Long id) {

        return accountDao.findById(id)
                .map(account -> {
                    account.setLastName(newAccount.getLastName());
                    account.setFirstName(newAccount.getFirstName());
                    return accountDao.save(account);
                })
                .orElseGet(() -> {
                    return accountDao.save(newAccount);
                });
    }
    /**
     * TODO implémenter une méthode qui traite les requêtes DELETE
     * L'identifiant du compte devra être passé en "variable de chemin" (ou "path
     * variable")
     * Dans le cas d'un suppression effectuée avec succès, le serveur doit retourner
     * un status http 204 (No content)
     * 
     */
    @DeleteMapping("/account/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAccount(@PathVariable Long id) {
        accountDao.findById(id).ifPresent(accountDao::delete);
    }
}    


