package fr.afpa.restapi.dao.impl;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import fr.afpa.restapi.dao.AccountDao;
import fr.afpa.restapi.model.Account;

/**
 * Une implémentation de {@link AccountDao} basée sur un {@link java.util.HashMap} 
 * 
 * TODO annoter cette classe de façon à en faire un "bean". Quelle est l'annotation à utiliser dans ce cas de figure ?
 * Pour vous aider, lisez l'article suivant -> https://www.axopen.com/blog/2019/02/java-spring-les-beans/
 */
@Component
public class InMemoryAccountDao implements AccountDao {

    /**
     * Table de hachage permettant de stocker les objets de {@link Account}
     */
    private Map<Long, Account> accountMap = new HashMap<>();
    private long idSequence = 1L;

    // Constructeur avec comptes en dur
    public InMemoryAccountDao() {
        // Compte 1
        Account account1 = new Account();
        account1.setId(idSequence++);
        account1.setFirstName("Alice");
        account1.setLastName("Durand");
        account1.setEmail("alice.durand@example.com");
        account1.setBirthday(LocalDate.of(1985, 5, 20));
        account1.setCreationTime(LocalDateTime.now());
        account1.setBalance(new BigDecimal("1000.50"));
        accountMap.put(account1.getId(), account1);

        // Compte 2
        Account account2 = new Account();
        account2.setId(idSequence++);
        account2.setFirstName("Bob");
        account2.setLastName("Martin");
        account2.setEmail("bob.martin@example.com");
        account2.setBirthday(LocalDate.of(1990, 7, 15));
        account2.setCreationTime(LocalDateTime.now());
        account2.setBalance(new BigDecimal("250.00"));
        accountMap.put(account2.getId(), account2);

        // Compte 3
        Account account3 = new Account();
        account3.setId(idSequence++);
        account3.setFirstName("Charlie");
        account3.setLastName("Dupont");
        account3.setEmail("charlie.dupont@example.com");
        account3.setBirthday(LocalDate.of(1975, 12, 3));
        account3.setCreationTime(LocalDateTime.now());
        account3.setBalance(new BigDecimal("3000.00"));
        accountMap.put(account3.getId(), account3);
    }

    @Override
    public List<Account> findAll() {
      
        return new ArrayList<>(accountMap.values());
    }

    @Override
    public Optional<Account> findById(long id) {
        
        return Optional.ofNullable(accountMap.get(id));
    }

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            account.setId(idSequence++);
        }
        accountMap.put(account.getId(), account);
        return account;
    }

    @Override
    public void delete(Account account) {
        accountMap.remove(account.getId());
    }

    public void clear() {
        accountMap.clear();
        idSequence = 1L;
    }
}
