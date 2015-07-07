package game2d;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aperte on 06.07.2015.
 */
public class SoundController {
    // TODO: Add sfxvolume, bgvolume, voicevolume
    double volume;
    // TODO: Need better volume structure (messy down there)
    boolean muted;
    boolean enabled;
    final String SFX_PATH = "sfx/%s";
    HashMap<String, PlayableSound> soundMap = new HashMap<>();
    ArrayList<SoundPlayer> playing = new ArrayList<>();

    public SoundController() {
        Settings config = new Settings("config.properties");
        enabled = Boolean.parseBoolean(config.get("sound_enabled", "true"));
        if (enabled) {
            // TODO: Get volume from config?
            volume = 0.05;
            // TODO: Get sounds from config?
            try {
                newSound("bgm", "Cold_Silence.mp3");
                newSoundEffect("beep", "beep.wav");
            } catch (IOException ioe) {
                System.out.println("Error when reading sound files: turned off sound.");
                enabled = false;
                return;
            }
        } else {
            return;
        }
    }

    void newSoundEffect(String id, String uri) throws IOException {
        // TODO: Error handling
        putSound(id, new SoundEffect(getStringURI(String.format(SFX_PATH, uri)), volume, this));
    }

    void newSound(String id, String uri) throws IOException {
        // TODO: Error handling
        putSound(id, new SoundPlayer(getStringURI(String.format(SFX_PATH, uri)), volume, this));
    }

    String getStringURI(String uri) throws IOException {
        File tmp = new File(uri);
        return tmp.toURI().toString();
    }

    PlayableSound getSound(String id) {
        // TODO: Error handling
        return soundMap.get(id);
    }

    PlayableSound putSound(String id, PlayableSound sound) {
        // TODO: Error handling
        return soundMap.put(id, sound);
    }

    public void playSound(String id) {
        if (enabled) {
            PlayableSound sound = getSound(id);
            sound.play();
            if (sound instanceof SoundPlayer) {
                playing.add((SoundPlayer) sound);
            }
        }
    }

    public void mutePlaying() {
        if (muted) {
            for (SoundPlayer sp: playing) {
                sp.mediaPlayer.setVolume(volume);
                muted = false;
            }
        } else {
            for (SoundPlayer sp: playing) {
                sp.mediaPlayer.setVolume(0);
                muted = true;
            }
        }
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }
}

/* Class for playing AudioClips, ment for short sounds with no control - fire and forget. */
class SoundEffect extends PlayableSound {
    AudioClip clip;

    SoundEffect(String uri, double volume, SoundController master) {
        super(master);
        this.clip = new AudioClip(uri);
    }

    void play() {
        // TODO: Add a short cooldown so we don't play several soundeffects too fast(?)
        if (master.muted) return;
        clip.setVolume(master.volume);
        new Thread(() -> clip.play()).start();
    }

}

/* Class for playing Media sound, ment for longer sounds with control possible. */
class SoundPlayer extends PlayableSound {
    Media media;
    MediaPlayer mediaPlayer;
    Thread th;

    SoundPlayer(String uri, double volume, SoundController master) throws MediaException {
        super(master);
        this.volume = volume;
        this.media = new Media(uri);
    }

    void play() {
        th = new Thread(() -> {
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
        });
        th.start();
    }
}

/* Abstract class for gathering the playable sounds under one polymorphism */
abstract class PlayableSound {
    SoundController master;
    double volume; // implementation choose themselves how to handle volume

    abstract void play();

    PlayableSound(SoundController master) {
        this.master = master;
    }
}
