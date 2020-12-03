package Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mycompany.threadsandlocks.BucketSortSolver;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class bucketSortTest {

    @Test
    public void bucketSortHasBeenFiltered() {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        final int ARRAY_SIZE = 2_000_000;

        BucketSortSolver sorter = new BucketSortSolver();
        List<Long> start = sorter.sequential(maxThreads, ARRAY_SIZE);

        // assert statements
        for (int i = 1; i < start.size(); i++) {
           Long a = start.get(i);
           Long b = start.get(i-1);
            assertTrue(a >= b);
        }
    }

    @Test
    public void compareTimes() {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        final int ARRAY_SIZE = 2_000_000;

        long before = System.currentTimeMillis();
        BucketSortSolver sorter = new BucketSortSolver();
        List<Long> start = sorter.sequential(maxThreads, ARRAY_SIZE);

        System.out.println(before);

        // assert statements
        for (int i = 1; i < start.size(); i++) {
            Long a = start.get(i);
            Long b = start.get(i-1);
            assertTrue(a >= b);
        }


        long after = System.currentTimeMillis();
        System.out.println(after);

        long difference = after - before;

        System.out.println(difference);
    }

}