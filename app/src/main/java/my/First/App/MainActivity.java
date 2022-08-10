package my.First.App;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final int ONE_PLAYER = 0;
    private static final int TWO_PLAYER = 1;
    private static final int RED_CODE_HUMAN = 1;
    private static final int YELLOW_CODE_AI = 2;
    private static final int NOT_PLAYED = 0;
    private static final int DRAW = 0;
    private static final int NO_WINNER_DRAW = -1;

    int winner = NO_WINNER_DRAW;
    int[] gameState = {NOT_PLAYED, NOT_PLAYED, NOT_PLAYED,
            NOT_PLAYED, NOT_PLAYED, NOT_PLAYED,
            NOT_PLAYED, NOT_PLAYED, NOT_PLAYED};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};

    Random random = new Random();
    int play_type = TWO_PLAYER;
    int activePlayer = RED_CODE_HUMAN;

    RelativeLayout msgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgLayout = findViewById(R.id.msg_layout);
        msgLayout.setVisibility(View.GONE);
        reset(null);
    }

    public void dropIn(View view) {

        int tag = Integer.parseInt((String) view.getTag());
        if ((winner != NO_WINNER_DRAW) || (gameState[tag] != NOT_PLAYED)) {
            return;
        }

        if ((play_type == ONE_PLAYER) && (activePlayer == YELLOW_CODE_AI))
            return;

        ImageView img = (ImageView) view;
        img.setTranslationY(-2000f);
        if (activePlayer == YELLOW_CODE_AI) {
            img.setImageResource(R.drawable.yellow);
            gameState[tag] = YELLOW_CODE_AI;
            activePlayer = RED_CODE_HUMAN;
            img.animate().translationY(0f).setDuration(150);
        } else if (activePlayer == RED_CODE_HUMAN) {
            img.setImageResource(R.drawable.red);
            gameState[tag] = RED_CODE_HUMAN;
            activePlayer = YELLOW_CODE_AI;
            img.animate().translationY(0f).setDuration(150);
        }
        // check winner
        winnerMsg();
    }

    public void winnerMsg() {
        winner = checkWinner();
        if (winner != NO_WINNER_DRAW) {
            String msg = "";
            int color = Color.GREEN;
            if (winner == DRAW) {
                msg = getString(R.string.nowinner);
            } else if (winner == RED_CODE_HUMAN) {
                msg = getString(R.string.redwon);
                color = Color.RED;
            } else if (winner == YELLOW_CODE_AI) {
                msg = getString(R.string.yellowon);
                color = Color.YELLOW;
            }
            msgLayout.setBackgroundColor(color);
            ((TextView) msgLayout.findViewById(R.id.winner_message)).setText(msg);
            msgLayout.setVisibility(View.VISIBLE);
        }
    }

    // no winner : -1
    // draw : 0
    // red : RED_CODE
    // yellow : YELLOW_CODE
    public int checkWinner() {
        for (int[] positions : winningPositions) {
            if (gameState[positions[0]] == gameState[positions[1]] &&
                    gameState[positions[1]] == gameState[positions[2]] &&
                    gameState[positions[0]] != NOT_PLAYED) {
                return gameState[positions[0]];
            }
        }

        if (filled())
            return DRAW;

        return NO_WINNER_DRAW;
    }

    public boolean filled() {
        int i;
        for (i = 0; i < gameState.length; i++) {
            if (gameState[i] == NOT_PLAYED) {
                return false;
            }
        }
        return true;
    }

    public void reset(View v) {
        // winner
        winner = NO_WINNER_DRAW;
        // gameState
        Arrays.fill(gameState, NOT_PLAYED);
        // play_ground
        LinearLayout pgLayout = findViewById(R.id.pg_layout);
        for (int i = 0; i < pgLayout.getChildCount(); i++) {
            LinearLayout row = (pgLayout.getChildAt(i) instanceof LinearLayout) ?
                    (LinearLayout) pgLayout.getChildAt(i) : null;
            if (row == null) return;
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView iv = (row.getChildAt(j) instanceof ImageView) ?
                        (ImageView) row.getChildAt(j) : null;
                if (iv == null) return;
                iv.setImageResource(0);
            }
        }
        msgLayout.setVisibility(View.GONE);

        activePlayer = random.nextBoolean() ? RED_CODE_HUMAN : YELLOW_CODE_AI;
        String message = "";
        if (play_type == TWO_PLAYER)
            message = activePlayer == RED_CODE_HUMAN ? " RED " : " YELLOW ";
        else if (play_type == ONE_PLAYER)
            message = activePlayer == RED_CODE_HUMAN ? " Player " : " AI ";
        Toast.makeText(this, "Start ::  " + message, Toast.LENGTH_SHORT).show();
    }

    public int SOLVE(int x){
        if (x==0)
            return 0;
        return SOLVE(x);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem resetItem = menu.add(R.string.reset);
        resetItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        resetItem.setOnMenuItemClickListener(this::onResetMenuClick);

        MenuItem Item2 = menu.add("😉");
        Item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        Item2.setOnMenuItemClickListener(this::onItem2MenuClick);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onItem2MenuClick(MenuItem menuItem) {
        int x = SOLVE(0);
        return false;
    }

    public boolean onResetMenuClick(MenuItem menuItem) {
        reset(null);
        return false;
    }
}
