package stereometric.echovoice.voice.client;

import de.maxhenkel.voicechat.api.Position;
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import stereometric.echovoice.voice.client.processing.DispersiveVoiceProcessor;

import java.util.UUID;

public class ReceivePipeline {

    private static final DispersiveVoiceProcessor processor = new DispersiveVoiceProcessor();

    public static void onLocational(ClientReceiveSoundEvent.LocationalSound event) {
        short[] in = event.getRawAudio();
        if (in.length == 0) {
            return;
        }

        LocalPlayer listener = Minecraft.getInstance().player;
        if (listener == null) {
            return;
        }

        Position p = event.getPosition();
        double sx = p.getX();
        double sy = p.getY();
        double sz = p.getZ();

        double dist = listener.distanceToSqr(sx, sy, sz);
        double distance = Math.sqrt(dist);

        float maxHearDistance = event.getDistance();
        UUID sender = event.getId();

        short[] out = processor.processFrame(sender, in, distance, maxHearDistance);
        event.setRawAudio(out);
    }

    public static void onEntity(ClientReceiveSoundEvent.EntitySound event) {
        short[] in = event.getRawAudio();
        if (in.length == 0) {
            return;
        }

        LocalPlayer listener = Minecraft.getInstance().player;
        var level = Minecraft.getInstance().level;
        if (listener == null || level == null) {
            return;
        }

        UUID entId = event.getEntityId();
        Entity src = findEntity(level, entId);
        if (src == null) {
            return;
        }

        double dist = listener.distanceTo(src);

        float maxHearDistance = event.getDistance();
        UUID sender = event.getId();

        boolean whisper = event.isWhispering();
        if (whisper) {
            maxHearDistance *= 0.6f;
        }

        short[] out = processor.processFrame(sender, in, dist, maxHearDistance);
        event.setRawAudio(out);
    }

    private static Entity findEntity(ClientLevel level, UUID id) {
        var player = level.getPlayerByUUID(id);
        if (player != null) {
            return player;
        }

        for (Entity ent : level.entitiesForRendering()) {
            if (id.equals(ent.getUUID())) {
                return ent;
            }
        }

        return null;
    }
}
