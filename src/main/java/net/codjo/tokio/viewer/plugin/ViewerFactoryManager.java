package net.codjo.tokio.viewer.plugin;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
/**
 *
 */
public class ViewerFactoryManager {
    private static final String VIEWER_MAIN_CLASS = "net.codjo.tokio.viewer.TokioLauncher";
    private static final Logger LOG = Logger.getLogger(ViewerFactoryManager.class);

    private Map<String, Process> viewers = new HashMap<String, Process>();


    public ViewerFactoryManager() {
    }


    public ViewerFactoryManager(Map<String, Process> viewers) {
        this.viewers = viewers;
    }


    public Process displayViewer(final String file, String classPath) throws IOException {
        final Process process;

        if (viewers.containsKey(file)) {
            process = viewers.get(file);
            sendMessage(process, "reload");
        }
        else {
            process = buildProcess(file, classPath);
        }
        return process;
    }


    private Process buildProcess(final String file, String classPath) throws IOException {
        final Process process;
        process = new ProcessBuilder("java",
                                     "-cp",
                                     classPath,
                                     VIEWER_MAIN_CLASS,
                                     file).start();

        new Thread() {
            @Override
            public void run() {
                try {
                    process.waitFor();
                    viewers.remove(file);
                }
                catch (InterruptedException exception) {
                    LOG.error("impossible de suivre le cycle du processus", exception);
                }
            }
        }.start();

        viewers.put(file, process);
        return process;
    }


    private void sendMessage(Process process, String message) {
        PrintWriter writer = new PrintWriter(process.getOutputStream());
        writer.println(message);
        writer.flush();
    }
}
