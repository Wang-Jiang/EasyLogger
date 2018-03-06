package space.wangjiang.easylogger.ui;

public class DefaultUI implements UI {

    @Override
    public String topBorder() {
        return "╔═════════════════════════════════════════════════════════════════════════════════════";
    }

    @Override
    public String leftBorder() {
        return "║ ";
    }

    @Override
    public String dividingLine() {
        return "╟─────────────────────────────────────────────────────────────────────────────────────";
    }

    @Override
    public String bottomBorder() {
        return "╚═════════════════════════════════════════════════════════════════════════════════════";
    }

}
