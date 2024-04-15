import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionEvaluatorTest {

    @Test
    public void testSimpleExpression() {
        assertEquals(11.0, Calculator.safeEvaluateExpression("2 + 3 * 3"), 0.0001);
    }

    @Test
    public void testExpressionWithParentheses() {
        assertEquals(20.0, Calculator.safeEvaluateExpression("(2 + 3) * 4"), 0.0001);
    }

    @Test
    public void testExpressionWithDecimals() {
        assertEquals(9.8596, Calculator.safeEvaluateExpression("3.14 * (6.28 / 2)"), 0.0001);
    }

    @Test
    public void testExpressionWithNegativeNumbers() {
        assertEquals(-11.0, Calculator.safeEvaluateExpression("-5 + (-2) * 3"), 0.0001);
    }

    @Test
    public void testInvalidExpression() {
        assertTrue(Double.isNaN(Calculator.safeEvaluateExpression("2 + * 3"))); // Некорректный оператор
    }

    @Test
    public void testDivisionByZero() {
        assertTrue(Double.isNaN(Calculator.safeEvaluateExpression("3 / 0"))); // Деление на ноль
    }

    @Test
    public void testInvalidParentheses() {
        assertTrue(Double.isNaN(Calculator.safeEvaluateExpression("(2 + 3 * 4"))); // Некорректное количество скобок
    }
}
