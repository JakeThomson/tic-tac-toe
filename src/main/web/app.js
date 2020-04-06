// Public variables
let side;
let games = [];
let board;
let game;

// Game Controller
const GameCtrl = (function() {
  // Game Constructor
  const Game = function(id, board, player_x_id, player_o_id, status) {
    this.id = id;
    this.board = board;
    this.player_x_id = player_x_id;
    this.player_o_id = player_o_id;
    this.status = status;

    this.getBoard = function() {
      return this.board;
    };
    this.getId = function() {
      return this.id;
    };
    this.getStatus = function() {
      return this.status;
    };
    this.getPlayerXId = function() {
      return this.player_x_id;
    };
    this.setBoard = function(newBoard) {
      this.board = newBoard;
    };
    this.setStatus = function(status) {
      this.status = status;
    };
  };

  // Public methods
  return {
    getGames: function() {
      axios.get("http://localhost:8080/games").then(response => {
        const data = response.data;
        for (i = 0; i < data.length; i++) {
          d = data[i];

          newGame = new Game(
            d.id,
            d.board,
            d.player_x_id,
            d.player_o_id,
            d.status
          );
          games.push(newGame);
        }
      });
    },
    newGame: function(side) {
      const body = {
        name: "Player",
        side: side
      };
      axios.post("http://localhost:8080/games", body).then(
        response => {
          const data = response.data;
          newGame = new Game(
            data.id,
            data.board,
            data.player_x_id,
            data.player_o_id,
            data.status
          );
          games.push(newGame);
          game = newGame;
          UICtrl.drawBoard();
          for (i = 0; i < 9; i++) {
            UICtrl.updateBlock(i);
          }
          UICtrl.updateWindow();
          return true;
        },
        error => {
          console.log(error);
          return false;
        }
      );
    },
    makeMove: function(side, pos) {
      pos = pos.charAt(1);
      let board = game.getBoard();
      const row = Math.floor(pos / 3);
      const col = pos % 3;
      const id = game.getId();

      board[row] =
        board[row].substring(0, col) + side + board[row].substring(col + 1);

      game.setBoard(board);
      const body = {
        board: board
      };
      axios.post(`http://localhost:8080/games/${id}`, body).then(
        response => {
          const data = response.data;
          game.setBoard(data.board);
          game.setStatus(data.status);

          for (i = 0; i < 9; i++) {
            UICtrl.updateBlock(i);
          }
          UICtrl.updateWindow();
          return true;
        },
        error => {
          console.log(error);
          return false;
        }
      );
    },
    prevGame: function() {
      UICtrl.drawGame(game.getId() - 2);
    },
    nextGame: function() {
      UICtrl.drawGame(game.getId());
    }
  };
})();

// UI Controller
const UICtrl = (function() {
  const UISelectors = {
    gameContainer: ".game-container",
    buttons: ".buttons",
    newGameBtn: "#new-game-submit",
    newGameX: "#new-game-x",
    newGameO: "#new-game-o",
    board: ".board",
    blocks: ["#b0", "#b1", "#b2", "#b3", "#b4", "#b5", "#b6", "#b7", "#b8"],
    gameStatus: ".game-status",
    gameId: ".game-id",
    prevGame: "#prev-game",
    nextGame: "#next-game"
  };

  // Public methods
  return {
    drawBoard() {
      if (games.length > 0) {
        document.querySelector(UISelectors.gameContainer).innerHTML = `     
          <h1>Tic-Tac-Toe</h1> 
          <div>
            <h3 class="game-id"></h3>
            <p class="game-status"></p>
          </div>
          <div class="board">
            <div id="b0" class="block"></div>
            <div id="b1" class="block"></div>
            <div id="b2" class="block"></div>
            <div id="b3" class="block"></div>
            <div id="b4" class="block"></div>
            <div id="b5" class="block"></div>
            <div id="b6" class="block"></div>
            <div id="b7" class="block"></div>
            <div id="b8" class="block"></div>
          </div>`;
        document.querySelector(UISelectors.buttons).innerHTML = `
          <button id="new-game" type="button" class="new" data-toggle="modal" data-target="#locModal">
            NEW GAME
          </button>
          <button id="prev-game" type="button" class="prev">
            PREV
          </button>
          <button id="next-game" type="button" class="next">
            NEXT
          </button>`;
        App.reloadListeners();
      }
    },
    drawGame: function(id) {
      game = games[id];
      if (game.getPlayerXId() === "Player") {
        side = "X";
      } else {
        side = "O";
      }
      for (i = 0; i < 9; i++) {
        let board = game.getBoard();
        const row = Math.floor(i / 3);
        const col = i % 3;
        const counter = board[row].charAt(col);
        document.querySelector(UISelectors.blocks[i]).innerHTML = counter;
        if (counter === " ") {
          document.querySelector(UISelectors.blocks[i]).classList = "block";
        } else {
          document.querySelector(UISelectors.blocks[i]).classList =
            "block occupied";
        }
      }
      this.updateWindow();
      App.reloadBoardEventListeners();
    },
    updateBlock: function(pos) {
      let board = game.getBoard();
      const row = Math.floor(pos / 3);
      const col = pos % 3;

      const counter = board[row].charAt(col);
      if (counter !== " ") {
        document
          .querySelector(UISelectors.blocks[pos])
          .classList.add("occupied");
        document.querySelector(UISelectors.blocks[pos]).innerHTML = counter;
      }
    },
    updateWindow: function() {
      document.querySelector(UISelectors.gameId).innerHTML =
        "Game " + game.getId();
      document.querySelector(UISelectors.gameStatus).innerHTML = game
        .getStatus()
        .split("_")
        .join(" ");
      if (game.id === 1) {
        document.querySelector(UISelectors.prevGame).disabled = true;
      } else {
        document.querySelector(UISelectors.prevGame).disabled = false;
      }
      if (game.id === games.length) {
        document.querySelector(UISelectors.nextGame).disabled = true;
      } else {
        document.querySelector(UISelectors.nextGame).disabled = false;
      }
    },
    getSelectors: function() {
      return UISelectors;
    }
  };
})();

