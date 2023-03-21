package com.example.tictactoe

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import java.io.File

class MainActivity : AppCompatActivity() {

    // Represents the internal state of the game
    var mGame = TicTacToeGame()

    // Buttons making up the board
    private lateinit var mBoardButtons: Array<Button>

    private val TAKE_PHOTO_ID = 0
    lateinit var imageView1: ImageView

    // TextView
    private lateinit var information: TextView

    // TextView
    private lateinit var win_count: TextView

    lateinit var mediaPlayer: MediaPlayer


    var android_win = 0
    var user_win = 0
    var cnt = 0

    // Game Over
    var mGameOver = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        information = findViewById(R.id.information)
        win_count = findViewById(R.id.win_count)

        val button0 = findViewById(R.id.button0) as Button
        val button1 = findViewById(R.id.button1) as Button
        val button2 = findViewById(R.id.button2) as Button
        val button3 = findViewById(R.id.button3) as Button
        val button4 = findViewById(R.id.button4) as Button
        val button5 = findViewById(R.id.button5) as Button
        val button6 = findViewById(R.id.button6) as Button
        val button7 = findViewById(R.id.button7) as Button
        val button8 = findViewById(R.id.button8) as Button

        val videoView = findViewById<VideoView>(R.id.videoView)


//        val button_camera = findViewById<Button>(R.id.button_camera)
//        imageView1 = findViewById<ImageView>(R.id.imageView1)
//
//        button_camera.setOnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(
//                intent,
//                TAKE_PHOTO_ID
//            )
//        }


//        val videoView = findViewById<VideoView>(R.id.videoView)


//        val mc = MediaController(this)
//        mc.setAnchorView(videoView)
//        videoView.setMediaController(mc)

        // Buttons making up the board
        mBoardButtons =
            arrayOf(
                button0, button1, button2, button3, button4, button5,
                button6, button7, button8
            )

        startNewGame()

        val uri = Uri.parse(
            "android.resource://"
                    + packageName + "/" + R.raw.audio
        )
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
        mediaPlayer.start()

        val uri2 = Uri.parse(
            "android.resource://" +
                    packageName
                    + "/" + R.raw.video
        )
        videoView.setVideoURI(uri2)
        videoView.start()
    }

//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        if (requestCode == TAKE_PHOTO_ID) {
//            if (data != null && data.hasExtra("data")) {
//                val bitmap = data.getParcelableExtra<Bitmap>("data")
//                imageView1.setImageBitmap(bitmap)
//                Toast.makeText(
//                    this, "A photo is taken.",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//    }


    //--- Set up the game board.
    fun startNewGame() {
        mGameOver = false
        mGame.clearBoard()
        //---Reset all buttons
        for (i in mBoardButtons.indices) {
            mBoardButtons[i].text = ""
            mBoardButtons[i].isEnabled = true
        }
        // depend it turns on user or android
        if(cnt %2 ==0){
            //---Human goes first
            //information.text = "You go first."
            information.setText(R.string.prompt6)
        }else{
            //information.text = "It's Android's turn."
            information.setText(R.string.prompt1)
            val move = mGame.computerMove
            setMove(TicTacToeGame.COMPUTER_PLAYER, move)
        }


    }
    // multiple button click method
    fun onButtonClicked(view: View) {
        val buSelected: Button = view as Button
        var location = 0
        when (buSelected.id) {
            R.id.button0 -> location = 0
            R.id.button1 -> location = 1
            R.id.button2 -> location = 2

            R.id.button3 -> location = 3
            R.id.button4 -> location = 4
            R.id.button5 -> location = 5
            R.id.button6 -> location = 6
            R.id.button7 -> location = 7
            R.id.button8 -> location = 8
        }

        if (mGameOver == false) {
            if (mBoardButtons[location].isEnabled) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location)

                //--- If no winner yet, let the computer make a move
                var winner = mGame.checkForWinner()

                if (winner == 0) {
                    //information.text = "It's Android's turn."
                    information.setText(R.string.prompt1)
                    val move = mGame.computerMove
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move)
                    winner = mGame.checkForWinner()
                }
                if (winner == 0) {
                    information.setTextColor(Color.rgb(0, 0, 0))
                    //information.text = "It's your turn (X)."
                    information.setText(R.string.prompt2)
                } else if (winner == 1) {
                    information.setTextColor(Color.rgb(0, 0, 200))
                    //information.text = "It's a tie!"
                    information.setText(R.string.prompt3)

                    win_count.text = getString(R.string.Android) + android_win + getString(R.string.you) + user_win
                    mGameOver = true
                } else if (winner == 2) {
                    information.setTextColor(Color.rgb(0, 200, 0))
                    //information.text = "You won!"
                    information.setText(R.string.prompt4)
                    user_win +=1
                    win_count.text = getString(R.string.Android) + android_win + getString(R.string.you) + user_win
                    mGameOver = true
                } else {
                    information.setTextColor(Color.rgb(200, 0, 0))
                    //information.text = "Android won!"
                    android_win += 1
                    information.setText(R.string.prompt5)
                    win_count.text = getString(R.string.Android) + android_win + getString(R.string.you) + user_win
                    mGameOver = true
                }
            }
        }
    }
    private fun setMove(player: Char, location: Int) {
        mGame.setMove(player, location)
        mBoardButtons[location].isEnabled = false
        mBoardButtons[location].text = player.toString()
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location]!!.setTextColor(
                Color.parseColor(
                    "#ff0000"
                )
            )
        else mBoardButtons[location]!!.setTextColor(Color.parseColor("#00ff00"))
    }





    //--- OnClickListener for Restart a New Game Button
    fun newGame(v: View) {
        cnt+=1
        startNewGame()
    }

    // --- Option Menu ---
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_Level1 -> {
                Toast.makeText(applicationContext, getString(R.string.difficulty)+": "+getString(R.string.Level1),
                    Toast.LENGTH_LONG).show()
                return true
            }
            R.id.menu_Level2 -> {
                Toast.makeText(applicationContext, getString(R.string.difficulty)+": "+getString(R.string.Level2),
                    Toast.LENGTH_LONG).show()
                return true
            }
            R.id.menu_Level3 -> {
                Toast.makeText(applicationContext, getString(R.string.difficulty)+": "+getString(R.string.Level3),
                    Toast.LENGTH_LONG).show()
                return true
            }
            R.id.menu_exit -> {
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun goToSecondActivity(v: View) {

        intent = Intent(this, takePhotoActivity::class.java)
        startActivity(intent)
    }







}





