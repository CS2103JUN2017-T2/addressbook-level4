package seedu.multitasky.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

// @@author A0132788U
/**
 * An Immutable EntryBook that is serializable to XML format
 */
@XmlRootElement(name = "entrybook")
public class XmlSerializableEntryBook implements ReadOnlyEntryBook {

    @XmlElement
    private List<XmlAdaptedEntry> events;
    @XmlElement
    private List<XmlAdaptedEntry> floatingTasks;
    @XmlElement
    private List<XmlAdaptedEntry> deadlines;
    @XmlElement
    private List<XmlAdaptedEntry> archive;
    @XmlElement
    private List<XmlAdaptedEntry> bin;
    @XmlElement
    private List<XmlAdaptedEntry> active;
    @XmlElement
    private List<XmlAdaptedTag> tags;

    /**
     * Creates an empty XmlSerializableEntryBook. This empty constructor is
     * required for marshalling.
     */
    public XmlSerializableEntryBook() {
        events = new ArrayList<>();
        floatingTasks = new ArrayList<>();
        deadlines = new ArrayList<>();
        archive = new ArrayList<>();
        bin = new ArrayList<>();
        active = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableEntryBook(ReadOnlyEntryBook src) {
        this();
        events.addAll(src.getEventList().stream().map(XmlAdaptedEntry::new).collect(Collectors.toList()));
        deadlines.addAll(src.getDeadlineList().stream().map(XmlAdaptedEntry::new)
                .collect(Collectors.toList()));
        floatingTasks.addAll(src.getFloatingTaskList().stream().map(XmlAdaptedEntry::new)
                .collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
    }

    @Override
    public ObservableList<ReadOnlyEntry> getEventList() {
        final ObservableList<Entry> events = this.events.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(events);
    }

    @Override
    public ObservableList<ReadOnlyEntry> getDeadlineList() {
        final ObservableList<Entry> deadlines = this.deadlines.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(deadlines);
    }

    @Override
    public ObservableList<ReadOnlyEntry> getFloatingTaskList() {
        final ObservableList<Entry> floatingTasks = this.floatingTasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(floatingTasks);
    }

    @Override
    public ObservableList<ReadOnlyEntry> getAllEntries() {
        final ObservableList<Entry> active = this.active.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(active);
    }

    @Override
    public ObservableList<Tag> getTagList() {
        final ObservableList<Tag> tags = this.tags.stream().map(t -> {
            try {
                return t.toModelType();
            } catch (IllegalValueException e) {
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(tags);
    }

}
