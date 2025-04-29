package edu.tcu.cs.frogcrew.security;

import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/auth")
public class AuthController {

    private final AuthService authService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication) {
        LOGGER.debug("Authenticated user: '{}'", authentication.getName());
        return new Result(true, StatusCode.SUCCESS, "User Info and JSON Web Token", this.authService.createLoginInfo(authentication));
    }

    @PostMapping("/resetPassword")
    public Result resetPassword(Authentication authentication) {
        return null;
    }

    @PostMapping("/forgotPassword/{email}")
    public Result forgotPassword(@PathVariable String email) {
        return null;
    }
}
