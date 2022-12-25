import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class App {
    private static final String PILGRIM = "filgrim";
    private static final String NORMAR = "normar";
    private static final String[] HONHAMIMMANZAP = {"홍련", "하란", "라푼젤", "미만잡", "반박", "안받음"};
    public static void main(String[] args) throws Exception {
        Map<Integer, Integer> average = new TreeMap<>();
        int averageGacha = 0;
		for (int a = 0; a < 20; a++) {
            Map<Integer, Integer> winner = new TreeMap<>();
            int allTotalGacha = 0;
            for (int i = 0; i < 50000; i++) { // 케이스는 대충 유저수만큼
                Map<String, Integer> getStatus = new HashMap<>();
                Map<String, Integer> normarSsrGetStatus = new HashMap<>();
                int totalGacha = 0;
                int limitBreak = 0;
                int mileage = 0;
                int totalRequiredLimitBreak = 20;
                while(true) {
                    totalGacha++;
                    if (totalGacha % 200 == 0) mileage++;
                    // 솔직히 갸쌉수알못이라 가챠 확률이 어떻게 동작하는지는 모르겠고
                    // 걍 1부터 1000까지 랜덤 돌려서 1~5(0.5%)면 필그림, 6~40(3.5%)이면 일반 SSR로 계산함.
                    int ddeotnya = (int)(Math.random()*1000) + 1;
                    if (ddeotnya < 6) {
                        String pilgrim = addSsr(PILGRIM);
                        getStatus.put(pilgrim, getStatus.getOrDefault(pilgrim, 0) + 1);
                        if (getStatus.get(pilgrim) == 4) {
                            limitBreak++;
                            totalRequiredLimitBreak -= 4;
                        }
                    } else if (ddeotnya > 5 && ddeotnya < 41) {
                        String normarSsr = addSsr(NORMAR);
                        getStatus.put(normarSsr, getStatus.getOrDefault(normarSsr, 0) + 1);
                        normarSsrGetStatus.put(normarSsr, normarSsrGetStatus.getOrDefault(normarSsr, 0) + 1);
                        if (getStatus.get(normarSsr) == 4) {
                            normarSsrGetStatus.remove(normarSsr);
                            totalRequiredLimitBreak -= 4;
                            limitBreak++;
                        }  
                    }
                    if (mileage*4 >= totalRequiredLimitBreak) {
                        limitBreak = useMileage(normarSsrGetStatus, totalRequiredLimitBreak, mileage, limitBreak);
                    }
    
                    if (limitBreak == 5) {
                        allTotalGacha += totalGacha;
                        totalGacha = editGachaCount(totalGacha);
                        winner.put(totalGacha, winner.getOrDefault(totalGacha, 0) + 1);
                        break; // 씹무라 개새끼	
                    }
                }
            }
            for (Map.Entry<Integer, Integer> entry : winner.entrySet()) {
                average.put(entry.getKey(), average.getOrDefault(entry.getKey(), 0)+entry.getValue());
            }
            averageGacha += allTotalGacha / 50000;
        }
        double totalProbability = 0.0;
        for (Map.Entry<Integer, Integer> entry : average.entrySet()) {
            double probability = (double)entry.getValue()/20;
            totalProbability += (probability / 50000)*100;
            System.out.print(entry.getKey() + "회 근처에 탈출한 사람 : " + String.format("%4.2f", probability) + "명");
            System.out.println(" / 누적확률" + String.format("%3.10f", totalProbability));
        }

        System.out.println("평균 가챠 횟수 : " + averageGacha/20);

    }
    
    public static String addSsr(String enterprise) {
        String result = "";
        switch (enterprise) {
            case "PILGRIM" :
                result = HONHAMIMMANZAP[(int)(Math.random()*(HONHAMIMMANZAP.length - 1))];
                break;
            default :
                result = Integer.toString((int)(Math.random()*14));
        }
        return result;
    }

    public static int editGachaCount(int totalGacha) {
        int round = (int)(Math.round((double)((totalGacha % 100) / 10)));
        if (round > 7) {
            round = 10;
        } else if (round < 8 && round > 2) {
            round = 5;
        } else {
            round = 0;
        }
        return ((totalGacha / 100)*10 + round)*10;
    }

    public static int useMileage(Map<String, Integer> normarSsrGetStatus, int totalRequiredLimitBreak, int mileage, int limitBreak) {
        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(normarSsrGetStatus.entrySet());
        entryList.sort(((o2, o1) -> normarSsrGetStatus.get(o2.getKey()) - normarSsrGetStatus.get(o1.getKey())));
        for(Map.Entry<String, Integer> entry : entryList){
            if (entry.getValue() < 4) {
                int requiredPoint = 4 - entry.getValue();
                if (requiredPoint <= mileage) {
                    totalRequiredLimitBreak -= 4;
                    mileage -= requiredPoint;
                } else break;
            }
        }
        return totalRequiredLimitBreak == 0 ? 5 : limitBreak;
    }
}
