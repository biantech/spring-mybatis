package com.biantech.ssmd.test.threadtest;

public class ThreadLocalDemo2 {
    private static final ThreadLocal<Integer> local=new ThreadLocal<Integer>(){
        protected Integer initialValue(){
            return 0; //通过initialValue方法设置默认值
        }
    };

    public static void main(String[] args) {
        Thread[] threads=new Thread[5];
        for(int i=0;i<5;i++){
            threads[i]=new Thread(()->{
                int num=local.get().intValue();
                num+=5;
                System.out.println(Thread.currentThread().getName()+" : "+num);
            },"Thread-"+i);
        }
        for(Thread thread:threads){
            thread.start();
        }
    }



}
