package Service;

import java.io.BufferedReader;

class UtilP {
    public Integer scanInt(BufferedReader scanner) {
        String toProccess;
        int toRet;
        while (true) {
            try {
                synchronized (this) {
                    toProccess = scanner.readLine();
                    toRet = Integer.parseInt(toProccess);
                    return toRet;
                }
            } catch (Exception e) {
                System.out.println("invalid input, please try again.");
            }
        }
    }


    public static Double scanDouble(BufferedReader scanner) {
        String toProccess;
        double toRet;
        while (true) {
            try {
                toProccess = scanner.readLine();
                toRet = Double.parseDouble(toProccess);
                return toRet;
            } catch (Exception e) {
                System.out.println("invalid input, please try again.");
            }
        }
    }


}