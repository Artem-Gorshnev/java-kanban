public class SubTask extends Task {

    private int epicId;

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        setEpicId(epicId);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
