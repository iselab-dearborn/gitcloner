package edu.iselab.cloner.monitor;

import java.util.ArrayList;
import java.util.List;

import edu.iselab.cloner.model.Project;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitProgressMonitor {

    protected List<ProgressListener> listeners = new ArrayList<>();

    public void add(ProgressListener listener) {
        listeners.add(listener);
    }

    public void message(Project project, String message) {

        for (ProgressListener listener : listeners) {
            listener.message(project, message);
        }
    }
}
