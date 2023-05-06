package changwonNationalUniv.koko.enums;

public enum ClearState {

    Y("성공"), N("실패");

    private final String name;

    private ClearState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
