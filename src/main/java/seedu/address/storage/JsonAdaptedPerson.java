package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Address;
import seedu.address.model.person.Doctor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Medication;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String nric;
    private final String address;
    private final String medication;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();
    private final ArrayList<JsonAdaptedAppointment> patientAppointments = new ArrayList<>();
    private final String role;


    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("nric") String nric,
            @JsonProperty("address") String address, @JsonProperty("medication") String medication,
            @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
            @JsonProperty("patientAppointments") ArrayList<JsonAdaptedAppointment> patientAppointments,
            @JsonProperty("role") String role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nric = nric;
        this.address = address;
        this.medication = medication;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
        if (patientAppointments != null) {
            this.patientAppointments.addAll(patientAppointments);
        }
        this.role = role;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        nric = source.getNric().nric;
        address = source.getAddress().value;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));

        if (source.isPatient()) {
            Patient sourcePatient = (Patient) source;

            medication = sourcePatient.getMedication().value;
            patientAppointments.addAll(sourcePatient.getPatientAppointments().stream()
                    .map(JsonAdaptedAppointment::new)
                    .collect(Collectors.toList()));
        } else {
            Doctor sourceDoctor = (Doctor) source;

            medication = "";
            patientAppointments.addAll(sourceDoctor.getPatientAppointments().stream()
                    .map(JsonAdaptedAppointment::new)
                    .collect(Collectors.toList()));
        }
        role = source.getRole().role;

    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName()));
        }
        if (!Nric.isValidNric(nric)) {
            throw new IllegalValueException(Nric.MESSAGE_CONSTRAINTS);
        }
        final Nric modelNric = new Nric(nric);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);
        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Role modelRole = new Role(role);

        // Return a new Patient object if Role field is "Patient". Otherwise, return new Doctor object.
        if (role.toString().equals("Patient")) {
            final ArrayList<Appointment> appointments = new ArrayList<>();
            for (JsonAdaptedAppointment appointment : patientAppointments) {
                appointments.add(appointment.toModelType());
            }

            if (medication == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                        Medication.class.getSimpleName()));
            }
            if (!Medication.isValidMedication(medication)) {
                throw new IllegalValueException(Medication.MESSAGE_CONSTRAINTS);
            }

            final Medication modelMedication = new Medication(medication);
            final ArrayList<Appointment> modelAppointments = new ArrayList<>(appointments);

            return new Patient(modelName, modelPhone, modelEmail, modelNric, modelAddress, modelMedication, modelTags,
                    modelAppointments, modelRole);
        } else {
            final ArrayList<Appointment> appointments = new ArrayList<>();
            for (JsonAdaptedAppointment appointment : patientAppointments) {
                appointments.add(appointment.toModelType());
            }
            final ArrayList<Appointment> modelAppointments = new ArrayList<>(appointments);
            return new Doctor(modelName, modelPhone, modelEmail, modelNric, modelAddress, modelTags, modelAppointments,
                    modelRole);
        }
    }
}
