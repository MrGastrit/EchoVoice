package stereometric.echovoice.voice.client.processing;

import stereometric.echovoice.voice.client.dsp.AudioMath;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DispersiveVoiceProcessor {

    private static final int SAMPLE_RATE = 48_000;

    private static final float NEAR_CUTOFF_HZ = 9000f;
    private static final float FAR_CUTOFF_HZ = 2200f;

    private final Map<UUID, OnePoleLowPassState> lpState = new HashMap<>();

    public short[] processFrame(UUID sender, short[] in, double dist, float maxDist) {
        float x = (float) (dist / maxDist);

        if (x >= 1.2f) {
            return silence(in.length);
        }

        float gain = gainCurve(x);

        float cutoff = cutoffCurve(x);

        OnePoleLowPassState st = lpState.computeIfAbsent(sender, k -> new OnePoleLowPassState());
        float a = OnePoleLowPassState.alphaFromCutoff(cutoff, SAMPLE_RATE);

        short[] out = new short[in.length];
        for (int i = 0; i < in.length; i++) {
            float s = in[i] * gain;

            st.y += a * (s - st.y);

            out[i] = AudioMath.toShort(st.y);
        }

        return out;
    }

    private static float gainCurve(float x) {

        if (x < 0f) {
            x = 0f;
        }
        if (x > 1.1f) {
            x = 1.1f;
        }

        float r = x;
        float k = 6.0f;
        float g = 1.0f / (1.0f + k * r * r);

        if (x > 1.0f) {
            g *= (1.1f - x) / 0.1f;
        }

        return g;
    }

    private static float cutoffCurve(float x) {

        float t = x;
        if (t < 0f) {
            t = 0f;
        }
        if (t > 1f) {
            t = 1f;
        }

        t = t * t;

        return NEAR_CUTOFF_HZ + (FAR_CUTOFF_HZ - NEAR_CUTOFF_HZ) * t;
    }

    private static short[] silence(int n) {
        return new short[n];
    }

    private static final class OnePoleLowPassState {
        float y = 0f;

        static float alphaFromCutoff(float cutoffHz, int sr) {
            double a = 1.0 - Math.exp(-2.0 * Math.PI * cutoffHz / sr);
            return (float) a;
        }
    }
}
