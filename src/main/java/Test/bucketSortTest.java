package Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mycompany.threadsandlocks.Main;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class bucketSortTest {

    @Test
    public void bucketSortHasBeenFiltered() {

        int maxThreads = Runtime.getRuntime().availableProcessors();
        final int ARRAY_SIZE = 2_000_000;
        List<Long> start = Main.start(maxThreads, ARRAY_SIZE);

        // assert statements
        for (int i = 1; i < start.size(); i++) {
           Long a = start.get(i);
           Long b = start.get(i-1);
            assertTrue(a >= b);
        }


    }
}