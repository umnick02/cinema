package com.cinema.core.service.bt.selectors.piece.impl;

import bt.torrent.PieceStatistics;
import com.cinema.core.service.bt.selectors.piece.Positioning;

import java.util.PrimitiveIterator;

public class PieceIterator implements PrimitiveIterator.OfInt, Positioning {

    private PieceStatistics pieceStatistics;
    private int i = 0;

    public PieceIterator(PieceStatistics pieceStatistics) {
        this.pieceStatistics = pieceStatistics;
    }

    @Override
    public int nextInt() {
        return i++;
    }

    @Override
    public boolean hasNext() {
        while (i < pieceStatistics.getPiecesTotal() && pieceStatistics.getCount(i) == 0) {
            i++;
        }
        return i < pieceStatistics.getPiecesTotal();
    }

    @Override
    public void toPosition(double ratio) {
        if (ratio >= 1) return;
        if (ratio < 0) ratio = 0;
        i = (int) (pieceStatistics.getPiecesTotal() * ratio);
    }
}
