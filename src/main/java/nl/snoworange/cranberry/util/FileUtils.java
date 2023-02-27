package nl.snoworange.cranberry.util;

import com.google.gson.*;
import nl.snoworange.cranberry.Main;
import nl.snoworange.cranberry.features.module.Module;
import nl.snoworange.cranberry.features.module.modules.misc.Announcer;
import nl.snoworange.cranberry.features.setting.Bind;
import nl.snoworange.cranberry.features.setting.Setting;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtils {

    public static FileUtils instance;

    public FileUtils() {
        instance = this;
    }

    public FileUtils getInstance() {
        return instance;
    }

    public static File config = new File("cranberry/config");
    public static File cranberry = new File("cranberry");

    public static String path = "";

    public static void init() {
        makeDirectories();
    }

    public static void saveAll(File file) {
        saveActiveModules(file);
        saveFriends(file);
        saveFriends(file);
        saveConfig(config);
    }

    public static void loadAll(File file) {
        loadActiveModules(file);
        loadFriends(file);
        loadPrefix(file);
        loadConfig(config.getAbsolutePath());
    }

    public static void makeDirectories() {
        if (!cranberry.exists()) cranberry.mkdir();
        if (!config.exists()) config.mkdir();
    }

    public static void saveActiveModules(File file) {
        try {
            File modules = new File(file.getAbsolutePath(), "activemodules.txt");
            FileWriter fileWriter = new FileWriter(modules);

            for (Module module : Main.moduleManager.getModules()) {
                fileWriter.write(module.getName() + ":");
                if (module.isToggled()) {
                    fileWriter.write("true");
                }
                else {
                    fileWriter.write("false");
                }
                fileWriter.write("\r\n");
            }
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadActiveModules(File file) {
        try {

            File modules = new File(file.getAbsolutePath(), "activemodules.txt");

            if (!modules.exists()) {
                modules.createNewFile();
                return;
            }

            Scanner scanner = new Scanner(modules);
            List<String> linezz = Files.readAllLines(modules.toPath());

            for (String line : linezz) {

                String[] regex = line.split(":");
                Module module = Main.moduleManager.getModuleByName(regex[0]);

                module.setEnabledTryEnableAndDisable(Boolean.parseBoolean(regex[1]));
            }
            scanner.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void saveFriends(File file) {
        try {
            File friends = new File(file.getAbsolutePath(), "friends.txt");
            FileWriter fileWriter = new FileWriter(friends);

            for (String friend : Main.friendManager.getFriends()) {
                fileWriter.write(friend);
                fileWriter.write("\r\n");
            }
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadFriends(File file) {
        try {
            File friends = new File(file.getAbsolutePath(), "friends.txt");

            if (!friends.exists()) {
                friends.createNewFile();
                return;
            }

            Scanner scanner = new Scanner(friends);
            final List<String> linezz = Files.readAllLines(friends.toPath());

            for (String line : linezz) {
                Main.friendManager.addFriend(line);
            }
            scanner.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void savePrefix(File file, String prefix) {
        try {
            File prefixFile = new File(file.getAbsolutePath(), "prefix.txt");
            FileWriter fileWriter = new FileWriter(prefixFile);

            fileWriter.write(prefix);
            fileWriter.write("\r\n");

            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadPrefix(File file) {
        try {
            File prefixFile = new File(file.getAbsolutePath(), "prefix.txt");

            if (!prefixFile.exists()) {
                prefixFile.createNewFile();
                return;
            }

            final FileReader fileReader = new FileReader(prefixFile);
            final List<String> linezz = Files.readAllLines(prefixFile.toPath());

            for (String line : linezz) {
                if (linezz.size() > 1) return;
                Main.commandManager.setCommandPrefix(line);
            }
            fileReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setValueFromJson(Module module, Setting setting, JsonElement element) {
        String str;
        switch (setting.getType()) {
            case "Boolean":
                setting.setValue(element.getAsBoolean());
                return;
            case "Double":
                setting.setValue(element.getAsDouble());
                return;
            case "Float":
                setting.setValue(element.getAsFloat());
                return;
            case "Integer":
                setting.setValue(element.getAsInt());
                return;
            case "String":
                str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                return;
            case "Bind":
                setting.setValue((new Bind.BindConverter()).doBackward(element));
                return;
            case "Enum":
                try {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
        }
    }

    public static void loadConfig(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(cranberry.listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        if (files.contains(new File(cranberry.getAbsolutePath() + "/" + name + "/"))) {
            path = cranberry.getAbsolutePath() + "/" + name + "/";
        } else {
            path = config + "/";
        }
        for (Module module : Main.moduleManager.getModules()) {
            try {
                loadSettings(module);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveConfig(File file) {
        for (Module module : Main.moduleManager.getModules()) {
            try {
                saveSettings(module);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void saveSettings(Module module) throws IOException {
        JsonObject jsonObject = new JsonObject();
        File directory = new File(path + getDirectoryWithCategory(module));

        if (!directory.exists()) {
            directory.mkdir();
        }

        String featureName = path + getDirectoryWithCategory(module) + module.getName() + ".json";
        Path outputFile = Paths.get(featureName);

        if (!Files.exists(outputFile)) {
            Files.createFile(outputFile);
        }

        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        String json = gson.toJson(writeSettings(module));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));

        writer.write(json);
        writer.close();
    }

    private static void loadSettings(Module module) throws IOException {
        String featureName = path + getDirectoryWithCategory(module) + module.getName() + ".json";
        Path featurePath = Paths.get(featureName);
        if (!Files.exists(featurePath))
            return;
        loadPath(featurePath, module);
    }

    private static void loadPath(Path path, Module module) throws IOException {
        InputStream stream = Files.newInputStream(path);
        try {
            loadFile((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject(), module);
        } catch (IllegalStateException e) {
            Main.LOGGER.error("Invalid config file found for module " + module.getName() + "! Resetting...");
            loadFile(new JsonObject(), module);
        }
        stream.close();
    }

    private static void loadFile(JsonObject input, Module module) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String settingName = entry.getKey();
            JsonElement element = entry.getValue();

            for (Setting setting : module.getSettings()) {
                if (settingName.equals(setting.getName())) {
                    try {
                        setValueFromJson(module, setting, element);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static JsonObject writeSettings(Module module) {

        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();

        for (Setting setting : module.getSettings()) {
            try {
                if (setting.isEnumSetting()) {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    object.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
                    continue;
                }

                if (setting.isStringSetting()) {
                    String str = (String) setting.getValue();
                    setting.setValue(str.replace(" ", "_"));
                }

                try {
                    object.add(setting.getName(), jp.parse(setting.getValueAsString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return object;
    }

    public static String getDirectoryWithCategory(Module module) {
        return module.getCategory().name + "/";
    }
}