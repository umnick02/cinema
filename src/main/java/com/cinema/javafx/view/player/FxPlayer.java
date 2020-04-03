package com.cinema.javafx.view.player;

import com.cinema.javafx.view.Stoppable;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import java.nio.ByteBuffer;

public enum FxPlayer implements Stoppable {
    INSTANCE;

    private static final Logger logger = LogManager.getLogger(FxPlayer.class);

    private EmbeddedMediaPlayer embeddedMediaPlayer;
    private MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private ImageView videoImageView;
    private PixelBuffer<ByteBuffer> videoPixelBuffer;
    private BorderPane borderPane;

    FxPlayer() {
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(new FXCallbackVideoSurface());

        ControlsPane controlsPane = new ControlsPane(embeddedMediaPlayer);

        borderPane = new BorderPane();
        borderPane.setBottom(controlsPane);
        borderPane.setStyle("-fx-background-color: black;");
        videoImageView = new ImageView();
        videoImageView.setPreserveRatio(true);
        videoImageView.fitWidthProperty().bind(borderPane.widthProperty());
        videoImageView.fitHeightProperty().bind(borderPane.heightProperty());
        borderPane.setCenter(videoImageView);
    }

    public MediaPlayerFactory getMediaPlayerFactory() {
        return mediaPlayerFactory;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
        return embeddedMediaPlayer;
    }

    @Override
    public void stop() {
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        mediaPlayerFactory.release();
    }

    private class FXCallbackVideoSurface extends CallbackVideoSurface {
        FXCallbackVideoSurface() {
            super(new FXBufferFormatCallback(), new FXRenderCallback(), true, VideoSurfaceAdapters.getVideoSurfaceAdapter());
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
            assert buffers[0].capacity() == sourceWidth * sourceHeight * 4;
            PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
            videoPixelBuffer = new PixelBuffer<>(sourceWidth, sourceHeight, buffers[0], pixelFormat);
            WritableImage videoImage = new WritableImage(videoPixelBuffer);
            videoImageView.setImage(videoImage);
        }
    }

    private class FXRenderCallback implements RenderCallback {
        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            Platform.runLater(() -> videoPixelBuffer.updateBuffer(pb -> null));
        }
    }
}
