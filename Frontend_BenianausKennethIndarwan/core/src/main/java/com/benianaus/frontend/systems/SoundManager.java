package com.benianaus.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;

    private Map<String, Sound> soundMap;
    private Map<String, Music> musicMap;
    private Music currentBGM;

    private SoundManager() {
        soundMap = new HashMap<>();
        musicMap = new HashMap<>();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public Sound loadSound(String key, String filename) {
        if (!soundMap.containsKey(key)) {
            if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal(filename));
                soundMap.put(key, sound);
            } else {
                return null;
            }
        }
        return soundMap.get(key);
    }

    public Music loadMusic(String key, String filename) {
        if (!musicMap.containsKey(key)) {
            if (Gdx.files != null && Gdx.files.internal(filename).exists()) {
                Music music = Gdx.audio.newMusic(Gdx.files.internal(filename));
                musicMap.put(key, music);
            } else {
                return null;
            }
        }
        return musicMap.get(key);
    }

    public void playSound(String key) {
        playSound(key, 1.0f);
    }

    public void playSound(String key, float volume) {
        Sound sound = soundMap.get(key);
        if (sound != null) {
            sound.play(volume);
        }
    }

    public void playMusic(String key, float volume, boolean looping) {
        if (currentBGM != null && currentBGM.isPlaying()) {
            currentBGM.stop();
        }
        Music music = musicMap.get(key);
        if (music != null) {
            music.setLooping(looping);
            music.setVolume(volume);
            music.play();
            currentBGM = music;
        }
    }

    public void playBGM() {
        playMusic("bgm", 0.3f, true);
    }
    public void playEnemyDie() {
        playSound("sfx_enemy_die", 2f);
    }
    public void playShoot() {
        playSound("sfx_shoot", 0.5f);
    }
    public void playItemPickup() {
        playSound("sfx_item_pickup", 1.4f);
    }

    public void init() {
        loadMusic("bgm", "bgm_Catastrophe in Bhava-agra.wav");

        loadSound("sfx_enemy_die", "sfx_enemy_death.wav");
        loadSound("sfx_shoot", "sfx_gunshot.wav");
        loadSound("sfx_item_pickup", "sfx_items_pickup.wav");
    }

    public void dispose() {
        if (currentBGM != null) {
            currentBGM.stop();
        }
        for (Sound sound : soundMap.values()) {
            if (sound != null) sound.dispose();
        }
        for (Music music : musicMap.values()) {
            if (music != null) music.dispose();
        }
        soundMap.clear();
        musicMap.clear();
    }
}

