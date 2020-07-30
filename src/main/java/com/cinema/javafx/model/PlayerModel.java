package com.cinema.javafx.model;

import com.cinema.core.config.Preferences;
import com.cinema.core.model.ObservableModel;
import com.cinema.core.model.impl.SceneModel;
import com.cinema.core.service.Stoppable;
import com.cinema.javafx.controller.player.PlayerController;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import java.nio.ByteBuffer;

import static com.cinema.core.config.Preferences.getPreference;
import static uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters.getVideoSurfaceAdapter;

public class PlayerModel extends ObservableModel implements Stoppable {

    public static final PlayerModel INSTANCE = new PlayerModel();

    private MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private EmbeddedMediaPlayer embeddedMediaPlayer;
    private PixelBuffer<ByteBuffer> videoPixelBuffer;

    private ImageView videoImageView;

    public void setVideoImageView(ImageView videoImageView) {
        this.videoImageView = videoImageView;
    }

    public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
        if (embeddedMediaPlayer == null) {
            embeddedMediaPlayer = buildMediaPlayer();
        }
        return embeddedMediaPlayer;
    }

    private EmbeddedMediaPlayer buildMediaPlayer() {
        EmbeddedMediaPlayer embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(new FXCallbackVideoSurface());
        return embeddedMediaPlayer;
    }

    public void play() {
        embeddedMediaPlayer.submit(() -> embeddedMediaPlayer.media().play(getPreference(Preferences.PrefKey.STORAGE) + SceneModel.INSTANCE.getActiveMovieModel().getMovie().getFile()));
    }

    @Override
    public void stop() {
        if (embeddedMediaPlayer != null) {
            embeddedMediaPlayer.controls().stop();
            embeddedMediaPlayer.release();
            embeddedMediaPlayer = null;
        }
    }

    private class FXCallbackVideoSurface extends CallbackVideoSurface {
        FXCallbackVideoSurface() {
            super(new FXBufferFormatCallback(), new FXRenderCallback(), true, getVideoSurfaceAdapter());
        }
    }

    private class FXBufferFormatCallback implements BufferFormatCallback {
        private int sourceWidth;
        private int sourceHeight;

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            this.sourceWidth = sourceWidth;
            this.sourceHeight = sourceHeight;
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void allocatedBuffers(ByteBuffer[] buffers) {
            videoPixelBuffer = new PixelBuffer<>(sourceWidth, sourceHeight, buffers[0], PixelFormat.getByteBgraPreInstance());
            videoImageView.setImage(new WritableImage(videoPixelBuffer));
        }
    }

    private class FXRenderCallback implements RenderCallback {
        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            Platform.runLater(() -> videoPixelBuffer.updateBuffer(pb -> null));
        }
    }
}
