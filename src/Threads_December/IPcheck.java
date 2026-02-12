package Threads_December;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IPcheck {
    private static volatile String lastIp = null;

    public static void main(String[] args) {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable ipCheckTask = new Runnable() {
            @Override
            public void run() {
                try {
                    String currentIp = getLocalIpAddress();

                    if (lastIp == null) {
                        lastIp = currentIp;
                        System.out.println("Initial IP detected: " + currentIp);
                    } else if (!currentIp.equals(lastIp)) {
                        System.out.println("⚠ IP address has changed!");
                        System.out.println("Previous IP: " + lastIp);
                        System.out.println("Current IP:  " + currentIp);
                        lastIp = currentIp;
                    } else {
                        System.out.println("IP unchanged: " + currentIp);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Schedule the task every 2 minutes
        scheduler.scheduleAtFixedRate(ipCheckTask, 0, 2, TimeUnit.MINUTES);
    }

    // Finds the first non-loopback IPv4 address
    private static String getLocalIpAddress() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();

            if (iface.isLoopback() || !iface.isUp()) continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr.getHostAddress().indexOf(":") < 0) {
                    return addr.getHostAddress(); // IPv4 only
                }
            }
        }

        return "UNKNOWN";
    }
}









