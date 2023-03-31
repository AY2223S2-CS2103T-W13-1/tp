package seedu.address.model.prescription;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

class CostTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Cost(null));
    }

    @Test
    public void isValidCost_valid_returnsTrue() {
        assertTrue(Cost.isValidCost("0"));
        assertTrue(Cost.isValidCost("1"));
        assertTrue(Cost.isValidCost("1.0"));
        assertTrue(Cost.isValidCost("1.00"));
    }

    @Test
    public void isValidCost_invalid_returnsFalse() {
        assertFalse(Cost.isValidCost("1."));
        assertFalse(Cost.isValidCost("-1"));
    }

}