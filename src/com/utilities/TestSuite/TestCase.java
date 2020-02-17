package com.utilities.TestSuite;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
public class TestCase {
    @Test
    void testSomething(){
        int a = 2; //ARRANGE
        int b = 3;

        int result = a + b; //ACT

        assertEquals(5,result); //ASSERT
    }

    @Test
    void testSomething2(){
        int a = 2; //ARRANGE
        int b = 3;

        int result = a + b; //ACT

        assertNotEquals(6,result); //ASSERT
    }
}
