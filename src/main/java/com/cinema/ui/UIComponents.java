package com.cinema.ui;

public class UIComponents {

//    private static void player() {
//        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
//            @Override
//            public void playing(MediaPlayer mediaPlayer) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
////                                showVideoView();
//                    }
//                });
//            }
//
//            @Override
//            public void finished(MediaPlayer mediaPlayer) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
////                                showDefaultView();
//                    }
//                });
//            }
//
//            @Override
//            public void error(MediaPlayer mediaPlayer) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        JOptionPane.showMessageDialog(
//                                frame,
//                                "Failed to play media",
//                                "Error",
//                                JOptionPane.ERROR_MESSAGE
//                        );
//                    }
//                });
//            }
//        };
//        frame.setBounds(100, 100, 600, 400);
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                mediaPlayerComponent.release();
//                System.exit(0);
//            }
//        });
//
//        JPanel contentPane = new JPanel();
//        contentPane.setLayout(new BorderLayout());
//        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
//
//        JPanel controlsPane = new JPanel();
//        JButton pauseButton = new JButton("Pause");
//        controlsPane.add(pauseButton);
//        JButton rewindButton = new JButton("Rewind");
//        controlsPane.add(rewindButton);
//        JButton skipButton = new JButton("Skip");
//        controlsPane.add(skipButton);
//        contentPane.add(controlsPane, BorderLayout.SOUTH);
//
//        pauseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mediaPlayerComponent.mediaPlayer().controls().pause();
//            }
//        });
//
//        rewindButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mediaPlayerComponent.mediaPlayer().controls().skipTime(-10000);
//            }
//        });
//
//        skipButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                mediaPlayerComponent.mediaPlayer().controls().skipTime(10000);
//
//            }
//        });
//
//        frame.setVisible(true);
//        frame.setContentPane(contentPane);
//
//        Marquee marquee = Marquee.marquee()
//                .size(40)
//                .colour(Color.WHITE)
//                .timeout(3000)
//                .position(MarqueePosition.BOTTOM_RIGHT)
//                .opacity(0.8f)
//                .enable();
//        mediaPlayerComponent.mediaPlayer().marquee().set(marquee);
//
//        frame.setVisible(true);
//        mediaPlayerComponent.mediaPlayer().media().play(file);
//
//    }
}
