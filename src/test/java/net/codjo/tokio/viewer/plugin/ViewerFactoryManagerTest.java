package net.codjo.tokio.viewer.plugin;
import net.codjo.test.common.LogString;
import java.util.HashMap;
import junit.framework.TestCase;
import org.junit.Test;
/**
 *
 */
public class ViewerFactoryManagerTest extends TestCase {
    LogString log = new LogString();
    ViewerFactoryManager viewerManager = new ViewerFactoryManager(new HashMapMock());
    private static final String TEST_CLASS_PATH = "target\\classes";


    @Test
    public void test_getViewer() throws Exception {
        Process frame = viewerManager.displayViewer("pathToFile", TEST_CLASS_PATH);
        assertNotNull(frame);
        log.assertAndClear("put(pathToFile)");

        frame = viewerManager.displayViewer("pathToFile", TEST_CLASS_PATH);
        assertNotNull(frame);
        log.assertContent("");

        frame.destroy();
    }


    @Test
    public void test_closeWindow() throws Exception {
        Process frame = viewerManager.displayViewer("aFile", TEST_CLASS_PATH);

        frame.destroy();
        Thread.sleep(50);

        log.assertContent("put(aFile), remove(aFile)");
    }


    private class HashMapMock extends HashMap<String, Process> {

        @Override
        public Process put(String key, Process value) {
            log.call("put", key);
            return super.put(key, value);
        }


        @Override
        public Process remove(Object key) {
            log.call("remove", key);
            return super.remove(key); // Todo
        }
    }
}
