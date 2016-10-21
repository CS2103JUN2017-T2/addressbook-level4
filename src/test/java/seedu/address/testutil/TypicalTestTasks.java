package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskScheduler;
import seedu.address.model.task.*;

/**
 *
 */
public class TypicalTestTasks {

    public static TestTask alice, benson, carl, daniel, elle, fiona, george, hoon, ida, overdue;

    public TypicalTestTasks() {
        try {
            alice =  new TaskBuilder().withName("Career Fair").withAddress("NUS COM1")
                    .withEndDate("11 Oct 2016").withStartDate("07 Oct 2016").withTags("Event").build();
            benson = new TaskBuilder().withName("Groupwork Project").withAddress("")
                    .withEndDate("14 Oct 2016").withStartDate("").withTags("Deadline").build(); // deadline task
            carl = new TaskBuilder().withName("Project Discussion").withAddress("Technoedge")
                    .withEndDate("21 Oct 2016").withStartDate("14 Oct 2016").withTags("Event").build();
            daniel = new TaskBuilder().withName("Groupwork Discussion").withAddress("")
                    .withEndDate("").withStartDate("").withTags().build(); // floating task
            elle = new TaskBuilder().withName("CS2103 Lecture").withAddress("ICube Lecture Hall")
                    .withEndDate("311247").withStartDate("010417").withTags("Event").build();
            fiona = new TaskBuilder().withName("Send kid to NUS").withAddress("NUS")
                    .withEndDate("010451").withStartDate("010847").withTags("Event").build();
            george = new TaskBuilder().withName("Project Briefing").withAddress("ICube Lecture Hall")
                    .withEndDate("020491").withStartDate("020451").withTags("Event").build();
            //Manually added
            hoon = new TaskBuilder().withName("Wildlife Photoshoot").withAddress("Wildlife Reserves")
                    .withEndDate("today").withStartDate("yesterday").build();
            ida = new TaskBuilder().withName("University Graduation").withAddress("University Cultural Centre")
                    .withEndDate("15 July 2016").withStartDate("7 July 2016").build();
            overdue = new TaskBuilder().withName("Overdue Task Colour Test").withAddress("At ICube Lecture Hall")
                    .withEndDate("071016").withStartDate("071016").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadTaskSchedulerWithSampleData(TaskScheduler ab) {

        try {
            ab.addTask(new EventTask(alice));
            ab.addTask(new DeadlineTask(benson));
            ab.addTask(new Task(carl));
            ab.addTask(new FloatingTask(daniel));
            ab.addTask(new Task(elle));
            ab.addTask(new Task(fiona));
            ab.addTask(new Task(george));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public TaskScheduler getTypicalTaskScheduler(){
        TaskScheduler ab = new TaskScheduler();
        loadTaskSchedulerWithSampleData(ab);
        return ab;
    }
}
