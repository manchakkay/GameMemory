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
import android.util.Log;
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

    final Bitmap bitmapSource; // обратная сторона карты
    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    final int n; // размер поля для игры
    int openedCard = 0;// число открытых карт
    final int width, height; // ширина и высота канвы


    public GameView(Context context, int size, int width, int height) {

        super(context);
        this.context = context;
        this.width = width;
        this.height = height;
        this.n = size;
        bitmapSource = BitmapFactory.decodeResource(getResources(), R.drawable.image_part_055);
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_001));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_002));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_003));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_004));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_005));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_006));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_007));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_008));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_009));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_010));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_011));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_012));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_013));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_014));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_015));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_016));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_017));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_018));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_019));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_020));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_021));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_022));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_023));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_024));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_025));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_026));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_027));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_028));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_029));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_030));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_031));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_032));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_033));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_034));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_035));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_036));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_037));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_038));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_039));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_040));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_041));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_042));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_043));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_044));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_045));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_046));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_047));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_048));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_049));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_050));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_051));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_052));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_053));
        store.add(BitmapFactory.decodeResource(getResources(), R.drawable.image_part_054));

        Random r = new Random();
        int dx = width / n, dy = height / n;
        int tek = 0, temp = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                int k = r.nextInt(store.size());
                if (temp == 0){
                    tek = k;
                    cards.add(new Card(store.get(k), bitmapSource, 0, 0, dx, dy));

                }
                else {
                    cards.add(new Card(store.get(tek), bitmapSource, 0, 0, dx, dy));
                    temp = -1;
                }
                temp++;
            }
        }
        Collections.shuffle(cards);
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                cards.get(i * n + j).x = dx * i;
                cards.get(i * n + j).y = dy * j;
            }
        }
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
        int x = (int) event.getX();
        int y = (int) event.getY();
        x = x / (width / n);
        y = y / (height / n);
        int dx = width / n, dy = height / n;
        x = x * dx;
        y = y * dy;
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            for (Card c: cards){
                if (openedCard == 0){
                    if (c.flip(x,y)){
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
            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаём число секунд ожидания
            } catch (InterruptedException ignored) {}
            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно


        @Override
        protected void onPostExecute(Void aVoid) {
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
        Random r = new Random();
        int dx = width / n, dy = height / n;
        int tek = 0;
        int temp = 0;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                int k = r.nextInt(store.size());
                if (temp == 0){
                    tek = k;
                    cards.add(new Card(store.get(k), bitmapSource, 0, 0, dx, dy));

                }
                else {
                    cards.add(new Card(store.get(tek), bitmapSource, 0, 0, dx, dy));
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
        invalidate();
    }

    public boolean equals(Bitmap bitmap1, Bitmap bitmap2) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);

        return Arrays.equals(buffer1.array(), buffer2.array());
    }

}

