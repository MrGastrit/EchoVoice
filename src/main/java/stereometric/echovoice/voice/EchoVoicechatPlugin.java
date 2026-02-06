package stereometric.echovoice.voice;

import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
import stereometric.echovoice.EchoVoice;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import stereometric.echovoice.voice.client.ReceivePipeline;

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

        registration.registerEvent(ClientReceiveSoundEvent.LocationalSound.class, ReceivePipeline::onLocational);
        registration.registerEvent(ClientReceiveSoundEvent.EntitySound.class, ReceivePipeline::onEntity);
    }
}
