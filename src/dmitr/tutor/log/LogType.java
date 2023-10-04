package dmitr.tutor.log;

public enum LogType {

    INFO("INFO"), ERROR("ERROR"), WARNING("WARNING");

    private final String caption;

    private LogType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
