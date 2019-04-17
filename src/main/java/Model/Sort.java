package Model;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Clase para ordenar un arreglo de Clases que extienden a comparable. El ordenamiento se realiza segun compareTO
 */
public class Sort {
    /**
     *
     * @param arr arreglo a ordenar. El ordenamiento se realiza segun compareTo
     * @param <T> clase a la cual pertenecen los elementos del arreglo. Deben extender Comparable
     */
    public static <T extends Comparable<T>> void sortByCriteria(T[] arr){
        Arrays.sort(arr, Comparator.nullsLast(T::compareTo));
    }

}
