package mutationoperatortest;

import mutation.ConditionalBoundaryOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionalBoundaryOperatorTest
{
    private static CtType<?> type;

    @BeforeAll
    public static void setup() throws IOException
    {
        type = TestUtils.mutateTestClass(new ConditionalBoundaryOperator(null, false));
    }
    @ParameterizedTest
    @ValueSource(strings = {"incrementCounter", "addToCounter", "subtractFromCounter", "getCounter", "getCounterObject", "abstractMethod", "main"})
    public void testUnchangedMethods(String methodName)
    {
        CtMethod method = type.getMethodsByName(methodName).get(0);
        assertEquals(TestUtils.getOriginalMethodContent(methodName), method.toString());
    }
    @Test
    public void testMutationIsCounterHigh()
    {
        CtMethod method = type.getMethod("isCounterHigh");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return (counter) >= 10", method.getBody().getStatements().get(0).toString());
    }
    @Test
    public void testMutationIsCounterCloseToZero()
    {
        CtMethod method = type.getMethod("isCounterCloseToZero");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return ((counter) <= 5) && ((counter) >= (-5))", method.getBody().getStatements().get(0).toString());
    }
    @Test
    public void testMutationIsCounterFarFromZero()
    {
        CtMethod method = type.getMethod("isCounterFarFromZero");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return ((counter) >= 10) || ((counter) <= (-10))", method.getBody().getStatements().get(0).toString());
    }
    @Test
    public void testMutationIsCounterVeryFarFromZero()
    {
        CtMethod method = type.getMethod("isCounterVeryFarFromZero");
        assertEquals(1, method.getBody().getStatements().size());
        assertEquals("return ((counter) > 100) || ((counter) < (-100))", method.getBody().getStatements().get(0).toString());
    }
    @Test
    public void testRevert() throws IOException
    {
        CtType<?> type = TestUtils.mutateTestClass(new ConditionalBoundaryOperator(null, true));
        assertEquals(TestUtils.getOriginalClassContent(), type.toString());
    }
}
