package game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.minlog.Log;
import game.handlers.AOAssetManager;
import game.handlers.DefaultAOAssetManager;
import game.screens.GameScreen;
import game.screens.ScreenManager;
import game.screens.ScreenManager.*;
import game.systems.network.ClientSystem;
import shared.util.LogSystem;

/**
 * This is the <b>main class</b> for the game application (cross-platform).
 * See libGDX {@link ApplicationListener}.
 */
public class AOGame extends Game implements AssetManagerHolder {
    public static final float GAME_SCREEN_ZOOM = 1f;
    public static final float GAME_SCREEN_MAX_ZOOM = 1.3f;

    private final ClientConfiguration clientConfiguration;
    private Sync fpsSync;

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
        Log.setLogger(new LogSystem());
        Log.debug("AOGame", "Creating AOGame...");
        toLoading();
        this.fpsSync = new Sync();
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
    public void render() {
//        fpsSync.sync(100);
        super.render();
    }

    @Override
    public void dispose() {
        Log.debug("AOGame", "Closing client...");
        screen.dispose();
        //screenManager.dispose();
        clientSystem.stop();
        assetManager.dispose();
        Log.debug("Thank you for playing! See you soon...");
        //@todo disponer de todos los recursos utilizados, la JVM debería cerrar sola
        //System.exit(0);
    }
}
