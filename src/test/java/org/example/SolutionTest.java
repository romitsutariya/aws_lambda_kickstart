package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    private final Solution solution=new Solution();
    @Test
    public void superiorElements(){
        int[] a={1, 2, 2, 1};
        List<Integer> integers = solution.superiorElements(a);
        assertEquals(List.of(1,2),integers);
    }
}