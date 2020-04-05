package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import game.AOGame;
import game.ClientConfiguration;
import game.handlers.AOAssetManager;
import game.systems.network.ClientSystem;
import game.utils.Resources;
import game.utils.Skins;

import javax.annotation.Nonnull;

public abstract class AbstractScreen extends ScreenAdapter {
    private static final Skin SKIN = Skins.COMODORE_SKIN;
    private static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal(Resources.GAME_IMAGES_PATH + "background.jpg"));
    private static final SpriteDrawable BACKGROUND = new SpriteDrawable(new Sprite(BACKGROUND_TEXTURE));

    // dependencies
    protected final AOGame game;
    protected final AOAssetManager assetManager;
    protected final ClientConfiguration config;
    protected final ClientSystem clientSystem;

    // scene2d ui
    protected final Stage stage;
    protected final Table mainTable;

    public AbstractScreen(@Nonnull AOGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        config = game.getClientConfiguration();
        clientSystem = game.getClientSystem();

        stage = new Stage() {
            @Override
            public boolean keyUp(int keyCode) {
                keyPressed(keyCode);
                return super.keyUp(keyCode);
            }
        };
        mainTable = new Table(SKIN);
        mainTable.setFillParent(true);
        mainTable.setBackground(BACKGROUND);
        stage.addActor(mainTable);
        createUI();
    }

    abstract void createUI();

    protected void keyPressed(int keyCode) {
        //override me
    }

    public Stage getStage() {
        return stage;
    }

    public Table getMainTable() {
        return mainTable;
    }

    public Skin getSkin() {
        return SKIN;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getStage());
    }

    @Override
    public void render(float delta) {
        getStage().act(delta);
        getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        getStage().getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        getStage().dispose();
    }
}
