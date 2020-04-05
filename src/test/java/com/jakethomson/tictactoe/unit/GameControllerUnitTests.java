package com.jakethomson.tictactoe.unit;

import com.jakethomson.tictactoe.controllers.GameController;
import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.services.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.codehaus.jackson.map.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerUnitTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameService gameService;

    @Test
    public void givenBody_whenNewGame_thenReturnJsonArray()
            throws Exception {
        Game game = new Game("Server", "Player", new String[] {"   ", "   ", "   "});

        given(gameService.newGame("Server", "Player")).willReturn(game);

        ObjectMapper mapperObj = new ObjectMapper();
        Map<String, String> body = Map.of("name", "Player",
                "side", "X");

        mvc.perform(post("/games")
                .content(mapperObj.writeValueAsString(body))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$.id", is(game.getId())));
    }

    @Test
    public void givenGames_whenGetAllGames_thenReturnJsonArray()
            throws Exception {

        Game game = new Game("Player", "Server", new String[] {"   ", "   ", "   "});

        List<Game> allGames = Collections.singletonList(game);

        given(gameService.getAllGames()).willReturn(allGames);

        mvc.perform(get("/games")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("[0].id", is(game.getId())));
    }

    @Test
    public void givenGames_whenGetGame_thenReturnJsonArray()
            throws Exception {

        Game game = new Game("Player", "Server", new String[] {"   ", "   ", "   "});

        given(gameService.getGame(0)).willReturn(game);

        mvc.perform(get("/games/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$.id", is(game.getId())));
    }
}

