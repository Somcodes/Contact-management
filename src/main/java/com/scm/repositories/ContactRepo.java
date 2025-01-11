package com.scm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    //find the contact by user

    //custom finder method
    Page<Contact> findByuser(User user, Pageable pageable);

    //custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    List<Contact> findByUserAndFavouriteTrue(User user);

    List<Contact> findByUserAndNameContaining(User user, String nameKeyword);
    List<Contact> findByUserAndEmailContaining(User user, String emailKeyword);
    List<Contact> findByUserAndPhoneNumberContaining(User user, String phoneKeyword);
    Page<Contact> findPagedContactsByUserAndNameContaining(User user, String nameKeyword, Pageable pageable);
    Page<Contact> findPagedContactsByUserAndEmailContaining(User user, String emailKeyword, Pageable pageable);
    Page<Contact> findPagedContactsByUserAndPhoneNumberContaining(User user, String phoneKeyword, Pageable pageable);
}
