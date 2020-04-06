package com.jakethomson.tictactoe.unit;

import com.jakethomson.tictactoe.controllers.MoveController;
import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.repositories.GameRepository;
import com.jakethomson.tictactoe.services.MoveService;
import com.jakethomson.tictactoe.services.ValidationService;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MoveController.class)
public class MoveControllerUnitTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MoveService moveService;
    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private ValidationService validationService;

    private ObjectMapper mapperObj = new ObjectMapper();

    @Test
    public void givenGame_whenMove_thenReturnJsonArray()
            throws Exception {
        // Given
        String[] newBoard = new String[] {"X  ", "   ", "   "};
        String[] board = new String[] {"   ", "   ", "   "};
        Game game = new Game("Player", "Server", board);
        Game newGame = new Game("Player", "Server", newBoard);
        given(gameRepository.findById(0)).willReturn(game);
        given(validationService.validateMoveInput(board, newBoard, 'X')).willReturn(true);
        given(moveService.move(game, newBoard)).willReturn(newGame);
        Map<String, String[]> body = Map.of("board", newBoard);

        // When
        mvc.perform(post("/games/0")
                .content(mapperObj.writeValueAsString(body))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))

        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$.board", Matchers.containsInAnyOrder(body.get("board"))));
    }
}
