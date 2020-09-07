package com.cinema.javafx.player;

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

import static uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters.getVideoSurfaceAdapter;

public final class PlayerFactory {

    private static MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private static EmbeddedMediaPlayer embeddedMediaPlayer;
    private static PixelBuffer<ByteBuffer> videoPixelBuffer;

    public static EmbeddedMediaPlayer buildMediaPlayer(ImageView imageView) {
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(new FXCallbackVideoSurface(imageView));
        return embeddedMediaPlayer;
    }

    private static class FXCallbackVideoSurface extends CallbackVideoSurface {
        FXCallbackVideoSurface(ImageView imageView) {
            super(new FXBufferFormatCallback(imageView), new FXRenderCallback(), true, getVideoSurfaceAdapter());
        }
    }

    private static class FXBufferFormatCallback implements BufferFormatCallback {

        private ImageView videoImageView;

        private int sourceWidth;
        private int sourceHeight;

        FXBufferFormatCallback(ImageView imageView) {
            this.videoImageView = imageView;
        }

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

    private static class FXRenderCallback implements RenderCallback {
        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            Platform.runLater(() -> videoPixelBuffer.updateBuffer(pb -> null));
        }
    }
}
