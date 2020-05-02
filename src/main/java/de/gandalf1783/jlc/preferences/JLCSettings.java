package de.gandalf1783.jlc.preferences;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class JLCSettings implements Serializable {

    /**
     *
     */
    private final long serialVersionUID = 8394221355275389116L;

    private String project_path = "";
    private String version = "";
    private Timestamp latest_save;

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
