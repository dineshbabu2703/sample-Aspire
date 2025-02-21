package Project.Pro;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Customreport implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        StringBuilder report = new StringBuilder();
        
        // Add HTML and CSS Styling
        report.append("<html><head><title>TestNG Custom Report</title>")
              .append("<style>")
              .append("body { font-family: Arial, sans-serif; margin: 20px; padding: 20px; }")
              .append("h2 { background: #333366; color: white; padding: 10px; }")
              .append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }")
              .append("th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }")
              .append("th { background: #444488; color: white; }")
              .append("tr:nth-child(even) { background: #f2f2f2; }")
              .append(".passed { background-color: #d4edda; }")
              .append(".failed { background-color: #f8d7da; }")
              .append(".skipped { background-color: #fff3cd; }")
              .append("</style></head><body>");

        report.append("<h1>Test Execution Report</h1>");

        // Test Suite Summary Table
        report.append("<h2>Test Suite Summary</h2>")
              .append("<table><tr><th>Total Tests</th><th>Passed</th><th>Skipped</th><th>Failed</th><th>Start Time</th><th>End Time</th><th>Total Time (s)</th></tr>");

        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                long totalTimeInSeconds = (context.getEndDate().getTime() - context.getStartDate().getTime()) / 1000;

                report.append("<tr><td>").append(context.getAllTestMethods().length).append("</td>")
                      .append("<td>").append(context.getPassedTests().size()).append("</td>")
                      .append("<td>").append(context.getSkippedTests().size()).append("</td>")
                      .append("<td>").append(context.getFailedTests().size()).append("</td>")
                      .append("<td>").append(formatDate(context.getStartDate())).append("</td>")
                      .append("<td>").append(formatDate(context.getEndDate())).append("</td>")
                      .append("<td>").append(totalTimeInSeconds).append(" sec</td></tr>");
            }
        }
        report.append("</table>");

        // Passed Test Cases Table
        report.append("<h2>Passed Test Cases</h2>")
              .append("<table><tr><th>Test Case</th><th>Start Time</th><th>End Time</th><th>Execution Time (s)</th><th>Logs</th><th>Screenshot</th></tr>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                for (ITestResult test : context.getPassedTests().getAllResults()) {
                    report.append(createTestRow(test, "Passed"));
                }
            }
        }
        report.append("</table>");

        // Failed Test Cases Table
        report.append("<h2>Failed Test Cases</h2>")
              .append("<table><tr><th>Test Case</th><th>Start Time</th><th>End Time</th><th>Error Message</th><th>Logs</th></tr>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                for (ITestResult test : context.getFailedTests().getAllResults()) {
                    report.append(createTestRow(test, "Failed"));
                }
            }
        }
        report.append("</table>");

        // Skipped Test Cases Table
        report.append("<h2>Skipped Test Cases</h2>")
              .append("<table><tr><th>Test Case</th><th>Start Time</th><th>End Time</th><th>Reason</th></tr>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                for (ITestResult test : context.getSkippedTests().getAllResults()) {
                    report.append(createTestRow(test, "Skipped"));
                }
            }
        }
        report.append("</table>");

        report.append("</body></html>");

        // Save Report to File
        try {
            File reportFile = new File(outputDirectory + "/CustomTestReport.html");
            FileWriter writer = new FileWriter(reportFile);
            writer.write(report.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Formats Date to Readable String
    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    // Creates Row for Test Case with Execution Time in Seconds
    private String createTestRow(ITestResult result, String status) {
        long durationInSeconds = (result.getEndMillis() - result.getStartMillis()) / 1000;
        String errorMsg = status.equals("Failed") ? result.getThrowable().getMessage() : "";

        // CSS class based on status
        String rowClass = status.equals("Passed") ? "passed" : status.equals("Failed") ? "failed" : "skipped";

        return "<tr class='" + rowClass + "'><td>" + result.getName() + "</td>"
                + "<td>" + formatDate(new Date(result.getStartMillis())) + "</td>"
                + "<td>" + formatDate(new Date(result.getEndMillis())) + "</td>"
                + "<td>" + (status.equals("Failed") ? errorMsg : durationInSeconds + " sec") + "</td>"
                + "<td>Logs Placeholder</td>"  // Modify if using logging
                + "<td>Screenshot Placeholder</td></tr>"; // Modify if capturing screenshots
    }
}
