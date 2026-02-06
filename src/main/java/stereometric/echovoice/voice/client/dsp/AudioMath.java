package stereometric.echovoice.voice.client.dsp;

public class AudioMath {

    private AudioMath() {}

    public static short toShort(float v) {
        if (v > Short.MAX_VALUE) return Short.MAX_VALUE;
        if (v < Short.MIN_VALUE) return Short.MIN_VALUE;

        return (short) v;
    }
}
