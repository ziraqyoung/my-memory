package com.github.ziraqyoung.mymemory.models

import com.github.ziraqyoung.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it ) }
    }

    fun flipCard(position: Int) : Boolean {
        val card = cards[position]
        // Three cases
        // 0 cards flipped over => restore cards + flip over the selected card
        // 1 card previously flipped over => flip over selected card + check if the images matches
        // 2 cards previously flipped over => restore cards + flip over the selected card
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 previously selected cards
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // exactly one card previously flipped over
             foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp

        return foundMatch
    }

    private fun checkForMatch(position1:  Int, position2: Int): Boolean {
        if(cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++

        return true
    }

    private fun restoreCards() {
        for(card in cards) {
            if(!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }
}
