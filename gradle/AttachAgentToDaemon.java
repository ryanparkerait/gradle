import java.time.format.*;
import java.time.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class AttachAgentToDaemon {
    public static void main(String[] args) throws Exception {
        int port = tryBind();

        Properties gradleProperties = new Properties();
        gradleProperties.load(new FileInputStream(new File("gradle.properties")));

        String oldJvmArgs = (String) Optional.ofNullable(gradleProperties.get("org.gradle.jvmargs")).orElseThrow(IllegalStateException::new);
        String newJvmArgs = String.format("-javaagent:%s=http=%d,excludes=%s %s", getAgentFilePath(), port, getGradleUserHomeDir(), oldJvmArgs);

        System.out.println("New jvm args: " + newJvmArgs);

        gradleProperties.put("org.gradle.jvmargs", newJvmArgs);
        gradleProperties.store(new FileOutputStream(new File("gradle.properties")), "");
        gradleProperties.store(new FileOutputStream(new File("buildSrc/gradle.properties")), "");
    }

    private static String getGradleUserHomeDir() {
        return new File(System.getProperty("user.home"), ".gradle").getAbsolutePath();
    }

    private static String getAgentFilePath() {
        return new File("gradle/file-leak-detector-1.14-SNAPSHOT-fat.jar").getAbsolutePath();
    }

    private static int tryBind() throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        int port = 50000;
        while (port < 51000) {
            try {
                serverSocket.bind(new InetSocketAddress("localhost", port));
                return port;
            } catch (IOException ignore) {
                System.err.println("Bind to " + port + " failed");
            }
            port++;
        }
        System.err.println("No available ports found between 50000 and 51000");
        System.exit(-1);
        return -1;
    }
}
