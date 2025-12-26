package LLD.OpenClosedPrinciple.Example2;

import java.util.List;

public class ReportSender {
    void sendReport(List<Report> reports) {
        for (Report report : reports) {
            report.report();
        }
    }
}
