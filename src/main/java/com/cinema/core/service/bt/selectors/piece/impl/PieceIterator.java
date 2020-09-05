package com.cinema.core.service.bt.selectors.piece.impl;

import bt.torrent.PieceStatistics;
import com.cinema.core.service.bt.selectors.piece.Positioning;

import java.util.PrimitiveIterator;

public class PieceIterator implements PrimitiveIterator.OfInt, Positioning {

    private PieceStatistics pieceStatistics;
    private int position = 0;
    private static int toPosition = 0;
    private boolean changed = false;

    public PieceIterator(PieceStatistics pieceStatistics) {
        this.pieceStatistics = pieceStatistics;
    }

    @Override
    public synchronized int nextInt() {
        if (!changed) {
            position = toPosition;
            changed = true;
        }
        return position++;
    }

    @Override
    public synchronized boolean hasNext() {
        while (position < pieceStatistics.getPiecesTotal() && pieceStatistics.getCount(position) == 0) {
            System.out.format("peers for position [%d] not found\n", position);
            position++;
        }
        return position < pieceStatistics.getPiecesTotal();
    }

    @Override
    public synchronized int toPosition(double ratio) {
        toPosition = (int) (pieceStatistics.getPiecesTotal() * ratio);
        changed = false;
        return toPosition;
    }
}