// App Controller
const App = (function(GameCtrl, UICtrl) {
  // Load event listeners
  const loadEventListeners = function() {
    // Get UI Selectors
    const UISelectors = UICtrl.getSelectors();
    if (
      document.querySelector(UISelectors.gameContainer).innerHTML.trim() !==
      "<h1>Tic-Tac-Toe</h1>"
    ) {
      // Make move
      for (i = 0; i < 9; i++) {
        document
          .querySelector(UISelectors.blocks[i])
          .addEventListener("click", function(e) {
            if (
              e.target.innerHTML == "" &&
              game.getStatus() === "IN_PROGRESS"
            ) {
              e.target.innerHTML = side;
              e.target.classList.add("occupied");
              GameCtrl.makeMove(side, e.target.id);
            }
          });
      }
    }
    if (
      document.querySelector(UISelectors.buttons).innerHTML.trim() !==
      `<button id="new-game" type="button" class="new" data-toggle="modal" data-target="#locModal">
          NEW GAME
        </button>`
    ) {
      // Prev game button
      document
        .querySelector(UISelectors.prevGame)
        .addEventListener("click", function() {
          GameCtrl.prevGame();
        });
      // Next game button
      document
        .querySelector(UISelectors.nextGame)
        .addEventListener("click", function() {
          GameCtrl.nextGame();
        });
    }
  };

  const startNewGame = function(side) {
    try {
      if (GameCtrl.newGame(side) === false) {
        throw error;
      }
      UICtrl.drawBoard();
    } catch (error) {
      window.alert("Server is not running.");
    }
  };

  const loadBoardEventListeners = function() {
    // Get UI Selectors
    const UISelectors = UICtrl.getSelectors();

    // Make move
    for (i = 0; i < 9; i++) {
      document
        .querySelector(UISelectors.blocks[i])
        .addEventListener("click", function(e) {
          if (e.target.innerHTML == " " && game.getStatus() === "IN_PROGRESS") {
            e.target.innerHTML = side;
            e.target.classList.add("occupied");
            GameCtrl.makeMove(side, e.target.id);
          }
        });
    }
  };

  // Public methods
  return {
    getSide: function() {},
    init: function() {
      GameCtrl.getGames();
      loadEventListeners();
      // New game event
      document
        .querySelector(UICtrl.getSelectors().newGameX)
        .addEventListener("click", function() {
          side = "X";
          startNewGame("X");
        });
      document
        .querySelector(UICtrl.getSelectors().newGameO)
        .addEventListener("click", function() {
          side = "O";
          startNewGame("O");
        });
    },
    reloadListeners: function() {
      loadEventListeners();
    },
    reloadBoardEventListeners: function() {
      loadBoardEventListeners();
    }
  };
})(GameCtrl, UICtrl);

// Initialize App
App.init();
