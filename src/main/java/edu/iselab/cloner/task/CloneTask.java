package edu.iselab.cloner.task;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.eclipse.jgit.lib.TextProgressMonitor;

import edu.iselab.cloner.model.Project;
import edu.iselab.cloner.monitor.GitProgressMonitor;
import edu.iselab.cloner.util.GitUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CloneTask extends TextProgressMonitor implements Callable<Void> {

    protected Project project;

    protected Path output;

    protected GitProgressMonitor gitProgressListener;

    @Override
    public Void call() throws Exception {

        GitUtils.clone(project, output, this);

        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected void onUpdate(String taskName, int workCurr) {
        StringBuilder s = new StringBuilder();
        format(s, taskName, workCurr);
        send(s);
    }

    /** {@inheritDoc} */
    @Override
    protected void onEndTask(String taskName, int workCurr) {
        StringBuilder s = new StringBuilder();
        format(s, taskName, workCurr);
        s.append("\n"); //$NON-NLS-1$
        send(s);
    }

    private void format(StringBuilder s, String taskName, int workCurr) {
        s.append("\r"); //$NON-NLS-1$
        s.append(taskName);
        s.append(": "); //$NON-NLS-1$
        while (s.length() < 25)
            s.append(' ');
        s.append(workCurr);
    }

    /** {@inheritDoc} */
    @Override
    protected void onUpdate(String taskName, int cmp, int totalWork, int pcnt) {
        StringBuilder s = new StringBuilder();
        format(s, taskName, cmp, totalWork, pcnt);
        send(s);
    }

    /** {@inheritDoc} */
    @Override
    protected void onEndTask(String taskName, int cmp, int totalWork, int pcnt) {
        StringBuilder s = new StringBuilder();
        format(s, taskName, cmp, totalWork, pcnt);
        s.append("\n"); //$NON-NLS-1$
        send(s);
    }
    
    public void send(StringBuilder s) {
        send(s.toString());
    }

    public void send(String message) {
        gitProgressListener.message(project, message);
    }
    
    private void format(StringBuilder s, String taskName, int cmp,
            int totalWork, int pcnt) {
        s.append(taskName);
        s.append(": "); //$NON-NLS-1$
        while (s.length() < 25)
            s.append(' ');

        String endStr = String.valueOf(totalWork);
        String curStr = String.valueOf(cmp);
        while (curStr.length() < endStr.length())
            curStr = " " + curStr; //$NON-NLS-1$
        if (pcnt < 100)
            s.append(' ');
        if (pcnt < 10)
            s.append(' ');
        s.append(pcnt);
        s.append("% ("); //$NON-NLS-1$
        s.append(curStr);
        s.append("/"); //$NON-NLS-1$
        s.append(endStr);
        s.append(")\r"); //$NON-NLS-1$
    }

}
