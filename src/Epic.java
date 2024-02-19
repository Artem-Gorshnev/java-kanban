import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, StatusTask.NEW);
    }

    public void addSubTaskToEpic(SubTask subTask) {
        subTask.setEpicId(getIdNumber());
        addSubTaskId(subTask.getIdNumber());
    }

    public void clearSubTaskIds() {
        subTaskIds.clear();
    }

    public void removeSubTaskId(Integer idNumber) {
        subTaskIds.remove(idNumber);
    }

    public void addSubTaskId(Integer idNumber) {
        subTaskIds.add(idNumber);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

}
