package Finance.organizador_financeiro.controller;

import Finance.organizador_financeiro.dto.ChangePasswordDTO;
import Finance.organizador_financeiro.dto.UserDTO;
import Finance.organizador_financeiro.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthenticatedUser() {
        UserDTO userDTO = userService.findAuthenticatedUserDTO();
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateAuthenticatedUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // Or a more specific error response
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            userService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Incorrect old password
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // Other errors
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
