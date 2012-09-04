package net.codjo.tokio.viewer.plugin;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * Classe simulant le veritable TokioLauncher. La classe TokioLauncher se trouve dans la librairie :
 * codjo-tokio
 */
public class TokioLauncher {

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("LAncement de tokio Viewer avec le fichier");
            System.out.println("tokioFile = " + args[0]);
        }
        catch (Throwable e) {
            throw new Exception("Aucun fichier spécifié dans les arguments", e);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        while (str != null) {
            str = in.readLine();
            process(str);
        }
    }


    private static void process(String str) {
        if ("reload".equals(str)) {
            System.out.println("focus is requested");
        }
    }


    @Override
    protected void finalize() throws Throwable {
        System.out.println("TokioLauncher.finalize");
        super.finalize();
    }
}
