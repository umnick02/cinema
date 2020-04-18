package com.cinema.core.service.bt.selectors.piece;

import bt.torrent.PieceStatistics;
import bt.torrent.selector.BaseStreamSelector;
import com.cinema.core.service.bt.selectors.piece.impl.PieceIterator;

import java.util.PrimitiveIterator;

public class SequentialPositioningSelector extends BaseStreamSelector {

    @Override
    protected PrimitiveIterator.OfInt createIterator(PieceStatistics pieceStatistics) {
        return new PieceIterator(pieceStatistics);
    }
}
