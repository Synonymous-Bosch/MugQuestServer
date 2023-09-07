package com.muggame.mug.controllers;

import com.muggame.mug.models.Game;
import com.muggame.mug.models.Player;
import com.muggame.mug.models.User;
import com.muggame.mug.repositories.GameRepository;
import com.muggame.mug.repositories.PlayerRepository;
import com.muggame.mug.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping(value = "/games")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Game>> getAllGames() {
        return new ResponseEntity<>(gameRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/games/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity getGame(@PathVariable Long id) {
        return new ResponseEntity(gameRepository.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/games")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createGame(@RequestBody Map<String, Long> requestBody) {
        Optional<User> user = userRepository.findById(requestBody.get("userId"));
        Optional<Player> player = playerRepository.findById(requestBody.get("playerId"));

        if (!user.isPresent() || !player.isPresent()) {
            // Handle the case where user or player is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or player not found.");
        }

        Game game = new Game(user.get(), player.get());

        try {
            game = gameRepository.save(game);
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating game.");
        }
    }
}

