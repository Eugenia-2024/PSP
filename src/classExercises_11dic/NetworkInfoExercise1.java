package classExercises_11dic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;

public class NetworkInfoExercise1 {

    public static void main(String[] args) throws Exception {

        // 1) Choose OS command that gives network information
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder pb = buildNetworkCommand(isWindows);

        // 4) Select the directory you want (working directory for the subprocess)
        // Here we run in the current project folder; later we will "change directory" to /result for the output file.
        pb.redirectErrorStream(true);

        // 1a) Execute the process
        Process p = pb.start();

        // 3) Read the command output (we need it later to extract Hostname and IPv4)
        String output = readAll(p.getInputStream());

        // 2) Wait until the process ends
        int exitCode = p.waitFor();

        // 1c) Print whether it finished successfully
        if (exitCode == 0) {
            System.out.println("Command finished successfully (exit code 0).");
        } else {
            System.out.println("Command failed (exit code " + exitCode + ").");
        }

        // 2a) Extract hostname and IPv4 from output
        String hostname = extractHostname(output, isWindows);
        String ipv4 = extractIPv4(output, isWindows);

        System.out.println("Extracted hostname: " + hostname);
        System.out.println("Extracted IPv4:     " + ipv4);

        // 2b) Change directory to "result" (create it if it does not exist)
        Path resultDir = Paths.get("result");
        Files.createDirectories(resultDir);

        // 2c) Create a file with hostname and IPv4 address inside
        // Safer filename (replace characters not good for filenames)
        String safeHost = hostname.replaceAll("[^A-Za-z0-9._-]", "_");
        String safeIp = ipv4.replaceAll("[^0-9.]", "_");

        Path outFile = resultDir.resolve(safeHost + "_" + safeIp + ".txt");

        String fileContent = "Hostname: " + hostname + System.lineSeparator()
                + "IPv4: " + ipv4 + System.lineSeparator();

        // 5) Create/open/write the file
        Files.writeString(outFile, fileContent, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Written file: " + outFile.toAbsolutePath());
    }

    // Build the correct OS command
    private static ProcessBuilder buildNetworkCommand(boolean isWindows) {
        if (isWindows) {
            // Windows: ipconfig /all -> includes "Host Name" and "IPv4 Address"
            return new ProcessBuilder("cmd.exe", "/c", "ipconfig /all");
        } else {
            // Linux: hostname + ip -4 addr (IPv4 only)
            return new ProcessBuilder("bash", "-lc", "hostname && ip -4 addr");
        }
    }

    // Read all process output
    private static String readAll(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    // Extract hostname depending on OS output
    private static String extractHostname(String output, boolean isWindows) {
        if (isWindows) {
            // Example line: "Host Name . . . . . . . . . . . . : MYPC"
            Pattern p = Pattern.compile("(?im)^\\s*Host Name\\s*\\.\\s*.*?:\\s*(\\S+)\\s*$");
            Matcher m = p.matcher(output);
            if (m.find()) return m.group(1);
            return "UNKNOWN_HOST";
        } else {
            // On Linux command "hostname && ip -4 addr", first line is hostname
            String firstLine = output.split("\\R", 2)[0].trim();
            return firstLine.isEmpty() ? "UNKNOWN_HOST" : firstLine;
        }
    }

    // Extract an IPv4 address depending on OS output
    private static String extractIPv4(String output, boolean isWindows) {
        if (isWindows) {
            // Example line: "IPv4 Address. . . . . . . . . . . : 192.168.1.20(Preferred)"
            Pattern p = Pattern.compile("(?im)^\\s*IPv4 Address.*?:\\s*([0-9]{1,3}(?:\\.[0-9]{1,3}){3})");
            Matcher m = p.matcher(output);
            if (m.find()) return m.group(1);
            return "0.0.0.0";
        } else {
            // Linux ip output contains lines like: "inet 192.168.1.20/24 ..."
            Pattern p = Pattern.compile("(?m)\\binet\\s+([0-9]{1,3}(?:\\.[0-9]{1,3}){3})/\\d+\\b");
            Matcher m = p.matcher(output);
            while (m.find()) {
                String ip = m.group(1);
                if (!ip.startsWith("127.")) return ip; // skip loopback
            }
            return "0.0.0.0";
        }
    }
}//class
