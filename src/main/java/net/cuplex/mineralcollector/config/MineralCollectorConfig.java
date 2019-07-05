package net.cuplex.mineralcollector.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class MineralCollectorConfig
{
    private static Logger logger = LogManager.getLogger();
    private String mineralCollectorJsonLocation = FabricLoader.getInstance().getConfigDirectory().getPath() + "/mineralCollector.json";
    private File configFile = new File(mineralCollectorJsonLocation);
    private JsonObject config;
    private Gson gson;

    public void loadConfig()
    {
        try {
            FileReader fileReader = new FileReader(mineralCollectorJsonLocation);
            setConfigs(fileReader);
        } catch (FileNotFoundException e) {
            buildFile();
        }
    }

    public void buildFile()
    {
        JsonObject jsonObject = getDefaults();
        try {
            FileWriter writer = new FileWriter(mineralCollectorJsonLocation);
            writer.write(jsonObject.toString());
            writer.flush();
        } catch (IOException e) {
            logger.fatal("Config IO Error", e);
        }
        config = jsonObject;
    }

    private void setConfigs(FileReader fileReader)
    {
        JsonParser parser = new JsonParser();
        config = parser.parse(fileReader).getAsJsonObject();
    }

    private JsonObject getDefaults() {
        JsonObject mineralCollectorJson = new JsonObject();
        mineralCollectorJson.add("lootGenerationTimeInTicks", new JsonPrimitive(200));
        return mineralCollectorJson;
    }

    public int getProperty(String key) {
        try{
            return config.get(key).getAsInt();
        } catch (NumberFormatException nex) {
            logger.error("Can not Format the value you entered for {} into a number.  reverting to default", key);
            return getDefaults().get(key).getAsInt();
        } catch (Exception ex) {
            logger.error("Can not Format the value you entered for {} into a number..  reverting to default", key);
            return getDefaults().get(key).getAsInt();
        }
    }
}
