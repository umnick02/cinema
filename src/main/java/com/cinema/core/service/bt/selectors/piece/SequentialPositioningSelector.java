package com.cinema.core.service.bt.selectors.piece;

import bt.torrent.PieceStatistics;
import bt.torrent.selector.BaseStreamSelector;
import com.cinema.core.model.impl.TorrentModel;
import com.cinema.core.service.bt.selectors.piece.impl.PieceIterator;

import java.util.PrimitiveIterator;

public class SequentialPositioningSelector extends BaseStreamSelector {

    private PieceIterator pieceIterator;

    @Override
    protected PrimitiveIterator.OfInt createIterator(PieceStatistics pieceStatistics) {
        pieceIterator = new PieceIterator(pieceStatistics);
        TorrentModel.INSTANCE.setPieceTotal(pieceStatistics.getPiecesTotal());
        return pieceIterator;
    }

    public PieceIterator getPieceIterator() {
        return pieceIterator;
    }
}
