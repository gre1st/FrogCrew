package edu.tcu.cs.frogcrew.user;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FrogCrewUserService implements UserDetailsService {

    private final FrogCrewUserRepository frogCrewUserRepository;

    private PasswordEncoder passwordEncoder;

    public FrogCrewUserService(FrogCrewUserRepository frogCrewUserRepository, PasswordEncoder passwordEncoder) {
        this.frogCrewUserRepository = frogCrewUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public FrogCrewUser findById(Integer id) {
        return this.frogCrewUserRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("user", id));
    }

    public FrogCrewUser addCrewMember(FrogCrewUser frogCrewUser) {
        frogCrewUser.setPassword(passwordEncoder.encode(frogCrewUser.getPassword()));
        return this.frogCrewUserRepository.save(frogCrewUser);
    }

    public List<FrogCrewUser> findAll() {
        return this.frogCrewUserRepository.findAll();
    }

    public FrogCrewUser updateCrewMember(Integer userId, FrogCrewUser frogCrewUser) {
        return this.frogCrewUserRepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setFirstName(frogCrewUser.getFirstName());
                    oldUser.setLastName(frogCrewUser.getLastName());
                    oldUser.setEmail(frogCrewUser.getEmail());
                    oldUser.setPhoneNumber(frogCrewUser.getPhoneNumber());
                    oldUser.setRole(frogCrewUser.getRole());
                    oldUser.setQualifiedPositions(frogCrewUser.getQualifiedPositions());
                    return this.frogCrewUserRepository.save(oldUser);
                }).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.frogCrewUserRepository.findByEmail(email)
                .map(user -> new MyUserPrincipal(user))
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " is not found"));
    }

}
