package fr.afpa.restapi.web.controller;

import fr.afpa.restapi.AccountNotFoundException;
import fr.afpa.restapi.dao.AccountDao;
import fr.afpa.restapi.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * AccountRestController
 * 
 * Ce contrôleur gère les opérations CRUD (Création, Lecture, Mise à jour, Suppression) pour les ressources de type `Account`.
 * Il expose des endpoints pour créer, récupérer, mettre à jour et supprimer des comptes via des requêtes HTTP.
 */
@RestController
@RequestMapping("/accounts")  // URL de base pour tous les endpoints relatifs aux comptes
public class AccountRestController {

    // AccountDao est l'objet d'accès aux données qui gère les interactions avec la base de données pour les comptes.
    private final AccountDao accountDao;

    /**
     * Constructeur du contrôleur AccountRestController.
     * Utilise l'injection de dépendances pour injecter une instance de AccountDao.
     *
     * @param accountDao le DAO utilisé pour accéder et gérer les comptes
     */
    @Autowired
    public AccountRestController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * GET /accounts
     * 
     * Récupère une liste de tous les comptes dans la base de données.
     * 
     * @return une ResponseEntity contenant la liste des comptes et un statut HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountDao.findAll();  // Récupère tous les comptes via le DAO
        return ResponseEntity.ok(accounts);  // Retourne la liste des comptes avec le statut HTTP 200 OK
    }

    /**
     * GET /accounts/{id}
     * 
     * Récupère un compte spécifique en fonction de son identifiant (ID).
     * Si le compte n'est pas trouvé, une exception AccountNotFoundException est levée.
     *
     * @param id l'ID du compte à récupérer
     * @return une ResponseEntity contenant le compte demandé et HTTP 200 OK si trouvé
     * @throws AccountNotFoundException si le compte avec l'ID donné n'est pas trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> accountOptional = accountDao.findById(id);  // Cherche le compte par ID
        return accountOptional.map(ResponseEntity::ok)  // Si trouvé, retourne le compte avec HTTP 200 OK
                .orElseThrow(() -> new AccountNotFoundException(id));  // Si non trouvé, lance une exception (HTTP 404)
    }

    /**
     * POST /accounts
     * 
     * Crée un nouveau compte avec les détails fournis dans le corps de la requête.
     * Retourne le compte nouvellement créé avec un statut HTTP 201 Created.
     *
     * @param newAccount l'objet compte à créer (analysé à partir du corps de la requête)
     * @return le compte créé avec l'ID généré
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // HTTP 201 Created est retourné après une création réussie
    public Account createAccount(@RequestBody Account newAccount) {
        return accountDao.save(newAccount);  // Enregistre le nouveau compte et le retourne
    }

    /**
     * PUT /accounts/{id}
     * 
     * Met à jour un compte existant par ID avec les nouveaux détails fournis dans le corps de la requête.
     * Si le compte n'est pas trouvé, une exception AccountNotFoundException est levée.
     * Retourne HTTP 204 No Content après une mise à jour réussie.
     *
     * @param updatedAccount l'objet compte avec les détails mis à jour (analysé à partir du corps de la requête)
     * @param id l'ID du compte à mettre à jour
     * @throws AccountNotFoundException si le compte avec l'ID donné n'est pas trouvé
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // HTTP 204 No Content est retourné après une mise à jour réussie
    public void updateAccount(@RequestBody Account updatedAccount, @PathVariable Long id) {
        accountDao.findById(id).map(account -> {
            // Met à jour les champs du compte existant avec les nouvelles valeurs
            account.setFirstName(updatedAccount.getFirstName());
            account.setLastName(updatedAccount.getLastName());
            account.setEmail(updatedAccount.getEmail());
            return accountDao.save(account);  // Enregistre le compte mis à jour
        }).orElseThrow(() -> new AccountNotFoundException(id));  // Si le compte n'est pas trouvé, lance une exception (HTTP 404)
    }

    /**
     * DELETE /accounts/{id}
     * 
     * Supprime le compte avec l'identifiant spécifié.
     * Si le compte n'est pas trouvé, rien ne se passe, mais le statut HTTP 204 No Content est retourné.
     *
     * @param id l'ID du compte à supprimer
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // HTTP 204 No Content est retourné après une suppression réussie
    public void deleteAccount(@PathVariable Long id) {
        accountDao.findById(id).ifPresent(accountDao::delete);  // Supprime le compte s'il existe
    }
}
