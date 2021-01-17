package br.com.aaribeiro.eventsApi.service;

import br.com.aaribeiro.eventsApi.document.UserDocument;
import br.com.aaribeiro.eventsApi.exception.InvalidPasswordException;
import br.com.aaribeiro.eventsApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDocument userDocument = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(userDocument.getEmail())
                .password(userDocument.getPassword())
                .roles("ADMIN")
                .build();
    }

    @Transactional
    public UserDocument saveUser(UserDocument userDocument){
        return userRepository.save(userDocument);
    }

    public UserDocument getUser(long id){
        Optional<UserDocument> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public UserDetails validatePassword(UserDocument userDocument) throws Exception {
        UserDetails userDetails = this.loadUserByUsername(userDocument.getEmail());
        boolean equalPasswords = this.passwordEncoder.matches(userDocument.getPassword(), userDetails.getPassword());
        if (equalPasswords){
            return userDetails;
        }
        throw new InvalidPasswordException("Invalid password.");
    }
}
