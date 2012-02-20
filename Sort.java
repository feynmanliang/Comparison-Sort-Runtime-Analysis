import java.lang.management.*;   // NOTE

public class Sort {

    public static void main (String[] args) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();   // NOTE

        int N = Integer.parseInt(args[0]);

        System.out.println ("Generating " + N + " random numbers");
        int[] a = new int[N];
        for (int i=0; i<N; ++i) a[i] = (int)(Math.random()*(1<<30));

        // To test a particular sorting algorithm, I make a copy of the original
        // array, and then record the current "user time".  After running the
        // sort, I compute the elapsed time by taking the difference between the
        // time and the time from before the sort.
        if (false)
        {
            int[] c = new int[N];
            for (int i=0; i<N; ++i) c[i] = a[i];

            long t = bean.getCurrentThreadUserTime();   // NOTE  (t is a *long*)

            insertionSort(c);
            System.out.printf ("Insertion sort took %f seconds.\n",     // NOTE
                               (bean.getCurrentThreadUserTime()-t) / 1e9);
        }

        // Here is a section of code that you could use to start a mergesort.
        // Merge Sort - Feynman Liang 2-18-2012
        {
            int[] c = new int[N];
            for (int i=0; i<N; ++i) c[i] = a[i];

            long t = bean.getCurrentThreadUserTime();   // NOTE  (t is a *long*)

            mergeSort(c);
            System.out.printf ("Mergesort took %f seconds.\n",     // NOTE
                               (bean.getCurrentThreadUserTime()-t) / 1e9);
        }

