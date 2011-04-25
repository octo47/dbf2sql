package dbf2sql;

import java.util.Date;

/**
 * @author octo
 */
public class ExecutionResult {
    enum Result {
        OK,
        FAILED
    }


    private Date createDate = new Date();
    private Date stopDate = new Date();

    private String fileName;
    private Result result = Result.FAILED;
    private long processed = 0;

    public ExecutionResult(String fileName) {
        this.fileName = fileName;
    }

    public boolean isOk() {
        return result == Result.OK;
    }

    public boolean isFailed() {
        return result == Result.FAILED;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.stopDate = new Date();
        this.result = result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void incProcessed() {
        processed++;
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed(long processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "createDate=" + createDate +
                ", stopDate=" + stopDate +
                ", fileName='" + fileName + '\'' +
                ", result=" + result +
                ", processed=" + processed +
                '}';
    }
}
