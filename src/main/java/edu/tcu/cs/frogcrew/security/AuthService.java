package edu.tcu.cs.frogcrew.security;

import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.MyUserPrincipal;
import edu.tcu.cs.frogcrew.user.converter.FrogCrewUserToUserDtoConverter;
import edu.tcu.cs.frogcrew.user.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JWTProvider jwtProvider;

    private final FrogCrewUserToUserDtoConverter frogCrewUserToUserDtoConverter;

    public AuthService(JWTProvider jwtProvider, FrogCrewUserToUserDtoConverter frogCrewUserToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.frogCrewUserToUserDtoConverter = frogCrewUserToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal)authentication.getPrincipal();
        FrogCrewUser user = principal.getUser();

        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userId", user.getId());
        loginResultMap.put("role", user.getRole());
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
