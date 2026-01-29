package com.stereometri.echovoice.voice.client;

import de.maxhenkel.voicechat.api.events.ClientSoundEvent;

public class MicEchoProcessor {

    private static final EchoState echo = new EchoState(
            120,
            0.35f,
            0.45f,
            48_000
    );

    public static void onMicFrame(ClientSoundEvent event) {
        short[] in = event.getRawAudio();

        if (in.length == 0) {
            echo.reset();
            return;
        }

        short[] out = echo.process(in);
        event.setRawAudio(out);
    }
}
