package edu.iselab.cloner.monitor;

import edu.iselab.cloner.model.Project;

public interface ProgressListener {

    public void message(Project project, String message);

}
