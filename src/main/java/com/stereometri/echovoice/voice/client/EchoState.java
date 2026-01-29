package com.stereometri.echovoice.voice.client;

public class EchoState {

    private final float feedback;
    private final float mix;

    private final short[] delay;
    private int index;

    public EchoState(int delayMs, float feedback, float mix, int sampleRate) {
        this.feedback = feedback;
        this.mix = mix;

        int delaySamples = Math.max(1, (int) ((long) sampleRate * delayMs / 1000L));
        this.delay = new short[delaySamples];
        this.index = 0;
    }

    public void reset() {
        for (int i = 0; i < delay.length; i++) {
            delay[i] = 0;
        }

        index = 0;
    }

    public short[] process(short[] frame) {
        short[] out = new short[frame.length];

        for (int i = 0; i < frame.length; i++) {
            int dry = frame[i];
            int delayed = delay[index];

            int wet = (int) (dry + delayed * feedback);

            int mixed = (int) (dry * (1f - mix) + wet * mix);

            out[i] = (short) clamp16(mixed);

            delay[index] = (short) clamp16(wet);

            index++;
            if (index >= delay.length) {
                index = 0;
            }
        }

        return out;
    }

    public static int clamp16(int v) {
        if (v > Short.MAX_VALUE) return Short.MAX_VALUE;
        if (v < Short.MIN_VALUE) return Short.MIN_VALUE;
        return v;
    }
}
