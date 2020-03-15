package game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.esotericsoftware.minlog.Log;
import game.handlers.AOAssetManager;
import game.handlers.DefaultAOAssetManager;
import game.screens.ScreenManager;
import game.screens.GameScreen;
import game.screens.ScreenManager.*;
import game.screens.transitions.ColorFadeTransition;
import game.screens.transitions.FadingGame;
import game.systems.network.ClientSystem;
import game.utils.Cursors;
import shared.util.LogSystem;

/**
 * Represents the game application.
 * Implements {@link ApplicationListener}.
 * <p>
 * This should be the primary instance of the app.
 * </p>
 */
public class AOGame extends FadingGame implements AssetManagerHolder {
    public static final float GAME_SCREEN_ZOOM = 1f;
    public static final float GAME_SCREEN_MAX_ZOOM = 1.3f;

    private final ClientConfiguration clientConfiguration;

    private final AOAssetManager assetManager;
    private final ClientSystem clientSystem;

    private final ScreenManager screenManager;

    public AOGame(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        assetManager = new DefaultAOAssetManager(clientConfiguration);
        clientSystem = new ClientSystem();
        screenManager = new ScreenManager(this);
    }

    public static AOAssetManager getGlobalAssetManager() {
        AssetManagerHolder game = (AssetManagerHolder) Gdx.app.getApplicationListener();
        return game.getAssetManager();
    }

    @Override
    public void create() {
        super.create();
        Log.setLogger(new LogSystem());
        Log.info("AOGame", "Creating AOGame...");
        setTransition(new ColorFadeTransition(Color.BLACK, Interpolation.exp10), 1.0f);
        Cursors.setCursor("hand");
        toLoading();
        // @todo load platform-independent configuration (network, etc.)
    }

    private void toLoading() {
        screenManager.show(Screen.LOADING);
    }

    public void toLogin() {
        screenManager.show(Screen.LOGIN);
    }

    public void toSignUp() {
        screenManager.show(Screen.SIGNUP);
    }

    public void toLobby(Object... params) {
        screenManager.show(Screen.LOBBY, params);
    }

    public void toRoom(Object... params) {
        screenManager.show(Screen.ROOM, params);
    }

    public void toGame() {
        setTransition(new ColorFadeTransition(Color.BLACK, Interpolation.exp10), 0f);
        setScreen(new GameScreen(this));
    }

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    @Override
    public AOAssetManager getAssetManager() {
        return assetManager;
    }

    public ClientSystem getClientSystem() {
        return clientSystem;
    }

    @Override
    public void dispose() {
        Log.info("AOGame", "Closing client...");
        //screenManager.dispose();
        screen.dispose();
        //client.dispose();
        assetManager.dispose();
        //Gdx.app.exit();
        Log.info("Thank you for playing! See you soon...");
        //System.exit(0);
    }
}
