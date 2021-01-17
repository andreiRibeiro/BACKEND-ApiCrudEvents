package br.com.aaribeiro.eventsApi.controller;

import br.com.aaribeiro.eventsApi.document.UserDocument;
import br.com.aaribeiro.eventsApi.exception.InvalidPasswordException;
import br.com.aaribeiro.eventsApi.model.LoginDTO;
import br.com.aaribeiro.eventsApi.model.TokenDTO;
import br.com.aaribeiro.eventsApi.service.JwtService;
import br.com.aaribeiro.eventsApi.service.SequenceGeneratorService;
import br.com.aaribeiro.eventsApi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Api(tags = "Register and Authenticate Users")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register user.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User registred with successful."),
            @ApiResponse(code = 400, message = "Something wrong with your request."),
            @ApiResponse(code = 500, message = "General processing errors.")
    })
    ResponseEntity<UserDocument> saveUser(@RequestBody @Valid UserDocument userDocument) throws Exception {
        try {
            userDocument.setId(this.sequenceGeneratorService.generateSequence(UserDocument.USERS_SEQUENCE));
            userDocument.setPassword(this.passwordEncoder.encode(userDocument.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.saveUser(userDocument));
        } catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user registred.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return user."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "User not found."),
            @ApiResponse(code = 500, message = "General processing errors.")
    })
    ResponseEntity<UserDocument> getUser(@PathVariable Integer id){
        UserDocument user = this.userService.getUser(id);
        if (user != null) return ResponseEntity.status(HttpStatus.OK).body(user);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Autenticate user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authenticade with successful."),
            @ApiResponse(code = 401, message = "User unauthorized."),
            @ApiResponse(code = 500, message = "General processing errors.")
    })
    ResponseEntity<TokenDTO> authenticateUser(@RequestBody LoginDTO loginDTO) throws Exception {
        try {
            UserDocument userDocument = new UserDocument();
            userDocument.setEmail(loginDTO.getEmail());
            userDocument.setPassword(loginDTO.getPassword());

            this.userService.validatePassword(userDocument);

            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setLogin(userDocument.getEmail());
            tokenDTO.setToken(this.jwtService.generateToken(userDocument));
            return ResponseEntity.status(HttpStatus.OK).body(tokenDTO);
        } catch (InvalidPasswordException e){
            throw new InvalidPasswordException(e.getMessage());
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
