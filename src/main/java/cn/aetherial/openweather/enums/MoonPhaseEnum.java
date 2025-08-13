package cn.aetherial.openweather.enums;

public enum MoonPhaseEnum {

    NEW_MOON(0, "新月"),
    FIRST_QUARTER(0.25, "上弦月"),
    FULL_MOON(0.5, "满月"),
    LAST_QUARTER(0.75, "下弦月")
    ;

    private final double phase;
    private final String description;

    MoonPhaseEnum(double phase, String description) {
        this.phase = phase;
        this.description = description;
    }

    public static MoonPhaseEnum getByPhase(double phaseValue) {
        double normalizedPhase = phaseValue >= 1.0 ? 0.0 : phaseValue;

        for (MoonPhaseEnum moonPhase : values()) {
            if (Math.abs(moonPhase.phase - normalizedPhase) < 0.01) {
                return moonPhase;
            }
        }
        MoonPhaseEnum closest = NEW_MOON;
        double minDiff = Double.MAX_VALUE;

        for (MoonPhaseEnum moonPhase : values()) {
            double diff = Math.abs(moonPhase.phase - normalizedPhase);
            if (diff < minDiff) {
                minDiff = diff;
                closest = moonPhase;
            }
        }

        return closest;
    }

    public double getPhase() {
        return phase;
    }

    public String getDescription() {
        return description;
    }
}