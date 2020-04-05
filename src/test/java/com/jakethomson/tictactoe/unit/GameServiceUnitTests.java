package com.jakethomson.tictactoe.unit;

import com.jakethomson.tictactoe.models.Game;
import com.jakethomson.tictactoe.repositories.GameRepository;
import com.jakethomson.tictactoe.services.GameService;
import com.jakethomson.tictactoe.services.ServerMoveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(GameService.class)
public class GameServiceUnitTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository mockRepository;
    @MockBean
    private ServerMoveService serverMoveService;

    @Test
    public void givenParams_whenNewGame_thenReturnGameObj()
            throws Exception {
        // Given
        String player_x_id = "Server";
        String player_o_id = "Player";
        String[] board = new String[] {"X  ", "   ", "   "};
        Game game = new Game("Server", "Player", board);
        given(serverMoveService.firstMove()).willReturn(board);

        // When
        Game result = gameService.newGame(player_x_id,player_o_id);

        // Then
        assertArrayEquals(result.getBoard(), game.getBoard());
    }

}
