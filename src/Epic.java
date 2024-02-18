import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public void addSubTaskToEpic(SubTask subTask) {
        subTask.setEpicId(this.idNumber);
        subTasksId.add(subTask.idNumber);
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

}
