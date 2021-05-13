package com.example.gamememory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

class Card {
    final Paint paint;

    boolean isOpen = false; // перевёрнута ли карта
    final Bitmap btmOpen, btmNoOpen;
    final int width, height;
    int x, y;

    public Card(Bitmap btmOpen, Bitmap btmNoOpen, int x, int y, int width, int height) {
        this.btmOpen = btmOpen;
        this.btmNoOpen = btmNoOpen;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void draw(Canvas canvas) {
        @SuppressLint("DrawAllocation") Bitmap bmHalf;
        if (isOpen) bmHalf = Bitmap.createScaledBitmap(btmOpen, width, height, false);
        else bmHalf = Bitmap.createScaledBitmap(btmNoOpen, width, height, false);
        canvas.drawBitmap(bmHalf, x, y, paint);
    }

    public boolean flip (int touch_x, int touch_y) {
        if (touch_x == x &&  touch_y == y ) {
            isOpen = ! isOpen;
            return true;
        } else return false;
    }
}


@SuppressLint("ViewConstructor")
public class GameView extends View {
    // пауза для запоминания карт
    final int PAUSE_LENGTH = 2; // в секундах
    boolean isOnPauseNow = false;
    final Context context;

    final ArrayList<Card> cards = new ArrayList<>(); // текущие карты на поле
    final ArrayList<Bitmap> store = new ArrayList<>(); // для хранения всех карт

    final Bitmap cardShirt; // обратная сторона карты
    // --Commented out by Inspection (14.05.2021 2:44):final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    final int n; // размер поля для игры
    int openedCard = 0;// число открытых карт
    final int width, height; // ширина и высота канвы


    public GameView(Context context, int size, int width, int height) {

        super(context);
        this.context = context;
        this.width = width;
        this.height = height;
        this.n = size;
        cardShirt = BitmapFactory.decodeResource(getResources(), R.drawable.image_part_055);

        for (int i = 1; i <= 54; i++) {
            String image = String.format("image_part_%03d", i);

            int resId = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
            store.add(BitmapFactory.decodeResource(getResources(), resId));
        }

        add_cards();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Card c: cards) {
            c.draw(canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int dx = width / n, dy = height / n;

        int x = Math.round(event.getX() / dx) * dx;
        int y = Math.round(event.getY() / dy) * dy;

        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            for (Card c: cards){
                if (openedCard == 0){
                    if (c.flip(x, y)){
                        openedCard++;
                        invalidate();
                        return true;
                    }
                }
                if (openedCard == 1) {
                    // перевернуть карту с задержкой
                    if (!c.isOpen && c.flip(x, y)) {
                        openedCard++;
                        invalidate();
                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);
                        isOnPauseNow = true;
                        return true;
                    }
                }
            }
        }

        return true;
    }

    public void checkOpenCardsEqual(){
        Card b1 = null;
        Card b2 = null;
        for (int i = 0; i < cards.size(); i++){
            if (b1 == null && cards.get(i).isOpen){
                b1 = cards.get(i);
            }
            else if (b2 == null && cards.get(i).isOpen){
                b2 = cards.get(i);
            }
        }
        if (b2 != null && b1 != null)
            if (equals(b1.btmOpen, b2.btmOpen)) {
                cards.remove(b1);
                cards.remove(b2);
            }
    }

    @SuppressLint("StaticFieldLeak")
    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
//            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаём число секунд ожидания
            } catch (InterruptedException ignored) {}
//            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно


        @Override
        protected void onPostExecute(Void v) {
            checkOpenCardsEqual();
            if (cards.size() == 0){
                new AlertDialog.Builder(context)
                        .setTitle("Конец игры ")
                        .setMessage("Если хотите сыграть ещё нажмите: \"сыграть ещё\".\nДля выхода в меню нажмите:\n\"в главное меню\".")
                        .setCancelable(false)
                        .setPositiveButton("Сыграть ещё", (dialog, which) -> newGame()).setNegativeButton("В главное меню", (dialog, which) -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }).show();
            }
            for (Card c: cards) {
                if (c.isOpen) {
                    c.isOpen = false;
                }
            }
            openedCard = 0;
            isOnPauseNow = false;
            invalidate();
        }
    }

    public void newGame() {
        add_cards();
        invalidate();
    }

    private void add_cards(){
        Random r = new Random();
        int dx = width / n, dy = height / n;
        int tek = 0, temp = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                int k = r.nextInt(store.size());
                if (temp == 0){
                    tek = k;
                    cards.add(new Card(store.get(k), cardShirt, 0, 0, dx, dy));
                }
                else {
                    cards.add(new Card(store.get(tek), cardShirt, 0, 0, dx, dy));
                    temp = -1;
                }
                temp++;
            }
        }

        Collections.shuffle(cards);
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                r.nextInt(store.size());
                cards.get(i * n + j).x = dx * i;
                cards.get(i * n + j).y = dy * j;
            }
        }
    }

    public boolean equals(Bitmap bitmap1, Bitmap bitmap2) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);

        return Arrays.equals(buffer1.array(), buffer2.array());
    }

}

