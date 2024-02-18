public class SubTask extends Task {

    int epicId;

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description) {
        super(taskName, description);
    }
}
