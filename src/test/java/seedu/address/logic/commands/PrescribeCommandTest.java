package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import seedu.address.model.prescription.Medication;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PrescribeCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Nric amyNric = new Nric(VALID_NRIC_AMY);
    private Nric bobNric = new Nric(VALID_NRIC_BOB);
    private Medication amyMedication = new Medication(VALID_MEDICATION_AMY, ); // " " format
    private Medication bobMedication = new Medication(VALID_MEDICATION_BOB, ); // "qty medication" format

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PrescribeCommand(null, null));
        assertThrows(NullPointerException.class, () -> new PrescribeCommand(amyNric, null));
        assertThrows(NullPointerException.class, () -> new PrescribeCommand(null, amyMedication));
    }

    @Test
    public void execute_addMedication_success() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(targetPerson).withMedication(bobMedication.value).build();
        PrescribeCommand prescribeCommand = new PrescribeCommand(amyNric, bobMedication);

        String expectedMessage = String.format(PrescribeCommand.MESSAGE_ADD_PRESCRIBE_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, editedPerson);

        assertCommandSuccess(prescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeMedication_success() {
        Person targetPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(targetPerson).withMedication(amyMedication.value).build();
        PrescribeCommand prescribeCommand = new PrescribeCommand(amyNric, amyMedication);

        String expectedMessage = String.format(PrescribeCommand.MESSAGE_DELETE_PRESCRIBE_SUCCESS, editedPerson);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(targetPerson, editedPerson);

        assertCommandSuccess(prescribeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        PrescribeCommand prescribeAmy = new PrescribeCommand(amyNric, amyMedication);

        // same object -> returns true
        assertTrue(prescribeAmy.equals(prescribeAmy));

        // same values -> return true
        assertTrue(prescribeAmy.equals(new PrescribeCommand(amyNric, amyMedication)));

        // different types -> returns false
        assertFalse(prescribeAmy.equals(1));

        // null -> returns false
        assertFalse(prescribeAmy.equals(null));

        // different values -> returns false
        assertFalse(prescribeAmy.equals(new PrescribeCommand(bobNric, amyMedication)));
        assertFalse(prescribeAmy.equals(new PrescribeCommand(amyNric, bobMedication)));
        assertFalse(prescribeAmy.equals(new PrescribeCommand(bobNric, bobMedication)));
    }
}
