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
