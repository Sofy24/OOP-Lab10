
package it.unibo.oop.lab.workers02;


import java.util.*;






public class MultiThreadedSumMatrix implements SumMatrix {
    private final int nthread;

    public double sum(final double[][] matrix) {
        List<Double> limat;
        limat = matrixele(matrix);
        final int size = limat.size() % nthread + limat.size() / nthread;
        final List<Worker> workers = new ArrayList<>(nthread);
        for (int start = 0; start < limat.size(); start += size) {
            workers.add(new Worker(limat, start, size));
        }

        for (final Worker w: workers) {
            w.start();
        }

        long sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        return sum;
    }

    public List<Double> matrixele(final double[][] matrix) {
        final List<Double> listam = new ArrayList<>();
        for (final double[] i : matrix) {
            for (final double j : i) {
                listam.add(j);
            }
        } 
        return listam;

    }

    /**
     * 
     * @param nthread
     *            no. of thread performing the sum.
     */
    public MultiThreadedSumMatrix(final int nthread) {
        this.nthread = nthread;
    }

    private static class Worker extends Thread {
        private final List<Double> list;
        private final int startpos;
        private final int nelem;
        private long res;

        /**
         * Build a new worker.
         * 
         * @param list
         *            the list to sum
         * @param startpos
         *            the initial position for this worker
         * @param nelem
         *            the no. of elems to sum up for this worker
         */
        Worker(final List<Double> list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < list.size() && i < startpos + nelem; i++) {
                this.res += this.list.get(i);
            }
        }

        /**
         * Returns the result of summing up the integers within the list.
         * 
         * @return the sum of every element in the array
         */
        public long getResult() {
            return this.res;
        }

    }

}




