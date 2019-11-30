package server.systems;

import com.artemis.annotations.Wire;
import server.systems.manager.DefaultManager;
import server.systems.manager.WorldManager;
import shared.network.notifications.ConsoleMessage;
import shared.util.Messages;

@Wire
public class CommandSystem extends DefaultManager {

    // Injected Systems
    private ServerSystem networkManager;
    private WorldManager worldManager;

    public void handleCommand(String command, int senderId) {
        /* 
            Example command to get the online players
        
            28/11/2019. [4rl3nk1ng] 
            -Cambio de if a switch, ya que es mas rapido y entendible.
            -Change if to switch, is more fast and understandable.
        */
    
        switch(command.toUpperCase()) {
            case "/PLAYERSONLINE":
                String connections = String.valueOf(networkManager.getAmountConnections());
                worldManager.sendEntityUpdate(senderId, ConsoleMessage.info(Messages.PLAYERS_ONLINE, connections));
            break;
            
            default: //si no coincide, es inválido
                worldManager.sendEntityUpdate(senderId, Messages.COMMAND_ERR); 
        }
        
    }
}
