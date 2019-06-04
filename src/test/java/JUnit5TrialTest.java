import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JUnit5TrialTest {

    @Test
    void justAnExample() {
        System.out.println("GLORY!");
    }

    @Test
    public void testAdd(){
        double value1 = 3;
        int value2 = 3;
        double result = value1 + value2;
        assertTrue(result == 6);
    }

    @Test
    public void testAdd2(){
        double value1 = 3;
        int value2 = 3;
        double result = value1 + value2;
        assertTrue(result == 7);
    }
}
