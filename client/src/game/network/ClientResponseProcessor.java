package game.network;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.minlog.Log;
import game.AOGame;
import game.screens.*;
import game.screens.transitions.TransitionListener;
import game.systems.network.ClientSystem;
import game.systems.network.TimeSync;
import game.systems.physics.MovementProcessorSystem;
import shared.network.account.AccountCreationResponse;
import shared.network.account.AccountLoginResponse;
import shared.network.interfaces.IResponseProcessor;
import shared.network.lobby.*;
import shared.network.lobby.player.PlayerLoginRequest;
import shared.network.movement.MovementResponse;
import shared.network.time.TimeSyncResponse;

@Wire
public class ClientResponseProcessor extends BaseSystem implements IResponseProcessor {

    private TimeSync timeSync;

    private AOGame game;

    public ClientResponseProcessor(AOGame game) {
        this.game = game;
    }

    @Override
    public void processResponse(MovementResponse movementResponse) {
        MovementProcessorSystem.validateRequest(movementResponse.requestNumber, movementResponse.destination);
    }

    @Override
    public void processResponse(CreateRoomResponse createRoomResponse) {
        LobbyScreen lobby = (LobbyScreen) game.getScreen();

        switch (createRoomResponse.getStatus()) {
            case CREATED:
                game.toRoom(game.getClientSystem(), createRoomResponse.getRoom(), createRoomResponse.getPlayer());
                break;
            case MAX_ROOM_LIMIT:
                lobby.roomMaxLimit();
                break;
        }
    }

    @Override
    public void processResponse(JoinLobbyResponse joinLobbyResponse) {
        LoginScreen login = (LoginScreen) game.getScreen();
        game.toLobby(joinLobbyResponse.getPlayer(), joinLobbyResponse.getRooms(), login.getClientSystem());
    }

    @Override
    public void processResponse(JoinRoomResponse joinRoomResponse) {
        game.toRoom(game.getClientSystem(), joinRoomResponse.getRoom(), joinRoomResponse.getPlayer());
    }

    @Override
    public void processResponse(StartGameResponse startGameResponse) {
        if (game.getScreen() instanceof RoomScreen) {
            RoomScreen roomScreen = (RoomScreen) game.getScreen();
            //@todo revisar esto
            ClientSystem clientSystem = game.getClientSystem();
            clientSystem.stop();
            clientSystem.getKryonetClient().setHost(startGameResponse.getHost());
            clientSystem.getKryonetClient().setPort(startGameResponse.getTcpPort());
            clientSystem.start();
            game.toGame();
            clientSystem.getKryonetClient().sendToAll(new PlayerLoginRequest(roomScreen.getPlayer()));
        }
    }

    @Override
    public void processResponse(TimeSyncResponse timeSyncResponse) {
        timeSync.receive(timeSyncResponse);
        Log.info("Local timestamp: " + TimeUtils.millis() / 1000);
        Log.info("RTT: " + timeSync.getRtt() / 1000);
        Log.info("Time offset: " + timeSync.getTimeOffset() / 1000);
    }

    @Override
    public void processResponse(AccountCreationResponse accountCreationResponse) {
        AbstractScreen screen = (AbstractScreen) game.getScreen();

        if (accountCreationResponse.isSuccessful()) {
            game.addTransitionListener(new TransitionListener() {
                @Override
                public void onTransitionStart() {

                }

                @Override
                public void onTransitionFinished() {
                    AbstractScreen screen = (AbstractScreen) game.getScreen();

                    Dialog dialog = new Dialog("Exito", screen.getSkin());
                    dialog.text("Cuenta creada con exito");
                    dialog.button("OK");
                    dialog.show(screen.getStage());

                    game.removeTransitionListener(this);
                }
            });
            game.toLogin();
        }
        else {
            Dialog dialog = new Dialog("Error", screen.getSkin());
            dialog.text("Error al crear la cuenta");
            dialog.button("OK");
            dialog.show(screen.getStage());
        }
    }

    @Override
    public void processResponse(AccountLoginResponse accountLoginResponse) {
        LoginScreen screen = (LoginScreen) game.getScreen();

        if (accountLoginResponse.isSuccessful()) {
            /*
            Dialog dialog = new Dialog("Exito", screen.getSkin());
            dialog.text("Logueado con exito");
            dialog.button("OK");
            dialog.show(screen.getStage());
            */

            //@todo pasar al lobby del servidor
            //hotfix para recuperar funcionalidad
            screen.getClientSystem().getKryonetClient().sendToAll(new JoinLobbyRequest(accountLoginResponse.getUsername()));
        }
        else {
            Dialog dialog = new Dialog("Error", screen.getSkin());
            dialog.text("Error al loguearse");
            dialog.button("OK");
            dialog.show(screen.getStage());
        }
    }

    @Override
    protected void processSystem() {

    }
}
