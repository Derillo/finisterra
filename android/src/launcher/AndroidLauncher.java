package launcher;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.esotericsoftware.minlog.Log;
import game.AOGame;
import game.ClientConfiguration;
import game.utils.Resources;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Esto es bastante al pedo. Lo pongo porque el constructor me pide el argumento.
		ClientConfiguration config = ClientConfiguration.loadConfig(Resources.CLIENT_CONFIG);
		if (config == null) {
			Log.warn("DesktopLauncher", "Desktop config.json not found, creating default.");
			config = ClientConfiguration.createConfig();
			config.save(Resources.CLIENT_CONFIG);
		}

		// La verdadera configuracion se hace ACA!
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();

		initialize(new AOGame(config), configuration);
	}
}