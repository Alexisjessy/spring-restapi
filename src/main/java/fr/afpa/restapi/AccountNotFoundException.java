package fr.afpa.restapi;

public class AccountNotFoundException extends RuntimeException {
   public AccountNotFoundException(Long id){
        super("Compte non trouvé" + id);
    }
}
