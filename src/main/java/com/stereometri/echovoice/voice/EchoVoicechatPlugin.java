package com.stereometri.echovoice.voice;

import com.stereometri.echovoice.EchoVoice;
import com.stereometri.echovoice.voice.client.MicEchoProcessor;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientSoundEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;

@ForgeVoicechatPlugin
public class EchoVoicechatPlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return EchoVoice.MOD_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
        EchoVoice.LOGGER.info("Devastation voice chat plugin initialized!");
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        // TODO register your events

        registration.registerEvent(ClientSoundEvent.class, MicEchoProcessor::onMicFrame);
    }
}
