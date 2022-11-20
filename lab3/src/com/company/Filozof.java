package com.company;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Filozof extends Thread {
    static final int MAX = 100;
    static Semaphore[] widelec;
    static int wariant;
    static int liczbaF;
    int mojNum;
    Random losuj = new Random();

    public Filozof(int nr) {
        this.mojNum = nr;
    }

    public void run() {
        switch(wariant) {
            case 1:
                this.Pw();
                break;
            case 2:
                this.Dw();
                break;
            case 3:
                this.Tw();
        }

    }

    private void Pw() {
        while(true) {
            System.out.println("Mysle " + this.mojNum);

            try {
                Thread.sleep((long)(7000.0D * Math.random()));
            } catch (InterruptedException var3) {
            }

            widelec[this.mojNum].acquireUninterruptibly();
            widelec[(this.mojNum + 1) % liczbaF].acquireUninterruptibly();
            System.out.println("Zaczyna jesc " + this.mojNum);

            try {
                Thread.sleep((long)(5000.0D * Math.random()));
            } catch (InterruptedException var2) {
            }

            System.out.println("Konczy jesc " + this.mojNum);
            widelec[this.mojNum].release();
            widelec[(this.mojNum + 1) % liczbaF].release();
        }
    }

    private void Dw() {
        while(true) {
            System.out.println("Mysle " + this.mojNum);

            try {
                Thread.sleep((long)(5000.0D * Math.random()));
            } catch (InterruptedException var3) {
            }

            if (this.mojNum == 0) {
                widelec[(this.mojNum + 1) % liczbaF].acquireUninterruptibly();
                widelec[this.mojNum].acquireUninterruptibly();
            } else {
                widelec[this.mojNum].acquireUninterruptibly();
                widelec[(this.mojNum + 1) % liczbaF].acquireUninterruptibly();
            }

            System.out.println("Zaczyna jesc " + this.mojNum);

            try {
                Thread.sleep((long)(3000.0D * Math.random()));
            } catch (InterruptedException var2) {
            }

            System.out.println("Konczy jesc " + this.mojNum);
            widelec[this.mojNum].release();
            widelec[(this.mojNum + 1) % liczbaF].release();
        }
    }

    private void Tw() {
        while(true) {
            System.out.println("Mysle " + this.mojNum);

            try {
                Thread.sleep((long)(5000.0D * Math.random()));
            } catch (InterruptedException var5) {
            }

            int strona = this.losuj.nextInt(2);
            boolean podnosiDwaWidelce = false;

            do {
                if (strona == 0) {
                    widelec[this.mojNum].acquireUninterruptibly();
                    if (!widelec[(this.mojNum + 1) % liczbaF].tryAcquire()) {
                        widelec[this.mojNum].release();
                    } else {
                        podnosiDwaWidelce = true;
                    }
                } else {
                    widelec[(this.mojNum + 1) % liczbaF].acquireUninterruptibly();
                    if (!widelec[this.mojNum].tryAcquire()) {
                        widelec[(this.mojNum + 1) % liczbaF].release();
                    } else {
                        podnosiDwaWidelce = true;
                    }
                }
            } while(!podnosiDwaWidelce);

            System.out.println("Zaczyna jesc " + this.mojNum);

            try {
                Thread.sleep((long)(3000.0D * Math.random()));
            } catch (InterruptedException var4) {
            }

            System.out.println("Konczy jesc " + this.mojNum);
            widelec[this.mojNum].release();
            widelec[(this.mojNum + 1) % liczbaF].release();
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Liczba filozofow :");
        int liczba = in.nextInt();
        liczbaF = liczba;
        System.out.println("Wariant:\n1.Klasyczny problem\n2.Niesymetryczne sieganie po widelce\n3.Rzut monety");
        int wariant2 = in.nextInt();
        in.close();
        if (liczba <= 100 && liczba >= 2 && wariant2 >= 1 && wariant2 <= 3) {
            wariant = wariant2;
            widelec = new Semaphore[liczba];

            int i;
            for(i = 0; i < liczba; ++i) {
                widelec[i] = new Semaphore(1);
            }

            for(i = 0; i < liczba; ++i) {
                (new Filozof(i)).start();
            }
        } else {
            System.out.println("Niepoprawne ");
        }

    }
}