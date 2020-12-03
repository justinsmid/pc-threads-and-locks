package Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mycompany.threadsandlocks.BucketSortSolver;
import com.mycompany.threadsandlocks.Util;
import org.junit.jupiter.api.Test;

import java.util.List;

public class bucketSortTest {
    private final int maxThreads = Runtime.getRuntime().availableProcessors();

    @Test
    public void bucketSortHasBeenSorted() {
        final int ARRAY_SIZE = 2_000_000;

        BucketSortSolver sorter = new BucketSortSolver(maxThreads, ARRAY_SIZE);
        List<Long> sequentiallySortedList = sorter.sequential();
        List<Long> parallelSortedList = sorter.parallel();

        assertTrue(isSorted(sequentiallySortedList));
        assertTrue(isSorted(parallelSortedList));
    }

    @Test
    public void compareTimes() {
        final int ARRAY_SIZE = 2_000_000;

        BucketSortSolver sorter = new BucketSortSolver(maxThreads, ARRAY_SIZE);

        long timeTaken = measureTime(sorter::sequential);

        System.out.printf("Sequential sorting took %d ms\n", timeTaken);
    }

    /**
     * T
     */
    @Test
    public void methodTest() {
        final int ARRAY_SIZE = 2_000_000;
        BucketSortSolver sorter = new BucketSortSolver(maxThreads, ARRAY_SIZE);

        long parallelTimeTaken = measureTime(sorter::parallel);

        System.out.printf("Parallel sorting took %d ms\n", parallelTimeTaken);

        long sequentialTimeTaken = measureTime(sorter::sequential);
        System.out.printf("Sequential sorting took %d ms\n", sequentialTimeTaken);

        long difference = Math.abs(parallelTimeTaken - sequentialTimeTaken);
        System.out.printf("Difference between parallel and sequential time taken: %d ms\n", difference);
    }

    private boolean isSorted(List<Long> list) {
        for (int i = 1; i < list.size(); i++) {
            Long a = list.get(i - 1);
            Long b = list.get(i);
            if (a > b) {
                return false;
            }
        }
        return true;
    }

    private long measureTime(Util.Function function) {
        long before = System.currentTimeMillis();
        function.execute();
        long after = System.currentTimeMillis();

        return after - before;
    }
}