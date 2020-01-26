import java.time.format.*;
import java.time.*;
import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class DumpOpenFiles {
    public static void main(String[] args) throws Exception {
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(ZonedDateTime.now());
        String dumpFile = new File(time + ".filehandles").getAbsolutePath().replace('\\', '/');
        String url = "http://localhost:" + parsePort() + "/" + dumpFile;
        System.out.println("Sending request to " + url);
        try (BufferedReader response = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line = null;
            while ((line = response.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    // org.gradle.jvmargs=-javaagent:C:\\tcagent1\\work\\a16b87e0a70f8c6e\\gradle\\file-leak-detector-1.14-SNAPSHOT-fat.jar=http=50000,exclude=
    private static int parsePort() throws Exception {
        Properties gradleProperties = new Properties();
        gradleProperties.load(new FileInputStream(new File("gradle.properties")));
        String jvmArgs = (String) Optional.ofNullable(gradleProperties.get("org.gradle.jvmargs")).orElseThrow(IllegalStateException::new);

        Matcher matcher = Pattern.compile("http=(\\d+),").matcher(jvmArgs);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalStateException("Not found port in " + gradleProperties);
        }
    }
}
