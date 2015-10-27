package net.codjo.tokio.viewer.plugin;
import com.intellij.execution.CantRunException;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
/**
 *
 */
public class TovioViewerAction extends AnAction {

    ViewerFactoryManager viewerFactory = new ViewerFactoryManager();


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final VirtualFile file = extractPath(anActionEvent);

        if (file.exists()) {
            try {
                viewerFactory.displayViewer(file.getPath(), extractClassPath(anActionEvent));
            }
            catch (Exception exception) {
                showErrorDialog("Impossible de démarrer tokio-viewer", exception);
            }
        }
    }


    @Override
    public void update(AnActionEvent anActionEvent) {
        VirtualFile file = extractPath(anActionEvent);

        if (file != null && file.exists()) {
            if (!file.getPath().endsWith(".tokio")) {
                anActionEvent.getPresentation().setVisible(false);
            }
            else {
                anActionEvent.getPresentation().setVisible(true);
                anActionEvent.getPresentation().setText("Visualiser le tokio");
                anActionEvent.getPresentation().setIcon(IconLoader.getIcon("icon-small.png", getClass()));
            }
        }
    }


    private VirtualFile extractPath(AnActionEvent anActionEvent) {
        return (VirtualFile)anActionEvent.getDataContext().getData(DataConstants.VIRTUAL_FILE);
    }


    private String extractClassPath(AnActionEvent anActionEvent)
          throws ClassNotFoundException, CantRunException {
        Module module = (Module)anActionEvent.getDataContext().getData(DataConstants.MODULE);
        Project project = (Project)anActionEvent.getDataContext().getData(DataConstants.PROJECT);

        JavaParameters params = new JavaParameters();
        params.setJdk(ProjectRootManager.getInstance(project).getProjectSdk());
        params.configureByModule(module, JavaParameters.JDK_AND_CLASSES_AND_TESTS);

        String classPath = params.getClassPath().getPathsString();
        if (!classPath.contains("codjo-tokio")) {
            throw new ClassNotFoundException("le module ne contient pas la librairie tokio");
        }
        return classPath;
    }


    private void showErrorDialog(String message, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        JTextArea textArea = buildTextArea(stackTrace.toString());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 0, 0, 20));
        panel.add(new JScrollPane(textArea));
        Object[] array = {message, panel};

        Object[] options = {"OK"};

        JOptionPane optionPane =
              new JOptionPane(array, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION,
                              null, options, options[0]);

        JDialog dialog = optionPane.createDialog(null, "Erreur");
        dialog.setResizable(true);
        dialog.setVisible(true);
    }


    private JTextArea buildTextArea(String exceptionMsg) {
        JTextArea textArea = new JTextArea(exceptionMsg, 20, 60);
        textArea.setName("errorMessage");
        textArea.setEnabled(true);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(205, 205, 205));
        textArea.setDisabledTextColor(Color.black);
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return textArea;
    }
}
