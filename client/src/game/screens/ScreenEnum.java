package game.screens;

import com.badlogic.gdx.Screen;
import game.AOGame;
import game.ClientConfiguration;
import game.handlers.AOAssetManager;
import shared.model.lobby.Player;
import shared.model.lobby.Room;

public enum ScreenEnum { //@todo revisar
    /*
    LOADING {
        @Override
        public Screen getScreen(Object... params) {
            return new LoadingScreen(game);
        }
    },
    LOGIN {
        @Override
        public Screen getScreen(Object... params) {
            return new LoginScreen(game);
        }
    },
    SIGNUP {
        @Override
        public Screen getScreen(Object... params) {
            return new SignUpScreen(game);
        }
    },
    LOBBY {
        @Override
        public Screen getScreen(Object... params) {
            return new LobbyScreen(game, (Player) params[0], (Room[]) params[1]);
        }
    },
    ROOM {
        @Override
        public Screen getScreen(Object... params) {
            return new RoomScreen(game, (Room) params[1], (Player) params[2]);
        }
    },
    GAME {
        @Override
        public Screen getScreen(Object... params) {
            return new GameScreen((ClientConfiguration) params[0], (AOAssetManager) params[1]);
        }
    };

    protected final AOGame game;

    ScreenEnum(AOGame game) {
        this.game = game;
    }

    public abstract Screen getScreen(Object... params);
    */
}
