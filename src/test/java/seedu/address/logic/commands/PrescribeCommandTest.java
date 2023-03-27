package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICATION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEDICATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.prescription.Cost;
import seedu.address.model.prescription.Medication;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.prescription.Prescription;
import seedu.address.testutil.PersonBuilder;

public class PrescribeCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Nric amyNric = new Nric(VALID_NRIC_AMY);
    private Nric bobNric = new Nric(VALID_NRIC_BOB);
    private Prescription amyPrescription = new Prescription(new Medication(VALID_MEDICATION_AMY),
            new Cost(VALID_COST_AMY));
    private Prescription bobPrescription = new Prescription(new Medication(VALID_MEDICATION_BOB),
            new Cost(VALID_COST_BOB));

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PrescribeCommand(null, null));
        assertThrows(NullPointerException.class, () -> new PrescribeCommand(amyNric, null));
        assertThrows(NullPointerException.class, () -> new PrescribeCommand(null, amyPrescription));
    }

    @Test
    public void execute_addPrescription_success() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(targetPerson)
                .withPrescription(VALID_MEDICATION_BOB, VALID_COST_BOB).build();
        PrescribeCommand prescribeCommand = new PrescribeCommand(bobNric, bobPrescription);

        String expectedMessage = String.format(PrescribeCommand.MESSAGE_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, editedPerson);

        assertCommandSuccess(prescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addPrescription2_success() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(targetPerson)
                .withPrescription(VALID_MEDICATION_AMY, VALID_COST_AMY).build();
        PrescribeCommand prescribeCommand = new PrescribeCommand(amyNric, amyPrescription);

        String expectedMessage = String.format(PrescribeCommand.MESSAGE_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, editedPerson);

        assertCommandSuccess(prescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        PrescribeCommand prescribeAmy = new PrescribeCommand(amyNric, amyPrescription);

        // same object -> returns true
        assertTrue(prescribeAmy.equals(prescribeAmy));

        // same values -> return true
        assertTrue(prescribeAmy.equals(new PrescribeCommand(amyNric, amyPrescription)));

        // different types -> returns false
        assertFalse(prescribeAmy.equals(1));

        // null -> returns false
        assertFalse(prescribeAmy.equals(null));

        Prescription bobPrescription = new Prescription(new Medication(VALID_MEDICATION_BOB),
                new Cost(VALID_COST_BOB));

        // different values -> returns false
        assertFalse(prescribeAmy.equals(new PrescribeCommand(bobNric, amyPrescription)));
        assertFalse(prescribeAmy.equals(new PrescribeCommand(amyNric, bobPrescription)));
        assertFalse(prescribeAmy.equals(new PrescribeCommand(bobNric, bobPrescription)));
    }
}
