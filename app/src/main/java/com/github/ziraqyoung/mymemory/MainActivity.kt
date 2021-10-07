package com.github.ziraqyoung.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ziraqyoung.mymemory.models.BoardSize
import com.github.ziraqyoung.mymemory.models.MemoryCard
import com.github.ziraqyoung.mymemory.models.MemoryGame
import com.github.ziraqyoung.mymemory.utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame

    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    private val boardSize :BoardSize = BoardSize.MEDIUM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)


         memoryGame = MemoryGame(boardSize)

        // 3 components of any Android RecyclerView (adapter, layoutManager)
        // setHasFixedSize is an optimization when layout do not change
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                Log.i(TAG, "Card clicked $position")
                updateGameWithFlip(position)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())

    }

    private fun updateGameWithFlip(position: Int) {
        // Error checking
        if(memoryGame.haveWonGame()) {
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
         if(memoryGame.isCardFaceUp(position)) {
             Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_LONG).show()
             return
         }

        // Actually flip over the card at this position
        if (memoryGame.flipCard(position)) {
            Log.i(TAG, "Found a match! Num pairs found ${memoryGame.numPairsFound}")
        }
        adapter.notifyDataSetChanged()
    }
}