package mytimeacty.service.userdetails;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mytimeacty.model.users.UserDTO;
import mytimeacty.repository.UserRepository;
import mytimeacty.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        UserDTO user = userService.getUserByNickname(nickname);
        Set<String> userRoles = new HashSet<>();
        userRoles.add(user.getUserRole());

        return new org.springframework.security.core.userdetails.User(
            user.getNickname(), null, getAuthorities(userRoles));
    }

    private Set<GrantedAuthority> getAuthorities(Set<String> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());
    }
}