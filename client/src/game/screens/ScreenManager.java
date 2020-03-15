package game.screens;

import game.AOGame;

//@todo hacer las screens poolables (crear una sola vez y reutilizar)
//@todo testear consumo de memoria
public class ScreenManager {
    private AOGame game;

    public ScreenManager(AOGame game) {
        this.game = game;
    }

    public enum Screen {
        LOADING,
        LOGIN,
        SIGNUP,
        LOBBY,
        ROOM,
        GAME
    }

    // Show in the game the screen which enum type is received
    public void show(Screen screen, Object... params) {
        // Show new screen
        /*
        Screen newScreen = screen.getScreen(game, params);
        game.setScreen(newScreen);
        */
    }
}