        // Quick Sort - Feynman Liang 2-18-2012
        {
            int[] c = new int[N];
            for (int i=0; i<N; ++i) c[i] = a[i];

            long t = bean.getCurrentThreadUserTime();   // NOTE  (t is a *long*)

            quickSort(c);
            System.out.printf ("Quicksort took %f seconds.\n",     // NOTE
                               (bean.getCurrentThreadUserTime()-t) / 1e9);
        }
        // Extend testing times over <multiple> loops (set to false to
        // disable)
        if (false) {
            int multiple = 100;
            {
                int[] c = new int[N];

                long t = bean.getCurrentThreadUserTime();   // NOTE  (t is a *long*)
                for (int counter = 0; counter < multiple; counter++) {
                    for (int i=0; i<N; ++i) c[i] = a[i];
                    insertionSort(c);
                }
                System.out.printf ("Insertion sort * %d took %f seconds.\n",
                                   (multiple),
                                   (bean.getCurrentThreadUserTime()-t) / 1e9);
            }
            {
                int[] c = new int[N];

                long t = bean.getCurrentThreadUserTime();   // NOTE  (t is a *long*)
                for (int counter = 0; counter < multiple; counter++) {
                    for (int i=0; i<N; ++i) c[i] = a[i];
                    mergeSort(c);
                }
                System.out.printf ("Mergesort * %d took %f seconds.\n",
                                   (multiple),
                                   (bean.getCurrentThreadUserTime()-t) / 1e9);
            }
            {
                int[] c = new int[N];

                long t = bean.getCurrentThreadUserTime();   // NOTE  (t is a *long*)
                for (int counter = 0; counter < multiple; counter++) {
                    for (int i=0; i<N; ++i) c[i] = a[i];
                    quickSort(c);
                }
                System.out.printf ("Quicksort * %d took %f seconds.\n",
                                   (multiple),
                                   (bean.getCurrentThreadUserTime()-t) / 1e9);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // This method prints the contents of an array.  You might use it during
    // debugging.
    /////////////////////////////////////////////////////////////////////////

    public static void print(int[] a) {
        for (int i=0; i<a.length; ++i)
            System.out.println (a[i]);
        System.out.println();
    }

    /////////////////////////////////////////////////////////////////////////
    // This method compares the contents of two arrays.  You might use it
    // during debugging to compare the results of two different algorithms.
    /////////////////////////////////////////////////////////////////////////

    public static void check(int[] a, int[] b) {
        for (int i=0; i<a.length; ++i)
            if (a[i] != b[i])
                throw new RuntimeException ("Error in sorting results");
    }

    /////////////////////////////////////////////////////////////////////////
    // Here's the insertion sort.
    /////////////////////////////////////////////////////////////////////////

    public static void insertionSort(int[] a) {

        int n = a.length;

        for (int i=1; i<n; ++i) {

            int t = a[i];
            int j = i-1;
            while (j >= 0 && t < a[j]) {
                a[j+1] = a[j];
                j--;
            }
            a[j+1] = t;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // MergeSort - Feynman Liang (2-18-2012)
    /////////////////////////////////////////////////////////////////////////
    public static int[] mergeSort(int[] a) {
        // pre: array of integers
        // post: sorted in non-desceasing order

        int len = a.length;

        int blocksize = 1;
        while (blocksize < len) {
            // invariant: a is in sorted blocks of size
            // blocksize/2
            int lo = 0;
            while (lo < (len - blocksize)) {
                // invariant: a[0..lo-1] is sorted in blocks of
                // size blocksize
                int hi = lo + 2 * blocksize - 1;
                // check to see if we've run off a[]
                if ((len - 1) < hi)
                    hi = len - 1;
                inPlaceMerge(a,
                             blocksize,
                             lo,
                             hi);
                lo = lo + 2*blocksize;
            }
            blocksize = blocksize * 2;
        }
        // returns merged sorted array (or single array)
        return a;
    }

    public static void inPlaceMerge(int[] a, int blocksize, int lo, int hi) {
        // pre: two sorted subarrays leftn and right
        // post: a combined array sorted in non-decreasing order
        int[] merged = new int[hi-lo+1];
        int i = lo, j = lo + blocksize, k = 0;

        while (i <= lo + blocksize - 1 && j <= hi) {
            // invariant: merged[] contains all keys < a[i..lo +
            // blocksize-1] and a[j..hi] sorted in non-decreasing
            if (a[i] < a[j]) {
                merged[k] = a[i];
                i++;
            }
            else {
                merged[k] = a[j];
                j++;
            }
            k++;
        }
        // After i or j runs off its half, copy other remaining half
        while(i <= lo + blocksize - 1) {
            merged[k] = a[i];
            i++;
            k++;
        }
        while(j <= hi) {
            merged[k] = a[j];
            j++;
            k++;
        }

        // Copy merged back in to a[lo..hi]
        for (k = 0; k < merged.length; k++)
            a[lo+k] = merged[k];
    }

    /////////////////////////////////////////////////////////////////////////
    // quick Sort - Feynman Liang (2-18-2012)
    /////////////////////////////////////////////////////////////////////////
    public static int[] quickSort(int[] a) {
        // pre: a = array of ints
        // post: returns a sorted in non-decreasing order

        // pass a to recursive qsort method
        qsort(a, 0, a.length-1);
        return a;
    }

    public static void qsort(int[] a, int lo, int hi) {
        // pivot = median in small array, median of 3 in length > 7
        int mid = (lo+hi)/2;
        if ((hi - lo + 1) > 7) {
            mid = medofthree(a, lo, mid, hi);
        }
        int pivot = a[mid];

        int i = lo, j = hi;
        while (i <= j) {
            // Invariant: subarray [lo..i-1] contains keys < pivot
            // and [j+1..hi] contains keys > pivot, [i-1..j+1]
            // contains unpartitioned elements and pivots themselves
            // so that i and j converge around the pivot

            // set i to index of leftmost key > pivot
            while (a[i] < pivot) i++;
            // set j to rightmost index of key < pivot
            while (a[j] > pivot) j--;
            // if not overlapped, swap the two indexes
            if (i <= j) {
                swap(a, i++, j--);
            }
        }

        // Recursively sort sub-partitions
        if (lo < i-1) qsort(a, lo, i-1);
        if (i < hi) qsort(a, i, hi);
    }

    public static void swap(int[] a, int i, int j) {
        // swaps key at index i with key at index j in array a
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static int medofthree(int[] a, int lo, int mid, int hi) {
        // finds the median of lo, mid, and hi
        if (a[lo] < a[mid]) {
            if (a[mid] < a[hi]) return mid;
            else {
                if (a[lo] < a[hi]) return hi;
                else return lo;
            }
        }
        else {
            if (a[mid] > a[hi]) return mid;
            else {
                if (a[lo] > a[hi]) return hi;
                else return lo;
            }
        }
    }
}
