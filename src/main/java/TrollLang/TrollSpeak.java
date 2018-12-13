package TrollLang;

public enum TrollSpeak {
        LOGEVENT ("LogEvent:\n"),
        IF (""),
        GOTO ("Goto:\n"),
        SET ("Set:\n"),
        WAIT ("Wait:\n");

        private String prefix;

        TrollSpeak(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }

}
