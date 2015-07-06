import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aperte on 06.07.2015.
 */
public class SoundController {
    double volume;
    boolean muted;
    HashMap<String, PlayableSound> soundMap = new HashMap<>();
    ArrayList<SoundPlayer> playing = new ArrayList<>();

    SoundController() {
        volume = 0.1;
        newSound("bgm", "Cold_Silence.mp3");
        newSoundEffect("beep", "bgm_menu.wav");
    }

    void newSoundEffect(String id, String uri) {
        putSound(id, new SoundEffect(getClass().getResource(uri).toExternalForm(), volume));
    }

    void newSound(String id, String uri) {
        putSound(id, new SoundPlayer(getClass().getResource(uri).toExternalForm(), volume));
    }

    PlayableSound getSound(String id) {
        // TODO: Error handling
        return soundMap.get(id);
    }

    PlayableSound putSound(String id, PlayableSound sound) {
        // TODO: Error handling
        return soundMap.put(id, sound);
    }

    void playSound(String id) {
        PlayableSound sound = getSound(id);
        sound.play();
        if (sound instanceof SoundPlayer) {
            playing.add((SoundPlayer)sound);
        }
    }

    void mutePlaying() {
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

    void setVolume(Double volume) {
        this.volume = volume;
    }
}

/* Class for playing AudioClips, ment for short sounds with no control - fire and forget. */
class SoundEffect extends PlayableSound {
    AudioClip clip;

    SoundEffect(String uri, double volume) {
        super(volume);
        this.clip = new AudioClip(uri);
    }

    @Override
    public void run() {
        clip.play();
    }
}

/* Class for playing Media sound, ment for longer sounds with control possible. */
class SoundPlayer extends PlayableSound {
    Media media;
    MediaPlayer mediaPlayer;

    SoundPlayer(String uri, double volume) {
        super(volume);
        this.media = new Media(uri);
    }

    @Override
    public void run() {
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setOnReady(() -> mediaPlayer.play());
    }
}

/* Abstract class for gathering the playable sounds under one polymorphism */
abstract class PlayableSound extends Thread {
    double volume;
    void play() {
        this.start();
    }

    PlayableSound(double volume) {
        this.volume = volume;
    }
}
