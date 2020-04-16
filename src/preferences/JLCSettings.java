package preferences;

import java.io.Serializable;
import java.sql.Timestamp;

public class JLCSettings implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8394221355275389116L;

    public String project_path = "";
    public String version = "";
    public Timestamp latest_save;
    public JLCSettings() {}

    public String getProject_path() {
        return project_path;
    }
    public void setProject_path(String project_path) {
        this.project_path = project_path;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public Timestamp getLatest_save() {
        return latest_save;
    }
    public void setLatest_save(Timestamp latest_save) {
        this.latest_save = latest_save;
    }
}
