package com.huawei.threadpool;

public class QuickSort {
    public static void main(String[] args) {
        int[] a = {4,2,7,3,8,0,9};
        quickSort(a, 0, a.length - 1);
        System.out.println("排序后的顺序：");
        for (int i : a) {
            System.out.println(i);
        }

    }

    public static void quickSort(int[] a, int low, int high){
        if (low < high){
            int index = getIndex(a, low, high);
            //循环分出来的大于temp的部分和小于temp的部分
            quickSort(a, low, index - 1);
            quickSort(a, index + 1, high);
        }

    }

    public static int getIndex(int[] a, int low, int high){
        int temp = a[low];
        while (low < high) {
            while (low < high && a[high] >= temp) {//如果数组有重复值则需要加上"="，否则会程序会卡住
                high--;
            }
            //退出上面的循环之后则说明a[high] < temp 则交换值
            a[low] = a[high];

            while (low < high && a[low] <= temp) {
                low++;
            }
            a[high] = a[low];
        }
        //跳出循环的时候low和high相等，此时需要将temo值复制给[low]
        a[low] = temp;
        return low;//返回low和high都可以

    }
}
